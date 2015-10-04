package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Arr<D extends Datatype> extends Datatype {
	private ArrayList<D> value;
	
	public Arr(DatatypeDecoder<D> decoder, ByteBuffer in) {
		super((short) (decoder.getSample().getDatatypeCode() | 0x4000));
		UInt32 len = new UInt32(in);
		value = new ArrayList<>(len.getValue());
		for (int i = 0; i < len.getValue(); i++)
			value.add(i, decoder.decode(in));
	}
	
	public Arr(DatatypeDecoder<D> decoder, ArrayList<D> in_value) {
		super((short) (decoder.getSample().getDatatypeCode() | 0x4000));
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
		UInt32 len = new UInt32(value.size());
		len.serialize(stream);
		for (D data : value)
			data.serialize(stream);
	}
}
