package bitwise.devices.usbptpcamera.events;

public class EventCode {
	public static final short cancelTransaction		= (short) 0x4001;
	public static final short objectAdded			= (short) 0x4002;
	public static final short objectRemoved			= (short) 0x4003;
	public static final short storeAdded			= (short) 0x4004;
	public static final short storeRemoved			= (short) 0x4005;
	public static final short devicePropChanged		= (short) 0x4006;
	public static final short objectInfoChanged		= (short) 0x4007;
	public static final short deviceInfoChanged		= (short) 0x4008;
	public static final short requestObjectTransfer	= (short) 0x4009;
	public static final short storeFull				= (short) 0x400a;
	public static final short deviceReset			= (short) 0x400b;
	public static final short storageInfoChanged	= (short) 0x400c;
	public static final short captureComplete		= (short) 0x400d;
	public static final short unreportedStatus		= (short) 0x400e;
	
	private EventCode() {
		
	}
}
