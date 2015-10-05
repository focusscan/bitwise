package bitwise.devices.usb.drivers.ptp.operations;

import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class SetDevicePropValue<P extends PtpType> extends Operation {
	public static final UInt16 operationCode = new UInt16((short) 0x1016);
	
	private final DevicePropCode devicePropCode;
	
	public SetDevicePropValue(DevicePropCode in_devicePropCode, P in_propValue) {
		super("SetDevicePropValue", operationCode, 1, in_propValue);
		devicePropCode = in_devicePropCode;
		getArguments().add(devicePropCode.asInt32());
	}
}
