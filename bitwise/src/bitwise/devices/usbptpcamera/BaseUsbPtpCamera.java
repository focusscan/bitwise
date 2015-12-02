package bitwise.devices.usbptpcamera;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;

import bitwise.devices.BaseDriver;
import bitwise.devices.camera.CameraListener;
import bitwise.devices.camera.CameraProperty;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.events.EventCode;
import bitwise.devices.usbptpcamera.events.EventDecoder;
import bitwise.devices.usbptpcamera.operations.*;
import bitwise.devices.usbptpcamera.requests.TakeImage;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.devices.usbptpcamera.responses.DevicePropertyEnum;
import bitwise.devices.usbptpcamera.responses.DevicePropertyRange;
import bitwise.devices.usbptpcamera.responses.Response;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbptpcamera.responses.ResponseData;
import bitwise.devices.usbptpcamera.responses.ResponseDecoder;
import bitwise.devices.usbptpcamera.responses.StorageInfo;
import bitwise.devices.usbservice.UsbDevice;
import bitwise.engine.service.BaseRequest;
import bitwise.log.Log;

public abstract class BaseUsbPtpCamera<H extends BaseUsbPtpCameraHandle<?>> extends BaseDriver<UsbDevice, H> {
	public static final short containerCodeOperation = (short) 0x0001;
	public static final short containerCodeData      = (short) 0x0002;
	public static final short containerCodeResponse  = (short) 0x0003;
	public static final short containerCodeEvent     = (short) 0x0004;
	
	private final ResponseDecoder responseDecoder = new ResponseDecoder();
	private final EventDecoder eventDecoder = new EventDecoder();
	private final byte interfaceNum;
	private final byte dataInNum;
	private final byte dataOutNum;
	private final byte intrptNum;
	
	private javax.usb.UsbInterface iface;
	private javax.usb.UsbPipe dataInPipe;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe intrptPipe;
	
	private BaseUsbPtpReader<Response> dataOutReader;
	private BaseUsbPtpReader<Event> intrptReader;
	
	private String cameraManufacturer = "";
	private String cameraModel = "";
	private String cameraVersion = "";
	private String cameraSerial = "";
	
	protected BaseUsbPtpCamera(UsbDevice in_device, byte in_interfaceNum, byte in_dataInNum, byte in_dataOutNum, byte in_intrptNum) {
		super(in_device);
		interfaceNum = in_interfaceNum;
		dataInNum = in_dataInNum;
		dataOutNum = in_dataOutNum;
		intrptNum = in_intrptNum;
	}
	
	public String getCameraManufacturer() {
		return cameraManufacturer;
	}
	
	public String getCameraModel() {
		return cameraModel;
	}
	
	public String getCameraVersion() {
		return cameraVersion;
	}
	
	public String getCameraSerial() {
		return cameraSerial;
	}
	
	private void getEndpoints() throws UsbClaimException, UsbNotActiveException, UsbDisconnectedException, UsbException {
		javax.usb.UsbDevice        xDevice = getDevice().getXDevice();
		javax.usb.UsbConfiguration xConfig = xDevice.getActiveUsbConfiguration();
		iface = xConfig.getUsbInterface(interfaceNum);
		iface.claim(new UsbInterfacePolicy() {
			@Override
			public boolean forceClaim(UsbInterface ignored) {
				return true;
			}
		});
		dataInPipe = iface.getUsbEndpoint(dataInNum).getUsbPipe();
		dataOutPipe = iface.getUsbEndpoint(dataOutNum).getUsbPipe();
		intrptPipe = iface.getUsbEndpoint(intrptNum).getUsbPipe();
		dataInPipe.open();
		dataOutReader = new BaseUsbPtpReader<>(dataOutPipe, responseDecoder);
		intrptReader = new BaseUsbPtpReader<>(intrptPipe, eventDecoder);
		dataOutReader.start();
		intrptReader.start();
		addServiceTask(new UsbPtpInterruptTask(this, intrptReader.getDecoded()));
	}
	
	protected abstract boolean onStartPtpDriver();
	
	@Override
	protected boolean onStartDriver() throws InterruptedException {
		if (!onStartPtpDriver())
			return false;
		try {
			Log.log(this, "Starting PTP camera (interface=%02x, out=%02x, in=%02x, int=%02x)", interfaceNum, dataOutNum, dataInNum, intrptNum);
			getEndpoints();
			openSession();
			getDeviceInfo();
			Log.log(this, "Camera started");
			return true;
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			Log.logException(this, e);
		} catch (UsbPtpException e) {
			Log.logException(this, e);
		}
		return false;
	}
	
