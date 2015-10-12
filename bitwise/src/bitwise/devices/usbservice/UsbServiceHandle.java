package bitwise.devices.usbservice;

import bitwise.devices.Driver;
import bitwise.devices.DriverHandle;
import bitwise.devices.DriverRequest;
import bitwise.devices.usbservice.requests.AddUsbDriverFactory;
import bitwise.devices.usbservice.requests.AddUsbDriverFactoryRequester;
import bitwise.devices.usbservice.requests.StartUsbDriver;
import bitwise.devices.usbservice.requests.StartUsbDriverRequester;
import bitwise.engine.service.ServiceHandle;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public final class UsbServiceHandle extends ServiceHandle<UsbServiceRequest, UsbService> {
	protected UsbServiceHandle(UsbService in_service) {
		super(in_service);
	}
	
	public ObservableList<UsbDriverFactory<?, ?, ?>> getDriverFactoryList() {
		return getService().getDriverFactoryList();
	}
	
	public ObservableList<UsbDevice> getDeviceList() {
		return getService().getDeviceList();
	}
	
	public ObservableList<UsbReady<?, ?, ?>> getReadyList() {
		return getService().getReadyList();
	}
	
	public FilteredList<UsbReady<?, ?, ?>> getReadyByHandleType(Class<?> in_class) {
		return getService().getReadyByHandleType(in_class);
	}
	
	public <R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> AddUsbDriverFactory<R, H, A> addUsbDriverFactory(AddUsbDriverFactoryRequester requester, UsbDriverFactory<R, H, A> factory) {
		return new AddUsbDriverFactory<>(getService(), requester, factory);
	}
	
	public <R extends DriverRequest, H extends DriverHandle<R, ?>, A extends Driver<UsbDevice, R, H>> StartUsbDriver<R, H, A> startUsbDriver(StartUsbDriverRequester requester, UsbReady<R, H, A> ready) {
		return new StartUsbDriver<>(getService(), requester, ready);
	}
}
