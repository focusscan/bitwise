package bitwise.apps.focusscan;

import bitwise.devices.camera.CameraHandle;
import bitwise.engine.service.BaseServiceTask;
import bitwise.engine.service.Request;

public class LiveViewTask extends BaseServiceTask<FocusScan> {
	private final CameraHandle cameraHandle;
	
	protected LiveViewTask(FocusScan in_service, CameraHandle in_cameraHandle) {
		super(in_service);
		cameraHandle = in_cameraHandle;
	}

	@Override
	protected void taskMain() throws InterruptedException {
		while (!isCancelled()) {
			Request r = cameraHandle.getLiveViewImage(getService());
			r.awaitServed();
			Thread.sleep(100);
		}
	}
}
