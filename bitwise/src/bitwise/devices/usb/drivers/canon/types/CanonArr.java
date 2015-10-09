package bitwise.devices.usb.drivers.canon.types;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import bitwise.devices.usb.drivers.ptp.types.prim.Arr;
import bitwise.devices.usb.drivers.ptp.types.prim.Decoder;
import bitwise.devices.usb.drivers.ptp.types.prim.IntegralType;
import bitwise.devices.usb.drivers.ptp.types.prim.PtpType;
import bitwise.devices.usb.drivers.ptp.types.prim.UInt32;

public class CanonArr<D extends IntegralType> implements PtpType {
	private ArrayList<D> value;
	
	public CanonArr(Decoder<D> decoder, ByteBuffer in) {
		UInt32 len = new UInt32(in);
		value = new ArrayList<>((len.getValue() - 1) / 4);
		for (int i = 4; i < len.getValue(); i += 4)
			value.add((i-4)/4, decoder.decode(in));
	}
	
	public CanonArr(Decoder<D> decoder, ArrayList<D> in_value) {
		setValue(in_value);
		assert(null != value);
	}
	
	public ArrayList<D> getValue() {
		return value;
	}
	
	public void setValue(ArrayList<D> in_value) {
		if (null == in_value)
			value = new ArrayList<>(0);
		else
			value = in_value;
	}

	@Override
	public void serialize(ByteArrayOutputStream stream) {
		UInt32 len = new UInt32((value.size()+1) * 4);
		len.serialize(stream);
		for (D data : value)
			data.serialize(stream);
	}
}
