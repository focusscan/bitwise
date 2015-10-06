package bitwise.devices.usb.drivers.ptp.types.containers;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.Code;
import bitwise.devices.usb.drivers.ptp.types.TransactionID;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public abstract class Container {
	private final ContainerType containerType;
	
	public Container(ContainerType in_containerType) {
		containerType = in_containerType;
		assert(null != containerType);
	}
	
	protected Container(ByteBuffer in) {
		UInt32.decoder.decode(in); // Discard length
		containerType = ContainerType.decoder.decode(in);
	}
	
	public ContainerType getContainerType() {
		return containerType;
	}
	
	public abstract Code getCode();	
	public abstract TransactionID getTransactionID();
}
