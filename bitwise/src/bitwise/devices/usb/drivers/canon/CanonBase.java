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
			short[] exivals = new short[1];
			exivals[0] = (short)evtData.getExposureIndex().getValue();
			exposureIndexChanged((short)evtData.getExposureIndex().getValue(), exivals);
			
			short[] exvals = new short[1];
			exvals[0] = (short)evtData.getExposureMode().getValue();
			exposureModeChanged((short)evtData.getExposureMode().getValue(), exvals);
			
			int[] extvals = new int[1];
			extvals[0] = evtData.getExposureTime().getValue();
			exposureTimeChanged(evtData.getExposureTime().getValue(), extvals);
			
			short[] fnvals = new short[1];
			fnvals[0] = (short)evtData.getFNumber().getValue();
			fNumberChanged((short)evtData.getFNumber().getValue(), fnvals);
			
			short[] focus = new short[1];
			focus[0] = (short)evtData.getFocusMode().getValue();
			focusModeChanged((short)evtData.getFocusMode().getValue(), focus);
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
/*		Arr<ObjectFormatCode> formats = getDeviceInfo().getCaptureFormats();
		ArrayList<ImageFormat> ret = new ArrayList<>(formats.getValue().size());
		for (ObjectFormatCode code : getDeviceInfo().getImageFormats().getValue())
			ret.add(decode_imageFormat(code.getValue()));
		return ret;*/
		return new ArrayList<ImageFormat>();
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
		switch (in) {
			case (short) 0x0000:
				name = "Program";
				break;
			case (short) 0x0001:
				name = "Tv";
				break;
			case (short) 0x0002:
				name = "Av";
				break;
			case (short) 0x0003:
				name = "Manual";
				break;
			case (short) 0x0004:
				name = "Bulb";
				break;
			case (short) 0x0009:
				name = "Auto";
				break;
			case (short) 0x0013:
				name = "Creative Auto";
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
			case (short) 0x0000:
				name = "One Shot";
				break;
			case (short) 0x0001:
				name = "AI Focus";
				break;
			case (short) 0x0002:
				name = "AI Servo";
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
		switch (in) {
		case 0:
			name = "30s";
			break;
		case 1:
			name = "25s";
			break;
		case 2:
			name = "20s";
			break;
		case 3:
			name = "15s";
			break;
		case 4:
			name = "13s";
			break;
		case 5:
			name = "10s";
			break;
		case 6:
			name = "8s";
			break;
		case 7:
			name = "6s";
			break;
		case 8:
			name = "5s";
			break;
		case 9:
			name = "4s";
			break;
		case 10:
			name = "3.2s";
			break;
		case 11:
			name = "2.5s";
			break;
		case 12:
			name = "2s";
			break;
		case 13:
			name = "1.6s";
			break;
		case 14:
			name = "1.3s";
			break;
		case 15:
			name = "1s";
			break;
		case 16:
			name = "0.8s";
			break;
		case 17:
			name = "0.6s";
			break;
		case 18:
			name = "0.5s";
			break;
		case 19:
			name = "0.4s";
			break;
		case 20:
			name = "0.3s";
			break;
		case 21:
			name = "1/4s";
			break;
		case 22:
			name = "1/5s";
			break;
		case 23:
			name = "1/6s";
			break;
		case 24:
			name = "1/8s";
			break;
		case 25:
			name = "1/10s";
			break;
		case 26:
			name = "1/13s";
			break;
		case 27:
			name = "1/15s";
			break;
		case 28:
			name = "1/20s";
			break;
		case 29:
			name = "1/25s";
			break;
		case 30:
			name = "1/30s";
			break;
		case 31:
			name = "1/40s";
			break;
		case 32:
			name = "1/50s";
			break;
		case 33:
			name = "1/60s";
			break;
		case 34:
			name = "1/80s";
			break;
		case 35:
			name = "1/100s";
			break;
		case 36:
			name = "1/125s";
			break;
		case 37:
			name = "1/160s";
			break;
		case 38:
			name = "1/200s";
			break;
		case 39:
			name = "1/250s";
			break;
		case 40:
			name = "1/320s";
			break;
		case 41:
			name = "1/400s";
			break;
		case 42:
			name = "1/500s";
			break;
		case 43:
			name = "1/640s";
			break;
		case 44:
			name = "1/800s";
			break;
		case 45:
			name = "1/1000s";
			break;
		case 46:
			name = "1/1250s";
			break;
		case 47:
			name = "1/1600s";
			break;
		case 48:
			name = "1/2000s";
			break;
		case 49:
			name = "1/2500s";
			break;
		case 50:
			name = "1/3200s";
			break;
		case 51:
			name = "1/4000s";
			break;
		case 52:
			name = "1/5000s";
			break;
		case 53:
			name = "1/6400s";
			break;
		case 0x63:
			name = "1/8000s";
			break;
			
		default:
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
		switch (in) {
			case (short) 0x0:
				name = "Auto";
				break;
			case (short) 0x40:
				name = "100";
				break;
			case (short) 0x50:
				name = "200";
				break;
			case (short) 0x58:
				name = "400";
				break;
			case (short) 0x60:
				name = "800";
				break;
			case (short) 0x68:
				name = "1600";
				break;
			case (short) 0x70:
				name = "3200";
				break;
			case (short) 0x78:
				name = "6400";
				break;
			case (short) 0x80:
				name = "12800";
				break;
			default:
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
