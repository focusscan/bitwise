package bitwise.devices.usbptpcamera.operations;

import bitwise.devices.usbptpcamera.responses.StorageInfo;

public class GetStorageInfo extends Operation<StorageInfo> {
	public static final int argStorageID = 0;
	
	public GetStorageInfo(int in_storageID) {
		super((short) 0x1005, 1);
		this.setArgument(argStorageID, in_storageID);
	}

	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private StorageInfo decoded = null;
	
	@Override
	public StorageInfo getDecodedData() {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {
			decoded = new StorageInfo(getResponseData().getData());
		}
		return decoded;
	}
}