	private void closeEndpoints() throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException {
		boolean connected = getDevice().isConnected();
		try {
			dataOutReader.stop(connected);
			intrptReader.stop(connected);
			if (connected) {
				if (null != dataInPipe && dataInPipe.isOpen()) {
					dataInPipe.abortAllSubmissions();
					dataInPipe.close();
				}
				iface.release();
			}
		} catch (UsbException e) {
			Log.logException(this, e);
		}
	}
	
	protected abstract void onStopPtpDriver();

	@Override
	protected void onStopDriver() {
		try {
			Log.log(this, "Stopping PTP camera");
			if (getDevice().isConnected()) {
				closeSession();
			}
			closeEndpoints();
		} catch (InterruptedException | UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException e) {
			Log.logException(this, e);
		}
		onStopPtpDriver();
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
	}
	
	private volatile CameraListener cameraListener = null;
	
	protected void setCameraListener(CameraListener in_cameraListener) {
		cameraListener = in_cameraListener;
	}
	
	protected void handlePtpEvent(Event in) {
		Log.log(this, "PTP event %04x", in.getEventCode());
		CameraListener listener = cameraListener;
		if (null != listener) {
			if (in.getEventCode() == EventCode.devicePropChanged) {
				CameraProperty property = null;
				final short deviceProp = (short) in.getArguments()[0];
				if (deviceProp == DevicePropCode.batteryLevel)
					property = CameraProperty.BatteryLevel;
				else if (deviceProp == DevicePropCode.exposureProgramMode)
					property = CameraProperty.ExposureProgramMode;
				else if (deviceProp == DevicePropCode.exposureTime)
					property = CameraProperty.ExposureTime;
				else if (deviceProp == DevicePropCode.flashMode)
					property = CameraProperty.FlashMode;
				else if (deviceProp == DevicePropCode.fNumber)
					property = CameraProperty.FNumber;
				else if (deviceProp == DevicePropCode.focalLength)
					property = CameraProperty.FocalLength;
				else if (deviceProp == DevicePropCode.focusMode)
					property = CameraProperty.FocusMode;
				else if (deviceProp == DevicePropCode.exposureIndex)
					property = CameraProperty.Iso;
				if (null != property)
					listener.onCameraPropertyChanged(this.getServiceHandle(), property);
			}
			else if (in.getEventCode() == EventCode.storeAdded || in.getEventCode() == EventCode.storeRemoved) {
				listener.onCameraPropertyChanged(this.getServiceHandle(), CameraProperty.StorageDevices);
			}
		}
	}
	
	private volatile Operation<?> currentOperation = null;
	
	protected final Operation<?> getCurrentOperation() {
		return currentOperation;
	}
	
	private int nextTransactionID = 1;
	
