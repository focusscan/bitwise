package bitwise.devices.canon;

import java.util.HashMap;

import bitwise.devices.usbptpcamera.operations.DevicePropCode;

public class CanonDeviceProperties {
	
	public static final int FNumber = 0xd101;
	public static final int ExposureTime = 0xd102;
	public static final int ExposureIndex = 0xd103;
	public static final int ExposureMode = 0xd105;
	public static final int FocusMode = 0xd108;
	public static final int BatteryLevel = 0xd111;
	public static final int CaptureTarget = 0xd11c;
	public static final int EVFOutputDevice = 0xd1b0;
	
	/* This is hack-ish... Not a real "property" */
	public static final int NewObjectReady = 0xffffc186;
	
	public enum ShutterState { 
		HalfPress(0x01), FullPress(0x02);
		private final int value;
		ShutterState(int v) {
			value = v;
		}
		public int getInt() {
			return value;
		}
	}
	
	public static short toPtpPropCode(int canonCode) {
		switch (canonCode) {
		case FNumber: return DevicePropCode.fNumber;
		case ExposureTime: return DevicePropCode.exposureTime;
		case ExposureIndex: return DevicePropCode.exposureIndex;
		case ExposureMode: return DevicePropCode.exposureProgramMode;
		case FocusMode: return DevicePropCode.focusMode;
		case BatteryLevel: return DevicePropCode.batteryLevel;
		
		/* Canon-specific properties just map to themselves */		
		default: return (short)canonCode;
		}
	}
	
	public static int toCanonPropCode(short ptpCode) {
		switch (ptpCode) {
		case DevicePropCode.fNumber: return FNumber;
		case DevicePropCode.exposureTime: return ExposureTime;
		case DevicePropCode.exposureIndex: return ExposureIndex;
		case DevicePropCode.exposureProgramMode: return ExposureMode;
		case DevicePropCode.focusMode: return FocusMode;
		case DevicePropCode.batteryLevel: return BatteryLevel;
		
		/* Canon-specific properties just map to themselves */
		default: return 0xffff & ptpCode;
		}
	}
	
	public static boolean isValidCode(int code) {
		return (code == FNumber ||
				code == ExposureTime ||
				code == ExposureIndex ||
				code == ExposureMode ||
				code == FocusMode ||
				code == BatteryLevel
				);
	}
	
	public static final HashMap<Integer,String> ShutterSpeedValues = new HashMap<>();
	public static final HashMap<Integer,String> ApertureValues = new HashMap<>();
	public static final HashMap<Integer,String> ExposureIndexValues = new HashMap<>();
	public static final HashMap<Integer,String> ExposureModeValues = new HashMap<>();
	public static final HashMap<Integer,String> FocusModeValues = new HashMap<>();
	
