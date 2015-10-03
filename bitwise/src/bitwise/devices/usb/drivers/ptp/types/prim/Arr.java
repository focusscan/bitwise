package bitwise.devices.usb.drivers.ptp.types.prim;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class Arr<D extends Datatype> extends Datatype {
	private ArrayList<D> value;
	
	public Arr(ArrayList<D> in_value, D sampleValue) {
		super((short) (sampleValue.getDatatypeCode() | 0x4000));
		setValue(in_value);
		assert(null != value);
	}
	
	public ArrayList<D> getValue() {
		return value;
	}
	
	public void setValue(ArrayList<D> in_value) {
		if (null == in_value)
			value = new ArrayList<>();
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
