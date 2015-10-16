package bitwise.apps.focusscan;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import bitwise.apps.focusscan.gui.Completed;
import bitwise.apps.focusscan.gui.Scan;
import bitwise.apps.focusscan.gui.ScanSetup;
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

public class FocusScanStateScan extends FocusScanState {
	private final int steps;
	private final int stepsPerImage;
	private volatile Scan scan = null;
	private volatile ScanTask scanTask = null;
	
	public FocusScanStateScan(FocusScan in_app, int in_steps, int in_stepsPerImage) {
		super(in_app);
		steps = in_steps;
		stepsPerImage = in_stepsPerImage;
	}

	@Override
	public FocusScanState updateBatteryLevel(BatteryLevel in) {
		// TODO
		return this;
	}

	@Override
	public FocusScanState updateExposureTime(ExposureTime in, List<ExposureTime> values) {
		// TODO
		return this;
	}

	@Override
	public FocusScanState updateFNumber(FNumber in, List<FNumber> values) {
		// TODO
		return this;
	}

	@Override
	public FocusScanState updateFocalLength(FocalLength in) {
		// TODO
		return this;
	}

	@Override
	public FocusScanState updateIso(Iso in, List<Iso> values) {
		// TODO
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
		throw new FocusScanException();
	}

	@Override
	public FocusScanState takeTestImage() throws FocusScanException {
		throw new FocusScanException();
	}
	
	@Override
	public FocusScanState startScan(int steps, int stepsPerImage) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState scanHello(Scan in) throws FocusScanException {
		scan = in;
		scanTask = new ScanTask(getApp(), getApp().cameraHandle, steps, stepsPerImage);
		getApp().statedo_addServiceTask(scanTask);
		return this;
	}
	
	@Override
	public FocusScanState scanNewImage(byte[] lv, byte[] si) throws FocusScanException {
		Image imageLV = null;
		Image imageSI = null;
		try {
			ByteArrayInputStream imageStream = new ByteArrayInputStream(lv);
			BufferedImage imageBuffered;
				imageBuffered = ImageIO.read(imageStream);
			imageLV = SwingFXUtils.toFXImage(imageBuffered, null);
		} catch (IOException e) {
			Log.logException(getApp(), e);
		}
		try {
			ByteArrayInputStream imageStream = new ByteArrayInputStream(si);
			BufferedImage imageBuffered;
				imageBuffered = ImageIO.read(imageStream);
			imageSI = SwingFXUtils.toFXImage(imageBuffered, null);
		} catch (IOException e) {
			Log.logException(getApp(), e);
		}
		final Image theLV = imageLV;
		final Image theSI = imageSI;
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (null != theLV)
					scan.setImageLV(theLV);
				if (null != theSI)
					scan.setImageSI(theSI);
			}
		});

		return this;
	}
	
	private FocusScanState gotoCompleted() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Completed.showCompleted(getApp(), getApp().stage);
			}
		});
		return new FocusScanStateComplete(getApp());
	}

	@Override
	public FocusScanState scanCancelled() throws FocusScanException {
		if (null != scanTask)
			scanTask.cancel();
		return gotoCompleted();
	}
	
	@Override
	public FocusScanState scanComplete() throws FocusScanException {
		return gotoCompleted();
	}
	
	@Override
	public FocusScanState completedHello(Completed in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewOn(LiveViewOnRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewOff(LiveViewOffRequest in) throws FocusScanException {
		throw new FocusScanException();
	}

	@Override
	public FocusScanState notifyLiveViewImage(GetLiveViewImageRequest in) throws FocusScanException {
		return this;
	}

	@Override
	public FocusScanState notifyImage(TakeImageLVRequest in) throws FocusScanException {
		return this;
	}
}
