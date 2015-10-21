package bitwise.devices.usbptpcamera.responses;

import bitwise.devices.usbptpcamera.BaseUsbPtpCamera;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpDecoder;

public class ResponseDecoder implements UsbPtpDecoder<Response> {
	public ResponseDecoder() {
		
	}
	
	@Override
	public Response decode(UsbPtpBuffer in) throws UsbPtpCoderException {
		final int length = in.getInt();
		final short type = in.getShort();
		if (type == BaseUsbPtpCamera.containerCodeResponse)
			return new ResponseCode(in, length - 12);
		if (type == BaseUsbPtpCamera.containerCodeData)
			return new ResponseData(in, length - 12);
		return null;
	}
}
