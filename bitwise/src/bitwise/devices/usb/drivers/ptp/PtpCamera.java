package bitwise.devices.usb.drivers.ptp;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.App;
import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.UsbRequest;
import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.containers.DataContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.DecodableContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.OperationContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.ResponseContainer;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.ExposureProgramMode;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.PtpFlashMode;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.PtpFocusMode;
import bitwise.devices.usb.drivers.ptp.types.events.DevicePropChanged;
import bitwise.devices.usb.drivers.ptp.types.events.Event;
import bitwise.devices.usb.drivers.ptp.types.operations.CloseSession;
import bitwise.devices.usb.drivers.ptp.types.operations.GetDeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.operations.GetDevicePropDesc;
import bitwise.devices.usb.drivers.ptp.types.operations.OpenSession;
import bitwise.devices.usb.drivers.ptp.types.operations.Operation;
import bitwise.devices.usb.drivers.ptp.types.operations.SetDevicePropValue;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt8;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceProperty;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceProperty.PropertyDescribingEnum;
import javafx.concurrent.Task;

public abstract class PtpCamera extends UsbDriver {
	private final byte interfaceAddr;
	private final byte dataOutEPNum;
	private final byte dataInEPNum;
	private final byte interruptEPNum;
	private javax.usb.UsbConfiguration activeConfig;
	private javax.usb.UsbInterface ptpInterface;
	private javax.usb.UsbEndpoint dataOutEP;
	private javax.usb.UsbEndpoint dataInEP;
	javax.usb.UsbEndpoint interruptEP;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe dataInPipe;
	javax.usb.UsbPipe interruptPipe;
	private Task<Void> interruptTask;
	private DeviceInfo deviceInfo;
	
