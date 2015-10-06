package bitwise.devices.usb.drivers.ptp.types.operations;

import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;

public class SetDevicePropValue<P extends PtpType> extends Operation {
	private final DevicePropCode devicePropCode;
	
	public SetDevicePropValue(DevicePropCode in_devicePropCode, P in_propValue) {
		super("SetDevicePropValue", OperationCode.setDevicePropValue, 1, in_propValue);
		devicePropCode = in_devicePropCode;
		getArguments().add(devicePropCode.asInt32());
	}
}
