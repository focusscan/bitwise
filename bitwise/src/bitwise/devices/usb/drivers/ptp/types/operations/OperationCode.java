package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.Code;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class OperationCode extends Code {
	public static final OperationCode getDeviceInfo = new OperationCode((short) 0x1001);
	public static final OperationCode openSession = new OperationCode((short) 0x1002);
	public static final OperationCode closeSession = new OperationCode((short) 0x1003);
	public static final OperationCode getDevicePropDesc = new OperationCode((short) 0x1014);
	public static final OperationCode setDevicePropValue = new OperationCode((short) 0x1016);
	
	public static final Decoder<OperationCode> decoder = new Decoder<OperationCode>() {
		@Override
		public OperationCode decode(ByteBuffer in) {
			return new OperationCode(in);
		}
	};
	
	protected OperationCode(ByteBuffer in) {
		super(in);
	}
	
	protected OperationCode(short in_value) {
		super(in_value);
	}
}
