package bitwise.apps.focusscan;

import bitwise.apps.BaseAppFactory;
import bitwise.log.Log;

public final class FocusScanFactory extends BaseAppFactory<FocusScanHandle, FocusScan> {
	private static FocusScanFactory instance;
	public static FocusScanFactory getInstance() {
		if (null == instance) {
			instance = new FocusScanFactory();
			Log.log(instance, "Instance created");
		}
		return instance;
	}
	
	protected FocusScanFactory() {
		
	}
	
	@Override
	public String getAppName() {
		return "Focus Scan";
	}
	
	@Override
	public FocusScan doMakeApp() {
		return new FocusScan();
	}
}
