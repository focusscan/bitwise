package bitwise.apps.focusscan.scan;

import java.io.IOException;
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
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class ScanFile {
	private static final String SCAN = "scan";
	private static final String PROPERTIES = "properties";
	private static final String CAMERA_MANUFACTURER = "cameraManufacturer";
	private static final String CAMERA_MODEL = "cameraModel";
	private static final String CAMERA_VERSION = "cameraVersion";
	private static final String CAMERA_SERIAL = "cameraSerial";
	private static final String STEPS_PER_IMAGE = "stepsPerImage";
	private static final String STEPS_TOTAL = "stepsTotal";
	private static final String DATA = "data";
	private static final String CAPTURE = "capture";
	private static final String IMAGE_NUMBER = "imageNumber";
	private static final String LIVE_VIEW = "liveView";
	private static final String STILL = "still";
	
	private String cameraManufacturer = "";
	private String cameraModel = "";
	private String cameraVersion = "";
	private String cameraSerial = "";
	private int stepsPerImage = 0;
	private int stepsTotal = 0;
	
	private List<ScanFileDatum> data = new ArrayList<>();
	
	public ScanFile() {
		
	}
	
	public ScanFile(Path in) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		Document doc = db.parse(in.toFile());
		
		Node scan = doc.getFirstChild();
		while (null != scan && (Node.ELEMENT_NODE != scan.getNodeType() || !scan.getNodeName().equalsIgnoreCase(SCAN))) {
			scan = scan.getNextSibling();
		}
		assert (null != scan);
		
		// Scan properties
		{
			Node properties = scan.getFirstChild();
			while (null != properties && (Node.ELEMENT_NODE != properties.getNodeType() || !properties.getNodeName().equalsIgnoreCase(PROPERTIES))) {
				properties = properties.getNextSibling();
			}
			assert (null != properties);
			
			for (Node prop = properties.getFirstChild(); prop != null; prop = prop.getNextSibling()) {
				if (Node.ELEMENT_NODE == prop.getNodeType()) {
					if (prop.getNodeName().equalsIgnoreCase(CAMERA_MANUFACTURER)) {
						cameraManufacturer = prop.getNodeValue();
					}
					else if (prop.getNodeName().equalsIgnoreCase(CAMERA_MODEL)) {
						cameraModel = prop.getNodeValue();
					}
					else if (prop.getNodeName().equalsIgnoreCase(CAMERA_VERSION)) {
						cameraVersion = prop.getNodeValue();
					}
					else if (prop.getNodeName().equalsIgnoreCase(CAMERA_SERIAL)) {
						cameraSerial = prop.getNodeValue();
					}
					else if (prop.getNodeName().equalsIgnoreCase(STEPS_PER_IMAGE)) {
						stepsPerImage = Integer.parseInt(prop.getTextContent());
					}
					else if (prop.getNodeName().equalsIgnoreCase(STEPS_TOTAL)) {
						stepsTotal = Integer.parseInt(prop.getTextContent());
					}
				}
			}
		}
		// End scan properties
		
		Node dataNode = scan.getFirstChild();
		while (null != dataNode && (Node.ELEMENT_NODE != dataNode.getNodeType() || !dataNode.getNodeName().equalsIgnoreCase(DATA))) {
			dataNode = dataNode.getNextSibling();
		}
		assert (null != dataNode);
		for (Node capture = dataNode.getFirstChild(); capture != null; capture = capture.getNextSibling()) {
			if (Node.ELEMENT_NODE == capture.getNodeType() && capture.getNodeName().equalsIgnoreCase(CAPTURE)) {
				int imageNumber = -1;
				String liveView = null;
				String still = null;
				
				imageNumber = Integer.parseInt(capture.getAttributes().getNamedItem(IMAGE_NUMBER).getNodeValue());
				assert (imageNumber >= 0);
				
				for (Node prop = capture.getFirstChild(); prop != null; prop = prop.getNextSibling()) {
					if (Node.ELEMENT_NODE == prop.getNodeType()) {
						if (prop.getNodeName().equalsIgnoreCase(LIVE_VIEW)) {
							liveView = prop.getTextContent();
						}
						else if (prop.getNodeName().equalsIgnoreCase(STILL)) {
							still = prop.getTextContent();
						}
					}
				}
				
				ScanFileDatum datum = new ScanFileDatum();
				datum.setImageNumber(imageNumber);
				datum.setLiveViewImage(liveView);
				datum.setStillImage(still);
				data.add(datum);
			}
		}
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
		
		Element scanE = doc.createElement(SCAN);
		doc.appendChild(scanE);
		
		{
			Element scanPropertiesE = doc.createElement(PROPERTIES);
			scanE.appendChild(scanPropertiesE);
			
			{
				Element cameraManufacturerE = doc.createElement(CAMERA_MANUFACTURER);
				cameraManufacturerE.appendChild(doc.createTextNode(cameraManufacturer));
				scanPropertiesE.appendChild(cameraManufacturerE);
			}
			
			{
				Element cameraModelE = doc.createElement(CAMERA_MODEL);
				cameraModelE.appendChild(doc.createTextNode(cameraModel));
				scanPropertiesE.appendChild(cameraModelE);
			}
			
			{
				Element cameraVersionE = doc.createElement(CAMERA_VERSION);
				cameraVersionE.appendChild(doc.createTextNode(cameraVersion));
				scanPropertiesE.appendChild(cameraVersionE);
			}
			
			{
				Element cameraSerialE = doc.createElement(CAMERA_SERIAL);
				cameraSerialE.appendChild(doc.createTextNode(cameraSerial));
				scanPropertiesE.appendChild(cameraSerialE);
			}
			
			{
				Element stepsPerImageE = doc.createElement(STEPS_PER_IMAGE);
				stepsPerImageE.appendChild(doc.createTextNode(String.format("%d", stepsPerImage)));
				scanPropertiesE.appendChild(stepsPerImageE);
			}
			
			{
				Element stepsTotalE = doc.createElement(STEPS_TOTAL);
				stepsTotalE.appendChild(doc.createTextNode(String.format("%d", stepsTotal)));
				scanPropertiesE.appendChild(stepsTotalE);
			}
		}
		
		{
			Element dataE = doc.createElement(DATA);
			scanE.appendChild(dataE);
			
			for (ScanFileDatum datum : data) {
				Element captureE = doc.createElement(CAPTURE);
				captureE.setAttribute(IMAGE_NUMBER, String.format("%d", datum.getImageNumber()));
				dataE.appendChild(captureE);
				
				{
					Element liveViewE = doc.createElement(LIVE_VIEW);
					liveViewE.appendChild(doc.createTextNode(datum.getLiveViewImage()));
					captureE.appendChild(liveViewE);
				}
				
				{
					Element stillE = doc.createElement(STILL);
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
