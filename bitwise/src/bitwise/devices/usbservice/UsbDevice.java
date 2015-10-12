package bitwise.devices.usbservice;

import java.io.UnsupportedEncodingException;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import bitwise.devices.Device;

public final class UsbDevice extends Device {
	private final javax.usb.UsbDevice xDevice;
	private final String manufacturer;
	private final String product;
	
	protected UsbDevice(javax.usb.UsbDevice in_device) throws UnsupportedEncodingException, UsbDisconnectedException, UsbException {
		super();
		xDevice = in_device;
		manufacturer = xDevice.getManufacturerString();
		product = xDevice.getProductString();
	}
	
	protected javax.usb.UsbDevice getXDevice() {
		return xDevice;
	}
	
	public short getVendorID() {
		return xDevice.getUsbDeviceDescriptor().idVendor();
	}
	
	public short getProductID() {
		return xDevice.getUsbDeviceDescriptor().idProduct();
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public String getProduct() {
		return product;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%04x:%04x %s)", super.toString(), getVendorID(), getProductID(), getProduct());
	}
}
