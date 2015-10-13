package bitwise.devices.usb.drivers.canon.operations;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.canon.deviceproperties.CanonDevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.operations.Operation;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class SetDevicePropValueEx extends Operation<Void> {
	private final UInt32 len = new UInt32(0x0c);
	private final CanonDevicePropCode prop;
	private final UInt32 value;
	
	public SetDevicePropValueEx(CanonDevicePropCode p, UInt32 v) {
		super("SetCanonDevicePropValueEx", CanonOperationCode.setDevicePropValueEx, 0, null);
		prop = p;
		value = v;
	}
	
	@Override
	public PtpType getOutData() {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		
		len.serialize(data);
		prop.serialize(data);
		value.serialize(data);
		
		ByteBuffer bb = ByteBuffer.wrap(data.toByteArray());
		
		return UInt32.canonArrayDecoder.decode(bb);
	}
}
