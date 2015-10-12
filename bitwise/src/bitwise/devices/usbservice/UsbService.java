package bitwise.devices.usbservice;

import java.util.List;

import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.UsbHub;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.engine.service.Request;
import bitwise.engine.service.Requester;
import bitwise.engine.service.Service;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class UsbService extends Service<UsbServiceRequest, UsbServiceHandle> implements UsbServicesListener {
	private final UsbServiceCertificate cert = new UsbServiceCertificate();
	private final UsbServiceHandle serviceHandle;
	private final UsbTree tree = new UsbTree();
	private final ObservableList<UsbDriverFactory<?, ?, ?>> factories = FXCollections.observableArrayList();
	
	public UsbService(SupervisorCertificate supervisorCert) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		serviceHandle = new UsbServiceHandle(this);
	}
	
	public void addDriverFactory(UsbDriverFactory<?, ?, ?> factory) {
		factories.add(factory);
		Log.log(this, "Added factory %s", factory);
	}
	
	public <R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> H startDriver(Requester requester, UsbDriverFactory<R, H, A> factory, UsbDevice device) {
		Log.log(this, "Starting driver from factory %s", factory);
		A driver = factory.makeDriver(cert, device);
		requester.generalNotifyChildDriver(cert, driver);
		Supervisor.getInstance().addService(driver);
		Log.log(this, "Started driver %s", driver);
		return driver.getServiceHandle();
	}
	
	@Override
	public UsbServiceHandle getServiceHandle() {
		return serviceHandle;
	}
	
	@SuppressWarnings("unchecked")
	private void enumerateDevices(UsbHub hub) {
		for (javax.usb.UsbDevice device : (List<javax.usb.UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub())
				enumerateDevices((UsbHub) device);
			tree.addAttachedDevice(device);
		}
	}
	
	@Override
	protected boolean onStartService() {
		try {
			tree.clearTree();
			UsbHostManager.getUsbServices().addUsbServicesListener(this);
			enumerateDevices(UsbHostManager.getUsbServices().getRootUsbHub());
			return true;
		} catch (SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onStopService() {
		try {
			UsbHostManager.getUsbServices().removeUsbServicesListener(this);
			tree.clearTree();
		} catch (SecurityException | UsbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onRequestComplete(Request in) {
	}

	@Override
	public void usbDeviceAttached(UsbServicesEvent event) {
		UsbDevice device = tree.addAttachedDevice(event.getUsbDevice());
		if (null != device)
			Log.log(this, "Device attached: %s", device);
	}

	@Override
	public void usbDeviceDetached(UsbServicesEvent event) {
		UsbDevice device = tree.removeDetachedDevice(event.getUsbDevice());
		if (null != device)
			Log.log(this, "Device removed:  %s", device);
	}
}
