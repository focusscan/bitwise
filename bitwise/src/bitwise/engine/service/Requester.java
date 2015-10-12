package bitwise.engine.service;

import bitwise.apps.App;
import bitwise.appservice.AppServiceCertificate;
import bitwise.devices.Driver;
import bitwise.devices.usbservice.UsbServiceCertificate;

public interface Requester {
	public ServiceID getID();
	public void generalNotifyChildApp(AppServiceCertificate appCert, App<?, ?> in);
	public void generalNotifyChildDriver(UsbServiceCertificate usbCert, Driver<?, ?, ?> in);
	public void generalNotifyRequestComplete(Request in) throws InterruptedException;
	public void generalNotifyRequestFailure(Request in);
}
