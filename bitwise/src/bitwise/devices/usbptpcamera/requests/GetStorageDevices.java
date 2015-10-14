package bitwise.devices.usbptpcamera.requests;

import java.util.ArrayList;
import java.util.List;

import bitwise.devices.camera.StorageDevice;
import bitwise.devices.camera.GetPropertyRequest;
import bitwise.devices.camera.GetPropertyRequester;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.BaseUsbPtpCameraRequest;
import bitwise.devices.usbptpcamera.CameraPropertyFactory;
import bitwise.devices.usbptpcamera.UsbPtpException;
import bitwise.devices.usbptpcamera.BaseUsbPtpCamera.StorageIDwInfo;
import bitwise.engine.service.RequestContext;
import bitwise.log.Log;

public class GetStorageDevices<A extends BaseUsbPtpCamera<?>> extends BaseUsbPtpCameraRequest<A, GetPropertyRequester> implements GetPropertyRequest<List<StorageDevice>> {
	private final CameraPropertyFactory propertyFactory;
	
	public GetStorageDevices(A in_service, GetPropertyRequester in_requester, CameraPropertyFactory in_propertyFactory) {
		super(in_service, in_requester);
		propertyFactory = in_propertyFactory;
	}

	private boolean success = false;
	private List<StorageDevice> value = null;
	
	@Override
	public boolean gotValues() {
		return success;
	}

	@Override
	public List<StorageDevice> getValue() {
		return value;
	}

	@Override
	public List<List<StorageDevice>> getLegalValues() {
		return null;
	}

	@Override
	public boolean canSet() {
		return false;
	}

	@Override
	protected void onServeRequest(RequestContext ctx) throws InterruptedException {
		try {
			List<StorageIDwInfo> stores = getService().getStorageIDs();
			if (null != stores) {
				value = new ArrayList<>(stores.size());
				for (StorageIDwInfo store : stores)
					value.add(propertyFactory.getStorageDevice(store));
				success = true;
			}
		} catch (UsbPtpException e) {
			Log.logException(this, e);
		}
	}

	@Override
	protected void onEpilogueRequest(RequestContext ctx) throws InterruptedException {
		// TODO Auto-generated method stub
		
	}
}
