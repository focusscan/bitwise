package bitwise.devices.usb;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.usb.UsbDisconnectedException;
import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbServices;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import bitwise.devices.kinds.DeviceKind;
import bitwise.devices.usb.events.UsbAttachedEvent;
import bitwise.devices.usb.events.UsbDetachedEvent;
import bitwise.devices.usb.events.UsbFastGoodbyeEvent;
import bitwise.devices.usb.events.UsbGoodbyeEvent;
import bitwise.devices.usb.events.UsbHelloEvent;
import bitwise.engine.supervisor.Service;
import bitwise.engine.supervisor.Supervisor;

public class UsbService extends Service implements UsbServicesListener {
	public static String serviceName() {
		return "USB";
	}
	
	public static int threadPoolSize() {
		return 1;
	}
	
	private final UsbServices platformServices;
	private final ObservableList<UsbDevice> devices = FXCollections.observableArrayList();
	private final ObservableList<UsbDriverFactory<?>> drivers = FXCollections.observableArrayList();
	private final ObservableList<ReadyDevice<?>> ready = FXCollections.observableArrayList();
	
	public UsbService() throws SecurityException, UsbException {
		super(serviceName(), threadPoolSize());
		platformServices = UsbHostManager.getUsbServices();
		assert(null != platformServices);
	}
	
	public ObservableList<UsbDevice> getDevices() {
		return devices;
	}
	
	public synchronized void installUsbDriverFactory(UsbDriverFactory<?> factory) {
		drivers.add(factory);
	}
	
	public ObservableList<UsbDriverFactory<?>> getDrivers() {
		return drivers;
	}
	
	public ObservableList<ReadyDevice<?>> getReadyByKind(Class<? extends DeviceKind> kind) {
		assert(null != kind);
		return ready.filtered(new Predicate<ReadyDevice<?>>() {
			@Override
			public boolean test(ReadyDevice<?> t) {
				return kind.isAssignableFrom(t.getDriverFactory().getDriverClass());
			}
		});
	}
	
	@Override
	protected synchronized void onStart() {
		populateDeviceCarousel();
		Supervisor.getEventBus().publishEventToBus(new UsbHelloEvent(this));
		platformServices.addUsbServicesListener(this);
	}

	@Override
	protected synchronized void onShutdown() {
		platformServices.removeUsbServicesListener(this);
		Supervisor.getEventBus().publishEventToBus(new UsbGoodbyeEvent(this));
	}

	@Override
	protected synchronized void onShutdownNow() {
		platformServices.removeUsbServicesListener(this);
		Supervisor.getEventBus().publishEventToBus(new UsbFastGoodbyeEvent(this));
	}
	
	@Override
	public synchronized void usbDeviceAttached(UsbServicesEvent event) {
		try {
			UsbDevice device = new UsbDevice(event.getUsbDevice());
			addDevice(device);
			Supervisor.getEventBus().publishEventToBus(new UsbAttachedEvent(device));
		} catch (UsbDisconnectedException | UsbException e) {
		}
	}

	@Override
	public synchronized void usbDeviceDetached(UsbServicesEvent event) {
		UsbDevice foundIt = null;
		for (UsbDevice device : devices) {
			if (device.getPlatformDescriptor() == event.getUsbDevice().getUsbDeviceDescriptor()) {
				foundIt = device;
				break;
			}
		}
		if (null != foundIt) {
			removeDevice(foundIt);
			Supervisor.getEventBus().publishEventToBus(new UsbDetachedEvent(foundIt));
		}
	}
	
	private synchronized void removeDevice(UsbDevice device) {
		boolean deviceWasRemoved = devices.remove(device);
		assert(deviceWasRemoved);
		ready.removeIf(new Predicate<ReadyDevice<?>>() {
			@Override
			public boolean test(ReadyDevice<?> t) {
				return t.getDevice().equals(device);
			}
		});
	}
	
	private synchronized void addDevice(UsbDevice device) {
		devices.add(device);
		for (UsbDriverFactory<?> driver : drivers) {
			if (driver.isCompatibleWith(device)) {
				ready.add(new ReadyDevice<>(device, driver));
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private synchronized void populateDeviceCarousel(javax.usb.UsbHub hub) {
		List<javax.usb.UsbDevice> rawDevices = (List<javax.usb.UsbDevice>)hub.getAttachedUsbDevices();
		for (javax.usb.UsbDevice device : rawDevices) {
			if (device.isUsbHub()) {
				populateDeviceCarousel((javax.usb.UsbHub)device);
			}
			else {
				try {
					addDevice(new UsbDevice(device));
				} catch (UsbDisconnectedException | UsbException e) {
				}
			}
		}
	}
	
	private synchronized void populateDeviceCarousel() {
		devices.clear();
		try {
			populateDeviceCarousel(platformServices.getRootUsbHub());
		} catch (UsbDisconnectedException | SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized <D extends UsbDriver> D getDriver(ReadyDevice<D> r) {
		UsbDevice device = r.getDevice();
		UsbDriverFactory<D> factory = r.getDriverFactory();
		D driver = null;
		synchronized(device) {
			if (device.inUse())
				return null;
			driver = factory.makeDriver(device);
			assert(null != driver);
			device.setDriver(driver);
		}
		return driver;
	}
}
