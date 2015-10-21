package bitwise.devices.usbptpcamera.operations;

public class DevicePropCode {
	public static final short ptpMask		= (short) 0x5000;
	public static final short vendorMask	= (short) 0xd000;
	
	public static final short batteryLevel				= (short) 0x5001;
	public static final short functionalMode			= (short) 0x5002;
	public static final short imageSize					= (short) 0x5003;
	public static final short compressionSetting		= (short) 0x5004;
	public static final short whiteBalance				= (short) 0x5005;
	public static final short rgbGain					= (short) 0x5006;
	public static final short fNumber					= (short) 0x5007;
	public static final short focalLength				= (short) 0x5008;
	public static final short focusDistance				= (short) 0x5009;
	public static final short focusMode					= (short) 0x500a;
	public static final short exposureMeteringMode		= (short) 0x500b;
	public static final short flashMode					= (short) 0x500c;
	public static final short exposureTime				= (short) 0x500d;
	public static final short exposureProgramMode		= (short) 0x500e;
	public static final short exposureIndex				= (short) 0x500f;
	public static final short exposureBiasCompensation	= (short) 0x5010;
	public static final short dateTime					= (short) 0x5011;
	public static final short captureDelay				= (short) 0x5012;
	public static final short stillCaptureMode			= (short) 0x5013;
	public static final short contrast					= (short) 0x5014;
	public static final short sharpness					= (short) 0x5015;
	public static final short digitalZoom				= (short) 0x5016;
	public static final short effectMode				= (short) 0x5017;
	public static final short burstNumber				= (short) 0x5018;
	public static final short burstInterval				= (short) 0x5019;
	public static final short timelapseNumber			= (short) 0x501a;
	public static final short timelapseInterval			= (short) 0x501b;
	public static final short focusMeteringMode			= (short) 0x501c;
	public static final short uploadUrl					= (short) 0x501d;
	public static final short artist					= (short) 0x501e;
	public static final short copyrightInfo				= (short) 0x501f;
	
	private DevicePropCode() {
		
	}
}
