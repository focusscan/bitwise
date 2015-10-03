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

public abstract class PTPCamera extends UsbDriver implements FullCamera {
	private final byte dataOutEPNum;
	private final byte dataInEPNum;
	private final byte interruptEPNum;
	private javax.usb.UsbEndpoint dataOutEP;
	private javax.usb.UsbEndpoint dataInEP;
	private javax.usb.UsbEndpoint interruptEP;
	private javax.usb.UsbPipe dataOutPipe;
	private javax.usb.UsbPipe dataInPipe;
	private javax.usb.UsbPipe interruptPipe;
	
	public PTPCamera(byte in_dataOutEPNum, byte in_dataInEPNum, byte in_interruptEPNum, App in_app, UsbDevice in_device) {
		super(in_app, in_device);
		dataOutEPNum = in_dataInEPNum;
		dataInEPNum = in_dataOutEPNum;
		interruptEPNum = in_interruptEPNum;
	}
	
	@Override
	public boolean initialize() throws UsbNotActiveException, UsbDisconnectedException, UsbException {
		if (!super.initialize())
			return false;
		dataOutEP = getIface().getUsbEndpoint(dataOutEPNum);
		dataInEP = getIface().getUsbEndpoint(dataInEPNum);
		interruptEP = getIface().getUsbEndpoint(interruptEPNum);
		assert(null != dataOutEP);
		assert(null != dataInEP);
		assert(null != interruptEP);
		System.out.println(String.format("dataInEP: %02x, type %02x", dataOutEP.getUsbEndpointDescriptor().bEndpointAddress(), dataOutEP.getType()));
		System.out.println(String.format("dataOutEP: %02x, type %02x", dataInEP.getUsbEndpointDescriptor().bEndpointAddress(), dataInEP.getType()));
		System.out.println(String.format("interruptEP: %02x, type %02x", interruptEP.getUsbEndpointDescriptor().bEndpointAddress(), interruptEP.getType()));
		
		dataOutPipe = dataOutEP.getUsbPipe();
		dataOutPipe.open();
		dataInPipe = dataInEP.getUsbPipe();
		dataInPipe.open();
		interruptPipe = interruptEP.getUsbPipe();
		interruptPipe.open();
		
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
		int sent = dataOutPipe.syncSubmit(out);
		System.out.println(String.format("Submitted %d bytes", sent));
		
		byte[] resp = new byte[dataInPipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize()];
		int recv = dataInPipe.syncSubmit(resp);
		System.out.println(String.format("Read %d bytes (max %d bytes)", recv, resp.length));
		
		return true;
	}
}
