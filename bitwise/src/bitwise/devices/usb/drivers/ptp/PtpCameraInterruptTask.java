package bitwise.devices.usb.drivers.ptp;

import bitwise.devices.usb.drivers.ptp.types.containers.DecodableContainer;
import bitwise.devices.usb.drivers.ptp.types.containers.EventContainer;
import bitwise.devices.usb.drivers.ptp.types.events.Event;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import javafx.concurrent.Task;

public class PtpCameraInterruptTask extends Task<Void> {
	private final PtpCamera camera;
	
	protected PtpCameraInterruptTask(PtpCamera in_camera) {
		super();
		camera = in_camera;
	}
	
	private boolean keepRunning() {
		return !isCancelled() && camera.getResourceIsOpen().get();
	}
	
	@Override
	protected Void call() {
		try {
			do {
				DecodableContainer interrupt = camera.readPtpPacket(camera.interruptPipe);
				if (null != interrupt && interrupt instanceof EventContainer) {
					Decoder<? extends Event> decoder = ((EventContainer) interrupt).getCode().getEventDecoder();
					if (null != decoder) {
						System.out.println("Decoder found");
						Event event = decoder.decode(interrupt.getPayload());
						System.out.println("Decoded");
						camera.handleEvent(event);
						System.out.println("Handled");
					}
				}
			} while (keepRunning());
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Interrupt thread exiting");
		return null;
	}
}
