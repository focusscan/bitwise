package bitwise.devices.usb.drivers.ptp.responses;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.operations.GetDevicePropDesc;
import bitwise.devices.usb.drivers.ptp.types.*;
import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class DeviceProperty extends BaseResponse {
	private DevicePropCode devicePropCode = null;
	private DataType dataType = null;
	private UInt8 getSet = null;
	private PtpType factoryDefaultValue = null;
	private PtpType currentValue = null;
	private UInt8 formFlag = null;
	// TODO: FORM
	
	public DeviceProperty(ByteBuffer in) {
		super(in);
		if (!getCode().equals(GetDevicePropDesc.operationCode))
			return;
		devicePropCode = new DevicePropCode(in);
		System.out.println(String.format("devicePropCode %s", devicePropCode));
		dataType = new DataType(in);
		System.out.println(String.format("dataType %s", dataType));
		getSet = new UInt8(in);
		System.out.println(String.format("getSet %s", getSet));
		Decoder<?> decoder = dataType.getDecoder();
		if (null == decoder)
			return;
		factoryDefaultValue = decoder.decode(in);
		System.out.println(String.format("factoryDefaultValue %s", factoryDefaultValue));
		currentValue = decoder.decode(in);
		System.out.println(String.format("currentValue %s", currentValue));
		formFlag = new UInt8(in);
		System.out.println(String.format("formFlag %s", formFlag));
	}
	
	public DevicePropCode getDevicePropCode() {
		return devicePropCode;
	}
	
	public DataType getDataType() {
		return dataType;
	}
	
	public UInt8 getGetSet() {
		return getSet;
	}
	
	public PtpType getFactoryDefaultValue() {
		return factoryDefaultValue;
	}
	
	public PtpType getCurrentValue() {
		return currentValue;
	}
	
	public UInt8 getFormFlag() {
		return formFlag;
	}
}
