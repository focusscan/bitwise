package bitwise.apps.focusscan;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.imageio.ImageIO;

import bitwise.apps.BaseApp;
import bitwise.apps.focusscan.gui.CameraPropListener;
import bitwise.apps.focusscan.gui.Completed;
import bitwise.apps.focusscan.gui.DeviceSelect;
import bitwise.apps.focusscan.gui.Scan;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.apps.focusscan.gui.TestImage;
import bitwise.devices.camera.*;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.UsbServiceHandle;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.devices.usbservice.requests.StartUsbDriverRequester;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.supervisor.Supervisor;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public final class FocusScan extends BaseApp<FocusScanHandle> implements StartUsbDriverRequester, CameraListener, GetPropertyRequester, SetPropertyRequester, LiveViewOnRequester, LiveViewOffRequester, GetLiveViewImageRequester, DriveFocusRequester, TakeImageLVRequester, GetCameraInfoRequester {
	private final FocusScanHandle handle = new FocusScanHandle(this);
	protected FocusScanState state = null;
	protected Stage stage = null;
	protected CameraPropListener window = null;
	protected LiveViewTask liveViewTask = null;
	protected ScanTask scanTask = null;
	protected CameraHandle cameraHandle = null;
	
	protected FocusScan() {
		super();
	}
	
	@Override
	public FocusScanHandle getServiceHandle() {
		return handle;
	}

	@Override
	protected boolean onStartService() {
		Log.log(this, "Focus Scan starting");
		final CountDownLatch latch = new CountDownLatch(1);
		final FocusScan thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				stage = new Stage();
				stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						Log.log(thing, "Stage closed");
						stopServiceFromWithin();
					}
				});
				window = DeviceSelect.showDeviceSelect(thing, stage);
				state = FocusScanState.DeviceSelect;
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onStopService() {
		Log.log(this, "Focus Scan stopped");
	}

	@Override
	protected void onRequestComplete(BaseRequest<?, ?> in) {
		Log.log(this, "Focus Scan request complete");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void notifyRequestComplete(GetPropertyRequest<?> in) {
		if (!in.gotValues()) {
			Log.log(this, "Failed to get values: %s", in);
			return;
		}
		switch (in.getProperty()) {
		case BatteryLevel:
			window.updateBatteryLevel((BatteryLevel) in.getValue());
			break;
		case ExposureTime:
			window.updateExposureTime((ExposureTime) in.getValue(), (List<ExposureTime>) in.getLegalValues());
			break;
		case FNumber:
			window.updateFNumber((FNumber) in.getValue(), (List<FNumber>) in.getLegalValues());
			break;
		case FocalLength:
			window.updateFocalLength((FocalLength) in.getValue());
			break;
		case Iso:
			window.updateIso((Iso) in.getValue(), (List<Iso>) in.getLegalValues());
			break;
		case ExposureProgramMode:
		case FlashMode:
		case FocusMode:
		case StorageDevices:
		case ImageFormats:
		default:
			Log.log(this, "I sent this request but I don't know what to do with it: %s", in);
		}
	}

	@Override
	public void onCameraPropertyChanged(CameraHandle camera, CameraProperty property) {
		switch (property) {
		case BatteryLevel:
			camera.getBatteryLevel(this);
			break;
		case ExposureTime:
			camera.getExposureTime(this);
			break;
		case FNumber:
			camera.getFNumber(this);
			break;
		case FocalLength:
			camera.getFocalLength(this);
			break;
		case Iso:
			camera.getIso(this);
			break;
		case ExposureProgramMode:
		case FlashMode:
		case FocusMode:
		case StorageDevices:
		case ImageFormats:
		default:
			Log.log(this, "Property %s - %s changed, ignoring", camera, property);
		}
	}
	
	private volatile ExposureTime currentExposureTime = null;
	public void fxdo_setExposureTime(ExposureTime in) {
		if (null != cameraHandle && (null == currentExposureTime || !in.equals(currentExposureTime))) {
			currentExposureTime = in;
			cameraHandle.setExposureTime(this, in);
		}
	}
	
	private volatile FNumber currentFNumber = null;
	public void fxdo_setFNumber(FNumber in) {
		if (null != cameraHandle && (null == currentFNumber || !in.equals(currentFNumber))) {
			currentFNumber = in;
			cameraHandle.setFNumber(this, in);
		}
	}
	
	private volatile Iso currentIso = null;
	public void fxdo_setIso(Iso in) {
		if (null != cameraHandle && (null == currentIso || !in.equals(currentIso))) {
			currentIso = in;
			cameraHandle.setIso(this, in);
		}
	}
	
	@Override
	public void notifyRequestComplete(SetPropertyRequest<?> in) {
		switch (in.getProperty()) {
		case ExposureTime:
			cameraHandle.getExposureTime(this);
			break;
		case FNumber:
			cameraHandle.getFNumber(this);
			break;
		case Iso:
			cameraHandle.getIso(this);
			break;
		default:
			Log.log(this, "I sent this request but I don't know what to do with it: %s", in);
		}
	}
	
	public void fxdo_selectDevice(UsbReady<?> ready) {
		UsbServiceHandle usbService = Supervisor.getInstance().getUsbServiceHandle();
		usbService.startUsbDriver(this, ready);
	}
	
	@Override
	public synchronized void notifyRequestComplete(StartUsbDriver<?> in) {
		cameraHandle = (CameraHandle) in.getHandle();
		cameraHandle.setCameraEventListener(this);
		
		final CountDownLatch latch = new CountDownLatch(1);
		final FocusScan thing = this;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				window = ScanSetup.showScanSetup(thing, stage);
				state = FocusScanState.ScanSetup;
				latch.countDown();
			}
		});
		try {
			latch.await();
			cameraHandle.getBatteryLevel(this);
			cameraHandle.getExposureTime(this);
			cameraHandle.getFNumber(this);
			cameraHandle.getFocalLength(this);
			cameraHandle.getIso(this);
			cameraHandle.liveViewOn(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void notifyRequestComplete(LiveViewOnRequest in) {
		liveViewTask = new LiveViewTask(this, cameraHandle);
		this.addServiceTask(liveViewTask);
	}
	
	@Override
	public void notifyRequestComplete(LiveViewOffRequest in) {
	}

	@Override
	public void notifyRequestComplete(GetLiveViewImageRequest in) {
		switch (state) {
		case Completed:
			break;
		case DeviceSelect:
			break;
		case Scan:
			break;
		case ScanSetup:
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(in.getImage());
				BufferedImage imageBuffered;
					imageBuffered = ImageIO.read(imageStream);
				final Image image = SwingFXUtils.toFXImage(imageBuffered, null);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						((ScanSetup) window).setImage(image);
					}
				});
			} catch (IOException e) {
				Log.logException(this, e);
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void notifyRequestComplete(TakeImageLVRequest in) {
		switch (state) {
		case Completed:
			break;
		case DeviceSelect:
			break;
		case Scan:
			break;
		case ScanSetup:
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(in.getImage());
				BufferedImage imageBuffered;
					imageBuffered = ImageIO.read(imageStream);
				final Image image = SwingFXUtils.toFXImage(imageBuffered, null);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						TestImage testImage = TestImage.showTestImage(new Stage());
						testImage.setImage(image);
					}
				});
			} catch (IOException e) {
				Log.logException(this, e);
			}
			break;
		default:
			break;
		}
	}

	public void fxdo_takeTestImage() {
		cameraHandle.takeImageLV(this);
	}
	
	public void fxdo_focusNear(int steps) {
		cameraHandle.driveFocus(this, DriveFocusRequest.Direction.TowardsNear, steps, false);
	}
	
	public void fxdo_focusFar(int steps) {
		cameraHandle.driveFocus(this, DriveFocusRequest.Direction.TowardsFar, steps, false);
	}

	@Override
	public void notifyRequestComplete(DriveFocusRequest in) {
	}

	public synchronized void fxdo_scanNearToFar(Path scanName, int steps, int stepsPerImage) {
		if (null != liveViewTask) {
			liveViewTask.cancel();
			liveViewTask = null;
		}
		
		window = Scan.showScan(this, stage);
		state = FocusScanState.Scan;
		
		cameraHandle.getBatteryLevel(this);
		
		scanTask = new ScanTask(this, cameraHandle, scanName, steps, stepsPerImage);
		this.addServiceTask(scanTask);
	}
	
	public synchronized void fxdo_scanCancelled() {
		if (null != scanTask) {
			scanTask.cancel();
			scanTask = null;
		}
		cameraHandle.liveViewOff(this);
		
		window = Completed.showCompleted(this, stage);
		state = FocusScanState.Completed;
	}

	public synchronized void notifyScanComplete() {
		// This is invoked by scanTask on its way to exiting;
		// hence no need to cancel scanTask.
		scanTask = null;
		cameraHandle.liveViewOff(this);
		
		final CountDownLatch latch = new CountDownLatch(1);
		final FocusScan thing = this;
		// state = state.scanComplete();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				window = Completed.showCompleted(thing, stage);
				state = FocusScanState.Completed;
				latch.countDown();
			}
		});
		try {
			latch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void notifyScannedImage(byte[] lv, byte[] si) {
		Image imageLV = null;
		Image imageSI = null;
		if (null != lv) {
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(lv);
				BufferedImage imageBuffered;
					imageBuffered = ImageIO.read(imageStream);
				imageLV = SwingFXUtils.toFXImage(imageBuffered, null);
			} catch (IOException e) {
				Log.logException(this, e);
			}
		}
		if (null != si) {
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(si);
				BufferedImage imageBuffered;
					imageBuffered = ImageIO.read(imageStream);
				imageSI = SwingFXUtils.toFXImage(imageBuffered, null);
			} catch (IOException e) {
				Log.logException(this, e);
			}
		}
		final Image theLV = imageLV;
		final Image theSI = imageSI;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (null != theLV)
					((Scan) window).setImageLV(theLV);
				if (null != theSI)
					((Scan) window).setImageSI(theSI);
			}
		});
	}

	@Override
	public void notifyRequestComplete(GetCameraInfoRequest in) {
		// Nothing to do here
	}
}
