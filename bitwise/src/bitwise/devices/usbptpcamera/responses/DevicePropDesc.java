package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;

public class DevicePropDesc {
	public final short devicePropertyCode;
	public final short dataType;
	public final byte getSet;
	public final UsbPtpPrimType factoryDefaultValue;
	public final UsbPtpPrimType currentValue;
	public final byte formFlag;
	public final DevicePropertyForm form;
	
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
}
