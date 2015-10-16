package bitwise.apps.focusscan;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import bitwise.apps.focusscan.gui.Scan;
import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.apps.focusscan.gui.TestImage;
import bitwise.devices.camera.BatteryLevel;
import bitwise.devices.camera.ExposureTime;
import bitwise.devices.camera.FNumber;
import bitwise.devices.camera.FocalLength;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.Iso;
import bitwise.devices.camera.LiveViewOffRequest;
import bitwise.devices.camera.LiveViewOnRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.devices.usbservice.UsbReady;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.log.Log;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class FocusScanStateScanSetup extends FocusScanState {
	private volatile ScanSetup scanSetup = null;
	private volatile LiveViewTask liveViewTask = null;
	
	public FocusScanStateScanSetup(FocusScan in_app) {
		super(in_app);
	}
	
	@Override
	public FocusScanState updateBatteryLevel(BatteryLevel in) {
		if (null != scanSetup) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					scanSetup.setBatteryLevel(in);
				}
			});
		}
		return this;
	}

	@Override
	public FocusScanState updateExposureTime(ExposureTime in, List<ExposureTime> values) {
		if (null != scanSetup) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					scanSetup.setExposureTime(in, values);
				}
			});
		}
		return this;
	}

	@Override
	public FocusScanState updateFNumber(FNumber in, List<FNumber> values) {
		if (null != scanSetup) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					scanSetup.setFNumber(in, values);
				}
			});
		}
		return this;
	}

	@Override
	public FocusScanState updateFocalLength(FocalLength in) {
		if (null != scanSetup) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					scanSetup.setFocalLength(in);
				}
			});
		}
		return this;
	}

	@Override
	public FocusScanState updateIso(Iso in, List<Iso> values) {
		if (null != scanSetup) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					scanSetup.setIso(in, values);
				}
			});
		}
		return this;
	}
	
	@Override
	public FocusScanState selectDevice(UsbReady<?> ready) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState startUsbComplete(StartUsbDriver<?> in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState scanSetupHello(ScanSetup in) throws FocusScanException {
		scanSetup = in;
		getApp().cameraHandle.getBatteryLevel(getApp());
		getApp().cameraHandle.getExposureTime(getApp());
		getApp().cameraHandle.getFNumber(getApp());
		getApp().cameraHandle.getFocalLength(getApp());
		getApp().cameraHandle.getIso(getApp());
		getApp().cameraHandle.liveViewOn(getApp());
		return this;
	}
	
	@Override
	public FocusScanState takeTestImage() throws FocusScanException {
		getApp().cameraHandle.takeImageLV(getApp());
		return this;
	}
	
	@Override
	public FocusScanState startScan(int steps, int stepsPerImage) throws FocusScanException {
		liveViewTask.cancel();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Scan.showScan(getApp(), getApp().stage);
			}
		});
		return new FocusScanStateScan(getApp(), steps, stepsPerImage);
	}
	
	@Override
	public FocusScanState scanHello(Scan in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException {
		liveViewTask = new LiveViewTask(getApp(), getApp().cameraHandle);
		getApp().statedo_addServiceTask(liveViewTask);
		return this;
	}

	@Override
	public FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException {
		liveViewTask.cancel();
		return this;
	}

	@Override
	public FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException {
		if (null != in.getImage()) {
			try {
				ByteArrayInputStream imageStream = new ByteArrayInputStream(in.getImage());
				BufferedImage imageBuffered;
					imageBuffered = ImageIO.read(imageStream);
				final Image image = SwingFXUtils.toFXImage(imageBuffered, null);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						scanSetup.setImage(image);
					}
				});
			} catch (IOException e) {
				Log.logException(getApp(), e);
			}
		}
		return this;
	}

	@Override
	public FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException {
		if (null != in.getImage()) {
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
				Log.logException(getApp(), e);
			}
		}
		return this;
	}
}
