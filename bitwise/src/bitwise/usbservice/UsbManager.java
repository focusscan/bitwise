package bitwise.usbservice;

import org.usb4java.Context;
import org.usb4java.LibUsb;

import bitwise.engine.service.ServiceTask;
import bitwise.log.Log;

public final class UsbManager extends ServiceTask {
	private final Context context;
	
	protected UsbManager(UsbService usbService) throws LibUsbException {
		super(usbService);
		context = new Context();
		int result = LibUsb.init(context);
		if (result != LibUsb.SUCCESS)
			throw new LibUsbException(result);
	}
	
	@Override
	protected void taskMain() throws InterruptedException {
		Log.log(this, "Running");
		while (!isCancelled()) {
			Thread.sleep(20);
		}
		Log.log(this, "Finished");
	}
}
