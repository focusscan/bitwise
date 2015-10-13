package bitwise.devices.usb.drivers.canon.types;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt16;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt8;

public class CanonArr implements PtpType {
	private ArrayList<UInt32> value;
	
	public CanonArr(Decoder<UInt32> decoder, ByteBuffer in) {
		UInt32 len_in_bytes = new UInt32(in);
		int num_int32 = (len_in_bytes.getValue()) / 4 - 1;
		int remainder = (len_in_bytes.getValue()) % 4;		
		
		value = new ArrayList<>(num_int32);
		for (int i = 0; i < num_int32; i++)
			value.add(i, decoder.decode(in));
		
		if (remainder == 1) {
			value.add(new UInt32(UInt8.decoder.decode(in).getValue()));
		} else if (remainder == 2) {
			value.add(new UInt32(UInt16.decoder.decode(in).getValue()));
		} else if (remainder == 3) {
			int val = UInt16.decoder.decode(in).getValue();
			val += UInt8.decoder.decode(in).getValue() << 16;
			value.add(new UInt32(val));
		}
	}
	
	public CanonArr(Decoder<UInt32> decoder, ArrayList<UInt32> in_value) {
		setValue(in_value);
		assert(null != value);
	}
	
	public ArrayList<UInt32> getValue() {
		return value;
	}
	
	public void setValue(ArrayList<UInt32> in_value) {
		if (null == in_value)
			value = new ArrayList<>(0);
		else
			value = in_value;
	}

	@Override
	public void serialize(ByteArrayOutputStream stream) {
		UInt32 len = new UInt32((value.size()+1) * 4);
		len.serialize(stream);
		for (UInt32 data : value)
			data.serialize(stream);
	}
}
