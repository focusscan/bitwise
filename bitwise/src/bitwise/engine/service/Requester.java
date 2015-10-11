package bitwise.engine.service;

import bitwise.appservice.AppServiceCertificate;

public interface Requester {
	public ServiceID getID();
	public void generalNotifyChildService(AppServiceCertificate appCert, Service<?, ?> in);
	public void generalNotifyRequestComplete(Request in) throws InterruptedException;
	public void generalNotifyRequestFailure(Request in);
}
