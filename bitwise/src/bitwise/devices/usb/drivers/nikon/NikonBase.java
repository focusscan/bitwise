package bitwise.devices.usb.drivers.nikon;

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
import bitwise.devices.kinds.fullcamera.events.*;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.ptp.PtpCamera;
import bitwise.devices.usb.drivers.ptp.types.events.Event;

public abstract class NikonBase extends PtpCamera implements FullCamera {
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
	
	@Override
	protected boolean handleEvent(Event preEvent) {
		return super.handleEvent(preEvent);
	}
	
	@Override
	public void fetchAllCameraProperties() {
		System.out.println("checking battery properties");
		updateBatteryLevel();
		System.out.println("checking other properties");
		updateExposureIndex();
		updateExposureMode();
		updateExposureTime();
		updateFlashMode();
		updateFNumber();
		updateFocalLength();
		updateFocusMode();
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
	
	private ExposureMode decode_exposureMode(short in, boolean user) {
		return new ExposureMode(String.format("%04x", in), in, user);
	}
	
	@Override
	public synchronized void exposureModeChanged(short in, short[] in2) {
		prop_exposureMode = decode_exposureMode(in, false);
		prop_exposureModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_exposureModeValid.add(decode_exposureMode(v, v != in));
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
		prop_fNumber = new FNumber(in, false);
		prop_fNumberValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_fNumberValid.add(new FNumber(v, v != in));
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
	
	private FocusMode decode_focusMode(short in, boolean user) {
		return new FocusMode(String.format("%04x", in), in, user);
	}
	
	@Override
	public synchronized void focusModeChanged(short in, short[] in2) {
		prop_focusMode = decode_focusMode(in, false);
		prop_focusModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_focusModeValid.add(decode_focusMode(v, v != in));
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
	
	private FlashMode decode_flashMode(short in, boolean user) {
		return new FlashMode(String.format("%04x", in), in, user);
	}
	
	@Override
	public synchronized void flashModeChanged(short in, short[] in2) {
		prop_flashMode = decode_flashMode(in, false);
		prop_flashModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_flashModeValid.add(decode_flashMode(v, v != in));
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
	
	private ExposureTime decode_exposureTime(int in, boolean user) {
		int whole = in / 10000;
		int part  = in % 10000;
		return new ExposureTime(String.format("%s.%ss", whole, part), in, user);
	}
	
	@Override
	public synchronized void exposureTimeChanged(int in, int[] in2) {
		prop_exposureTime = decode_exposureTime(in, false);
		prop_exposureTimeValid = new ArrayList<>(in2.length);
		for (int v : in2)
			prop_exposureTimeValid.add(decode_exposureTime(v, v != in));
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
	
	private ExposureIndex decode_exposureIndex(short in, boolean user) {
		return new ExposureIndex(String.format("%s", in), in, user);
	}
	
	@Override
	public synchronized void exposureIndexChanged(short in, short in2[]) {
		prop_exposureIndex = decode_exposureIndex(in, false);
		prop_exposureIndexValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_exposureIndexValid.add(decode_exposureIndex(v, v != in));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureIndexChanged(this, prop_exposureIndex, prop_exposureIndexValid));
	}
}
