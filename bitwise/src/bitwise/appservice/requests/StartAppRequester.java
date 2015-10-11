package bitwise.appservice.requests;

import bitwise.engine.service.Requester;

public interface StartAppRequester extends Requester {
	public void notifyRequestComplete(StartApp<?, ?, ?> in);
}
