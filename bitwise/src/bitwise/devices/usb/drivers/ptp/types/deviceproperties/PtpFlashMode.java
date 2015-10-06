package bitwise.devices.usb.drivers.ptp.types.deviceproperties;

import java.nio.ByteBuffer;

import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class PtpFlashMode extends UInt16 {
	public static final PtpFlashMode automatic = new PtpFlashMode((short) 0x0001);
	public static final PtpFlashMode off = new PtpFlashMode((short) 0x0002);
	public static final PtpFlashMode fill = new PtpFlashMode((short) 0x0003);
	public static final PtpFlashMode redEyeAutomatic = new PtpFlashMode((short) 0x0004);
	public static final PtpFlashMode redEyeFill = new PtpFlashMode((short) 0x0005);
	public static final PtpFlashMode externalSync = new PtpFlashMode((short) 0x0006);
	
	public PtpFlashMode(short in) {
		super(in);
	}
	
	public PtpFlashMode(ByteBuffer in) {
		super(in);
	}
	
	public FlashMode getFlashMode() {
		if (this.equals(PtpFlashMode.automatic))
			return FlashMode.Automatic;
		if (this.equals(PtpFlashMode.off))
			return FlashMode.Off;
		if (this.equals(PtpFlashMode.fill))
			return FlashMode.Fill;
		if (this.equals(PtpFlashMode.redEyeAutomatic))
			return FlashMode.RedEyeAutomatic;
		if (this.equals(PtpFlashMode.redEyeFill))
			return FlashMode.RedEyeFill;
		if (this.equals(PtpFlashMode.externalSync))
			return FlashMode.ExternalSync;
		return FlashMode.Unknown;
	}
}
