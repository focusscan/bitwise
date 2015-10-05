package bitwise.devices.usb.drivers.ptp.operations;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.responses.BaseResponse;
import bitwise.devices.usb.drivers.ptp.types.DevicePropCode;
import bitwise.devices.usb.drivers.ptp.types.GenericContainer;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class SetDevicePropValue<P extends PtpType> extends BaseOperation<BaseResponse> {
	public static final UInt16 operationCode = new UInt16((short) 0x1016);
	
	private final DevicePropCode devicePropCode;
	private final P propValue;
	
	public SetDevicePropValue(TransactionID id, DevicePropCode in_devicePropCode, P in_propValue) {
		super("SetDevicePropValue", operationCode, id, 1);
		devicePropCode = in_devicePropCode;
		propValue = in_propValue;
		getArgs().add(devicePropCode.asInt32());
	}
	
	@Override
	public boolean serializeData(ByteArrayOutputStream stream) {
		byte[] dataBytes = null;
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			propValue.serialize(data);
			dataBytes = data.toByteArray();
		}
		GenericContainer dataContainer = new GenericContainer(new UInt32(dataBytes.length), GenericContainer.containerTypeData, getCode(), getTransactionID());
		dataContainer.serialize(stream);
		for (byte b : dataBytes)
			stream.write(b);
		return true;
	}

	@Override
	public BaseResponse decodeResponse(ByteBuffer in) {
		return new BaseResponse(in);
	}
}
