package bitwise.devices.usb.drivers.ptp.types.deviceproperties;

import java.nio.ByteBuffer;

import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class PtpFocusMode extends UInt16 {
	public static final PtpFocusMode manual = new PtpFocusMode((short) 0x0001);
	public static final PtpFocusMode automatic = new PtpFocusMode((short) 0x0002);
	public static final PtpFocusMode automaticMacro = new PtpFocusMode((short) 0x0003);
	
	public PtpFocusMode(short in) {
		super(in);
	}
	
	public PtpFocusMode(ByteBuffer in) {
		super(in);
	}
	
	public FocusMode getFocusMode() {
		if (this.equals(PtpFocusMode.manual))
			return FocusMode.Manual;
		if (this.equals(PtpFocusMode.automatic))
			return FocusMode.Automatic;
		if (this.equals(PtpFocusMode.automaticMacro))
			return FocusMode.AutomaticMacro;
		return FocusMode.Unknown;
	}
}
