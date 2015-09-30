package bitwise.apps.focusscan3d;

import bitwise.apps.AppFactory;

public class FocusScan3DFactory extends AppFactory<FocusScan3D> {
	protected static final String driverName = "Focus Scan 3D";
	
	private static final FocusScan3DFactory factory = new FocusScan3DFactory();
	public static FocusScan3DFactory getInstance() {
		return factory;
	}
	
	private FocusScan3DFactory() {
		super();
	}

	@Override
	public FocusScan3D makeApp() {
		return new FocusScan3D();
	}

	@Override
	public String getName() {
		return driverName;
	}
}
