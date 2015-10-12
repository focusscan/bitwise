package bitwise.devices.usbservice;

import java.io.UnsupportedEncodingException;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class UsbTree {
	private ObservableList<UsbDevice> devices = FXCollections.observableArrayList();
	
	protected UsbTree() {
		
	}
	
	public ObservableList<UsbDevice> getDeviceList() {
		return devices;
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
		try {
			final UsbDevice newDevice = new UsbDevice(in);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					devices.add(newDevice);
				}
			});
			return newDevice;
		} catch (UnsupportedEncodingException | UsbDisconnectedException | UsbException e) {
			// If these things happen, we simply
			// don't want to add the device.
		}
		return null;
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
		if (null != foundIt) {
			final UsbDevice toRemove = foundIt;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					devices.remove(toRemove);
				}
			});
		}
		return foundIt;
	}
}
