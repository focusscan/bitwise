package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.responses.StorageInfo;

public class GetStorageInfo extends Operation<StorageInfo> {
	private final UInt32 storageID;
	
	public GetStorageInfo(UInt32 in_storageID) {
		super("GetStorageInfo", OperationCode.getStorageInfo, 1, null);
		storageID = in_storageID;
		getArguments().add(storageID);
	}
	
	private StorageInfo data = null;
	
	@Override
	public StorageInfo getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = StorageInfo.decoder.decode(in);
		return true;
	}
}
