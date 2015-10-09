package bitwise.devices.usb.drivers.canon.operations;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.operations.Operation;
import bitwise.devices.usb.drivers.ptp.types.operations.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.Arr;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.Int32;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.responses.DeviceInfo;

public class SetDevicePropValueEx extends Operation<Void> {
	private final UInt32 len = new UInt32(0x0c);
	private final UInt32 prop = new UInt32(0xd101);
	private final UInt32 value = new UInt32(0x25);
	
	public SetDevicePropValueEx() {
		super("SetDevicePropValueEx", CanonOperationCode.setDevicePropValueEx, 0, null);
	}
	
	@Override
	public PtpType getOutData() {
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		
		len.serialize(data);
		prop.serialize(data);
		value.serialize(data);
		
		ByteBuffer bb = ByteBuffer.wrap(data.toByteArray());
		
		return UInt32.canonArrayDecoder.decode(bb);
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
