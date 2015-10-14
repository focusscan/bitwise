package bitwise.devices.nikon;

import bitwise.devices.usbptpcamera.BaseUsbPtpCameraHandle;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;

public class NikonHandle extends BaseUsbPtpCameraHandle<BaseNikon> {
	private final NikonPropertyFactory propertyFactory = new NikonPropertyFactory();
	
	protected NikonHandle(BaseNikon in_service) {
		super(in_service);
	}
	
	@Override
	public CameraPropertyFactory getCameraPropertyFactory() {
		return propertyFactory;
	}
}
