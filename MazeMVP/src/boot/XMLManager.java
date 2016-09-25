package boot;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import presenter.Properties;

public class XMLManager {

	private Properties properties;
	private String fileName;
	
	public void writeXML(Properties p) throws FileNotFoundException {
		XMLEncoder xmlEncoder = null;
		
		try {
			xmlEncoder = new XMLEncoder(new FileOutputStream(getFileName()));
			xmlEncoder.writeObject(p);
			xmlEncoder.flush();
			setProperties(p);
			
			xmlEncoder.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public void readXML (String fName) {
		XMLDecoder xmlDecoder = null;
		setFileName(fName);
		
		try {
			FileInputStream fis = new FileInputStream(fileName);
			BufferedInputStream bis = new BufferedInputStream(fis);
			
			xmlDecoder = new XMLDecoder(bis);
			
			setProperties((Properties)xmlDecoder.readObject());
			
			xmlDecoder.close();
		}
		
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
