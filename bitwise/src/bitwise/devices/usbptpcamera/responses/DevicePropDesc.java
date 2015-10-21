package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;

public class DevicePropDesc {
	protected short devicePropertyCode;
	protected short dataType;
	protected byte getSet;
	protected UsbPtpPrimType factoryDefaultValue;
	protected UsbPtpPrimType currentValue;
	protected byte formFlag;
	protected DevicePropertyForm form;
	
	public DevicePropDesc() {

	}
	
	public DevicePropDesc(UsbPtpBuffer buf) throws UsbPtpCoderException {
		devicePropertyCode = buf.getShort();
		dataType = buf.getShort();
		getSet = buf.getByte();
		factoryDefaultValue = buf.getPrimType(dataType);
		currentValue = buf.getPrimType(dataType);
		formFlag = buf.getByte();
		if (null == getFormEnum())
			form = null;
		else {
			switch (getFormEnum()) {
			case Range:
				form = new DevicePropertyRange(buf, dataType);
				break;
			case Enum:
				form = new DevicePropertyEnum(buf, dataType);
				break;
			case None:
			default:
				form = null;
				break;
			}
		}
	}
	
	public boolean supportsSet() {
		return getSet == (byte) 0x01;
	}
	
	public DevicePropertyForm.Form getFormEnum() {
		switch (formFlag) {
		case (byte) 0x00:
			return DevicePropertyForm.Form.None;
		case (byte) 0x01:
			return DevicePropertyForm.Form.Range;
		case (byte) 0x02:
			return DevicePropertyForm.Form.Enum;
		default:
			return DevicePropertyForm.Form.None;
		}
	}
	
	public short getDevicePropertyCode() {
		return devicePropertyCode;
	}
	
	public UsbPtpPrimType getFactoryDefaultValue() {
		return factoryDefaultValue;
	}
	
	public UsbPtpPrimType getCurrentValue() {
		return currentValue;
	}
	
	public DevicePropertyForm getValidValues() {
		return form;
	}
}
