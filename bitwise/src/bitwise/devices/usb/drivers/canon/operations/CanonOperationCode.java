package bitwise.devices.usb.drivers.canon.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.Code;
import bitwise.devices.usb.drivers.ptp.types.operations.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class CanonOperationCode extends OperationCode {
	public static final CanonOperationCode getEvent = new CanonOperationCode((short) 0x9116);
	public static final CanonOperationCode getDeviceInfoEx = new CanonOperationCode((short) 0x9108);
	public static final CanonOperationCode getDevicePropValue = new CanonOperationCode((short) 0x9127);
	public static final CanonOperationCode setEventMode = new CanonOperationCode((short) 0x9115);
	public static final CanonOperationCode setRemoteMode = new CanonOperationCode((short) 0x9114);
	public static final CanonOperationCode setDevicePropValueEx = new CanonOperationCode((short) 0x9110);

	
	public static final Decoder<CanonOperationCode> decoder = new Decoder<CanonOperationCode>() {
		@Override
		public CanonOperationCode decode(ByteBuffer in) {
			return new CanonOperationCode(in);
		}
	};
	
	protected CanonOperationCode(ByteBuffer in) {
		super(in);
	}
	
	protected CanonOperationCode(short in_value) {
		super(in_value);
	}
}
