package bitwise.devices.usbptpcamera.requests;

import bitwise.devices.camera.GetCameraInfoRequest;
import bitwise.devices.camera.GetCameraInfoRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.engine.service.RequestContext;

public class GetCameraInfo<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetCameraInfoRequester> implements GetCameraInfoRequest {
	public GetCameraInfo(A in_service, GetCameraInfoRequester in_requester) {
		super(in_service, in_requester);
	}
	
	private String cameraManufacturer = "";
	private String cameraModel = "";
	private String cameraVersion = "";
	private String cameraSerial = "";
	
	@Override
	public String getCameraManufacturer() {
		return cameraManufacturer;
	}
	
	@Override
	public String getCameraModel() {
		return cameraModel;
	}
	
	@Override
	public String getCameraVersion() {
		return cameraVersion;
	}

	@Override
	public String getCameraSerial() {
		return cameraSerial;
	}
	
	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		cameraManufacturer = getService().getCameraManufacturer();
		cameraModel = getService().getCameraModel();
		cameraVersion = getService().getCameraVersion();
		cameraSerial = getService().getCameraSerial();
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		getRequester().notifyRequestComplete(this);
	}
}
