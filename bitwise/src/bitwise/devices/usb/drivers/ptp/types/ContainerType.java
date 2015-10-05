package bitwise.devices.usb.drivers.ptp.types;

import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;

public class ContainerType extends UInt16 {
	public static final ContainerType containerTypeCommand = new ContainerType((short) 1);
	public static final ContainerType containerTypeData = new ContainerType((short) 2);
	public static final ContainerType containerTypeResponse = new ContainerType((short) 3);
	public static final ContainerType containerTypeEvent = new ContainerType((short) 4);
	
	public ContainerType(short in_value) {
		super(in_value);
	}
}
