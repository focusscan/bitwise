package bitwise.devices.usb;

import java.io.UnsupportedEncodingException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.event.UsbDeviceDataEvent;
import javax.usb.event.UsbDeviceErrorEvent;
import javax.usb.event.UsbDeviceEvent;
import javax.usb.event.UsbDeviceListener;

import bitwise.devices.usb.drivers.UsbDriver;
import bitwise.devices.usb.events.UsbDriverSetEvent;
import bitwise.devices.usb.events.UsbDriverUnsetEvent;

public final class UsbDevice implements UsbDeviceListener {
	private final UsbDeviceID id;
	private final javax.usb.UsbDevice platformDevice;
	private final short vendorID;
	private final short productID;
	private String manufacturer = null;
	private String product = null;
	private String serialNumber = null;
	
	public UsbDevice(javax.usb.UsbDevice in_platformDevice) throws UsbDisconnectedException, UsbException {
		id = new UsbDeviceID();
		platformDevice = in_platformDevice;
		assert(null != platformDevice);
		
		vendorID = platformDevice.getUsbDeviceDescriptor().idVendor();
		productID = platformDevice.getUsbDeviceDescriptor().idProduct();
		
		try {
			manufacturer = platformDevice.getManufacturerString();
			product = platformDevice.getProductString();
			serialNumber = platformDevice.getSerialNumberString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public javax.usb.UsbDevice getPlatformDevice() {
		return platformDevice;
	}
	
	public UsbDeviceID getID() {
		return id;
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
	
	private UsbDriver driver = null;
	private BooleanProperty deviceInUse = new SimpleBooleanProperty(false);
	
	public BooleanProperty getDeviceInUse() {
		return deviceInUse;
	}
	
	public synchronized UsbDriver getDriver() {
		return driver;
	}
	
	public synchronized void setDriver(UsbDriver in_driver) {
		assert(null == driver);
		platformDevice.addUsbDeviceListener(this);
		driver = in_driver;
		deviceInUse.set(true);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new UsbDriverSetEvent(this, driver));
	}
	
	public synchronized void unsetDriver() {
		UsbDriver oldDriver = driver;
		driver = null;
		deviceInUse.set(false);
		platformDevice.removeUsbDeviceListener(this);
		bitwise.engine.supervisor.Supervisor.getEventBus().publishEventToBus(new UsbDriverUnsetEvent(this, oldDriver));
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
		return String.format("USB<%08x> (%s - %s ser# %s)", id.getValue(), manufacturer, product, serialNumber);
	}

	@Override
	public void dataEventOccurred(UsbDeviceDataEvent event) {
		
	}

	@Override
	public void errorEventOccurred(UsbDeviceErrorEvent event) {
		System.out.println(String.format("errorEventOccurred(Device: %s)", event));
	}

	@Override
	public void usbDeviceDetached(UsbDeviceEvent event) {
		driver.resourceClose();
		unsetDriver();
	}
}
