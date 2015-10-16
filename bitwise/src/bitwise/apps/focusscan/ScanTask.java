package bitwise.apps.focusscan;

import bitwise.devices.camera.CameraHandle;
import bitwise.devices.camera.DriveFocusRequest;
import bitwise.devices.camera.DriveFocusRequest.Direction;
import bitwise.devices.camera.GetLiveViewImageRequest;
import bitwise.devices.camera.TakeImageLVRequest;
import bitwise.engine.service.BaseServiceTask;

public class ScanTask extends BaseServiceTask<FocusScan> {
	private final int steps;
	private final int stepsPerImage;
	private final CameraHandle cameraHandle;
	
	protected ScanTask(FocusScan in_service, CameraHandle in_cameraHandle, int in_steps, int in_stepsPerImage) {
		super(in_service);
		cameraHandle = in_cameraHandle;
		steps = in_steps;
		stepsPerImage = in_stepsPerImage;
	}

	@Override
	protected void taskMain() throws InterruptedException {
		// TODO: create scan object
		
		int stepsTaken = 0;
		boolean takeAnother = true;
		while (!isCancelled() && takeAnother) {
			GetLiveViewImageRequest lvRequest = cameraHandle.getLiveViewImage(getService());
			lvRequest.awaitEpilogued();
			TakeImageLVRequest siRequest = cameraHandle.takeImageLV(getService());
			siRequest.awaitEpilogued();
			
			getService().notifyScannedImage(lvRequest.getImage(), siRequest.getImage());
			// TODO: save scan images
			
			takeAnother = (stepsTaken + stepsPerImage <= steps);
			if (takeAnother) {
				DriveFocusRequest dfRequest = cameraHandle.driveFocus(getService(), Direction.TowardsFar, stepsPerImage, true);
				dfRequest.awaitEpilogued();
				stepsTaken += stepsPerImage;
			}
		}
		if (!isCancelled()) {
			cameraHandle.driveFocus(getService(), Direction.TowardsNear, stepsTaken, true);
			getService().notifyScanComplete();
		}
	}
}
