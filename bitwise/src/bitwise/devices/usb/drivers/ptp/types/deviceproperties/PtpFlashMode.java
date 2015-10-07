package bitwise.devices.usb.drivers.ptp.types.deviceproperties;

import java.nio.ByteBuffer;

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
}
