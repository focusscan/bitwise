package bitwise.devices.usb.drivers.canon.responses;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.canon.types.CanonArr;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.responses.Response;

public class CanonEventResponse implements Response {
	public static final Decoder<CanonEventResponse> decoder = new Decoder<CanonEventResponse>() {
		@Override
		public CanonEventResponse decode(ByteBuffer in) {
			return new CanonEventResponse(in);
		}
	};
	
	private UInt32 batteryLevel = null;
	private UInt32 exposureIndex = null;
	private UInt32 exposureMode = null;
	private UInt32 exposureTime = null;
	private UInt32 fNumber = null;
	private UInt32 focusMode = null;
	private Boolean statusChanged = false;
	
	public CanonEventResponse(ByteBuffer in) {
		System.out.println("Canon Event Response:");
		while(in.hasRemaining()) {
			parseNextDataBlock(in);
		}
		return;
	}
	
	private void parseNextDataBlock(ByteBuffer in) {
		CanonArr block = UInt32.canonArrayDecoder.decode(in);
		ArrayList<UInt32> data = block.getValue();
		
		if (data.size() == 1) return;		// Signals end of data block

		int property = data.get(1).getValue();		
		System.out.println("Parsing property value: " + Integer.toHexString(property));
		
		if (data.get(0).getValue() == 0xc18a) return;		// Ignoring this for now (valid values)
		
		switch (property) {
			case (0xd111):
				batteryLevel = data.get(2);
				break;
			case (0xd103):
				exposureIndex = data.get(2);
				break;
			case (0xd105):
				exposureMode = data.get(2);
				break;
			case (0xd102):
				exposureTime = data.get(2);
				break;
			case (0xd101):
				fNumber = data.get(2);
				break;
			case (0xd108):
				focusMode = data.get(2);
				break;
		}
	}
	
	public UInt32 getBatteryLevel() {
		return batteryLevel;
	}
	
	public UInt32 getExposureIndex() {
		return exposureIndex;
	}
	
	public UInt32 getExposureMode() {
		return exposureMode;
	}
	
	public UInt32 getExposureTime() {
		return exposureTime;
	}
	
	public UInt32 getFNumber() {
		return fNumber;
	}
	
	public UInt32 getFocusMode() {
		return focusMode;
	}
}
