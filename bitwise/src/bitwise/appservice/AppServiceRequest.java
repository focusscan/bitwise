package bitwise.appservice;

import bitwise.engine.service.BaseRequest;
import bitwise.engine.service.BaseRequester;

public abstract class AppServiceRequest<R extends BaseRequester> extends BaseRequest<AppService, R> {
	protected AppServiceRequest(AppService in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
