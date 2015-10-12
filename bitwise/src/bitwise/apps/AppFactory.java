package bitwise.apps;

import bitwise.appservice.AppServiceCertificate;
import bitwise.engine.Thing;

public abstract class AppFactory<R extends AppRequest, H extends AppHandle<R, ?>, A extends App<R, H>> extends Thing<AppFactoryID> {
	protected AppFactory() {
		super(new AppFactoryID());
	}
	
	public final A makeApp(AppServiceCertificate cert) {
		if (null == cert)
			throw new IllegalArgumentException("AppServiceCertificate");
		return doMakeApp();
	}
	
	public abstract A doMakeApp();
}
