package bitwise.devices.usb.drivers.ptp;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.App;
import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.kinds.fullcamera.events.ExposureModeChanged;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.containers.DataContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.DecodableContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.OperationContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.ResponseContainer;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.deviceproperties.ExposureProgramMode;
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
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceInfo;
import javafx.concurrent.Task;

public abstract class PtpCamera extends UsbDriver implements FullCamera {
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
	private ExposureMode exposureMode = ExposureMode.Unknown;
	
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
	
	protected boolean setExposureProgramMode(ExposureProgramMode in) {
		try {
			runOperation(new SetDevicePropValue<>(DevicePropCode.exposureProgramMode, in));
			return true;
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	protected boolean updateExposureProgramMode() {
		try {
			GetDevicePropDesc operation = new GetDevicePropDesc(DevicePropCode.exposureProgramMode);
			runOperation(operation);
			PtpType currentValue = operation.getResponseData().getCurrentValue();
			if (currentValue instanceof UInt16) {
				ExposureProgramMode mode = new ExposureProgramMode(((UInt16)currentValue).getValue());
					exposureMode = mode.getExposureMode();
				return true;
			}
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	protected void handleEvent(Event preEvent) {
		if (preEvent instanceof DevicePropChanged) {
			DevicePropChanged event = (DevicePropChanged)preEvent;
			if (event.getDevicePropCode().equals(DevicePropCode.exposureProgramMode)) {
				if (updateExposureProgramMode())
					bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureModeChanged(this, exposureMode));
			}
		}
	}
}
