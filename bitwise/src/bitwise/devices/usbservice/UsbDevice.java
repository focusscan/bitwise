package bitwise.devices.usbservice;

import java.io.UnsupportedEncodingException;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import bitwise.devices.Device;

public final class UsbDevice extends Device {
	private final UsbService usbService;
	private final javax.usb.UsbDevice xDevice;
	private final String manufacturer;
	private final String product;
	
	protected UsbDevice(UsbService in_service, javax.usb.UsbDevice in_device) throws UnsupportedEncodingException, UsbDisconnectedException, UsbException {
		super();
		usbService = in_service;
		xDevice = in_device;
		manufacturer = xDevice.getManufacturerString();
		product = xDevice.getProductString();
	}
	
	public javax.usb.UsbDevice getXDevice() {
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
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UsbDevice))
			return false;
		UsbDevice that = (UsbDevice) o;
		return this.xDevice.equals(that.xDevice);
	}
	
	@Override
	public String toString() {
		return String.format("%s(%04x:%04x %s)", super.toString(), getVendorID(), getProductID(), getProduct());
	}

	@Override
	protected void onSetDriver() {
		usbService.deviceDriverSet(this);
	}

	@Override
	protected void onUnsetDriver() {
		usbService.deviceDriverUnset(this);
	}
}
