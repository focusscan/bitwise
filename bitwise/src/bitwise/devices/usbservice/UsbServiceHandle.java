package bitwise.devices.usbservice;

import bitwise.devices.BaseDriverHandle;
import bitwise.devices.usbservice.requests.AddUsbDriverFactory;
import bitwise.devices.usbservice.requests.AddUsbDriverFactoryRequester;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.devices.usbservice.requests.StartUsbDriverRequester;
import bitwise.engine.service.BaseServiceHandle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public final class UsbServiceHandle extends BaseServiceHandle<UsbServiceRequest<?>, UsbService> {
	protected UsbServiceHandle(UsbService in_service) {
		super(in_service);
	}
	
	public ObservableList<UsbDriverFactory<?>> getDriverFactoryList() {
		return getService().getDriverFactoryList();
	}
	
	public ObservableList<UsbDevice> getDeviceList() {
		return getService().getDeviceList();
	}
	
	public ObservableList<UsbReady<?>> getReadyList() {
		return getService().getReadyList();
	}
	
	public FilteredList<UsbReady<?>> getReadyByHandleType(Class<?> in_class) {
		return getService().getReadyByHandleType(in_class);
	}
	
	public <H extends BaseDriverHandle<?, ?>> AddUsbDriverFactory<H> addUsbDriverFactory(AddUsbDriverFactoryRequester requester, UsbDriverFactory<H> factory) {
		AddUsbDriverFactory<H> request = new AddUsbDriverFactory<>(getService(), requester, factory);
		this.enqueueRequest(request);
		return request;
	}
	
	public <H extends BaseDriverHandle<?, ?>> StartUsbDriver<H> startUsbDriver(StartUsbDriverRequester requester, UsbReady<H> ready) {
		StartUsbDriver<H> request = new StartUsbDriver<>(getService(), requester, ready);
		this.enqueueRequest(request);
		return request;
	}
}
