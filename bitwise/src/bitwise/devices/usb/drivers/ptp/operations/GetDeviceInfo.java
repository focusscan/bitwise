package bitwise.devices.usb.drivers.ptp.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.responses.DeviceInfo;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class GetDeviceInfo extends Operation {
	public static final UInt16 operationCode = new UInt16((short) 0x1001);
	
	public GetDeviceInfo() {
		super("GetDeviceInfo", operationCode, 0, null);
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
