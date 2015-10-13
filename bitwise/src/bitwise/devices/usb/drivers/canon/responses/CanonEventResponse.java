package bitwise.devices.usb.drivers.canon.responses;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.canon.types.CanonArr;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.responses.Response;

public class CanonEventResponse implements Response {
	private static final int CurrentValue = 0xc189;
	private static final int ValidRange = 0xc18a;
	
	private static final int BatteryLevel = 0xd111;
	private static final int ExposureIndex = 0xd103;
	private static final int ExposureMode = 0xd105;
	private static final int ExposureTime = 0xd102;
	private static final int FNumber = 0xd101;
	private static final int FocusMode = 0xd108;
	private static final int ImageFormat = 0xd120;

	public static final Decoder<CanonEventResponse> decoder = new Decoder<CanonEventResponse>() {
		@Override
		public CanonEventResponse decode(ByteBuffer in) {
			return new CanonEventResponse(in);
		}
	};
	
	private UInt32 batteryLevel = null;
	private UInt32 exposureIndex = null;
	private short[] validExposureIndices = null;
	private UInt32 exposureMode = null;
	private short[] validExposureModes = null;
	private UInt32 exposureTime = null;
	private int[] validExposureTimes = null;
	private UInt32 fNumber = null;
	private short[] validFNumbers = null;
	private UInt32 focusMode = null;
	private short[] validFocusModes = null;
	private UInt32 imageFormat = null;
	private short[] validImageFormats = null;
	
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
		
		int recordType = data.get(0).getValue();
		
		switch (property) {
			case BatteryLevel:
				if (recordType == CurrentValue)
					batteryLevel = data.get(2);
				break;
			case ExposureIndex:
				if (recordType == CurrentValue)
					exposureIndex = data.get(2);
				else if (recordType == ValidRange)
					validExposureIndices = toArray(data, 4);
				break;
			case ExposureMode:
				if (recordType == CurrentValue)
					exposureMode = data.get(2);
				else if (recordType == ValidRange)
					if (null != exposureMode) {
						validExposureModes = new short[1];
						validExposureModes[0] = (short)exposureMode.getValue();
					}
				break;
			case ExposureTime:
				if (recordType == CurrentValue)
					exposureTime = data.get(2);
				break;
			case FNumber:
				if (recordType == CurrentValue)
					fNumber = data.get(2);
				else if (recordType == ValidRange)
					validFNumbers = toArray(data, 3);
				break;
			case FocusMode:
				if (recordType == CurrentValue)
					focusMode = data.get(2);
				else if (recordType == ValidRange)
					validFocusModes = toArray(data, 4);
				break;
			case ImageFormat:	
				if (recordType == CurrentValue) {
					imageFormat = convertImageFormat(data, 2);
				}
				break;
		}
	}
	
	public short getBatteryLevel() {
		return (short)batteryLevel.getValue();
	}
	
	public short getExposureIndex() {
		return (short)exposureIndex.getValue();
	}
	
	public short[] getValidExposureIndices() {
		return validExposureIndices;
	}
	
	public short getExposureMode() {
		return (short)exposureMode.getValue();
	}
	
	public short[] getValidExposureModes() {
		return validExposureModes;
	}
	
	public int getExposureTime() {
		return exposureTime.getValue();
	}
	
	public int[] getValidExposureTimes() {
		int[] temp = new int[1];
		temp[0] = exposureTime.getValue();
		return temp;
	}
	
	public short getFNumber() {
		return (short)fNumber.getValue();
	}
	
	public short[] getValidFNumbers() {
		return validFNumbers;
	}
	
	public short getFocusMode() {
		return (short)focusMode.getValue();
	}
	
	public short[] getValidFocusModes() {
		return validFocusModes;
	}
	
	public short getImageFormat() {
		return (short)imageFormat.getValue();
	}
	
	public short[] getValidImageFormats() {
		return validImageFormats;
	}
	
	private short[] toArray(ArrayList<UInt32> data, int start) {
		short[] ret = new short[data.size() - start];
		for (int i = start; i < data.size(); i++) {
			ret[i - start] = (short)data.get(i).getValue();
		}
		return ret;
	}
	
	private UInt32 convertImageFormat(ArrayList<UInt32> data, int i) {
		int numFiles = data.get(i).getValue();
		if (data.size() < i+5 || (numFiles == 2 && data.size() < i+9)) {
			return new UInt32(0x0);
		}			

		int imageSize1 = data.get(i+3).getValue();
		int imageCompression1 = data.get(i+4).getValue();
		int imageSize2 = numFiles > 1 ? data.get(i+7).getValue() : 0;
		int imageCompression2 = numFiles > 1 ? data.get(i+8).getValue() : 0;
		int value = (imageSize1 & 0xF) << 12 | (imageCompression1 & 0xF) << 8 |
					(imageSize2 & 0xF) << 4  | (imageCompression2 & 0xF);
		
		return new UInt32(value);
	}
}
