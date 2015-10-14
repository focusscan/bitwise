package bitwise.devices.usbptpcamera;

import java.util.List;

import bitwise.devices.BaseDriverHandle;
import bitwise.devices.camera.*;
import bitwise.devices.usbptpcamera.requests.*;

public abstract class BaseUsbPtpCameraHandle<A extends BaseUsbPtpCamera<?>> extends BaseDriverHandle<BaseUsbPtpCameraRequest<A, ?>, A> implements CameraHandle {
	protected BaseUsbPtpCameraHandle(A in_service) {
		super(in_service);
	}
	
	public abstract CameraPropertyFactory getCameraPropertyFactory();

	@Override
	public void setCameraEventListener(CameraListener in) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GetPropertyRequest<List<ImageFormat>> getImageFormats(GetPropertyRequester requester) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPropertyRequest<List<StorageDevice>> getStorageDevices(GetPropertyRequester requester) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GetPropertyRequest<BatteryLevel> getBatteryLevel(GetPropertyRequester requester) {
		GetBatteryLevel<A> r = new GetBatteryLevel<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<ExposureProgramMode> getExposureProgramMode(GetPropertyRequester requester) {
		GetExposureProgramMode<A> r = new GetExposureProgramMode<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}
	
	@Override
	public GetPropertyRequest<ExposureTime> getExposureTime(GetPropertyRequester requester) {
		GetExposureTime<A> r = new GetExposureTime<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<FlashMode> getFlashMode(GetPropertyRequester requester) {
		GetFlashMode<A> r = new GetFlashMode<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<FNumber> getFNumber(GetPropertyRequester requester) {
		GetFNumber<A> r = new GetFNumber<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<FocalLength> getFocalLength(GetPropertyRequester requester) {
		GetFocalLength<A> r = new GetFocalLength<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<FocusMode> getFocusMode(GetPropertyRequester requester) {
		GetFocusMode<A> r = new GetFocusMode<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<Iso> getIso(GetPropertyRequester requester) {
		GetIso<A> r = new GetIso<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}
}
