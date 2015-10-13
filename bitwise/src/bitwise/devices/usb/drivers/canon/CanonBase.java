package bitwise.devices.usb.drivers.canon;

import java.util.ArrayList;
import java.util.List;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotOpenException;

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
import bitwise.devices.usb.drivers.canon.operations.GetEvent;
import bitwise.devices.usb.drivers.canon.operations.SetEventMode;
import bitwise.devices.usb.drivers.canon.operations.SetRemoteMode;
import bitwise.devices.usb.drivers.canon.responses.CanonEventResponse;
import bitwise.devices.usb.drivers.ptp.PtpCamera;
import bitwise.devices.usb.drivers.ptp.types.events.Event;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class CanonBase extends PtpCamera implements FullCamera {
	private static final byte interfaceAddr = (byte)0x00;
	private static final byte dataInEPNum = (byte)0x02;
	private static final byte dataOutEPNum = (byte)0x81;
	private static final byte interruptEPNum = (byte)0x83;
	
	private UInt32 prop_batteryLevel = null;
	private ExposureIndex prop_exposureIndex = null;
	private List<ExposureIndex> prop_exposureIndexValid = null;
	private ExposureMode prop_exposureMode = null;
	private List<ExposureMode> prop_exposureModeValid = null;
	private ExposureTime prop_exposureTime = null;
	private List<ExposureTime> prop_exposureTimeValid = null;
	private FNumber prop_fNumber = null;
	private List<FNumber> prop_fNumberValid = null;
	private FocusMode prop_focusMode = null;
	private List<FocusMode> prop_focusModeValid = null;
	private ImageFormat prop_imageFormat = null;
	private List<ImageFormat> prop_imageFormatValid = new ArrayList<ImageFormat>();
	
	private FlashMode prop_flashMode = null;
	private List<FlashMode> prop_flashModeValid = null;
	
	public CanonBase(App in_app) {
		super(interfaceAddr, dataOutEPNum, dataInEPNum, interruptEPNum, in_app);
	}
	
	@Override
	protected boolean onPtpInitialize(UsbContext ctx) {
		SetRemoteMode setRemoteMode = new SetRemoteMode();
		SetEventMode setEventMode = new SetEventMode();
		
		try {
			runOperation(setRemoteMode);
			runOperation(setEventMode);
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		return true;
	}
	
	@Override
	protected void onPtpDisable() {
		
	}
	
	@Override
	protected boolean handleEvent(Event preEvent) {
		return super.handleEvent(preEvent);
	}
	
	protected CanonEventResponse parseCameraEvent() {
		GetEvent getEvent = new GetEvent();
		try {
			runOperation(getEvent);
		} catch (UsbNotActiveException | UsbNotOpenException | UsbDisconnectedException | InterruptedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CanonEventResponse evtData = getEvent.getResponseData();
		return evtData;
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
	
	@Override
	protected void cmd_fetchAllCameraProperties() {
			CanonEventResponse evtData = parseCameraEvent();
//			prop_batteryLevel = evtData.getBatteryLevel();
			exposureIndexChanged(evtData.getExposureIndex(), evtData.getValidExposureIndices());
			exposureModeChanged(evtData.getExposureMode(), evtData.getValidExposureModes());
			exposureTimeChanged(evtData.getExposureTime(), evtData.getValidExposureTimes());
			fNumberChanged(evtData.getFNumber(), evtData.getValidFNumbers());
			focusModeChanged(evtData.getFocusMode(), evtData.getValidFocusModes());
			imageFormatChanged(evtData.getImageFormat(), evtData.getValidImageFormats());
	}
	
	private ImageFormat decode_imageFormat(short in) {
		String name = null;
		if (CanonDevicePropTables.ImageFormat.containsKey(in)) {
			name = CanonDevicePropTables.ImageFormat.get(in);
		}
		else {
			name = String.format("[code %04x]", in);
		}
		return new ImageFormat(name, in);
	}
	
	public synchronized void imageFormatChanged(short in, short[] in2) {
		prop_imageFormat = decode_imageFormat(in);
		prop_imageFormatValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_imageFormatValid.add(decode_imageFormat(v));
	}
	
	@Override
	public List<ImageFormat> getImageFormats() {
		return prop_imageFormatValid;
	}
	
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
		if (CanonDevicePropTables.ExposureMode.containsKey(in)) {
			name = CanonDevicePropTables.ExposureMode.get(in);
		}
		else {
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
	
	private FNumber decode_fNumber(short in) {
		String name = null;
		if (CanonDevicePropTables.Aperture.containsKey(in)) {
			name = "f/" + CanonDevicePropTables.Aperture.get(in);
		}
		else {
			name = String.format("[code %04x]", in);
		}
		return new FNumber(name, in);
	}
	
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
		prop_fNumber = decode_fNumber(in);
		prop_fNumberValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_fNumberValid.add(decode_fNumber(in));
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
		if (CanonDevicePropTables.FocusMode.containsKey(in)) {
			name = CanonDevicePropTables.FocusMode.get(in);
		}
		else {
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
	
	@Override
	public FlashMode getFlashMode() {
		return prop_flashMode;
	}
	
	@Override
	public List<FlashMode> getValidFlashModes() {
		return prop_flashModeValid;
	}
	
	private FlashMode decode_flashMode(short in) {
		return new FlashMode("N/A", (short)0xffff);
	}
	
	@Override
	public synchronized void flashModeChanged(short in, short[] in2) {
		prop_flashMode = decode_flashMode(in);
		prop_flashModeValid = new ArrayList<>(in2.length);
		for (short v : in2)
			prop_flashModeValid.add(decode_flashMode(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new FlashModeChanged(this, prop_flashMode, prop_flashModeValid));
	}
	
	@Override
	public ExposureTime getExposureTime() {
		return prop_exposureTime;
	}
	
	@Override
	public List<ExposureTime> getValidExposureTimes() {
		return prop_exposureTimeValid;
	}
	
	private ExposureTime decode_exposureTime(int in) {
		String name = null;
		if (CanonDevicePropTables.ShutterSpeed.containsKey(in)) {
			name = CanonDevicePropTables.ShutterSpeed.get(in);
		}
		else {
			name = String.format("[code %04x]", in);
		}
		return new ExposureTime(name, in);
	}
	
	@Override
	public synchronized void exposureTimeChanged(int in, int[] in2) {
		prop_exposureTime = decode_exposureTime(in);
		prop_exposureTimeValid = new ArrayList<>(in2.length);
		for (int v : in2)
			prop_exposureTimeValid.add(decode_exposureTime(v));
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new ExposureTimeChanged(this, prop_exposureTime, prop_exposureTimeValid));
	}
	
	@Override
	public ExposureIndex getExposureIndex() {
		return prop_exposureIndex;
	}
	
	@Override
	public List<ExposureIndex> getValidExposureIndicies() {
		return prop_exposureIndexValid;
	}
	
	private ExposureIndex decode_exposureIndex(short in) {
		String name = null;
		if (CanonDevicePropTables.ISO.containsKey(in)) {
			name = CanonDevicePropTables.ISO.get(in);
		} else {
			name = String.format("[code %04x]", in);
		}
		return new ExposureIndex(name, in);
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
