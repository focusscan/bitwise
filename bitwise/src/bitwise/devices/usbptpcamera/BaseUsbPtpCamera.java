package bitwise.devices.usbptpcamera;

import javax.usb.UsbClaimException;
import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;

import bitwise.devices.BaseDriver;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.events.EventDecoder;
import bitwise.devices.usbptpcamera.operations.CloseSession;
import bitwise.devices.usbptpcamera.operations.GetDeviceInfo;
import bitwise.devices.usbptpcamera.operations.OpenSession;
import bitwise.devices.usbptpcamera.operations.Operation;
import bitwise.devices.usbptpcamera.responses.Response;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbptpcamera.responses.ResponseData;
import bitwise.devices.usbptpcamera.responses.ResponseDecoder;
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
	
	protected BaseUsbPtpCamera(UsbDevice in_device, byte in_interfaceNum, byte in_dataInNum, byte in_dataOutNum, byte in_intrptNum) {
		super(in_device);
		interfaceNum = in_interfaceNum;
		dataInNum = in_dataInNum;
		dataOutNum = in_dataOutNum;
		intrptNum = in_intrptNum;
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
	protected boolean onStartDriver() {
		if (!onStartPtpDriver())
			return false;
		try {
			Log.log(this, "Starting PTP camera (interface=%02x, out=%02x, in=%02x, int=%02x)", interfaceNum, dataOutNum, dataInNum, intrptNum);
			getEndpoints();
			openSession();
			getDeviceInfo();
			Log.log(this, "Camera started");
			return true;
		} catch (InterruptedException | UsbNotActiveException | UsbDisconnectedException | UsbException e) {
			Log.logException(this, e);
		}
		return false;
	}
	
	private void closeEndpoints() throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		if (null != dataInPipe && dataInPipe.isOpen()) {
			dataInPipe.abortAllSubmissions();
			dataInPipe.close();
		}
		iface.release();
		dataOutReader.stop();
		intrptReader.stop();
	}
	
	protected abstract void onStopPtpDriver();

	@Override
	protected void onStopDriver() {
		try {
			Log.log(this, "Stopping PTP camera");
			closeSession();
			closeEndpoints();
		} catch (InterruptedException | UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
			Log.logException(this, e);
		}
		onStopPtpDriver();
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
	}
	
	protected void handlePtpEvent(Event in) {
		Log.log(this, "PTP event %04x", in.getEventCode());
	}
	
	private volatile Operation currentOperation = null;
	private int nextTransactionID = 1;
	
	protected final Operation getCurrentOperation() {
		return currentOperation;
	}
	
	protected synchronized void runOperation(Operation operation) throws InterruptedException {
		currentOperation = operation;
		try {
			int length = 0;
			int transactionID = 0;
			if (operation.hasTransactionID())
				transactionID = nextTransactionID++;
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
				Log.log(this, "Operation %04x finished, code %04x, txid %08x", operation.getOperationCode(), operation.getResponseCode().getResponseCode(), operation.getResponseCode().getTransactionID());
			}
			else {
				Log.log(this, "Never got a response code for operation %04x txid %08x", operation.getOperationCode(), transactionID);
			}
		} catch (IllegalArgumentException | UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | UsbException e) {
			Log.logException(this, e);
		}
		currentOperation = null;
	}
	
	private int nextSessionID = 1;
	
	protected void openSession() throws InterruptedException {
		runOperation(new OpenSession(nextSessionID));
	}
	
	protected void closeSession() throws InterruptedException {
		runOperation(new CloseSession());
	}
	
	protected void getDeviceInfo() throws InterruptedException {
		runOperation(new GetDeviceInfo());
	}
}
