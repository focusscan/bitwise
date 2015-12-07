package bitwise.apps.focusscan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bitwise.apps.focusscan.scan.ScanFile;
import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.camera.DriveFocusRequest.Direction;
import bitwise.devices.camera.GetCameraInfoRequest;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.engine.service.BaseServiceTask;
import bitwise.log.Log;

public class ScanTask extends BaseServiceTask<FocusScan> {
	private final Path scanPath;
	private final int steps;
	private final int stepsPerImage;
	private final CameraHandle cameraHandle;
	private volatile boolean isPaused = false;
	
	protected ScanTask(FocusScan in_service, CameraHandle in_cameraHandle, Path in_scanPath, int in_steps, int in_stepsPerImage) {
		super(in_service);
		cameraHandle = in_cameraHandle;
		scanPath = in_scanPath;
		steps = in_steps;
		stepsPerImage = in_stepsPerImage;
	}
	
	public void pauseScan() {
		isPaused = true;
	}
	
	public void unpauseScan() {
		isPaused = false;
	}

	@Override
	protected void taskMain() throws InterruptedException {
		try {
			Files.createDirectories(scanPath);
			Path lvScan = scanPath.resolve("lvScan");
			Path siScan = scanPath.resolve("siScan");
			Files.createDirectory(lvScan);
			Files.createDirectory(siScan);
			Path scanFileOut = scanPath.resolve("scanManifest.xml");
			
			GetCameraInfoRequest cameraInfoRequest = cameraHandle.getCameraInfo(getService());
			cameraInfoRequest.awaitEpilogued();
			
			ScanFile scanFile = new ScanFile();
			scanFile.setCameraManufacturer(cameraInfoRequest.getCameraManufacturer());
			scanFile.setCameraModel(cameraInfoRequest.getCameraModel());
			scanFile.setCameraVersion(cameraInfoRequest.getCameraVersion());
			scanFile.setCameraSerial(cameraInfoRequest.getCameraSerial());
			scanFile.setStepsPerImage(stepsPerImage);
			scanFile.setStepsTotal(steps);
			
			// Write the scanFile to disk
			scanFile.saveToFile(scanFileOut);
			
			int imageNum = 1;
			int stepsTaken = 0;
			boolean takeAnother = true;
			while (!isCancelled() && takeAnother) {
				if (isPaused) {
					System.gc();
					while (isPaused && !isCancelled())
						Thread.sleep(200);
					if (isCancelled())
						break;
				}
				
				// Let the camera stabilize
				Thread.sleep(1000);
				
				// Take the pictures
				GetLiveViewImageRequest lvRequest = cameraHandle.getLiveViewImage(getService());
				lvRequest.awaitEpilogued();
				TakeImageLVRequest siRequest = cameraHandle.takeImageLV(getService());
				siRequest.awaitEpilogued();
				
				// Save the pictures to disk
				getService().notifyScannedImage(lvRequest.getImage(), siRequest.getImage());
				String imageName = String.format("image%06d.jpg", imageNum);
				Path lvImgPath = lvScan.resolve(imageName);
				Path siImgPath = siScan.resolve(imageName);
				Files.write(lvImgPath, lvRequest.getImage());
				Files.write(siImgPath, siRequest.getImage());
				
				// Add the pictures to the manifest
				String lvRel = scanPath.relativize(lvImgPath).toString();
				String siRel = scanPath.relativize(siImgPath).toString();
				scanFile.addData(imageNum, lvRel, siRel);
				
				// Write the scanFile to disk
				scanFile.saveToFile(scanFileOut);
				
				// Determine if we should take more pictures
				takeAnother = (stepsTaken + stepsPerImage <= steps);
				if (takeAnother) {
					DriveFocusRequest dfRequest = cameraHandle.driveFocus(getService(), Direction.TowardsFar, stepsPerImage, true);
					dfRequest.awaitEpilogued();
					Log.log(this, "Drive focus result: %s", dfRequest.getDriveFocusResult());
					takeAnother = dfRequest.getDriveFocusResult() == DriveFocusRequest.Result.FocusMoved;
					stepsTaken += stepsPerImage;
					imageNum++;
				}
			}
			if (!isCancelled()) {
				cameraHandle.driveFocus(getService(), Direction.TowardsNear, stepsTaken, true);
				getService().notifyScanComplete();
			}		} catch (IOException e) {
			Log.logException(this, e);
		}
	}
}
