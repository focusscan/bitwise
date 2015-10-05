package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.responses.DeviceProperty;
import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDevicePropDesc extends BaseOperation<DeviceProperty> {
	public static final UInt16 operationCode = new UInt16((short) 0x1014);
	
	private final DevicePropCode devicePropCode;
	
	public GetDevicePropDesc(TransactionID id, DevicePropCode in_devicePropCode) {
		super("GetDevicePropValue", operationCode, id, new ArrayList<>(1));
		devicePropCode = in_devicePropCode;
		getArgs().add(devicePropCode.asInt32());
	}

	@Override
	public DeviceProperty decodeResponse(ByteBuffer in) {
		return new DeviceProperty(in);
	}
}
