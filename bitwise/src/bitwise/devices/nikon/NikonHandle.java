package bitwise.devices.nikon;

import bitwise.devices.nikon.NikonCameraRequest;
import bitwise.devices.usbptpcamera.UsbPtpCameraHandle;

public class NikonHandle extends UsbPtpCameraHandle<NikonCameraRequest, NikonBase> {
	protected NikonHandle(NikonBase in_service) {
		super(in_service);
	}
}
