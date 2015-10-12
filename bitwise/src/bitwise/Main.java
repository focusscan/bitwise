package bitwise;

import bitwise.apps.focusscan.FocusScanFactory;
import bitwise.devices.nikon.d810.NikonD810Factory;
import bitwise.engine.supervisor.Supervisor;
import bitwise.gui.Workbench;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static MainCertificate cert;
	public static MainCertificate getCert() {
		if (null == cert)
			cert = new MainCertificate();
		return cert;
	}
	
	public static void main(String[] args) {
		System.out.println("Bitwise starting");
		System.out.println("Initializing supervisor...");
		Supervisor.getInstance().initialize(getCert());
		
		// Install the FocusScan app
		Supervisor.getInstance().addAppFactory(getCert(), FocusScanFactory.getInstance());
		Supervisor.getInstance().addUsbDriverFactory(getCert(), NikonD810Factory.getInstance());
		
		System.out.println("Supervisor initialized");
		
		System.out.println("Starting GUI...");
		launch(args);
		System.out.println("GUI exited");
		
		System.out.println("Stopping all services...");
		try {
			Supervisor.getInstance().stopAllServices(cert);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Bitwise exiting");
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Workbench.showNewWindow(primaryStage);
	}
}
