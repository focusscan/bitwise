package bitwise.devices.camera;

import java.util.List;

public interface CameraHandle {
	public void setCameraEventListener(CameraListener in);
	
	public LiveViewOnRequest liveViewOn(LiveViewOnRequester requester);
	public LiveViewOffRequest liveViewOff(LiveViewOffRequester requester);
	public GetLiveViewImageRequest getLiveViewImage(GetLiveViewImageRequester requester);
	public DriveFocusRequest driveFocus(DriveFocusRequester requester, DriveFocusRequest.Direction direction, int steps);
	
	public GetPropertyRequest<List<ImageFormat>> getImageFormats(GetPropertyRequester requester);
	public GetPropertyRequest<List<StorageDevice>> getStorageDevices(GetPropertyRequester requester);
	public GetPropertyRequest<BatteryLevel> getBatteryLevel(GetPropertyRequester requester);
	
	public GetPropertyRequest<ExposureProgramMode> getExposureProgramMode(GetPropertyRequester requester);
	public GetPropertyRequest<ExposureTime> getExposureTime(GetPropertyRequester requester);
	public GetPropertyRequest<FlashMode> getFlashMode(GetPropertyRequester requester);
	public GetPropertyRequest<FNumber> getFNumber(GetPropertyRequester requester);
	public GetPropertyRequest<FocalLength> getFocalLength(GetPropertyRequester requester);
	public GetPropertyRequest<FocusMode> getFocusMode(GetPropertyRequester requester);
	public GetPropertyRequest<Iso> getIso(GetPropertyRequester requester);
	
	public SetPropertyRequest<ExposureTime> setExposureTime(SetPropertyRequester requester, ExposureTime in);
	public SetPropertyRequest<FNumber> setFNumber(SetPropertyRequester requester, FNumber in);
	public SetPropertyRequest<Iso> setIso(SetPropertyRequester requester, Iso in);
	
	public TakeImageRequest takeImage(TakeImageRequester requester, ImageFormat imageFormat, StorageDevice storageDevice);
}
