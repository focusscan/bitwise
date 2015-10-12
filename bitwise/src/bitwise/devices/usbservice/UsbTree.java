package bitwise.devices.usbservice;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;

import bitwise.log.Log;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public final class UsbTree {
	private final UsbService service;
	private final ObservableList<UsbDriverFactory<?, ?, ?>> factories = FXCollections.observableArrayList();
	private final ObservableList<UsbDevice> devices = FXCollections.observableArrayList();
	private final ObservableList<UsbReady<?, ?, ?>> ready = FXCollections.observableArrayList();
	
	protected UsbTree(UsbService in_service) {
		service = in_service;
	}
	
	public ObservableList<UsbDriverFactory<?, ?, ?>> getDriverFactoryList() {
		return factories;
	}
	
	public ObservableList<UsbDevice> getDeviceList() {
		return devices;
	}
	
	public ObservableList<UsbReady<?, ?, ?>> getReadyList() {
		return ready;
	}
	
	public FilteredList<UsbReady<?, ?, ?>> getReadyByHandleType(Class<?> in_class) {
		return ready.filtered(new Predicate<UsbReady<?, ?, ?>>() {
			@Override
			public boolean test(UsbReady<?, ?, ?> rdy) {
				return in_class.isAssignableFrom(rdy.getFactory().getHandleClass());
			}
		});
	}
	
	public void deviceDriverSet(UsbDevice in) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				synchronized(ready) {
					UsbReady<?, ?, ?> foundIt = null;
					for (UsbReady<?, ?, ?> rdy : ready) {
						if (rdy.getDevice().equals(in)) {
							foundIt = rdy;
							break;
						}
					}
					if (null != foundIt)
						ready.remove(foundIt);
				}
			}
		});
	}
	
	public void deviceDriverUnset(UsbDevice in) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				synchronized (ready) {
					for (UsbDriverFactory<?, ?, ?> factory : factories) {
						if (factory.isCompatibleWith(in))
							ready.add(new UsbReady<>(in, factory));
					}
				}
			}
		});
	}
	
	public synchronized void clearTree() {
		devices.clear();
		ready.clear();
	}
	
	public void addDriverFactory(UsbDriverFactory<?, ?, ?> factory) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				factories.add(factory);
				synchronized (ready) {
					for (UsbDevice device : devices) {
						if (factory.isCompatibleWith(device))
							ready.add(new UsbReady<>(device, factory));
					}
				}
				Log.log(service, "Added factory %s", factory);
			}
		});
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
			final UsbDevice newDevice = new UsbDevice(service, in);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					devices.add(newDevice);
					synchronized (ready) {
						for (UsbDriverFactory<?, ?, ?> factory : factories) {
							if (factory.isCompatibleWith(newDevice))
								ready.add(new UsbReady<>(newDevice, factory));
						}
					}
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
					synchronized (ready) {
						ArrayList<UsbReady<?, ?, ?>> removeList = new ArrayList<>();
						for (UsbReady<?, ?, ?> rdy : ready) {
							if (rdy.getDevice().equals(toRemove))
								removeList.add(rdy);
						}
						for (UsbReady<?, ?, ?> rdy : removeList)
							ready.remove(rdy);
					}
				}
			});
		}
		return foundIt;
	}
}
