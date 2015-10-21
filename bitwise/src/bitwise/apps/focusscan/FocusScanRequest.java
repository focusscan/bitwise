package bitwise.apps.focusscan;

import bitwise.apps.BaseAppRequest;
import bitwise.engine.service.BaseRequester;

public abstract class FocusScanRequest<R extends BaseRequester> extends BaseAppRequest<FocusScan, R> {
	protected FocusScanRequest(FocusScan in_service, R in_requester) {
		super(in_service, in_requester);
	}
}
