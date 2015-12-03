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
		getService().setCameraListener(in);
	}
	
	@Override
	public GetCameraInfoRequest getCameraInfo(GetCameraInfoRequester requester) {
		GetCameraInfo<A> r = new GetCameraInfo<A>(getService(), requester);
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<List<ImageFormat>> getImageFormats(GetPropertyRequester requester) {
		GetImageFormats<A> r = new GetImageFormats<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<List<StorageDevice>> getStorageDevices(GetPropertyRequester requester) {
		GetStorageDevices<A> r = new GetStorageDevices<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<BatteryLevel> getBatteryLevel(GetPropertyRequester requester) {
		GetBatteryLevel<A> r = new GetBatteryLevel<A>(getService(), requester, getCameraPropertyFactory());
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public GetPropertyRequest<WhiteBalanceMode> getWhiteBalanceMode(GetPropertyRequester requester) {
		GetWhiteBalanceMode<A> r = new GetWhiteBalanceMode<A>(getService(), requester, getCameraPropertyFactory());
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
	
	@Override
	public SetPropertyRequest<WhiteBalanceMode> setWhiteBalanceMode(SetPropertyRequester requester, WhiteBalanceMode in) {
		SetWhiteBalanceMode<A> r = new SetWhiteBalanceMode<A>(getService(), requester, in);
		this.enqueueRequest(r);
		return r;
	}
	
	@Override
	public SetPropertyRequest<ExposureTime> setExposureTime(SetPropertyRequester requester, ExposureTime in) {
		SetExposureTime<A> r = new SetExposureTime<A>(getService(), requester, in);
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public SetPropertyRequest<FNumber> setFNumber(SetPropertyRequester requester, FNumber in) {
		SetFNumber<A> r = new SetFNumber<A>(getService(), requester, in);
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public SetPropertyRequest<Iso> setIso(SetPropertyRequester requester, Iso in) {
		SetIso<A> r = new SetIso<A>(getService(), requester, in);
		this.enqueueRequest(r);
		return r;
	}

	@Override
	public TakeImageRequest takeImage(TakeImageRequester requester, ImageFormat imageFormat,
			StorageDevice storageDevice) {
		TakeImage<A> r = new TakeImage<A>(getService(), requester, imageFormat, storageDevice);
		this.enqueueRequest(r);
		return r;
	}
}
