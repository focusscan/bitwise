package bitwise.devices.canon.responses;

import bitwise.devices.canon.CanonDeviceProperties;
import bitwise.devices.usbptpcamera.coder.Int32;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.responses.DevicePropDesc;
import bitwise.devices.usbptpcamera.responses.DevicePropertyEnum;

public class CanonDevicePropertyDesc extends DevicePropDesc {	
	private static final int CurrentValue = 0xc189;
	private static final int ValidRange = 0xc18a;
	private static final int Sentinel = 0x80000000;
	
	private int valueType = Sentinel;
	
	protected CanonDevicePropertyDesc(UsbPtpBuffer buf) throws UsbPtpCoderException {
		devicePropertyCode = (short)0xffff;
		dataType = (short)0x0006;              // Int32
		getSet = (byte) 0x01;                  // All Canon properties are "settable" for now
		factoryDefaultValue = new Int32(-1);   // currently not collected
		formFlag = (byte) 0x02;                // All Canon properties are enums
		currentValue = null;
		form = null;
		
		int lenInBytes = buf.getInt();
		if (lenInBytes == 8)
			System.out.println("ding!");
		
		// Most Canon property arrays use 32-bit ints, but a few properties 
		// (that we don't care about) don't, so just stuff the remainder into a 32-bit int.
		int rem32 = lenInBytes % 4;
		int len32 = lenInBytes / 4 - 1;
		
		if (rem32 > 0)
			System.out.println("ding!");
		
		if (len32 == 1) {
			buf.getInt();
			return;
		}
		
		valueType = buf.getInt(); --len32;	
		devicePropertyCode = CanonDeviceProperties.toPtpPropCode(buf.getInt()); --len32;
		if (valueType == ValidRange) {
			buf.getInt(); --len32;
			buf.getInt(); --len32;
		}
		
		Int32[] values = new Int32[len32 + (rem32 > 0 ? 1 : 0)];
		for (int i = 0; i < len32; i++) {
			values[i] = new Int32(buf.getInt());
		}
		
		if (rem32 == 1) {
			values[len32] = new Int32(buf.getByte() & 0x000000ff);
		} else if (rem32 == 2) {
			values[len32] = new Int32(buf.getShort() & 0x0000ffff);
		} else if (rem32 == 3) {
			values[len32] = new Int32(buf.getShort() + (buf.getByte() << 16) & 0x00ffffff);
		}
		
		if (devicePropertyCode != (short) 0xffff) {
			if (valueType == CurrentValue)
				currentValue = values[0];
			else if (valueType == ValidRange)
				form = new DevicePropertyEnum(values);
		}		
	}
	
	public static CanonDevicePropertyDesc readDevicePropertyDesc(UsbPtpBuffer buf) throws UsbPtpCoderException {
		CanonDevicePropertyDesc propdesc = new CanonDevicePropertyDesc(buf);
		
		if (propdesc.getDevicePropertyCode() != (short)0xffff)
			return propdesc;
		else return null;
	}
	
	public void update(CanonDevicePropertyDesc p) {
		if (null != p.getCurrentValue())
			currentValue = p.getCurrentValue();
		if (null != p.getValidValues())
			form = p.getValidValues();
	}
	
	public boolean isEnd() {
		return (currentValue == null && form == null);
	}
}
