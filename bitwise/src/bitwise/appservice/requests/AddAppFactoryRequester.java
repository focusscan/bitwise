package bitwise.appservice.requests;

import bitwise.engine.service.BaseRequester;

public interface AddAppFactoryRequester extends BaseRequester {
	public void notifyRequestComplete(AddAppFactory<?, ?> in);
}
