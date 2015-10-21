package bitwise.devices.usbptpcamera.operations;

import java.util.concurrent.CountDownLatch;

import bitwise.devices.usbptpcamera.coder.Encodable;
import bitwise.devices.usbptpcamera.coder.UsbPtpCoderException;
import bitwise.devices.usbptpcamera.events.Event;
import bitwise.devices.usbptpcamera.responses.ResponseCode;
import bitwise.devices.usbptpcamera.responses.ResponseData;

public abstract class Operation<T> {
	private final short operationCode;
	private final int[] arguments;
	protected Encodable dataOut;
	protected ResponseCode responseCode = null;
	protected ResponseData responseData = null;
	private final CountDownLatch finishedLatch = new CountDownLatch(1);
	
	protected Operation(short in_operationCode, int arity) {
		operationCode = in_operationCode;
		arguments = new int[arity];
		dataOut = null;
	}
	
	protected Operation(short in_operationCode, int arity, Encodable in_dataOut) {
		operationCode = in_operationCode;
		arguments = new int[arity];
		dataOut = in_dataOut;
	}
	
	public final short getOperationCode() {
		return operationCode;
	}
	
	public final int[] getArguments() {
		return arguments;
	}
	
	public final Encodable getDataOut() {
		return dataOut;
	}
	
	public final void setArgument(int argNum, byte val) {
		arguments[argNum] = 0xff & val;
	}
	
	public final void setArgument(int argNum, short val) {
		arguments[argNum] = 0xffff & val;
	}
	
	public final void setArgument(int argNum, int val) {
		arguments[argNum] = val;
	}
	
	public boolean isSuccess() {
		return (null != responseCode && responseCode.getResponseCode() == ResponseCode.success);
	}
	
	public final ResponseCode getResponseCode() {
		return responseCode;
	}
	
	public final ResponseData getResponseData() {
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
	
	public T getDecodedData() throws UsbPtpCoderException {
		return null;
	}
	
	public abstract boolean hasTransactionID();
}
