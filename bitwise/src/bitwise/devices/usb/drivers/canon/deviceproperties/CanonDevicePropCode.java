package bitwise.devices.usb.drivers.canon.deviceproperties;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.deviceproperties.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class CanonDevicePropCode extends DevicePropCode {
	
	public static final CanonDevicePropCode BatteryLevel = new CanonDevicePropCode((short) 0xd111);
	public static final CanonDevicePropCode ExposureIndex = new CanonDevicePropCode((short) 0xd103);
	public static final CanonDevicePropCode ExposureMode = new CanonDevicePropCode((short) 0xd105);
	public static final CanonDevicePropCode ExposureTime = new CanonDevicePropCode((short) 0xd102);
	public static final CanonDevicePropCode FNumber = new CanonDevicePropCode((short) 0xd101);
	public static final CanonDevicePropCode FocusMode = new CanonDevicePropCode((short) 0xd108);
	public static final CanonDevicePropCode ImageFormat = new CanonDevicePropCode((short) 0xd120);
	
	public static final Decoder<CanonDevicePropCode> decoder = new Decoder<CanonDevicePropCode>() {
		@Override
		public CanonDevicePropCode decode(ByteBuffer in) {
			return new CanonDevicePropCode(in);
		}
	};
	
	public CanonDevicePropCode(UInt32 in_value) {
		super((short)in_value.getValue());
	}
	
	protected CanonDevicePropCode(ByteBuffer in) {
		super(in);
	}
	
	protected CanonDevicePropCode(short in_value) {
		super(in_value);
	}
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		asInt32().serialize(stream);
	}
}
