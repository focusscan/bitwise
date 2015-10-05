package bitwise.devices.usb.drivers.ptp.responses;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.operations.GetDevicePropDesc;
import bitwise.devices.usb.drivers.ptp.types.*;
import bitwise.devices.usb.drivers.ptp.types.prim.*;

public class DeviceProperty implements Response {
	public static final Decoder<DeviceProperty> decoder = new Decoder<DeviceProperty>() {
		@Override
		public DeviceProperty decode(ByteBuffer in) {
			return new DeviceProperty(in);
		}
	};
	
	public static interface PropertyDescribingDataset {
		
	}
	
	public static class PropertyDescribingRange<P extends PtpType> implements PropertyDescribingDataset {
		private P minimumValue = null;
		private P maximumValue = null;
		private P stepSize = null;
		
		public PropertyDescribingRange(Decoder<P> decoder, ByteBuffer in) {
			System.out.println(" PropertyDescribingRange");
			minimumValue = decoder.decode(in);
			System.out.println(String.format("  minimumValue %s", minimumValue));
			maximumValue = decoder.decode(in);
			System.out.println(String.format("  maximumValue %s", maximumValue));
			stepSize = decoder.decode(in);
			System.out.println(String.format("  stepSize %s", stepSize));
		}
		
		public P getMinimumValue() {
			return minimumValue;
		}
		
		public P getMaximumValue() {
			return maximumValue;
		}
		
		public P getStepSize() {
			return stepSize;
		}
	}
	
	public static class PropertyDescribingEnum<P extends PtpType> implements PropertyDescribingDataset {
		private UInt16 numberOfValues = null;
		private ArrayList<P> supportedValues = null;
		
		public PropertyDescribingEnum(Decoder<P> decoder, ByteBuffer in) {
			System.out.println(" PropertyDescribingEnum");
			numberOfValues = new UInt16(in);
			System.out.println(String.format("  numberOfValues %s", numberOfValues));
			supportedValues = new ArrayList<>(getNumberOfValues());
			for (int i = 0; i < getNumberOfValues(); i++) {
				P decoded = decoder.decode(in);
				supportedValues.add(decoded);
				System.out.println(String.format("  decoded %s", decoded));
			}
		}
		
		public int getNumberOfValues() {
			return 0xffff & numberOfValues.getValue();
		}
		
		public ArrayList<P> getSupportedValues() {
			return supportedValues;
		}
	}
	
	private DevicePropCode devicePropCode = null;
	private DataType dataType = null;
	private UInt8 getSet = null;
	private PtpType factoryDefaultValue = null;
	private PtpType currentValue = null;
	private UInt8 formFlag = null;
	private PropertyDescribingDataset form = null;
	
	public DeviceProperty(ByteBuffer in) {
		System.out.println("DeviceProperty");
		devicePropCode = new DevicePropCode(in);
		System.out.println(String.format(" devicePropCode %s", devicePropCode));
		dataType = new DataType(in);
		System.out.println(String.format(" dataType %s", dataType));
		getSet = new UInt8(in);
		System.out.println(String.format(" getSet %s", getSet));
		Decoder<? extends PtpType> dataTypeDecoder = dataType.getDecoder();
		if (null == dataTypeDecoder)
			return;
		factoryDefaultValue = dataTypeDecoder.decode(in);
		System.out.println(String.format(" factoryDefaultValue %s", factoryDefaultValue));
		currentValue = dataTypeDecoder.decode(in);
		System.out.println(String.format(" currentValue %s", currentValue));
		formFlag = new UInt8(in);
		System.out.println(String.format(" formFlag %s", formFlag));
		switch (formFlag.getValue()) {
		case (byte) 0x00:
			break;
		case (byte) 0x01:
			form = new PropertyDescribingRange<>(dataTypeDecoder, in);
			break;
		case (byte) 0x02:
			form = new PropertyDescribingEnum<>(dataTypeDecoder, in);
			break;
		}
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
	
	public PropertyDescribingDataset getForm() {
		return form;
	}
}
