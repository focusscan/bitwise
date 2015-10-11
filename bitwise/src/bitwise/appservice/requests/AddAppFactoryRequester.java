package bitwise.appservice.requests;

import bitwise.engine.service.Requester;

public interface AddAppFactoryRequester extends Requester {
	public void notifyRequestComplete(AddAppFactory<?, ?, ?> in);
}
