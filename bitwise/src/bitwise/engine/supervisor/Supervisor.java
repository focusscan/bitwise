package bitwise.engine.supervisor;

import java.util.concurrent.TimeUnit;

import javax.usb.UsbException;

import bitwise.apps.AppService;
import bitwise.apps.focusscan3d.FocusScan3DFactory;
import bitwise.devices.usb.UsbService;
import bitwise.devices.usb.drivers.nikon.d7200.NikonD7200Factory;
import bitwise.devices.usb.drivers.nikon.d810.NikonD810Factory;
import bitwise.engine.carousel.Carousel;
import bitwise.engine.eventbus.EventBus;
import bitwise.engine.supervisor.events.BitwiseStartedEvent;
import bitwise.engine.supervisor.events.BitwiseTerminatedEvent;
import bitwise.engine.supervisor.events.ServiceTerminatedEvent;

public final class Supervisor {
	private static Supervisor INSTANCE = new Supervisor();
	
	public static synchronized Supervisor getInstance() {
		return INSTANCE;
	}
	
	private final EventBus eventBus = new EventBus();
	private final Carousel carousel;
	private final UsbService usb;
	private final AppService apps;
	
	protected Supervisor() {
		carousel = new Carousel();
		UsbService temp_usb = null;
		try {
			temp_usb = new UsbService();
		} catch (SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		usb = temp_usb;
		apps = new AppService();
	}
	
	public static synchronized boolean startBitwise() {
		// Install standard drivers
		getUSB().installUsbDriverFactory(NikonD810Factory.getInstance());
		getUSB().installUsbDriverFactory(NikonD7200Factory.getInstance());
		// Install standard apps
		getApps().installAppFactory(FocusScan3DFactory.getInstance());
		// Start everything
		if (!carouselServiceIsRunning() && !startCarouselService())
			return false;
		if (!usbServiceIsRunning() && !startUSBService())
			return false;
		if (!appsServiceIsRunning() && !startAppsService())
			return false;
		getEventBus().publishEventToBus(new BitwiseStartedEvent());
		return true;
	}
	
	public static synchronized boolean shutdownBitwise() throws InterruptedException {
		if (appsServiceIsRunning())
			stopAppsService();
		if (carouselServiceIsRunning())
			stopCarouselService();
		if (usbServiceIsRunning())
			stopUSBService();
		getEventBus().publishEventToBus(new BitwiseTerminatedEvent());
		return true;
	}
	
	public static EventBus getEventBus() {
		return INSTANCE.eventBus;
	}
	
	public static Carousel getCarousel() {
		return INSTANCE.carousel;
	}
	
	public static synchronized boolean carouselServiceIsRunning() {
		return getCarousel().isRunning();
	}
	
	public static synchronized boolean startCarouselService() {
		return getCarousel().start();
	}
	
	public static synchronized boolean stopCarouselService() throws InterruptedException {
		if (getCarousel().shutdown()) {
			if (getCarousel().awaitTermination(5,  TimeUnit.SECONDS)) {
				getEventBus().publishEventToBus(new ServiceTerminatedEvent(getCarousel()));
				return true;
			}
			return false;
		}
		return true;
	}
	
	public static UsbService getUSB() {
		return INSTANCE.usb;
	}
	
	public static synchronized boolean usbServiceIsRunning() {
		return getUSB().isRunning();
	}
	
	public static synchronized boolean startUSBService() {
		return getUSB().start();
	}
	
	public static synchronized boolean stopUSBService() throws InterruptedException {
		if (getUSB().shutdown()) {
			if (getUSB().awaitTermination(5,  TimeUnit.SECONDS)) {
				getEventBus().publishEventToBus(new ServiceTerminatedEvent(getUSB()));
				return true;
			}
			return false;
		}
		return true;
	}
	
	public static AppService getApps() {
		return INSTANCE.apps;
	}
	
	public static synchronized boolean appsServiceIsRunning() {
		return getApps().isRunning();
	}
	
	public static synchronized boolean startAppsService() {
		return getApps().start();
	}
	
	public static synchronized boolean stopAppsService() throws InterruptedException {
		if (getApps().shutdown()) {
			if (getApps().awaitTermination(5,  TimeUnit.SECONDS)) {
				getEventBus().publishEventToBus(new ServiceTerminatedEvent(getApps()));
				return true;
			}
			return false;
		}
		return true;
	}
}
