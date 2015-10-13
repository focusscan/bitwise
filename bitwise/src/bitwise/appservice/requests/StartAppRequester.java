package bitwise.appservice.requests;

import bitwise.engine.service.BaseRequester;

public interface StartAppRequester extends BaseRequester {
	public void notifyRequestComplete(StartApp<?, ?> in);
}
