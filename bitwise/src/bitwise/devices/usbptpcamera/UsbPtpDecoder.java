package bitwise.devices.usbptpcamera;

public interface UsbPtpDecoder<T> {
	public T decode(UsbPtpBuffer in);
}