	public PtpCamera(byte in_interfaceAddr, byte in_dataOutEPNum, byte in_dataInEPNum, byte in_interruptEPNum, App in_app) {
		super(in_app);
		interfaceAddr = in_interfaceAddr;
		dataOutEPNum = in_dataInEPNum;
		dataInEPNum = in_dataOutEPNum;
		interruptEPNum = in_interruptEPNum;
	}
	
	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}
	
	private int nextTransactionID = 1;

	protected synchronized TransactionID newTransactionID() {
		return new TransactionID(nextTransactionID++);
	}
	
	protected abstract boolean onPtpInitialize(UsbContext ctx) throws InterruptedException;
	protected abstract void onPtpDisable();
	
	@Override
	protected boolean onDriverInitialize(UsbContext ctx) throws InterruptedException {
		javax.usb.UsbDevice platformDevice = getDevice().getPlatformDevice();
		activeConfig = platformDevice.getActiveUsbConfiguration();
		ptpInterface = activeConfig.getUsbInterface(interfaceAddr);
		
		try {
			ptpInterface.claim(new UsbInterfacePolicy() {
				@Override
				public boolean forceClaim(UsbInterface iface) {
					return true;
				}
			});
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		
		dataOutEP = ptpInterface.getUsbEndpoint(dataOutEPNum);
		dataInEP = ptpInterface.getUsbEndpoint(dataInEPNum);
		interruptEP = ptpInterface.getUsbEndpoint(interruptEPNum);
		assert(null != dataOutEP);
		assert(null != dataInEP);
		assert(null != interruptEP);
		System.out.println("PTP driver initializing:");
		System.out.println(String.format("  dataInEP: %02x, type %02x", dataOutEP.getUsbEndpointDescriptor().bEndpointAddress(), dataOutEP.getType()));
		System.out.println(String.format("  dataOutEP: %02x, type %02x", dataInEP.getUsbEndpointDescriptor().bEndpointAddress(), dataInEP.getType()));
		System.out.println(String.format("  interruptEP: %02x, type %02x", interruptEP.getUsbEndpointDescriptor().bEndpointAddress(), interruptEP.getType()));
		
		dataOutPipe = dataOutEP.getUsbPipe();
		dataInPipe = dataInEP.getUsbPipe();
		interruptPipe = interruptEP.getUsbPipe();
		try {
			dataOutPipe.open();
			dataInPipe.open();
			interruptPipe.open();
		} catch (UsbNotActiveException | UsbNotClaimedException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		{
			interruptTask = new PtpCameraInterruptTask(this);
			Thread interruptThread = new Thread(interruptTask);
			interruptThread.setName(String.format("Interrupt task (%s)", this));
			interruptThread.start();
		}
		
		try {
			runOperation(new OpenSession());
			
			GetDeviceInfo getDeviceInfo = new GetDeviceInfo();
			runOperation(getDeviceInfo);
			deviceInfo = getDeviceInfo.getResponseData();
			
			updateExposureProgramMode();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return onPtpInitialize(ctx);
	}
	
	protected void onDriverDisable() {
		onPtpDisable();
		
		interruptTask.cancel();
		
		try {
			runOperation(new CloseSession());
		} catch (InterruptedException | UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			if (null != dataOutPipe && dataOutPipe.isOpen())
				dataOutPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		if (null != dataOutPipe && dataInPipe.isOpen())
			dataInPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
		if (null != dataOutPipe && interruptPipe.isOpen())
			interruptPipe.close();
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			ptpInterface.release();
		} catch (UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected DecodableContainer readPtpPacket(javax.usb.UsbPipe pipe) throws InterruptedException, UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		final int maxPacketSize = 0xffff & pipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize();
		byte[] incoming = null;
		
		int length = 0;
		int read = 0;
		do {
			byte[] bucket = new byte[maxPacketSize];
			javax.usb.UsbIrp irp = pipe.asyncSubmit(bucket);
			while (!irp.isComplete())
				Thread.sleep(20);
			if (0 == length) {
				UInt32 decodedLength = UInt32.decoder.decode(ByteBuffer.wrap(bucket));
				length = decodedLength.getValue();
				if (0 == length)
					return null;
				incoming = new byte[length];
			}
			for (int i = 0; i < irp.getActualLength(); i++)
				incoming[read + i] = bucket[i];
			read += irp.getActualLength();
		} while (read < length);
		
		return DecodableContainer.decoder.decode(ByteBuffer.wrap(incoming));
	}
	
	protected synchronized void sendPtpPipe(Operation operation) throws InterruptedException, UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		// Send the command
		{
			ByteArrayOutputStream args = new ByteArrayOutputStream();
			for (Int32 arg : operation.getArguments())
				arg.serialize(args);
			
			OperationContainer container = new OperationContainer(operation.getCode(), operation.getTransactionID(), ByteBuffer.wrap(args.toByteArray()));
			ByteArrayOutputStream packet = new ByteArrayOutputStream();
			container.serialize(packet);
			
			byte[] outgoing = packet.toByteArray();
			javax.usb.UsbIrp outIrp = dataOutPipe.asyncSubmit(outgoing);
			while (!outIrp.isComplete())
				Thread.sleep(20);
		}
		// Send the data (if there is any)
		if (null != operation.getOutData()) {
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			operation.getOutData().serialize(data);
			
			DataContainer container = new DataContainer(operation.getCode(), operation.getTransactionID(), ByteBuffer.wrap(data.toByteArray()));
			ByteArrayOutputStream packet = new ByteArrayOutputStream();
			container.serialize(packet);
			
			byte[] outgoing = packet.toByteArray();
			javax.usb.UsbIrp outIrp = dataOutPipe.asyncSubmit(outgoing);
			while (!outIrp.isComplete())
				Thread.sleep(20);
		}
	}
	
	protected synchronized void runOperation(Operation operation) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		if (null == operation.getTransactionID())
			operation.setTransactionID(newTransactionID());
		sendPtpPipe(operation);
		
		int loopRuns = 0;
		while (loopRuns < 2 && null == operation.getResponseArguments()) {
			DecodableContainer resp = readPtpPacket(dataInPipe);
			if (null == resp)
				continue;
			loopRuns++;
			if (resp instanceof ResponseContainer) {
				operation.setResponseArguments(resp.getPayload());
			}
			else if (resp instanceof DataContainer) {
				operation.setResponseData(resp.getPayload());
			}
		}
	}
	
	protected boolean handleEvent(Event preEvent) {
		if (preEvent instanceof DevicePropChanged) {
			DevicePropChanged event = (DevicePropChanged)preEvent;
			if (event.getDevicePropCode().equals(DevicePropCode.exposureProgramMode)) {
				updateExposureProgramMode();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.fNumber)) {
				updateFNumber();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.batteryLevel)) {
				updateBatteryLevel();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.focalLength)) {
				updateFocalLength();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.focusMode)) {
				updateFocusMode();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.flashMode)) {
				updateFlashMode();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.exposureTime)) {
				updateExposureTime();
				return true;
			}
			if (event.getDevicePropCode().equals(DevicePropCode.exposureIndex)) {
				updateExposureIndex();
				return true;
			}
		}
		return false;
	}
	
	private ExposureMode exposureMode = ExposureMode.Unknown;
	
	public ExposureMode getExposureMode() {
		return exposureMode;
	}
	
	public void exposureModeChanged(ExposureMode in) {
		exposureMode = in;
	}
	
	protected boolean cmd_setExposureProgramMode(ExposureProgramMode in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.exposureProgramMode, in));
		return true;
	}
	
	protected boolean updateExposureProgramMode() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.exposureProgramMode);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				ExposureProgramMode value = new ExposureProgramMode(((UInt16)currentValue).getValue());
				exposureModeChanged(value.getExposureMode());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private FNumber fNumber = new FNumber((short) 0);
	private List<FNumber> validFNumbers = new ArrayList<>(0);
	
	public FNumber getFNumber() {
		return fNumber;
	}
	
	public List<FNumber> getValidFNumbers() {
		return validFNumbers;
	}
	
	protected void fNumberChanged(FNumber in, List<FNumber> in2) {
		fNumber = in;
		validFNumbers = in2;
	}
	
	public UsbRequest setFNumber(FNumber in) {
		return new PtpSetFNumberRequest(this, in);
	}
	
	protected boolean cmd_setFNumber(FNumber in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.fNumber, new UInt16(in.getValue())));
		return true;
	}
	
	protected boolean updateFNumber() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.fNumber);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				FNumber value = new FNumber(((UInt16)currentValue).getValue());
				ArrayList<FNumber> values = null;
				if (null != operation.getResponseData().getForm() && operation.getResponseData().getForm() instanceof DeviceProperty.PropertyDescribingEnum<?>) {
					DeviceProperty.PropertyDescribingEnum<?> enumerated = (PropertyDescribingEnum<?>) operation.getResponseData().getForm();
					values = new ArrayList<FNumber>(enumerated.getNumberOfValues());
					for (PtpType v : enumerated.getSupportedValues())
						values.add(new FNumber(((UInt16)v).getValue()));
				}
				if (null == values)
					values = new ArrayList<>(0);
				fNumberChanged(value, values);
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private byte batteryLevel = 0;
	
	public byte getBatteryLevel() {
		return batteryLevel;
	}
	
	protected void batteryLevelChanged(byte in) {
		batteryLevel = in;
	}
	
	protected boolean updateBatteryLevel() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.batteryLevel);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt8) {
				UInt8 value = (UInt8)currentValue;
				batteryLevelChanged(value.getValue());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private int focalLength = 0;
	
	public int getFocalLength() {
		return focalLength;
	}
	
	protected void focalLengthChanged(int in) {
		focalLength = in;
	}
	
	protected boolean updateFocalLength() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.focalLength);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt32) {
				UInt32 value = (UInt32)currentValue;
				focalLengthChanged(value.getValue());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private FocusMode focusMode = null;
	
	public FocusMode getFocusMode() {
		return focusMode;
	}
	
	protected void focusModeChanged(FocusMode in) {
		focusMode = in;
	}
	
	protected boolean cmd_setFocusMode(PtpFocusMode in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.focusMode, in));
		return true;
	}
	
	protected boolean updateFocusMode() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.focusMode);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				PtpFocusMode value = new PtpFocusMode(((UInt16) currentValue).getValue());
				focusModeChanged(value.getFocusMode());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private FlashMode flashMode = null;
	
	public FlashMode getFlashMode() {
		return flashMode;
	}
	
	protected void flashModeChanged(FlashMode in) {
		flashMode = in;
	}
	
	protected boolean cmd_setFlashMode(PtpFlashMode in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.flashMode, in));
		return true;
	}
	
	protected boolean updateFlashMode() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.flashMode);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				PtpFlashMode value = new PtpFlashMode(((UInt16) currentValue).getValue());
				flashModeChanged(value.getFlashMode());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private int exposureTime = 0;
	
	public int getExposureTime() {
		return exposureTime;
	}
	
	protected void exposureTimeChanged(int in) {
		exposureTime = in;
	}
	
	protected boolean cmd_setExposureTime(int in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.exposureTime, new UInt32(in)));
		return true;
	}
	
	protected boolean updateExposureTime() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.exposureTime);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt32) {
				UInt32 value = (UInt32)currentValue;
				exposureTimeChanged(value.getValue());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private short exposureIndex;
	
	public short getExposureIndex() {
		return exposureIndex;
	}
	
	protected void exposureIndexChanged(short in) {
		exposureIndex = in;
	}
	
	protected boolean cmd_setExposureIndex(short in) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		runOperation(new SetDevicePropValue<>(DevicePropCode.exposureIndex, new UInt16(in)));
		return true;
	}
	
	protected boolean updateExposureIndex() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.exposureIndex);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				UInt16 value = (UInt16)currentValue;
				exposureIndexChanged(value.getValue());
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
