package bitwise.devices.usb.drivers.ptp.types;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class TransactionID extends UInt32 {
	public static final TransactionID zero = new TransactionID(0);
	public static final Decoder<TransactionID> decoder = new Decoder<TransactionID>() {
		@Override
		public TransactionID decode(ByteBuffer in) {
			return new TransactionID(in);
		}
	};
	
	protected TransactionID(ByteBuffer in) {
		super(in);
	}
	
	public TransactionID(int in_value) {
		super(in_value);
	}
}
