package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Arr<D extends IntegralType> implements PtpType {
	private ArrayList<D> value;
	
	public Arr(Decoder<D> decoder, ByteBuffer in) {
		UInt32 len = new UInt32(in);
		value = new ArrayList<>(len.getValue());
		for (int i = 0; i < len.getValue(); i++)
			value.add(i, decoder.decode(in));
	}
	
	public Arr(Decoder<D> decoder, ArrayList<D> in_value) {
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

	public void serialize(ByteArrayOutputStream stream) {
		UInt32 len = new UInt32(value.size());
		len.serialize(stream);
		for (D data : value)
			data.serialize(stream);
	}
}
