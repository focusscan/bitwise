package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class DevicePropCode extends UInt16 {
	public static final DevicePropCode batteryLevel = new DevicePropCode((short) 0x5001);
	public static final DevicePropCode functionalMode = new DevicePropCode((short) 0x5002);
	public static final DevicePropCode imageSize = new DevicePropCode((short) 0x5003);
	public static final DevicePropCode compressionSetting = new DevicePropCode((short) 0x5004);
	public static final DevicePropCode whiteBalance = new DevicePropCode((short) 0x5005);
	public static final DevicePropCode rgbGain = new DevicePropCode((short) 0x5006);
	public static final DevicePropCode fNumber = new DevicePropCode((short) 0x5007);
	public static final DevicePropCode focalLength = new DevicePropCode((short) 0x5008);
	public static final DevicePropCode focalDistance = new DevicePropCode((short) 0x5009);
	public static final DevicePropCode focusMode = new DevicePropCode((short) 0x500a);
	public static final DevicePropCode exposureMeteringMode = new DevicePropCode((short) 0x500b);
	public static final DevicePropCode flashMode = new DevicePropCode((short) 0x500c);
	public static final DevicePropCode exposureTime = new DevicePropCode((short) 0x500d);
	public static final DevicePropCode exposureProgramMode = new DevicePropCode((short) 0x500e);
	public static final DevicePropCode exposureIndex = new DevicePropCode((short) 0x500f);
	public static final DevicePropCode exposureBiasCompensation = new DevicePropCode((short) 0x5010);
	public static final DevicePropCode dateTime = new DevicePropCode((short) 0x5011);
	public static final DevicePropCode captureDelay = new DevicePropCode((short) 0x5012);
	public static final DevicePropCode stillCaptureMode = new DevicePropCode((short) 0x5013);
	public static final DevicePropCode contrast = new DevicePropCode((short) 0x5014);
	public static final DevicePropCode sharpness = new DevicePropCode((short) 0x5015);
	public static final DevicePropCode digitalZoom = new DevicePropCode((short) 0x5016);
	public static final DevicePropCode effectMode = new DevicePropCode((short) 0x5017);
	public static final DevicePropCode burstNumber = new DevicePropCode((short) 0x5018);
	public static final DevicePropCode burstInterval = new DevicePropCode((short) 0x5019);
	public static final DevicePropCode timelapseNumber = new DevicePropCode((short) 0x501a);
	public static final DevicePropCode timelapseInterval = new DevicePropCode((short) 0x501b);
	public static final DevicePropCode focusMeteringMode = new DevicePropCode((short) 0x501c);
	public static final DevicePropCode uploadURL = new DevicePropCode((short) 0x501d);
	public static final DevicePropCode artist = new DevicePropCode((short) 0x501e);
	public static final DevicePropCode copyrightInfo = new DevicePropCode((short) 0x501f);


	public static final Decoder<DevicePropCode> decoder = new Decoder<DevicePropCode>() {
		@Override
		public DevicePropCode decode(ByteBuffer in) {
			return new DevicePropCode(in);
		}
	};
	
	public DevicePropCode(ByteBuffer in) {
		super(in);
	}
	
	public DevicePropCode(short in_value) {
		super(in_value);
	}
}
