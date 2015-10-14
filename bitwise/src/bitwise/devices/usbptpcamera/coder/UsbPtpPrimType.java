package bitwise.devices.usbptpcamera.coder;

public abstract class UsbPtpPrimType implements Encodable {
	@SuppressWarnings("unchecked")
	public <T extends UsbPtpPrimType> T castTo(Class<T> to) throws UsbPtpTypeCastException {
		if (to.isAssignableFrom(this.getClass()))
			return (T) this;
		throw new UsbPtpTypeCastException(this.getClass(), to);
	}
}
