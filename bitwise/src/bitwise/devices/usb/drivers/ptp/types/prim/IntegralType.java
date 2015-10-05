package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;

public abstract class IntegralType implements PtpType {
	public IntegralType() {
	}
	
	public abstract void serialize(ByteArrayOutputStream stream);
}
