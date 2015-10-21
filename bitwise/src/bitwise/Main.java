package bitwise;

import java.io.File;

import bitwise.apps.focusscan.FocusScanFactory;
import bitwise.devices.canon.eos7d.CanonEOS7DFactory;
import bitwise.devices.nikon.d7200.NikonD7200Factory;
import bitwise.devices.nikon.d810.NikonD810Factory;
import bitwise.engine.supervisor.Supervisor;
import bitwise.gui.PathChooser;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private static MainCertificate cert;
	public static MainCertificate getCert() {
		if (null == cert)
			cert = new MainCertificate();
		return cert;
	}
	
	private static volatile boolean initialized = false;
	
	public static boolean startBitwise(String path) {
		// Verify that the path is a directory
		File workpath = new File(path);
		if (!workpath.exists()) {
			System.out.println(String.format("Proposed path '%s' does not exist", path));
			return false;
		}
		if (!workpath.isDirectory()) {
			System.out.println(String.format("Proposed path '%s' is not a directory", path));
			return false;
		}
		if (!workpath.canRead()) {
			System.out.println(String.format("Proposed path '%s' is not readable", path));
			return false;
		}
		if (!workpath.canWrite()) {
			System.out.println(String.format("Proposed path '%s' is not writable", path));
			return false;
		}
		
		// Start bitwise
		System.out.println(String.format("Bitwise starting, workpath '%s'", path));
		System.out.println("Initializing supervisor...");
		Supervisor.buildSupervisor(getCert(), workpath.toPath());
		Supervisor.getInstance().initialize(getCert());
		
		// Install the FocusScan app
		Supervisor.getInstance().addAppFactory(getCert(), FocusScanFactory.getInstance());
		Supervisor.getInstance().addUsbDriverFactory(getCert(), NikonD810Factory.getInstance());
		Supervisor.getInstance().addUsbDriverFactory(getCert(), NikonD7200Factory.getInstance());
		Supervisor.getInstance().addUsbDriverFactory(getCert(), CanonEOS7DFactory.getInstance());
		
		System.out.println("Supervisor initialized");
		initialized = true;
		return true;
	}
	
	public static void main(String[] args) {
		System.out.println("Starting GUI...");
		launch(args);
		System.out.println("GUI exited");
		
		if (initialized) {
			System.out.println("Stopping all services...");
			try {
				Supervisor.getInstance().stopAllServices(cert);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Bitwise exited");
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		PathChooser.showNewWindow(primaryStage);
	}
}
