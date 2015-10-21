package bitwise.engine.service;

import bitwise.apps.BaseApp;
import bitwise.appservice.AppServiceCertificate;
import bitwise.devices.BaseDriver;
import bitwise.devices.usbservice.UsbServiceCertificate;

public interface BaseRequester {
	public ServiceID getID();
	public void generalNotifyChildApp(AppServiceCertificate appCert, BaseApp<?> in);
	public void generalNotifyChildDriver(UsbServiceCertificate usbCert, BaseDriver<?, ?> in);
	public void generalNotifyRequestEnqueued(BaseRequest<?, ?> in);
	public void generalNotifyRequestComplete(BaseRequest<?, ?> in);
	public void generalNotifyRequestFailure(BaseRequest<?, ?> in);
}
