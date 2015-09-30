package bitwise.apps;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import bitwise.apps.events.AppLaunchedEvent;
import bitwise.devices.kinds.DeviceKind;
import bitwise.devices.usb.ReadyDevice;
import bitwise.devices.usb.UsbDevice;
import bitwise.devices.usb.UsbDriver;
import bitwise.engine.eventbus.Event;
import bitwise.engine.eventbus.EventNode;
import bitwise.engine.supervisor.Supervisor;

public abstract class App implements Runnable {
	private final AppID id;
	private boolean m_isRunning;
	private final ObservableList<UsbDriver> drivers = FXCollections.observableArrayList();
	
	public App() {
		id = new AppID();
		m_isRunning = true;
	}
	
	public final AppID getID() {
		return id;
	}
	
	public final boolean isRunning() {
		return m_isRunning;
	}
	
	public final void terminate() {
		m_isRunning = false;
		for (UsbDriver driver : drivers)
			driver.disableDriver();
	}
	
	@Override
	public boolean equals(Object o) {
		return (this == o); // Safe b/c uniquely determined by id
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("App<%08x> (%s)", id.getValue(), getName());
	}
	
	@Override
	public final void run() {
		try {
			EventNode lastProcessed = null;
			EventNode eventNode = Supervisor.getEventBus().getEventNode();
			Supervisor.getEventBus().publishEventToBus(new AppLaunchedEvent(this));
			do {
				if (lastProcessed != eventNode) {
					lastProcessed = eventNode;
					handleEvent(lastProcessed.getEvent());
				}
				if (m_isRunning) {
					EventNode next = eventNode.waitLimitedOnNext(2, TimeUnit.MILLISECONDS);
					if (null != next)
						eventNode = next;
				}
			} while (m_isRunning);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
		Supervisor.getApps().notifyAppTerminated(this);
	}
	
	@SuppressWarnings("unchecked")
	protected final <K extends DeviceKind> K getDriver(ReadyDevice<?> ready, Class<K> asKind) {
		assert(null != ready);
		UsbDevice device = ready.getDevice();
		synchronized(device) {
			if (device.inUse())
				return null;
			UsbDriver driver = Supervisor.getUSB().getDriver(ready);
			if (asKind.isAssignableFrom(driver.getClass())) {
				drivers.add(driver);
				return (K)driver;
			}
			else {
				driver.disableDriver();
				return null;
			}
		}
	}
	
	public abstract String getName();
	public abstract void handleEvent(Event event) throws IOException, InterruptedException;
}
