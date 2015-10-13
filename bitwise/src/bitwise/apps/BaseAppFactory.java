package bitwise.apps;

import bitwise.appservice.AppServiceCertificate;
import bitwise.engine.Thing;

public abstract class BaseAppFactory<H extends BaseAppHandle<?,?>, A extends BaseApp<H>> extends Thing<AppFactoryID> {
	protected BaseAppFactory() {
		super(new AppFactoryID());
	}
	
	public final A makeApp(AppServiceCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("AppServiceCertificate");
		return doMakeApp();
	}
	
	public abstract String getAppName();
	
	public abstract A doMakeApp();
}
