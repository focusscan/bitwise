package bitwise.devices.usb.drivers.ptp.types.events;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.Code;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;

public class EventCode extends Code {
	public static final EventCode cancelTransaction = new EventCode((short) 0x4001);
	public static final EventCode objectAdded = new EventCode((short) 0x4002);
	public static final EventCode objectRemoved = new EventCode((short) 0x4003);
	public static final EventCode storeAdded = new EventCode((short) 0x4004);
	public static final EventCode storeRemoved = new EventCode((short) 0x4005);
	public static final EventCode devicePropChanged = new EventCode((short) 0x4006);
	public static final EventCode objectInfoChanged = new EventCode((short) 0x4007);
	public static final EventCode deviceInfoChanged = new EventCode((short) 0x4008);
	public static final EventCode requestObjectTransfer = new EventCode((short) 0x4009);
	public static final EventCode storeFull = new EventCode((short) 0x400a);
	public static final EventCode deviceReset = new EventCode((short) 0x400b);
	public static final EventCode storageInfoChanged = new EventCode((short) 0x400c);
	public static final EventCode captureComplete = new EventCode((short) 0x400d);
	public static final EventCode unreportedStatus = new EventCode((short) 0x400e);
	
	public static final Decoder<EventCode> decoder = new Decoder<EventCode>() {
		@Override
		public EventCode decode(ByteBuffer in) {
			return new EventCode(in);
		}
	};
	
	protected EventCode(ByteBuffer in) {
		super(in);
	}
	
	protected EventCode(short in_value) {
		super(in_value);
	}
	
	public Decoder<? extends Event> getEventDecoder() {
		if (this.equals(storeAdded))
			return StoreAdded.decoder;
		if (this.equals(storeRemoved))
			return StoreRemoved.decoder;
		if (this.equals(devicePropChanged))
			return DevicePropChanged.decoder;
		return null;
	}
}