	static
	{
	ShutterSpeedValues.put(0x0, "Auto");
	ShutterSpeedValues.put(0x4, "Bulb");
	ShutterSpeedValues.put(0xc, "Bulb");
    ShutterSpeedValues.put(0x10, "30s");
    ShutterSpeedValues.put(0x13, "25s");
    ShutterSpeedValues.put(0x14, "20s");
    ShutterSpeedValues.put(0x15, "20s");
    ShutterSpeedValues.put(0x18, "15s");
    ShutterSpeedValues.put(0x1b, "13s");
    ShutterSpeedValues.put(0x1c, "10s");
    ShutterSpeedValues.put(0x1d, "10s");
    ShutterSpeedValues.put(0x20, "8s");
    ShutterSpeedValues.put(0x23, "6s");
    ShutterSpeedValues.put(0x24, "6s");
    ShutterSpeedValues.put(0x25, "5s");
    ShutterSpeedValues.put(0x28, "4s");
    ShutterSpeedValues.put(0x2b, "3.2s");
    ShutterSpeedValues.put(0x2c, "3s");
    ShutterSpeedValues.put(0x2d, "2.5s");
    ShutterSpeedValues.put(0x30, "2s");
    ShutterSpeedValues.put(0x33, "1.6s");
    ShutterSpeedValues.put(0x34, "1.5s");
    ShutterSpeedValues.put(0x35, "1.3s");
    ShutterSpeedValues.put(0x38, "1s");
    ShutterSpeedValues.put(0x3b, "0.8s");
    ShutterSpeedValues.put(0x3c, "0.7s");
    ShutterSpeedValues.put(0x3d, "0.6s");
    ShutterSpeedValues.put(0x40, "0.5s");
    ShutterSpeedValues.put(0x43, "0.4s");
    ShutterSpeedValues.put(0x44, "0.3s");
    ShutterSpeedValues.put(0x45, "0.3s");
    ShutterSpeedValues.put(0x48, "1/4s");
    ShutterSpeedValues.put(0x4b, "1/5s");
    ShutterSpeedValues.put(0x4c, "1/6s");
    ShutterSpeedValues.put(0x4d, "1/6s");
    ShutterSpeedValues.put(0x50, "1/8s");
    ShutterSpeedValues.put(0x53, "1/10s");
    ShutterSpeedValues.put(0x54, "1/10s");
    ShutterSpeedValues.put(0x55, "1/13s");
    ShutterSpeedValues.put(0x58, "1/15s");
    ShutterSpeedValues.put(0x5b, "1/20s");
    ShutterSpeedValues.put(0x5c, "1/20s");
    ShutterSpeedValues.put(0x5d, "1/25s");
    ShutterSpeedValues.put(0x60, "1/30s");
    ShutterSpeedValues.put(0x63, "1/40s");
    ShutterSpeedValues.put(0x64, "1/45s");
    ShutterSpeedValues.put(0x65, "1/50s");
    ShutterSpeedValues.put(0x68, "1/60s");
    ShutterSpeedValues.put(0x6b, "1/80s");
    ShutterSpeedValues.put(0x6c, "1/90s");
    ShutterSpeedValues.put(0x6d, "1/100s");
    ShutterSpeedValues.put(0x70, "1/125s");
    ShutterSpeedValues.put(0x73, "1/160s");
	ShutterSpeedValues.put(0x74, "1/180s");
    ShutterSpeedValues.put(0x75, "1/200s");
    ShutterSpeedValues.put(0x78, "1/250s");
    ShutterSpeedValues.put(0x7b, "1/320s");
    ShutterSpeedValues.put(0x7c, "1/350s");
    ShutterSpeedValues.put(0x7d, "1/400s");
    ShutterSpeedValues.put(0x80, "1/500s");
    ShutterSpeedValues.put(0x83, "1/640s");
    ShutterSpeedValues.put(0x84, "1/750s");
    ShutterSpeedValues.put(0x85, "1/800s");
    ShutterSpeedValues.put(0x88, "1/1000s");
    ShutterSpeedValues.put(0x8b, "1/1250s");
    ShutterSpeedValues.put(0x8c, "1/1500s");
    ShutterSpeedValues.put(0x8d, "1/1600s");
    ShutterSpeedValues.put(0x90, "1/2000s");
    ShutterSpeedValues.put(0x93, "1/2500s");
    ShutterSpeedValues.put(0x94, "1/3000s");
    ShutterSpeedValues.put(0x95, "1/3200s");
    ShutterSpeedValues.put(0x98, "1/4000s");
    ShutterSpeedValues.put(0x9b, "1/5000s");
    ShutterSpeedValues.put(0x9c, "1/6000s");
    ShutterSpeedValues.put(0x9d, "1/6400s");
    ShutterSpeedValues.put(0xa0, "1/8000s");
    
    ExposureIndexValues.put(0x28, "6");
    ExposureIndexValues.put(0x30, "12");
    ExposureIndexValues.put(0x38, "25");
    ExposureIndexValues.put(0x40, "50");
    ExposureIndexValues.put(0x43, "64");
    ExposureIndexValues.put(0x45, "80");
    ExposureIndexValues.put(0x48, "100");
    ExposureIndexValues.put(0x4b, "125");
    ExposureIndexValues.put(0x4d, "160");
    ExposureIndexValues.put(0x50, "200");
    ExposureIndexValues.put(0x53, "250");
    ExposureIndexValues.put(0x55, "320");
    ExposureIndexValues.put(0x58, "400");
    ExposureIndexValues.put(0x5b, "500");
    ExposureIndexValues.put(0x5d, "640");
    ExposureIndexValues.put(0x60, "800");
    ExposureIndexValues.put(0x63, "1000");
    ExposureIndexValues.put(0x65, "1250");
    ExposureIndexValues.put(0x68, "1600");
    ExposureIndexValues.put(0x6b, "2000");
    ExposureIndexValues.put(0x6d, "2500");
    ExposureIndexValues.put(0x70, "3200");
    ExposureIndexValues.put(0x73, "4000");
    ExposureIndexValues.put(0x75, "5000");
    ExposureIndexValues.put(0x78, "6400");
    ExposureIndexValues.put(0x7b, "8000");
    ExposureIndexValues.put(0x7d, "10000");
    ExposureIndexValues.put(0x80, "12800");
    ExposureIndexValues.put(0x83, "16000");
    ExposureIndexValues.put(0x85, "20000");
    ExposureIndexValues.put(0x88, "25600");
    ExposureIndexValues.put(0x90, "51200");
    ExposureIndexValues.put(0x98, "102400");
    ExposureIndexValues.put(0x00, "Auto");

    ApertureValues.put(0x00, "Auto");
    ApertureValues.put(0x08, "f/1");
    ApertureValues.put(0x0b, "f/1.1");
    ApertureValues.put(0x0c, "f/1.2");
    ApertureValues.put(0x0d, "f/1.2");
    ApertureValues.put(0x10, "f/1.4");
    ApertureValues.put(0x13, "f/1.6");
    ApertureValues.put(0x14, "f/1.8");
    ApertureValues.put(0x15, "f/1.8");
    ApertureValues.put(0x18, "f/2");
    ApertureValues.put(0x1b, "f/2.2");
    ApertureValues.put(0x1c, "f/2.5");
    ApertureValues.put(0x1d, "f/2.5");
    ApertureValues.put(0x20, "f/2.8");
    ApertureValues.put(0x23, "f/3.2");
    ApertureValues.put(0x24, "f/3.5");
    ApertureValues.put(0x25, "f/3.5");
    ApertureValues.put(0x28, "f/4");
    ApertureValues.put(0x2c, "f/4.5");
    ApertureValues.put(0x2b, "f/4.5");
    ApertureValues.put(0x2d, "f/5");
    ApertureValues.put(0x30, "f/5.6");
    ApertureValues.put(0x33, "f/6.3");
    ApertureValues.put(0x34, "f/6.7");
    ApertureValues.put(0x35, "f/7.1");
    ApertureValues.put(0x38, "f/8");
    ApertureValues.put(0x3b, "f/9");
    ApertureValues.put(0x3c, "f/9.5");
    ApertureValues.put(0x3d, "f/10");
    ApertureValues.put(0x40, "f/11");
    ApertureValues.put(0x43, "f/13");
    ApertureValues.put(0x44, "f/13");
    ApertureValues.put(0x45, "f/14");
    ApertureValues.put(0x48, "f/16");
    ApertureValues.put(0x4b, "f/18");
    ApertureValues.put(0x4c, "f/19");
    ApertureValues.put(0x4d, "f/20");
    ApertureValues.put(0x50, "f/22");
    ApertureValues.put(0x53, "f/25");
    ApertureValues.put(0x54, "f/27");
    ApertureValues.put(0x55, "f/29");
    ApertureValues.put(0x58, "f/32");
    ApertureValues.put(0x5b, "f/36");
    ApertureValues.put(0x5c, "f/38");
    ApertureValues.put(0x5d, "f/40");
    ApertureValues.put(0x60, "f/45");
    ApertureValues.put(0x63, "f/51");
    ApertureValues.put(0x64, "f/54");
    ApertureValues.put(0x65, "f/57");
    ApertureValues.put(0x68, "f/64");
    ApertureValues.put(0x6b, "f/72");

	ExposureModeValues.put(0x00, "Program");
	ExposureModeValues.put(0x01, "Tv");
	ExposureModeValues.put(0x02, "Av");
	ExposureModeValues.put(0x03, "Manual");
	ExposureModeValues.put(0x04, "Bulb");
	ExposureModeValues.put(0x09, "Auto");
	ExposureModeValues.put(0x13, "Creative Auto");
		
	FocusModeValues.put(0x00, "One Shot");
	FocusModeValues.put(0x01, "AI Focus");
	FocusModeValues.put(0x02, "AI Servo");
	}
}