	protected synchronized void runOperation(Operation<?> operation) throws InterruptedException {
		currentOperation = operation;
		try {
			int transactionID = 0;
			if (operation.hasTransactionID())
				transactionID = nextTransactionID++;
			{
				int length = 0;
				Log.log(this, "Operation %04x, txid %08x", operation.getOperationCode(), transactionID);
				UsbPtpBuffer outBuffer = new UsbPtpBuffer();
				do {
					outBuffer.put(length);
					outBuffer.put(containerCodeOperation);
					outBuffer.put(operation.getOperationCode());
					outBuffer.put(transactionID);
					for (int arg : operation.getArguments())
						outBuffer.put(arg);
					length = outBuffer.getLength();
				} while (outBuffer.disableMeasureMode());
				dataInPipe.asyncSubmit(outBuffer.getArray());
			}
			if (null != operation.getDataOut()) {
				int length = 0;
				UsbPtpBuffer outBuffer = new UsbPtpBuffer();
				do {
					outBuffer.put(length);
					outBuffer.put(containerCodeData);
					outBuffer.put(operation.getOperationCode());
					outBuffer.put(transactionID);
					operation.getDataOut().encode(outBuffer);
					length = outBuffer.getLength();
				} while (outBuffer.disableMeasureMode());
				dataInPipe.asyncSubmit(outBuffer.getArray());
			}
			
			boolean responseCodeFound = false;
			responseLoop: for (int i = 0; i < 2; i++) {
				Response preResponse = dataOutReader.getDecoded().take();
				if (preResponse instanceof ResponseCode) {
					ResponseCode response = (ResponseCode) preResponse;
					if (response.getTransactionID() == transactionID) {
						operation.recvResponseCode(response);
						responseCodeFound = true;
						break responseLoop;
					}
					else
						Log.log(this, "TransactionID mis-match: expected %08x, got %08x", transactionID, response.getTransactionID());
				}
				else if (preResponse instanceof ResponseData) {
					ResponseData response = (ResponseData) preResponse;
					if (response.getOperationCode() == operation.getOperationCode()) {
						if (response.getTransactionID() == transactionID)
							operation.recvResponseData(response);
						else
							Log.log(this, "TransactionID mis-match: expected %08x, got %08x", transactionID, response.getTransactionID());
					}
					else
						Log.log(this, "Operation code mis-match: expected %04x, got %04x", operation.getOperationCode(), response.getOperationCode());
				}
			}
			if (responseCodeFound) {
				operation.awaitFinished();
				int dataSize = 0;
				if (null != operation.getResponseData())
					dataSize = operation.getResponseData().getDataSize();
				Log.log(this, "Operation %04x finished, code %04x, txid %08x, %d bytes data", operation.getOperationCode(), operation.getResponseCode().getResponseCode(), operation.getResponseCode().getTransactionID(), dataSize);
			}
			else {
				Log.log(this, "Never got a response code for operation %04x txid %08x", operation.getOperationCode(), transactionID);
			}
		} catch (IllegalArgumentException | UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
			Log.logException(this, e);
		} catch (UsbPtpCoderException e) {
			Log.logException(this, e.exception);
		}
		currentOperation = null;
	}
	
	private int nextSessionID = 1;
	
	protected boolean openSession() throws InterruptedException {
		OpenSession request = new OpenSession(nextSessionID);
		runOperation(request);
		return request.isSuccess();
	}
	
	protected boolean closeSession() throws InterruptedException {
		CloseSession request = new CloseSession();
		runOperation(request);
		return request.isSuccess();
	}
	
	public static final class StorageIDwInfo {
		public final int storageID;
		public final StorageInfo info;
		
		protected StorageIDwInfo(int in_storageID, StorageInfo in_info) {
			storageID = in_storageID;
			info = in_info;
		}
	}
	
	public List<StorageIDwInfo> getStorageIDs() throws InterruptedException, UsbPtpException {
		GetStorageIDs request = new GetStorageIDs();
		runOperation(request);
		ArrayList<StorageIDwInfo> ret = null;
		StringBuilder sb = new StringBuilder();
		sb.append("Storage IDs:");
		for (int value : request.getDecodedData().value)
			sb.append(String.format(" %08x", value));
		Log.log(this, sb.toString());
		for (int value : request.getDecodedData().value) {
			GetStorageInfo infoRequest = new GetStorageInfo(value);
			runOperation(infoRequest);
			Log.log(this, "StorageInfo %08x", value);
			// if (0x2001 == infoRequest.getResponseCode().getResponseCode()) {
			StorageInfo info = infoRequest.getDecodedData();
			if (null != info) {
				if (null == ret)
					ret = new ArrayList<>();
				ret.add(new StorageIDwInfo(value, info));
				final long bytesPerMeg = 1024 * 1024;
				Log.log(this, " Desc %s label %s", info.storageDescription, info.volumeLabel);
				Log.log(this, " %s %s %s size %d megs (%d megs free)", info.getStorageTypeEnum(), info.getFilesystemTypeEnum(), info.getAccessCapabilityEnum(), info.maxCapacity/bytesPerMeg, info.freeSpaceInBytes/bytesPerMeg);
			}
			else
				Log.log(this, " (No info about this storage ID)");
		}
		return ret;
	}
	
