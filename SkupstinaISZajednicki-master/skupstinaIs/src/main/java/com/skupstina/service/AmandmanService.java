package com.skupstina.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.Amandman;
import com.skupstina.model.Propis;
import com.skupstina.model.Propisi;
import com.skupstina.utils.Rand;

import net.sf.saxon.TransformerFactoryImpl;

import com.skupstina.model.Amandmani;

@Service
public class AmandmanService {

	@Autowired
	Rand random;

	@Autowired
	DatabaseManager databaseManager;
	
	//za xsl-fo
		private FopFactory fopFactory;
		private TransformerFactory transformerFactory;

	public Amandman createAmandmanAddPropis(Propis propis) {

		Amandman noviAmandman = new Amandman();
		noviAmandman.setID(new BigInteger(random.getRandom().toString()));
		noviAmandman.setPropis(propis);

		return noviAmandman;
	}

	public void marshall(Amandman amandman) throws JAXBException, IOException {
		File amandmanFile = new File("src/data/tempAmandman.xml");

		if (!amandmanFile.exists()) {
			try {
				amandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
		Marshaller marshaller = context.createMarshaller();

		// KAKO BI UBACILI STYLESHEET za XSL
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
				"<?xml-stylesheet type='text/xsl' href='amandman.xsl' ?>");

		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		FileOutputStream fos = new FileOutputStream(amandmanFile);

		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller.marshal(amandman, fos);

		fos.flush();
		fos.close();

		// ODMAH CIM DODAMO PROPIS CUVAMO U BAZU, I KASNIJE ISCITAVAMO IZ BAZE
		saveAmandmanSamoSaPropisom();

	}

	public void marshall2(Amandman amandman) throws JAXBException, IOException {

		marshallPosebanAmandman(amandman);
		marshallSveAmandmane(amandman);
		saveModifikovanAmandman(amandman);

	}

	public void marshallPosebanAmandman(Amandman amandman) throws JAXBException, IOException {

		// CUVAMO IZMENU U POJEDINACNOM AMANDMANU
		File amandmanFile = new File("src/data/tempAmandman.xml");

		if (!amandmanFile.exists()) {
			try {
				amandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
		Marshaller marshaller = context.createMarshaller();

		// KAKO BI UBACILI STYLESHEET za XSL
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders",
				"<?xml-stylesheet type='text/xsl' href='amandman.xsl' ?>");

		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		FileOutputStream fos = new FileOutputStream(amandmanFile);

		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller.marshal(amandman, fos);

		fos.flush();
		fos.close();
	}

	public void marshallSveAmandmane(Amandman amandman) throws JAXBException, IOException {
		// CUVAMO IZMENU U FAJLU SVIH AMANDMANA
		File allAmandmanFile = new File("src/data/tempSviAmandmani.xml");
		if (!allAmandmanFile.exists()) {
			try {
				allAmandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JAXBContext context2 = JAXBContext.newInstance(Amandmani.class);
		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		Unmarshaller unmarshaller2 = context2.createUnmarshaller();
		// Unmarshalling generiše objektni model na osnovu XML fajla

		FileOutputStream fos2;
		// Amandmani amandmani = new Amandmani();

		Amandmani amandmani = (Amandmani) unmarshaller2.unmarshal(new File("src/data/tempSviAmandmani.xml"));

		amandmani.getAmandman().add(amandman);

		Marshaller marshaller2 = context2.createMarshaller();
		// Podešavanje marshaller-a
		marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		fos2 = new FileOutputStream(allAmandmanFile);
		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller2.marshal(amandmani, fos2);

		fos2.flush();
		fos2.close();
	}

	private void saveAmandmanSamoSaPropisom() throws JAXBException, FileNotFoundException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandman amandmanFajl = (Amandman) unmarshaller.unmarshal(new File("src/data/tempAmandman.xml"));

		String amandman = "/amandman/" + amandmanFajl.getID();
		String collId = "/amandmani";

		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		InputStreamHandle hendle2 = new InputStreamHandle(new FileInputStream("src/data/tempAmandman.xml"));

		xmlManager.write(amandman, metadata, hendle2);
		System.err.println("ZAVRSIO!");
	}

	public void saveModifikovanAmandman(Amandman amandman) throws FileNotFoundException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		String amandmanModifikovan = "/amandman/" + amandman.getID();
		String amandmani = "/amandmani/sviAmandmani.xml";
		
		String collId = "/amandmani";
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempSviAmandmani.xml"));
		InputStreamHandle hendle2 = new InputStreamHandle(new FileInputStream("src/data/tempAmandman.xml"));

		xmlManager.write(amandmani, hendle);
		xmlManager.write(amandmanModifikovan, metadata, hendle2);
		System.err.println("ZAVRSIO SAVE!");

	}
	
	public Amandmani getAmandmani() throws JAXBException {
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandmani = "/amandmani/sviAmandmani.xml";
		
		xmlManager.read(amandmani, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandmani.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandmani amandmaniSvi = (Amandmani) unmarshaller.unmarshal(doc);
		
		return amandmaniSvi;
	}
	
	public Amandman getAmandman(String uri) throws JAXBException {
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandman = uri;
		
		xmlManager.read(amandman, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandman amandmanById = (Amandman) unmarshaller.unmarshal(doc);
		
		return amandmanById;
	}

	public void izbaciAmandmanIzListe(Amandman amandmanById) throws JAXBException, IOException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandmani = "/amandmani/sviAmandmani.xml";
		
		xmlManager.read(amandmani, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandmani.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandmani amandmaniSvi = (Amandmani) unmarshaller.unmarshal(doc);
		
		for (int i = 0; i < amandmaniSvi.getAmandman().size(); i++) {
			if(amandmaniSvi.getAmandman().get(i).getID().equals(amandmanById.getID())){
				
				amandmaniSvi.getAmandman().remove(i);
				break;
			}
		}
		
		saveListuAmandmanaIzmenjenu(amandmaniSvi);
		
	}

	private void saveListuAmandmanaIzmenjenu(Amandmani amandmaniSvi) throws JAXBException, IOException {
		
		//PRVO UPISUJEMO U FAJL IZMENJANU LISTU AMANDMANA
		File allAmandmanFile = new File("src/data/tempSviAmandmani.xml");
		if (!allAmandmanFile.exists()) {
			try {
				allAmandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JAXBContext context2 = JAXBContext.newInstance(Amandmani.class);
		FileOutputStream fos2;

		Marshaller marshaller2 = context2.createMarshaller();
		marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		fos2 = new FileOutputStream(allAmandmanFile);
		marshaller2.marshal(amandmaniSvi, fos2);
		fos2.flush();
		fos2.close();
		
		
		//IZ FAJLA CUVAMO U BAZU
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		String amandmani = "/amandmani/sviAmandmani.xml";
		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempSviAmandmani.xml"));

		xmlManager.write(amandmani, hendle);
		System.err.println("ZAVRSIO SAVE NAKON IZBACIVANJA AMANDMANA!");
		
	}
	
	

	public void xslFOTransformer() throws SAXException, IOException {

		fopFactory = FopFactory.newInstance(new File("src/main/java/fop.xconf"));
		transformerFactory = new TransformerFactoryImpl();
	}

	public void kreirajPDFFileAmandman(String idAmandmana) throws JAXBException, IOException, FOPException, TransformerException {

		//u fajl upisemo bas koji nam treba propis
				XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
				DOMHandle content = new DOMHandle();
				String amandman = "/amandman/" + idAmandmana;
				xmlManager.read(amandman, content);
				Document doc = content.get();
				JAXBContext context = JAXBContext.newInstance(Amandman.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				Amandman amandmanById = (Amandman) unmarshaller.unmarshal(doc);
				marshalldrugiSetProperty(amandmanById);
				
				
				// Point to the XSL-FO file
				File xsltFile = new File("src/data/xsl-fo/amandman_fo.xsl");
				// Create transformation source
				StreamSource transformSource = new StreamSource(xsltFile);
				// Initialize the transformation subject
				StreamSource source = new StreamSource(new File("src/data/tempAmandman.xml"));
				// Initialize user agent needed for the transformation
				FOUserAgent userAgent = fopFactory.newFOUserAgent();
				// Create the output stream to store the results
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				// Initialize the XSL-FO transformer object
				Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
				// Construct FOP instance with desired output format
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);
				// Resulting SAX events 
				Result res = new SAXResult(fop.getDefaultHandler());
				// Start XSLT transformation and FOP processing
				xslFoTransformer.transform(source, res);

				// Generate PDF file
				String nazivFile = "src/data/gen/amandman_" + amandmanById.getRedniBroj()+"_" + amandmanById.getID() + ".pdf";
				File pdfFile = new File(nazivFile);
				OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
				out.write(outStream.toByteArray());

				System.out.println("[INFO] File \"" + pdfFile.getCanonicalPath() + "\" generated successfully.");
				out.close();
				System.out.println("[INFO] End.");
		
	}

	private void marshalldrugiSetProperty(Amandman amandmanById) throws JAXBException, IOException {
		File amandmanFile = new File("src/data/tempAmandman.xml");

		if (!amandmanFile.exists()) {
			try {
				amandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
		Marshaller marshaller = context.createMarshaller();

		// KAKO BI UBACILI STYLESHEET za XSL
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='amandman_fo.xsl'?>");

		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		FileOutputStream fos = new FileOutputStream(amandmanFile);

		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller.marshal(amandmanById, fos);

		fos.flush();
		fos.close();
		
	}

}
