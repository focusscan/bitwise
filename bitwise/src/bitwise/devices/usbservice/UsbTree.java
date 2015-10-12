package bitwise.devices.usbservice;

import java.io.UnsupportedEncodingException;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class UsbTree {
	private ObservableList<UsbDevice> devices = FXCollections.observableArrayList();
	
	protected UsbTree() {
		
	}
	
	public synchronized void clearTree() {
		devices.clear();
	}
	
	public synchronized UsbDevice addAttachedDevice(javax.usb.UsbDevice in) {
		if (in.isUsbHub())
			return null;
		// If for some reason the device is already
		// in our list, just return that copy.
		for (UsbDevice device : devices) {
			if (device.getXDevice().equals(in))
				return device;
		}
		UsbDevice newDevice = null;
		try {
			newDevice = new UsbDevice(in);
			devices.add(newDevice);
		} catch (UnsupportedEncodingException | UsbDisconnectedException | UsbException e) {
			// If these things happen, we simply
			// don't want to add the device.
		}
		return newDevice;
	}
	
	public synchronized UsbDevice removeDetachedDevice(javax.usb.UsbDevice in) {
		if (in.isUsbHub())
			return null;
		// Make sure the device is in our list
		UsbDevice foundIt = null;
		deviceSearch: for (UsbDevice device : devices) {
			if (device.getXDevice().equals(in)) {
				foundIt = device;
				break deviceSearch;
			}
		}
		if (null != foundIt)
			devices.remove(foundIt);
		return foundIt;
	}
}
