package bitwise;

import bitwise.apps.focusscan.FocusScanFactory;
import bitwise.devices.nikon.d810.NikonD810Factory;
import bitwise.engine.supervisor.Supervisor;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	
	public static void main(String[] args) {
		final MainCertificate cert = new MainCertificate();
		
		try {
			System.out.println("Bitwise starting");
			System.out.println("Initializing supervisor...");
			Supervisor.getInstance().initialize(cert);
			
			// Install the FocusScan app
			Supervisor.getInstance().addAppFactory(FocusScanFactory.getInstance());
			Supervisor.getInstance().addUsbDriverFactory(NikonD810Factory.getInstance());
			
			System.out.println("Supervisor initialized");
			
			System.out.println("Starting GUI...");
			launch(args);
			// Thread.sleep(5000);
			System.out.println("GUI exited");
			
			System.out.println("Stopping all services...");
			try {
				Supervisor.getInstance().stopAllServices(cert);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Bitwise exiting");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage stage) throws Exception {
	}
}
