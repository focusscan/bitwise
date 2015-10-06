package bitwise.devices.usb.drivers.nikon;

import java.util.List;

import bitwise.apps.App;
import bitwise.devices.kinds.fullcamera.ExposureMode;
import bitwise.devices.kinds.fullcamera.FNumber;
import bitwise.devices.kinds.fullcamera.FlashMode;
import bitwise.devices.kinds.fullcamera.FocusMode;
import bitwise.devices.kinds.fullcamera.FullCamera;
import bitwise.devices.kinds.fullcamera.events.*;
import bitwise.devices.usb.UsbContext;
import bitwise.devices.usb.drivers.ptp.PtpCamera;

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
	public void fetchAllCameraProperties() {
		System.out.println("checking battery properties");
		updateBatteryLevel();
		System.out.println("checking other properties");
		updateExposureIndex();
		updateExposureProgramMode();
		updateExposureTime();
		updateFlashMode();
		updateFNumber();
		updateFocalLength();
		updateFocusMode();
	}
	
	@Override
	public synchronized void exposureModeChanged(ExposureMode in) {
		super.exposureModeChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureModeChanged(this, getExposureMode()));
	}
	
	@Override
	public synchronized void fNumberChanged(FNumber in, List<FNumber> in2) {
		super.fNumberChanged(in, in2);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FNumberChanged(this, in, in2));
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
	
	@Override
	public synchronized void focusModeChanged(FocusMode in) {
		super.focusModeChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FocusModeChanged(this, in));
	}
	
	@Override
	public synchronized void flashModeChanged(FlashMode in) {
		super.flashModeChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FlashModeChanged(this, in));
	}
	
	@Override
	public synchronized void exposureTimeChanged(int in) {
		super.exposureTimeChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureTimeChanged(this, in));
	}
	
	@Override
	public synchronized void exposureIndexChanged(short in) {
		super.exposureIndexChanged(in);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureIndexChanged(this, in));
	}
}
