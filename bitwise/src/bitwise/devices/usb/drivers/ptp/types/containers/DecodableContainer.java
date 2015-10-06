package bitwise.devices.usb.drivers.ptp.types.containers;

import java.nio.ByteBuffer;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public interface DecodableContainer {
	public static Decoder<DecodableContainer> decoder = new Decoder<DecodableContainer>() {
		@Override
		public DecodableContainer decode(ByteBuffer in) {
			ByteBuffer peek = in.slice();
			// Read length
			UInt32.decoder.decode(peek);
			// Read type
			ContainerType containerType = ContainerType.decoder.decode(peek);
			// Decode and return appropriately
			Decoder<? extends DecodableContainer> _decoder = null;
			if (containerType.equals(ContainerType.containerTypeData))
				_decoder = DataContainer.decoder;
			if (containerType.equals(ContainerType.containerTypeResponse))
				_decoder = ResponseContainer.decoder;
			if (containerType.equals(ContainerType.containerTypeEvent))
				_decoder = EventContainer.decoder;
			return (null == _decoder) ? null : _decoder.decode(in);
		}
	};
	
	public ByteBuffer getPayload();
}
