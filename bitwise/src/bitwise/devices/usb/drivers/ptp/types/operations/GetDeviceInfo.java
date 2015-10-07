package bitwise.devices.usb.drivers.ptp.types.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.responses.DeviceInfo;

public class GetDeviceInfo extends Operation<DeviceInfo> {
	public GetDeviceInfo() {
		super("GetDeviceInfo", OperationCode.getDeviceInfo, 0, null);
	}
	
	private DeviceInfo data = null;
	
	@Override
	public DeviceInfo getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = DeviceInfo.decoder.decode(in);
		return true;
	}
}
