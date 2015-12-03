package bitwise.apps.focusscan.scan;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ScanFile {
	private String cameraManufacturer = "";
	private String cameraModel = "";
	private String cameraVersion = "";
	private String cameraSerial = "";
	private int stepsPerImage = 0;
	private int stepsTotal = 0;
	
	private List<ScanFileDatum> data = new ArrayList<>();
	
	public ScanFile() {
		
	}
	
	public boolean saveToFile(Path out) {
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		try {
			Transformer transformer = transformerFactory.newTransformer();
			transformer.transform(getXml(), new StreamResult(out.toFile()));
		} catch (TransformerException e) {
			return false;
		}
		
		return true;
	}
	
	public DOMSource getXml() {
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Document doc = docBuilder.newDocument();
		
		Element scanE = doc.createElement("scan");
		doc.appendChild(scanE);
		
		{
			Element scanPropertiesE = doc.createElement("properties");
			scanE.appendChild(scanPropertiesE);
			
			{
				Element cameraManufacturerE = doc.createElement("cameraManufacturer");
				cameraManufacturerE.appendChild(doc.createTextNode(cameraManufacturer));
				scanPropertiesE.appendChild(cameraManufacturerE);
			}
			
			{
				Element cameraModelE = doc.createElement("cameraModel");
				cameraModelE.appendChild(doc.createTextNode(cameraModel));
				scanPropertiesE.appendChild(cameraModelE);
			}
			
			{
				Element cameraVersionE = doc.createElement("cameraVersion");
				cameraVersionE.appendChild(doc.createTextNode(cameraVersion));
				scanPropertiesE.appendChild(cameraVersionE);
			}
			
			{
				Element cameraSerialE = doc.createElement("cameraSerial");
				cameraSerialE.appendChild(doc.createTextNode(cameraSerial));
				scanPropertiesE.appendChild(cameraSerialE);
			}
			
			{
				Element stepsPerImageE = doc.createElement("stepsPerImage");
				stepsPerImageE.appendChild(doc.createTextNode(String.format("%d", stepsPerImage)));
				scanPropertiesE.appendChild(stepsPerImageE);
			}
			
			{
				Element stepsTotalE = doc.createElement("stepsTotal");
				stepsTotalE.appendChild(doc.createTextNode(String.format("%d", stepsTotal)));
				scanPropertiesE.appendChild(stepsTotalE);
			}
		}
		
		{
			Element dataE = doc.createElement("data");
			scanE.appendChild(dataE);
			
			for (ScanFileDatum datum : data) {
				Element captureE = doc.createElement("capture");
				captureE.setAttribute("imageNumber", String.format("%d", datum.getImageNumber()));
				dataE.appendChild(captureE);
				
				{
					Element liveViewE = doc.createElement("liveView");
					liveViewE.appendChild(doc.createTextNode(datum.getLiveViewImage()));
					captureE.appendChild(liveViewE);
				}
				
				{
					Element stillE = doc.createElement("still");
					stillE.appendChild(doc.createTextNode(datum.getStillImage()));
					captureE.appendChild(stillE);
				}
			}
		}
		
		return new DOMSource(doc);
	}
	
	public String getCameraManufacturer() {
		return cameraManufacturer;
	}
	
	public void setCameraManufacturer(String in) {
		cameraManufacturer = in;
	}
	
	public String getCameraModel() {
		return cameraModel;
	}
	
	public void setCameraModel(String in) {
		cameraModel = in;
	}
	
	public String getCameraVersion() {
		return cameraVersion;
	}
	
	public void setCameraVersion(String in) {
		cameraVersion = in;
	}
	
	public String getCameraSerial() {
		return cameraSerial;
	}
	
	public void setCameraSerial(String in) {
		cameraSerial = in;
	}
	
	public int getStepsPerImage() {
		return stepsPerImage;
	}
	
	public void setStepsPerImage(int in) {
		stepsPerImage = in;
	}
	
	public int getStepsTotal() {
		return stepsTotal;
	}
	
	public void setStepsTotal(int in) {
		stepsTotal = in;
	}
	
	public List<ScanFileDatum> getData() {
		return data;
	}
	
	public ScanFileDatum addData(int imageNumber, String liveView, String still) {
		ScanFileDatum datum = new ScanFileDatum();
		datum.setImageNumber(imageNumber);
		datum.setLiveViewImage(liveView);
		datum.setStillImage(still);
		data.add(datum);
		return datum;
	}
}
