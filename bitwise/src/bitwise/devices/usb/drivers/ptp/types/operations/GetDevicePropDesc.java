package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceProperty;

public class GetDevicePropDesc extends Operation<DeviceProperty> {
	private final DevicePropCode devicePropCode;
	
	public GetDevicePropDesc(DevicePropCode in_devicePropCode) {
		super("GetDevicePropValue", OperationCode.getDevicePropDesc, 1, null);
		devicePropCode = in_devicePropCode;
		getArguments().add(devicePropCode.asInt32());
	}
	
	private DeviceProperty data = null;
	
	@Override
	public DeviceProperty getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = DeviceProperty.decoder.decode(in);
		return true;
	}
}
