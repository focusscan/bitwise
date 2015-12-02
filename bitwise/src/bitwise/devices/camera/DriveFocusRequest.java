package bitwise.devices.camera;

import bitwise.engine.service.Request;

public interface DriveFocusRequest extends Request {
	public static enum Direction {
		TowardsNear,
		TowardsFar,
	}
	
	public static enum Result {
		Nonblocking,
		FocusMoved,
		LimitReached,
	}
	
	public Result getDriveFocusResult();
}
