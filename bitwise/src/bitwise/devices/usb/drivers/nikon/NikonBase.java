package bitwise.devices.usb.drivers.nikon;

import bitwise.apps.App;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.ptp.PTPCamera;

public abstract class NikonBase extends PTPCamera {
	private static final byte interfaceAddr = (byte)0x00;
	private static final byte dataInEPNum = (byte)0x02;
	private static final byte dataOutEPNum = (byte)0x81;
	private static final byte interruptEPNum = (byte)0x83;
	
	public NikonBase(App in_app) {
		super(interfaceAddr, dataOutEPNum, dataInEPNum, interruptEPNum, in_app);
	}
	
	@Override
	protected boolean onPtpInitialize(UsbContext ctx) {
		return true;
	}
	
	@Override
	protected void onPtpDisable() {
		
	}
}
