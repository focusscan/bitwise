package bitwise.devices.canon.operations;

import java.util.Hashtable;

import bitwise.devices.canon.responses.CanonDevicePropertyDesc;
import bitwise.devices.usbptpcamera.coder.UsbPtpBuffer;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.operations.Operation;

public class GetEvent extends Operation<Hashtable<Short, CanonDevicePropertyDesc>> {
		public GetEvent() {
		super((short) 0x9116, 0);
	}
	
	@Override
	public boolean hasTransactionID() {
		return true;
	}
	
	private Hashtable<Short, CanonDevicePropertyDesc> decoded = null;
	
	@Override
	public Hashtable<Short, CanonDevicePropertyDesc> getDecodedData() throws UsbPtpCoderException {
		if (null == decoded && null != getResponseData() && 0 < getResponseData().getDataSize()) {			
			UsbPtpBuffer buf = this.getResponseData().getData();
			while (buf.hasRemaining()) {
				CanonDevicePropertyDesc propdesc = CanonDevicePropertyDesc.readDevicePropertyDesc(buf);
				
				if (null == propdesc) continue;
				if (null == decoded) 
					decoded = new Hashtable<>();
			
				CanonDevicePropertyDesc curr = decoded.get(propdesc.getDevicePropertyCode());
				if (null != curr) curr.update(propdesc);
				else decoded.put(propdesc.getDevicePropertyCode(), propdesc);
			}			
		}
		return decoded;
	}
}


