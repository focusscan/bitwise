package bitwise.devices.usb.drivers.ptp;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfacePolicy;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;

import bitwise.apps.App;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.drivers.ptp.operations.CloseSession;
import bitwise.devices.usb.drivers.ptp.operations.GetDeviceInfo;
import bitwise.devices.usb.drivers.ptp.operations.GetDevicePropDesc;
import bitwise.devices.usb.drivers.ptp.operations.OpenSession;
import bitwise.devices.usb.drivers.ptp.operations.Operation;
import bitwise.devices.usb.drivers.ptp.operations.SetDevicePropValue;
import bitwise.devices.usb.drivers.ptp.responses.DeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.ContainerType;
import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import javafx.concurrent.Task;

public abstract class PTPCamera extends UsbDriver implements FullCamera {
	private final byte interfaceAddr;
	private final byte dataOutEPNum;
	private final byte dataInEPNum;
	private final byte interruptEPNum;
	private javax.usb.UsbConfiguration activeConfig;
	private javax.usb.UsbInterface ptpInterface;
	private javax.usb.UsbEndpoint dataOutEP;
	private javax.usb.UsbEndpoint dataInEP;
	private javax.usb.UsbEndpoint interruptEP;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe dataInPipe;
	private javax.usb.UsbPipe interruptPipe;
	private Task<Void> interruptTask;
	private DeviceInfo deviceInfo;
	
	public PTPCamera(byte in_interfaceAddr, byte in_dataOutEPNum, byte in_dataInEPNum, byte in_interruptEPNum, App in_app) {
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
			final PTPCamera camera = this;
			interruptTask = new Task<Void>() {
				private boolean keepRunning() {
					return !isCancelled() && camera.getResourceIsOpen().get();
				}
				
				@Override
				protected Void call() throws Exception {
					final int maxPacketSize = 0xffff & interruptEP.getUsbEndpointDescriptor().wMaxPacketSize();
					do {
						byte[] buf = new byte[maxPacketSize];
						javax.usb.UsbIrp irp = interruptPipe.asyncSubmit(buf);
						while (!irp.isComplete() && keepRunning())
							Thread.sleep(20);
						if (irp.isComplete()) {
							System.out.print("Interrupt data: ");
							for (byte b : buf)
								System.out.print(String.format("%02x", b));
							System.out.println("");
						}
					} while (keepRunning());
					System.out.println("Interrupt thread exiting");
					return null;
				}
			};
			Thread interruptThread = new Thread(interruptTask);
			interruptThread.setName(String.format("Interrupt task (%s)", this));
			interruptThread.start();
		}
		
		try {
			runOperation(new OpenSession());
			GetDeviceInfo getDeviceInfo = new GetDeviceInfo();
			runOperation(getDeviceInfo);
			deviceInfo = getDeviceInfo.getResponseData();
			runOperation(new GetDevicePropDesc(DevicePropCode.exposureProgramMode));
			runOperation(new SetDevicePropValue<>(DevicePropCode.exposureProgramMode, new UInt16((short) 0x0003)));
			runOperation(new GetDevicePropDesc(DevicePropCode.exposureProgramMode));
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
	
	protected void runOperation(Operation operation) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, InterruptedException, UsbException {
		if (null == operation.getTransactionID())
			operation.setTransactionID(newTransactionID());
		// Send the command
		{
			ByteArrayOutputStream commandStream = new ByteArrayOutputStream();
			
			UInt32 length = new UInt32(12 + 4 * operation.getArguments().size());
			length.serialize(commandStream);
			ContainerType.containerTypeCommand.serialize(commandStream);
			operation.getCode().serialize(commandStream);
			operation.getTransactionID().serialize(commandStream);
			
			for (Int32 arg : operation.getArguments())
				arg.serialize(commandStream);
			javax.usb.UsbIrp outIrp = dataOutPipe.asyncSubmit(commandStream.toByteArray());
			while (!outIrp.isComplete())
				Thread.sleep(20);
			System.out.println(String.format("Command sent for %s (%s)", operation.getTransactionID(), operation.getOperationName()));
		}
		// Send any outgoing data associated with the command
		if (null != operation.getOutData()) {
			byte[] data = null;
			{
				ByteArrayOutputStream dataOutStream = new ByteArrayOutputStream();
				operation.getOutData().serialize(dataOutStream);
				data = dataOutStream.toByteArray();
			}
			ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			UInt32 length = new UInt32(12 + data.length);
			length.serialize(dataStream);
			ContainerType.containerTypeData.serialize(dataStream);
			operation.getCode().serialize(dataStream);
			operation.getTransactionID().serialize(dataStream);
			for (byte b : data)
				dataStream.write(b);
			javax.usb.UsbIrp outIrp = dataOutPipe.asyncSubmit(dataStream.toByteArray());
			while (!outIrp.isComplete())
				Thread.sleep(20);
			System.out.println(String.format("Data sent for %s (%s)", operation.getTransactionID(), operation.getOperationName()));
		}
		// Read any responses
		while (null == operation.getResponseCode()) {
			byte[] incoming = new byte[dataInEP.getUsbEndpointDescriptor().wMaxPacketSize()];
			javax.usb.UsbIrp inIrp = dataInPipe.asyncSubmit(incoming);
			while (!inIrp.isComplete())
				Thread.sleep(20);
			ByteBuffer incomingBuffer = ByteBuffer.wrap(incoming);
			UInt32 length = UInt32.decoder.decode(incomingBuffer);
			UInt16 type = UInt16.decoder.decode(incomingBuffer);
			UInt16 code = UInt16.decoder.decode(incomingBuffer);
			TransactionID.decoder.decode(incomingBuffer);
			if (type.equals(ContainerType.containerTypeResponse)) {
				operation.setResponseCode(code);
				if (length.getValue() > 12) {
					ArrayList<Int32> arguments = new ArrayList<>((length.getValue() - 12) / 4);
					for (int i = 0; i < length.getValue(); i++)
						arguments.add(Int32.decoder.decode(incomingBuffer));
					operation.setResponseArguments(arguments);
				}
				System.out.println(String.format("Response read for %s (%s)", operation.getTransactionID(), operation.getOperationName()));
			}
			else if (type.equals(ContainerType.containerTypeData)) {
				operation.setResponseData(incomingBuffer);
				System.out.println(String.format("Response data read for %s (%s)", operation.getTransactionID(), operation.getOperationName()));
			}
		}
	}
}
