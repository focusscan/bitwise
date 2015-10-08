package bitwise.devices.usb.drivers.canon;

import java.util.ArrayList;
import java.util.List;

import bitwise.apps.App;
import bitwise.devices.kinds.fullcamera.ExposureIndex;
import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.ExposureTime;
import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.kinds.fullcamera.ImageFormat;
import bitwise.devices.kinds.fullcamera.StorageDevice;
import bitwise.devices.kinds.fullcamera.events.*;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.ptp.PtpCamera;
import bitwise.devices.usb.drivers.ptp.types.ObjectFormatCode;
import bitwise.devices.usb.drivers.ptp.types.events.Event;
import bitwise.devices.usb.drivers.ptp.types.prim.Arr;

public abstract class CanonBase extends PtpCamera implements FullCamera {
	private static final byte interfaceAddr = (byte)0x00;
	private static final byte dataInEPNum = (byte)0x02;
	private static final byte dataOutEPNum = (byte)0x81;
	private static final byte interruptEPNum = (byte)0x83;
	
	public CanonBase(App in_app) {
		super(interfaceAddr, dataOutEPNum, dataInEPNum, interruptEPNum, in_app);
	}
	
	@Override
	protected boolean onPtpInitialize(UsbContext ctx) {
		return true;
	}
	
	@Override
	protected void onPtpDisable() {
		
	}
	
	@Override
	protected boolean handleEvent(Event preEvent) {
		return super.handleEvent(preEvent);
	}
	
	private List<StorageDevice> storageDevices = null;
	
	@Override
	public List<StorageDevice> getStorageDevices() {
		return storageDevices;
	}
	
