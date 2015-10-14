package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpPrimType;

public class DevicePropertyRange implements DevicePropertyForm {
	public final UsbPtpPrimType minimumValue;
	public final UsbPtpPrimType maximumValue;
	public final UsbPtpPrimType stepSize;
	
	public DevicePropertyRange(UsbPtpBuffer buf, short dataType) {
		minimumValue = buf.getPrimType(dataType);
		maximumValue = buf.getPrimType(dataType);
		stepSize = buf.getPrimType(dataType);
	}
}
