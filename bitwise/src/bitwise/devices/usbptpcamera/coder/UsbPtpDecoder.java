package bitwise.devices.usbptpcamera.coder;

public interface UsbPtpDecoder<T> {
	public T decode(UsbPtpBuffer in) throws UsbPtpCoderException;
}
