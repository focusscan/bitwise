package bitwise.devices.usb.drivers.ptp;

import java.io.ByteArrayOutputStream;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;
import javax.usb.event.UsbPipeListener;

import bitwise.apps.App;
import bitwise.devices.kinds.FullCamera;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriver;
import bitwise.devices.usb.drivers.ptp.operations.GetDeviceInfo;

public abstract class PTPCamera extends UsbDriver implements FullCamera, UsbPipeListener {
	private final byte dataInEPNum;
	private final byte dataOutEPNum;
	private final byte interruptEPNum;
	private javax.usb.UsbEndpoint dataInEP;
	private javax.usb.UsbEndpoint dataOutEP;
	private javax.usb.UsbEndpoint interruptEP;
	private javax.usb.UsbPipe dataInPipe;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe interruptPipe;
	
	public PTPCamera(byte in_dataInEPNum, byte in_dataOutEPNum, byte in_interruptEPNum, App in_app, UsbDevice in_device) {
		super(in_app, in_device);
		dataInEPNum = in_dataInEPNum;
		dataOutEPNum = in_dataOutEPNum;
		interruptEPNum = in_interruptEPNum;
	}
	
	@Override
	public boolean initialize() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		if (!super.initialize())
			return false;
		dataInEP = getIface().getUsbEndpoint(dataInEPNum);
		dataOutEP = getIface().getUsbEndpoint(dataOutEPNum);
		interruptEP = getIface().getUsbEndpoint(interruptEPNum);
		assert(null != dataInEP);
		assert(null != dataOutEP);
		assert(null != interruptEP);
		System.out.println(String.format("dataInEP: %02x, type %02x", dataInEP.getUsbEndpointDescriptor().bEndpointAddress(), dataInEP.getType()));
		System.out.println(String.format("dataOutEP: %02x, type %02x", dataOutEP.getUsbEndpointDescriptor().bEndpointAddress(), dataOutEP.getType()));
		System.out.println(String.format("interruptEP: %02x, type %02x", interruptEP.getUsbEndpointDescriptor().bEndpointAddress(), interruptEP.getType()));
		
		dataInPipe = dataInEP.getUsbPipe();
		dataInPipe.open();
		dataInPipe.addUsbPipeListener(this);
		dataOutPipe = dataOutEP.getUsbPipe();
		dataOutPipe.open();
		dataOutPipe.addUsbPipeListener(this);
		interruptPipe = interruptEP.getUsbPipe();
		interruptPipe.open();
		interruptPipe.addUsbPipeListener(this);
		
		/*
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		GetDeviceInfo.instance.serialize(stream);
		byte[] out = stream.toByteArray();
		{
			System.out.print(String.format("PTPCamera sending %d bytes: ", out.length));
			for (byte b : out)
				System.out.print(String.format("%02x", b));
			System.out.println("");
		}
		System.out.println("Submitting...");
		int sent = dataInPipe.syncSubmit(out);
		System.out.println(String.format("Submitted %s bytes", sent));
		*/
		return true;
	}
	
	@Override
	public synchronized final void dataEventOccurred(UsbPipeDataEvent event) {
		System.out.println(String.format("dataEventOccurred(Pipe: %s)", event));
		byte data[] = event.getData();
		if (null != data) {
			System.out.print(String.format("Read %d bytes: ", data.length));
			for (byte b : data)
				System.out.print(String.format("%02x", b));
			System.out.println("");
		}
	}
	
	@Override
	public synchronized final void errorEventOccurred(UsbPipeErrorEvent event) {
		System.out.println(String.format("errorEventOccurred(Pipe: %s)", event));
	}
}
