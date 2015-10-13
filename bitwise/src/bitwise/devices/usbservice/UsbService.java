package bitwise.devices.usbservice;

import javax.usb.UsbException;
import javax.usb.UsbHostManager;
import javax.usb.event.UsbServicesEvent;
import javax.usb.event.UsbServicesListener;

import bitwise.devices.BaseDriver;
import bitwise.devices.BaseDriverHandle;
import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;
import bitwise.engine.service.BaseService;
import bitwise.engine.supervisor.Supervisor;
import bitwise.engine.supervisor.SupervisorCertificate;
import bitwise.log.Log;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public final class UsbService extends BaseService<UsbServiceHandle> implements UsbServicesListener {
	private final UsbServiceCertificate cert = new UsbServiceCertificate();
	private final UsbServiceHandle serviceHandle;
	private final UsbTree tree;
	
	public UsbService(SupervisorCertificate supervisorCert) {
		super();
		if (null == supervisorCert)
			throw new IllegalArgumentException("SupervisorCertificate");
		tree = new UsbTree(this);
		serviceHandle = new UsbServiceHandle(this);
	}
	
	public ObservableList<UsbDriverFactory<?, ?>> getDriverFactoryList() {
		return tree.getDriverFactoryList();
	}
	
	public ObservableList<UsbDevice> getDeviceList() {
		return tree.getDeviceList();
	}
	
	public ObservableList<UsbReady<?, ?>> getReadyList() {
		return tree.getReadyList();
	}
	
	public FilteredList<UsbReady<?, ?>> getReadyByHandleType(Class<?> in_class) {
		return tree.getReadyByHandleType(in_class);
	}
	
	public void addDriverFactory(UsbDriverFactory<?, ?> factory) {
		tree.addDriverFactory(factory);
	}
	
	public <H extends BaseDriverHandle<?, ?>, A extends BaseDriver<UsbDevice, H>> H startDriver(BaseRequester requester, UsbDevice device, UsbDriverFactory<H, A> factory) {
		Log.log(this, "Starting driver from factory %s", factory);
		A driver = factory.makeDriver(cert, device);
		requester.generalNotifyChildDriver(cert, driver);
		Supervisor.getInstance().addService(driver);
		Log.log(this, "Started driver %s", driver);
		return driver.getServiceHandle();
	}
	
	protected void deviceDriverSet(UsbDevice in) {
		tree.deviceDriverSet(in);
	}
	
	protected void deviceDriverUnset(UsbDevice in) {
		tree.deviceDriverUnset(in);
	}
	
	@Override
	public UsbServiceHandle getServiceHandle() {
		return serviceHandle;
	}
	
	/*
	@SuppressWarnings("unchecked")
	private void enumerateDevices(UsbHub hub) {
		for (javax.usb.UsbDevice device : (List<javax.usb.UsbDevice>) hub.getAttachedUsbDevices()) {
			if (device.isUsbHub())
				enumerateDevices((UsbHub) device);
			tree.addAttachedDevice(device);
		}
	}
	*/
	
	@Override
	protected boolean onStartService() {
		try {
			tree.clearTree();
			UsbHostManager.getUsbServices().addUsbServicesListener(this);
			// enumerateDevices(UsbHostManager.getUsbServices().getRootUsbHub());
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
	protected void onRequestComplete(BaseRequest<?, ?> in) {
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
