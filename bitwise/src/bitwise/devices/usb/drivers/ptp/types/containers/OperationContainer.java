package bitwise.devices.usb.drivers.ptp.types.containers;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.operations.OperationCode;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class OperationContainer extends Container implements SendableContainer {
	private final OperationCode operationCode;
	private final TransactionID transactionID;
	private final ByteBuffer payload;
	
	public OperationContainer(OperationCode in_operationCode, TransactionID in_transactionID, ByteBuffer in_payload) {
		super(ContainerType.containerTypeCommand);
		operationCode = in_operationCode;
		transactionID = in_transactionID;
		payload = in_payload;
		assert(null != operationCode);
		assert(null != transactionID);
		assert(null != payload);
	}
	
	public OperationCode getCode() {
		return operationCode;
	}
	
	public TransactionID getTransactionID() {
		return transactionID;
	}
	
	
	@Override
	public void serialize(ByteArrayOutputStream stream) {
		int length = 12;
		if (null != payload)
			length += payload.remaining();
		
		(new UInt32(length)).serialize(stream);
		ContainerType.containerTypeCommand.serialize(stream);
		operationCode.serialize(stream);
		transactionID.serialize(stream);
		ByteBuffer _payload = payload.slice();
		while (_payload.hasRemaining())
			stream.write(_payload.get());
	}
}
