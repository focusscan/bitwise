package bitwise.devices.usbptpcamera;

import java.util.concurrent.BlockingQueue;

import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.operations.Operation;
import bitwise.engine.service.BaseServiceTask;

public class UsbPtpInterruptTask extends BaseServiceTask<BaseUsbPtpCamera<?>> {
	private final BlockingQueue<Event> events;
	
	public UsbPtpInterruptTask(BaseUsbPtpCamera<?> in_service, BlockingQueue<Event> in_events) {
		super(in_service);
		events = in_events;
	}

	@Override
	protected void taskMain() throws InterruptedException {
		while (!isCancelled()) {
			Event event = events.take();
			Operation<?> currentOperation = getService().getCurrentOperation();
			if (null != currentOperation)
				currentOperation.recvInterruptData(event);
			getService().handlePtpEvent(event);
		}
	}
}
