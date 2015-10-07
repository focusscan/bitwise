package bitwise.devices.usb.drivers.ptp.types.events;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class StoreRemoved extends Event {
	public static final Decoder<StoreRemoved> decoder = new Decoder<StoreRemoved>() {
		@Override
		public StoreRemoved decode(ByteBuffer in) {
			return new StoreRemoved(in);
		}
	};
	
	private final UInt32 storageID;
	
	protected StoreRemoved(ByteBuffer in) {
		super("StoreRemoved", EventCode.storeAdded);
		storageID = UInt32.decoder.decode(in);
	}
	
	public UInt32 getStorageID() {
		return storageID;
	}
}