	@Override
	protected void storageDevicesChanged(List<StorageDevice> in) {
		storageDevices = in;
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new StorageDevicesChanged(this, storageDevices));
	}
	
	private ImageFormat decode_imageFormat(short in) {
		String name = null;
		switch (in) {
			case (short) 0x3000:
				name = "Nikon RAW";
				break;
			case (short) 0x3001:
				name = "Association";
				break;
			case (short) 0x3002:
				name = "Script";
				break;
			case (short) 0x3003:
				name = "Executable";
				break;
			case (short) 0x3004:
				name = "Text";
				break;
			case (short) 0x3005:
				name = "HTML";
				break;
			case (short) 0x3006:
				name = "DPOF";
				break;
			case (short) 0x3007:
				name = "AIFF";
				break;
			case (short) 0x3008:
				name = "WAV";
				break;
			case (short) 0x3009:
				name = "MP3";
				break;
			case (short) 0x300a:
				name = "AVI";
				break;
			case (short) 0x300b:
				name = "MPEG";
				break;
			case (short) 0x300c:
				name = "ASF";
				break;
			case (short) 0x3801:
				name = "EXIF/JPEG";
				break;
			case (short) 0x3802:
				name = "TIFF/EP";
				break;
			case (short) 0x3803:
				name = "FlashPix";
				break;
			case (short) 0x3804:
				name = "Windows BMP";
				break;
			case (short) 0x3805:
				name = "Canon CIFF";
				break;
			case (short) 0x3807:
				name = "GIF";
				break;
			case (short) 0x3808:
				name = "JFIF";
				break;
			case (short) 0x3809:
				name = "PCD";
				break;
			case (short) 0x380a:
				name = "Quickdraw PICT";
				break;
			case (short) 0x380b:
				name = "PNG";
				break;
			case (short) 0x380d:
				name = "TIFF";
				break;
			case (short) 0x380e:
				name = "TIFF/IT";
				break;
			case (short) 0x380f:
				name = "JP2";
				break;
			case (short) 0x3810:
				name = "JPX";
				break;
			default:
				name = String.format("[code %04x]", in);
		}
		return new ImageFormat(name, in);
	}
	
	@Override
	public List<ImageFormat> getImageFormats() {
		Arr<ObjectFormatCode> formats = getDeviceInfo().getCaptureFormats();
		ArrayList<ImageFormat> ret = new ArrayList<>(formats.getValue().size());
		for (ObjectFormatCode code : getDeviceInfo().getImageFormats().getValue())
			ret.add(decode_imageFormat(code.getValue()));
		return ret;
	}
	
	
	private ExposureMode prop_exposureMode = null;
	private List<ExposureMode> prop_exposureModeValid = null;
	
	@Override
	public ExposureMode getExposureMode() {
		return prop_exposureMode;
	}
	
	@Override
	public List<ExposureMode> getValidExposureModes() {
		return prop_exposureModeValid;
	}
	
	private ExposureMode decode_exposureMode(short in) {
		String name = null;
		switch (in) {
			case (short) 0x0001:
				name = "[M]anual";
				break;
			case (short) 0x0002:
				name = "[P]ortrait";
				break;
			case (short) 0x0003:
				name = "[A]perture Priority";
				break;
			case (short) 0x0004:
				name = "[S]hutter Priority";
				break;
			case (short) 0x8010:
				name = "AUTO";
				break;
			case (short) 0x8011:
				name = "Portrait";
				break;
			case (short) 0x8012:
				name = "Landscape";
				break;
			case (short) 0x8013:
				name = "Close up";
				break;
			case (short) 0x8014:
				name = "Sports";
				break;
			case (short) 0x8016:
				name = "Flash prohib";
				break;
			case (short) 0x8017:
				name = "Child";
				break;
			default:
				name = String.format("[code %04x]", in);
		}
		return new ExposureMode(name, in);
	}
	
	@Override
	public synchronized void exposureModeChanged(short in, short[] in2) {
		prop_exposureMode = decode_exposureMode(in);
		prop_exposureModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_exposureModeValid.add(decode_exposureMode(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureModeChanged(this, prop_exposureMode, prop_exposureModeValid));
	}
	
	private FNumber prop_fNumber = null;
	private List<FNumber> prop_fNumberValid = null;
	
	@Override
	public FNumber getFNumber() {
		return prop_fNumber;
	}
	
	@Override
	public List<FNumber> getValidFNumbers() {
		return prop_fNumberValid;
	}
	
	@Override
	protected void fNumberChanged(short in, short[] in2) {
		prop_fNumber = new FNumber(in);
		prop_fNumberValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_fNumberValid.add(new FNumber(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FNumberChanged(this, prop_fNumber, prop_fNumberValid));
	}
	
	@Override
	public synchronized void batteryLevelChanged(byte in) {
		super.batteryLevelChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new BatteryLevelChanged(this, in));
	}
	
	@Override
	public synchronized void focalLengthChanged(int in) {
		super.focalLengthChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FocalLengthChanged(this, in));
	}
	
	private FocusMode prop_focusMode;
	private List<FocusMode> prop_focusModeValid;
	
	@Override
	public FocusMode getFocusMode() {
		return prop_focusMode;
	}
	
	@Override
	public List<FocusMode> getValidFocusModes() {
		return prop_focusModeValid;
	}
	
	private FocusMode decode_focusMode(short in) {
		String name = null;
		switch (in) {
			case (short) 0x0001:
				name = "[M]anual";
				break;
			case (short) 0x8010:
				name = "[S]ingle AF";
				break;
			case (short) 0x8011:
				name = "[C]ontinuous AF";
				break;
			case (short) 0x8012:
				name = "[A]F auto switching";
				break;
			case (short) 0x8013:
				name = "[F] Constant AF";
				break;
			default:
				name = String.format("[code %04x]", in);
		}
		return new FocusMode(name, in);
	}
	
	@Override
	public synchronized void focusModeChanged(short in, short[] in2) {
		prop_focusMode = decode_focusMode(in);
		prop_focusModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_focusModeValid.add(decode_focusMode(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FocusModeChanged(this, prop_focusMode, prop_focusModeValid));
	}
	
	private FlashMode prop_flashMode;
	private List<FlashMode> prop_flashModeValid;
	
	@Override
	public FlashMode getFlashMode() {
		return prop_flashMode;
	}
	
	@Override
	public List<FlashMode> getValidFlashModes() {
		return prop_flashModeValid;
	}
	
	private FlashMode decode_flashMode(short in) {
		String name = null;
		switch (in) {
			case (short) 0x0002:
				name = "No flash";
				break;
			case (short) 0x0004:
				name = "Red-eye reduction";
				break;
			case (short) 0x8010:
				name = "Normal sync";
				break;
			case (short) 0x8011:
				name = "Slow sync";
				break;
			case (short) 0x8012:
				name = "Rear sync";
				break;
			case (short) 0x8013:
				name = "Red-eye reduction (slow sync)";
				break;
			default:
				name = String.format("[code %04x]", in);
		}
		return new FlashMode(name, in);
	}
	
	@Override
	public synchronized void flashModeChanged(short in, short[] in2) {
		prop_flashMode = decode_flashMode(in);
		prop_flashModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_flashModeValid.add(decode_flashMode(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FlashModeChanged(this, prop_flashMode, prop_flashModeValid));
	}
	
	private ExposureTime prop_exposureTime = null;
	private List<ExposureTime> prop_exposureTimeValid = null;
	
	@Override
	public ExposureTime getExposureTime() {
		return prop_exposureTime;
	}
	
	@Override
	public List<ExposureTime> getValidExposureTimes() {
		return prop_exposureTimeValid;
	}
	
	private ExposureTime decode_exposureTime(int in) {
		float time = ((float)in) / 10000;
		return new ExposureTime(String.format("%fs", time), in);
	}
	
	@Override
	public synchronized void exposureTimeChanged(int in, int[] in2) {
		prop_exposureTime = decode_exposureTime(in);
		prop_exposureTimeValid = new ArrayList<>(in2.length);
		for (int v : in2)
			prop_exposureTimeValid.add(decode_exposureTime(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureTimeChanged(this, prop_exposureTime, prop_exposureTimeValid));
	}
	
	private ExposureIndex prop_exposureIndex = null;
	private List<ExposureIndex> prop_exposureIndexValid = null;
	
	@Override
	public ExposureIndex getExposureIndex() {
		return prop_exposureIndex;
	}
	
	@Override
	public List<ExposureIndex> getValidExposureIndicies() {
		return prop_exposureIndexValid;
	}
	
	private ExposureIndex decode_exposureIndex(short in) {
		return new ExposureIndex(String.format("%s", in), in);
	}
	
	@Override
	public synchronized void exposureIndexChanged(short in, short in2[]) {
		prop_exposureIndex = decode_exposureIndex(in);
		prop_exposureIndexValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_exposureIndexValid.add(decode_exposureIndex(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureIndexChanged(this, prop_exposureIndex, prop_exposureIndexValid));
	}
}
