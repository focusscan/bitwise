package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Arr;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class GetStorageIDs extends Operation<Arr<UInt32>> {
	public GetStorageIDs() {
		super("GetStorageIDs", OperationCode.getStorageIDs, 0, null);
	}
	
	private Arr<UInt32> data = null;
	
	@Override
	public Arr<UInt32> getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = UInt32.arrayDecoder.decode(in);
		return true;
	}
}
