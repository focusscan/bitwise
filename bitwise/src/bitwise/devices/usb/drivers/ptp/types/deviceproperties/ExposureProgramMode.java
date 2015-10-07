package bitwise.devices.usb.drivers.ptp.types.deviceproperties;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class ExposureProgramMode extends UInt16 {
	public static final ExposureProgramMode manual = new ExposureProgramMode((short) 0x0001);
	public static final ExposureProgramMode automatic = new ExposureProgramMode((short) 0x0002);
	public static final ExposureProgramMode aperturePriority = new ExposureProgramMode((short) 0x0003);
	public static final ExposureProgramMode shutterPriority = new ExposureProgramMode((short) 0x0004);
	public static final ExposureProgramMode programCreative = new ExposureProgramMode((short) 0x0005);
	public static final ExposureProgramMode programAction = new ExposureProgramMode((short) 0x0006);
	public static final ExposureProgramMode portrait = new ExposureProgramMode((short) 0x0007);
	
	public ExposureProgramMode(short in) {
		super(in);
	}
	
	public ExposureProgramMode(ByteBuffer in) {
		super(in);
	}
}
