package bitwise.apps.focusscan;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.camera.DriveFocusRequest.Direction;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.engine.service.BaseServiceTask;
import bitwise.log.Log;

public class ScanTask extends BaseServiceTask<FocusScan> {
	private final Path scanPath;
	private final int steps;
	private final int stepsPerImage;
	private final CameraHandle cameraHandle;
	
	protected ScanTask(FocusScan in_service, CameraHandle in_cameraHandle, Path in_scanPath, int in_steps, int in_stepsPerImage) {
		super(in_service);
		cameraHandle = in_cameraHandle;
		scanPath = in_scanPath;
		steps = in_steps;
		stepsPerImage = in_stepsPerImage;
	}

	@Override
	protected void taskMain() throws InterruptedException {
		try {
			Files.createDirectories(scanPath);
			Path lvScan = scanPath.resolve("lvScan");
			Path siScan = scanPath.resolve("siScan");
			Files.createDirectory(lvScan);
			Files.createDirectory(siScan);
			
			int imageNum = 1;
			int stepsTaken = 0;
			boolean takeAnother = true;
			while (!isCancelled() && takeAnother) {
				GetLiveViewImageRequest lvRequest = cameraHandle.getLiveViewImage(getService());
				lvRequest.awaitEpilogued();
				TakeImageLVRequest siRequest = cameraHandle.takeImageLV(getService());
				siRequest.awaitEpilogued();
				
				getService().notifyScannedImage(lvRequest.getImage(), siRequest.getImage());
				String imageName = String.format("image%06d.jpg", imageNum);
				Path lvImgPath = lvScan.resolve(imageName);
				Path siImgPath = siScan.resolve(imageName);
				Files.write(lvImgPath, lvRequest.getImage());
				Files.write(siImgPath, siRequest.getImage());
				
				takeAnother = (stepsTaken + stepsPerImage <= steps);
				if (takeAnother) {
					DriveFocusRequest dfRequest = cameraHandle.driveFocus(getService(), Direction.TowardsFar, stepsPerImage, true);
					dfRequest.awaitEpilogued();
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
