package bitwise.devices.usbptpcamera.operations;

import java.util.concurrent.CountDownLatch;

import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbptpcamera.responses.ResponseData;

public abstract class Operation<T> {
	private final short operationCode;
	private final int[] arguments;
	private ResponseCode responseCode = null;
	private ResponseData responseData = null;
	private final CountDownLatch finishedLatch = new CountDownLatch(1);
	
	protected Operation(short in_operationCode, int arity) {
		operationCode = in_operationCode;
		arguments = new int[arity];
	}
	
	public final short getOperationCode() {
		return operationCode;
	}
	
	public final int[] getArguments() {
		return arguments;
	}
	
	public void setArgument(int argNum, byte val) {
		arguments[argNum] = 0xff & val;
	}
	
	public void setArgument(int argNum, short val) {
		arguments[argNum] = 0xffff & val;
	}
	
	public void setArgument(int argNum, int val) {
		arguments[argNum] = val;
	}
	
	public ResponseCode getResponseCode() {
		return responseCode;
	}
	
	public ResponseData getResponseData() {
		return responseData;
	}
	
	protected final void notifyFinished() {
		finishedLatch.countDown();
	}
	
	public final void awaitFinished() throws InterruptedException {
		finishedLatch.await();
	}
	
	public void recvResponseCode(ResponseCode response) {
		responseCode = response;
		notifyFinished();
	}
	
	public void recvResponseData(ResponseData response) {
		responseData = response;
	}
	
	public void recvInterruptData(Event event) {
		
	}
	
	public T getDecodedData() {
		return null;
	}
	
	public abstract boolean hasTransactionID();
}
