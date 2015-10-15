package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface DriveFocusRequest extends Request {
	public static enum Direction {
		TowardsNear,
		TowardsFar,
	}
}
