package bitwise.devices.usbptpcamera;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbNotActiveException;
import javax.usb.UsbNotClaimedException;
import javax.usb.UsbNotOpenException;
import javax.usb.event.UsbPipeDataEvent;
import javax.usb.event.UsbPipeErrorEvent;

import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.coder.UsbPtpDecoder;

public class BaseUsbPtpReader<T> implements javax.usb.event.UsbPipeListener {
	private final BlockingQueue<T> decoded = new LinkedBlockingQueue<>();
	private final javax.usb.UsbPipe pipe;
	private final UsbPtpDecoder<T> decoder;
	private byte[] contBuf = null;
	private int contPos = 0;
	
	public BaseUsbPtpReader(javax.usb.UsbPipe in_pipe, UsbPtpDecoder<T> in_decoder) {
		pipe = in_pipe;
		decoder = in_decoder;
	}
	
	public BlockingQueue<T> getDecoded() {
		return decoded;
	}
	
	public void start() throws UsbNotActiveException, UsbNotClaimedException, UsbDisconnectedException, UsbException {
		pipe.open();
		pipe.addUsbPipeListener(this);
		submitNextBuffer();
	}
	
	public void stop(boolean close) throws UsbNotActiveException, UsbNotOpenException, UsbDisconnectedException, UsbException {
		pipe.removeUsbPipeListener(this);
		if (close) {
			pipe.abortAllSubmissions();
			pipe.close();
		}
	}
	
	protected void submitNextBuffer() {
		byte[] buf = new byte[pipe.getUsbEndpoint().getUsbEndpointDescriptor().wMaxPacketSize()];
		try {
			pipe.asyncSubmit(buf);
		} catch (UsbNotActiveException | UsbNotOpenException | IllegalArgumentException | UsbDisconnectedException
				| UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int peekInt(byte[] in) {
		int v0 = 0xff & (int) in[0];
		int v1 = 0xff & (int) in[1];
		int v2 = 0xff & (int) in[2];
		int v3 = 0xff & (int) in[3];
		return v0 | (v1 << 8) | (v2 << 16) | (v3 << 24);
	}
	
	private T doDecode(byte[] in) throws UsbPtpCoderException {
		/*
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Decoding %d bytes with %s: ", in.length, decoder.getClass().getSimpleName()));
		for (byte b : in)
			sb.append(String.format("%02x", b));
		System.out.println(sb);
		*/
		return decoder.decode(new UsbPtpBuffer(in));
	}

	@Override
	public void dataEventOccurred(UsbPipeDataEvent event) {
		try {
			if (4 >= event.getActualLength()) {
				contBuf = null;
				contPos = 0;
			}
			else {
				T d = null;
				byte[] eventData = event.getData();
				
				if (null == contBuf) {
					int length = peekInt(eventData);
					if (length <= event.getActualLength()) {
						d = doDecode(eventData);
					}
					else {
						contBuf = new byte[length];
						contPos = 0;
					}
				}
				if (null != contBuf) {
					for (int i = 0; i < event.getActualLength() && contPos < contBuf.length; i++)
						contBuf[contPos++] = eventData[i];
					if (contBuf.length == contPos) {
						d = doDecode(contBuf);
						contBuf = null;
						contPos = 0;
					}
				}
				if (null != d)
					decoded.add(d);
			}
		} catch(UsbPtpCoderException e) {
			// TODO
			e.printStackTrace();
		}
		
		submitNextBuffer();
	}

	@Override
	public void errorEventOccurred(UsbPipeErrorEvent event) {
	}
}