	public DevicePropDesc getDevicePropDesc(short deviceProp) throws InterruptedException, UsbPtpException {
		GetDevicePropDesc request = new GetDevicePropDesc(deviceProp);
		runOperation(request);
		DevicePropDesc desc = request.getDecodedData();
		if (null != desc) {
			synchronized(Log.class) {
				Log.log(this, "Device prop desc: %04x", deviceProp);
				Log.log(this, " Default %s current %s", desc.factoryDefaultValue, desc.currentValue);
				Log.log(this, " Form %s %s", desc.supportsSet() ? "Set/Get" : "Get", desc.getFormEnum());
				switch (desc.getFormEnum()) {
				case Range:
				{
					DevicePropertyRange values = (DevicePropertyRange) desc.form;
					Log.log(this, " Range [%s, %s] step by %s", values.minimumValue, values.maximumValue, values.stepSize);
					break;
				}
				case Enum:
				{
					DevicePropertyEnum values = (DevicePropertyEnum) desc.form;
					StringBuilder sb = new StringBuilder();
					sb.append(" Enum");
					for (UsbPtpPrimType val : values.supportedValues)
						sb.append(String.format(" %s", val));
					Log.log(this, sb.toString());
					break;
				}
				case None:
				default:
					Log.log(this, " (No legal values given)");
				}
			}
		}
		return desc;
	}
	
	public boolean setDevicePropValue(short deviceProp, UsbPtpPrimType value) throws InterruptedException {
		SetDevicePropValue request = new SetDevicePropValue(deviceProp, value);
		runOperation(request);
		return request.isSuccess();
	}
	
	public void takeImage(TakeImage<?> takeImage) throws InterruptedException {
		InitiateCapture request = new InitiateCapture(takeImage.getStorageDevice().getValue(), takeImage.getImageFormat().getValue());
		runOperation(request);
		takeImage.setObjectID(request.getObjectIDs());
		try {
			if (0 < takeImage.getObjectIDs().size()) {
				int objectID = takeImage.getObjectIDs().get(0).value;
				GetObject imageRequest = new GetObject(objectID);
				runOperation(imageRequest);
				if (null != imageRequest.getResponseData() && 0 < imageRequest.getResponseData().getDataSize()) {
					UsbPtpBuffer imageBuf = imageRequest.getResponseData().getData();
					byte[] image = new byte[imageRequest.getResponseData().getDataSize()];
					for (int i = 0; i < image.length; i++)
						image[i] = imageBuf.getByte();
					takeImage.setImage(image);
				}
			}
		} catch (UsbPtpCoderException e) {
			Log.logException(this, e);
		}
	}
	
	public DeviceInfo getDeviceInfo() throws InterruptedException, UsbPtpException {
		GetDeviceInfo request = new GetDeviceInfo();
		runOperation(request);
		DeviceInfo info = request.getDecodedData();
		if (null != info) {
			cameraManufacturer = info.manufacturer;
			cameraModel = info.model;
			cameraVersion = info.deviceVersion;
			cameraSerial = info.serialNumber;
			
			synchronized(Log.class) {
				Log.log(this, "Device info:");
				Log.log(this, " Versions: %04x %08x %04x %s", info.standardVersion, info.vendorExtensionID, info.vendorExtensionVersion, info.vendorExtensionDesc);
				Log.log(this, " Functional mode %04x", info.functionalMode);
				{
					StringBuilder operationsSupported = new StringBuilder();
					operationsSupported.append(" Operations supported:");
					for (short operation : info.operationsSupported)
						operationsSupported.append(String.format(" %04x", operation));
					Log.log(this, operationsSupported.toString());
				}
				{
					StringBuilder eventsSupported = new StringBuilder();
					eventsSupported.append(" Events supported:");
					for (short event : info.eventsSupported)
						eventsSupported.append(String.format(" %04x", event));
					Log.log(this, eventsSupported.toString());
				}
				{
					StringBuilder devicePropertiesSupported = new StringBuilder();
					devicePropertiesSupported.append(" Device properties supported:");
					for (short deviceProperty : info.devicePropertiesSupported)
						devicePropertiesSupported.append(String.format(" %04x", deviceProperty));
					Log.log(this, devicePropertiesSupported.toString());
				}
				{
					StringBuilder captureFormatsSupported = new StringBuilder();
					captureFormatsSupported.append(" Capture formats supported:");
					for (short captureFormat : info.captureFormats)
						captureFormatsSupported.append(String.format(" %04x", captureFormat));
					Log.log(this, captureFormatsSupported.toString());
				}
				{
					StringBuilder imageFormatsSupported = new StringBuilder();
					imageFormatsSupported.append(" Image formats supported:");
					for (short imageFormat : info.captureFormats)
						imageFormatsSupported.append(String.format(" %04x", imageFormat));
					Log.log(this, imageFormatsSupported.toString());
				}
				Log.log(this, " %s %s %s %s", info.manufacturer, info.model, info.deviceVersion, info.serialNumber);
			}
		}
		return info;
	}
}
