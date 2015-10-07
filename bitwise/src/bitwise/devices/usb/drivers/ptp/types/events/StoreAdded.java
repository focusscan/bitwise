package bitwise.devices.usb.drivers.ptp.types.events;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class StoreAdded extends Event {
	public static final Decoder<StoreAdded> decoder = new Decoder<StoreAdded>() {
		@Override
		public StoreAdded decode(ByteBuffer in) {
			return new StoreAdded(in);
		}
	};
	
	private final UInt32 storageID;
	
	protected StoreAdded(ByteBuffer in) {
		super("StoreAdded", EventCode.storeAdded);
		storageID = UInt32.decoder.decode(in);
	}
	
	public UInt32 getStorageID() {
		return storageID;
	}
}
