package bitwise.apps.focusscan;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import bitwise.apps.focusscan.gui.ScanSetup;
import bitwise.apps.focusscan.gui.TestImage;
import bitwise.devices.camera.GetLiveViewImageRequest;
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
	public ScanSetup getScanSetup() throws FocusScanException {
		return scanSetup;
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
