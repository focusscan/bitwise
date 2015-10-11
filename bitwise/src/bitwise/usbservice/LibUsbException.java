package bitwise.usbservice;

public final class LibUsbException extends Exception {
	private static final long serialVersionUID = -1409298124305155166L;
	
	
	public final int errno;
	
	protected LibUsbException(int in_errno) {
		errno = in_errno;
	}
}
