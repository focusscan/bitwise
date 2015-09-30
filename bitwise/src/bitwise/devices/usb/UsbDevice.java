package bitwise.devices.usb;

import java.io.UnsupportedEncodingException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

public class UsbDevice {
	private final UsbDeviceID id;
	private final javax.usb.UsbDevice platformDevice;
	private final javax.usb.UsbDeviceDescriptor platformDescriptor;
	private final short vendorID;
	private final short productID;
	private String manufacturer = null;
	private String product = null;
	private String serialNumber = null;
	
	private UsbDriver driver = null;
	private BooleanProperty deviceInUse = new SimpleBooleanProperty(false);
	
	public UsbDevice(javax.usb.UsbDevice in_platformDevice) throws UsbDisconnectedException, UsbException {
		id = new UsbDeviceID();
		platformDevice = in_platformDevice;
		assert(null != platformDevice);
		
		platformDescriptor = platformDevice.getUsbDeviceDescriptor();
		vendorID = platformDescriptor.idVendor();
		productID = platformDescriptor.idProduct();
		
		try {
			manufacturer = platformDevice.getManufacturerString();
			product = platformDevice.getProductString();
			serialNumber = platformDevice.getSerialNumberString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UsbDeviceID getID() {
		return id;
	}
	
	protected javax.usb.UsbDevice getPlatformDevice() {
		return platformDevice;
	}
	
	protected javax.usb.UsbDeviceDescriptor getPlatformDescriptor() {
		return platformDescriptor;
	}
	
	public short getVendorID() {
		return vendorID;
	}
	
	public short getProductID() {
		return productID;
	}
	
	public String getManufacturer() {
		return manufacturer;
	}
	
	public String getProduct() {
		return product;
	}
	
	public String getSerialNumber() {
		return serialNumber;
	}
	
	protected synchronized void setDriver(UsbDriver in_driver) {
		assert(null == driver);
		driver = in_driver;
		deviceInUse.set(true);
	}
	
	protected synchronized void unsetDriver() {
		driver = null;
		deviceInUse.set(false);
	}
	
	public boolean inUse() {
		return (null != driver);
	}
	
	public BooleanProperty getDeviceInUse() {
		return deviceInUse;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof UsbDevice))
			return false;
		UsbDevice that = (UsbDevice)o;
		return this.getID().equals(that.getID());
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		if (null != manufacturer && null != product && null != serialNumber)
			return String.format("USB<%08x> (%s - %s ser# %s)", id.getValue(), manufacturer, product, serialNumber);
		else if (null != manufacturer && null != product)
			return String.format("USB<%08x> (%s - %s)", id.getValue(), manufacturer, product);
		else
			return String.format("USB<%08x> (%04x:%04x)", id.getValue(), vendorID, productID);
	}
}
