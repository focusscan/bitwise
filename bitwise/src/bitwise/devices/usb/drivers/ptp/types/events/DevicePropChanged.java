package bitwise.devices.usb.drivers.ptp.types.events;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class DevicePropChanged extends Event {
	public static final Decoder<DevicePropChanged> decoder = new Decoder<DevicePropChanged>() {
		@Override
		public DevicePropChanged decode(ByteBuffer in) {
			return new DevicePropChanged(in);
		}
	};
	
	private final DevicePropCode devicePropCode;
	
	protected DevicePropChanged(ByteBuffer in) {
		super("DevicePropChanged", EventCode.devicePropChanged);
		devicePropCode = DevicePropCode.decoder.decode(in);
		System.out.println(String.format("devicePropCode %s", devicePropCode));
	}
	
	public DevicePropCode getDevicePropCode() {
		return devicePropCode;
	}
}
