package bitwise.apps.focusscan;

import bitwise.appservice.app.App;
import bitwise.engine.service.Request;
import bitwise.log.Log;

public final class FocusScan extends App<FocusScanRequest, FocusScanHandle> {
	private final FocusScanHandle handle = new FocusScanHandle(this);
	
	protected FocusScan() {
		super();
	}
	
	@Override
	public FocusScanHandle getServiceHandle() {
		return handle;
	}

	@Override
	protected boolean onStartService() {
		Log.log(this, "Focus Scan starting");
		return true;
	}

	@Override
	protected void onStopService() {
		Log.log(this, "Focus Scan stopped");
	}

	@Override
	protected void onRequestComplete(Request in) {
		Log.log(this, "Focus Scan request complete");
	}
}
