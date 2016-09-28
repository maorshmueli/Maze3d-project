package boot;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import presenter.Properties;

public class XMLManager {

		 
	private static Properties properties = null;
		
		/**
		 *  readXML from file
		 *  read all the properties fron the xml
		 * @return Properties
		 */
		public static Properties readXML(File file) {
			JAXBContext jaxbContext = null;
			Unmarshaller unmarshaller = null;
			try {
				jaxbContext = JAXBContext.newInstance(Properties.class);
				unmarshaller = jaxbContext.createUnmarshaller();
				properties = (Properties)unmarshaller.unmarshal(file);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			return properties;
		}
	
		/**
		 * writeXml
		 * write the the xml file 
		 */
		public static void writeXml(Properties p) {
			properties = p;
			properties.setThreadsNumber(10);
			
			File file = null;
			JAXBContext jaxbContext = null;
			Marshaller marshaller = null;
			try {
				file = new File("resources/properties.xml");
				jaxbContext = JAXBContext.newInstance(properties.getClass());
				marshaller = jaxbContext.createMarshaller();
				
				// output printed
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(properties, file);
				//marshaller.marshal(properties, System.out);
				
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			
		}
		
		/**
		 * getProperties
		 * @return Properties
		 */
		public static Properties getProperties() {
			return properties;
		}
		
}
