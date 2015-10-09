package bitwise.devices.usb.drivers.canon.operations;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.operations.Operation;
import bitwise.devices.usb.drivers.ptp.types.operations.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceInfo;

public class GetEvent extends Operation<Void> {
	
	public GetEvent() {
		super("GetCanonDeviceProps", CanonOperationCode.getEvent, 0, null);
	}
	
/*	private DeviceInfo data = null;
	
	@Override
	public DeviceInfo getResponseData() {
		return data;
	}
	
	@Override
	public boolean setResponseData(ByteBuffer in) {
		data = DeviceInfo.decoder.decode(in);
		return true;
	}*/
}
