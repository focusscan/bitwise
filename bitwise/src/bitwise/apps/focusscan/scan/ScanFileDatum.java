package bitwise.apps.focusscan.scan;

public class ScanFileDatum {
	private int imageNumber = 0;
	private String liveViewImage = "";
	private String stillImage = "";
	
	public ScanFileDatum() {
		
	}
	
	public int getImageNumber() {
		return imageNumber;
	}
	
	public void setImageNumber(int in) {
		imageNumber = in;
	}
	
	public String getLiveViewImage() {
		return liveViewImage;
	}
	
	public void setLiveViewImage(String in) {
		liveViewImage = in;
	}
	
	public String getStillImage() {
		return stillImage;
	}
	
	public void setStillImage(String in) {
		stillImage = in;
	}
}
