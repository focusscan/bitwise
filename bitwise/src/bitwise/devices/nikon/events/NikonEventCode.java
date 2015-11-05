package bitwise.devices.nikon.events;

public class NikonEventCode {
	public static final short objectAddedInSdram		= (short) 0xc101;
	public static final short captureCompleteRecInSdram	= (short) 0xc102;
	public static final short recordingInterrupted		= (short) 0xc105;
	
	private NikonEventCode() {
		
	}
}
