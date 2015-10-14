package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;

public class DevicePropertyEnum implements DevicePropertyForm {
	public final short numberOfValues;
	public final UsbPtpPrimType[] supportedValues;
	
	public DevicePropertyEnum(UsbPtpBuffer buf, short dataType) throws UsbPtpCoderException {
		numberOfValues = buf.getShort();
		supportedValues = new UsbPtpPrimType[numberOfValues];
		for (int i = 0; i < supportedValues.length; i++)
			supportedValues[i] = buf.getPrimType(dataType);
	}
}
