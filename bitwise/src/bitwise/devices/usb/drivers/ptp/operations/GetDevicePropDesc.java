package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.responses.DeviceProperty;
import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDevicePropDesc extends Operation {
	public static final UInt16 operationCode = new UInt16((short) 0x1014);
	
	private final DevicePropCode devicePropCode;
	
	public GetDevicePropDesc(DevicePropCode in_devicePropCode) {
		super("GetDevicePropValue", operationCode, 1, null);
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
