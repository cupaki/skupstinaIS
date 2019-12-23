package com.skupstina.service;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.Clan;
import com.skupstina.model.Clan.Sadrzaj;
import com.skupstina.model.Clan.Sadrzaj.Stav;
import com.skupstina.model.Propis;
import com.skupstina.model.Propis.Deo;
import com.skupstina.model.Propis.Deo.Glava;
import com.skupstina.model.Propisi;
import com.skupstina.utils.Rand;

import net.sf.saxon.TransformerFactoryImpl;

@Service
public class PropisService {

	@Autowired
	Rand random;

	@Autowired
	DatabaseManager databaseManager;

	private static TransformerFactory transformerFactory;
	
	//za xsl-fo
	private FopFactory fopFactory;
//	private TransformerFactory transformerFactory;

	static {
		transformerFactory = TransformerFactory.newInstance();
	}

	public Propis createPropis(String json) {
		JSONObject jsonObj = new JSONObject(json);

		String nazivPropisa = jsonObj.getString("naziv");

		JSONObject deo = jsonObj.getJSONObject("deo");
		String nazivDeo = deo.getString("naziv");
		JSONObject glava = deo.getJSONObject("glava");
		String nazivGlava = glava.getString("naziv");
		JSONObject clan = glava.getJSONObject("clan");
		String nazivClan = clan.getString("naziv");
		String opisClan = clan.getString("opis");
		JSONObject sadrzaj = clan.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
/*		JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

		Propis noviPropis = new Propis();
		noviPropis.setNaziv(nazivPropisa);
		noviPropis.setID(new BigInteger(random.getRandom().toString()));
		Deo noviDeo = new Deo();
		noviDeo.setNaziv(nazivDeo);
		noviDeo.setID(new BigInteger(random.getRandom().toString()));

//		Stav noviStav = new Stav();
//		noviStav.setTekst(tesktStav);
//		noviStav.setRedniBroj(1);
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));

		Glava novaGlava = new Glava();
		novaGlava.setID(new BigInteger(random.getRandom().toString()));
		novaGlava.setNaziv(nazivGlava);
		novaGlava.getClan().add(noviClan);

		noviDeo.getGlava().add(novaGlava);
		// noviDeo.getClan().add(noviClan);
		noviPropis.getDeo().add(noviDeo);

		// dodajemo status (ili 'u proceduri', ili 'usvojen');
		noviPropis.setStatus("u proceduri");

		return noviPropis;

	}

	public Propis createPropisBezGlave(String json) {
		JSONObject jsonObj = new JSONObject(json);

		String nazivPropisa = jsonObj.getString("naziv");

		JSONObject deo = jsonObj.getJSONObject("deo");
		String nazivDeo = deo.getString("naziv");
		JSONObject clan = deo.getJSONObject("clan");
		String nazivClan = clan.getString("naziv");
		String opisClan = clan.getString("opis");
		JSONObject sadrzaj = clan.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
/*		JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

		Propis noviPropis = new Propis();
		noviPropis.setNaziv(nazivPropisa);
		noviPropis.setID(new BigInteger(random.getRandom().toString()));
		Deo noviDeo = new Deo();
		noviDeo.setNaziv(nazivDeo);
		noviDeo.setID(new BigInteger(random.getRandom().toString()));

		Stav noviStav = new Stav();
//		noviStav.setTekst(tesktStav);
//		noviStav.setRedniBroj(1);
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));

		/*
		 * Glava novaGlava = new Glava(); novaGlava.setID(new
		 * BigInteger(random.getRandom().toString()));
		 * novaGlava.setNaziv(nazivGlava); novaGlava.getClan().add(noviClan);
		 */

		// noviDeo.getGlava().add(novaGlava);
		noviDeo.getClan().add(noviClan);
		noviPropis.getDeo().add(noviDeo);

		// dodajemo status (ili 'u proceduri', ili 'usvojen');
		noviPropis.setStatus("u proceduri");

		return noviPropis;
	}

	public void marshall(Propis propis) throws JAXBException, IOException {
		File propisFile = new File("src/data/tempPropis.xml");

		if (!propisFile.exists()) {
			try {
				propisFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance(Propis.class);
		// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
		Marshaller marshaller = context.createMarshaller();

		// KAKO BI UBACILI STYLESHEET za XSL
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='propis.xsl' ?>");
	//	marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='propis_fo.xsl'?>");
		//	marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='propis.xsl' ?>");

		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		FileOutputStream fos = new FileOutputStream(propisFile);

		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller.marshal(propis, fos);

		fos.flush();
		fos.close();
	}

	public void marshallAllPropis(Propis propis) throws JAXBException, IOException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisiID = "/propisi/sviPropisi.xml";
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();

		File allPropisFile = new File("src/data/tempSviPropisi.xml");
		File propisiFromDb = new File("src/data/tempPropisiFromDB.xml");

		if (!allPropisFile.exists()) {
			try {
				allPropisFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileOutputStream fos;
		FileOutputStream fos2;

		fos = new FileOutputStream(allPropisFile);
		fos2 = new FileOutputStream(propisiFromDb);

		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		Unmarshaller unmarshaller = context.createUnmarshaller();

		Marshaller marshaller = context.createMarshaller();
		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		if (xmlManager.exists(propisiID) != null) {
			xmlManager.read(propisiID, metadata, content);
			Document doc = content.get();
			transform(doc, fos2);
		}

		;
		// Propisi propisi = new Propisi();
		Propisi propisi = null;
		try {
			propisi = (Propisi) unmarshaller.unmarshal(propisiFromDb);
		} catch (JAXBException e) {
			propisi = new Propisi();
			propisi.getPropis().add(propis);
			marshaller.marshal(propisi, fos);
			savePropisi();
			fos.flush();
			fos.close();
			return;
		}

		propisi.getPropis().add(propis);

		marshaller.marshal(propisi, fos);

		propisiFromDb.delete();
		fos.flush();
		fos.close();
		fos2.flush();
		fos2.close();

		savePropisi();

	}

	public void savePropisi() throws FileNotFoundException, JAXBException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		JAXBContext context = JAXBContext.newInstance(Propis.class);
		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propisFajl = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		String propis = "/propis/" + propisFajl.getID();
		String propisi = "/propisi/sviPropisi.xml";
		String collId = "/propisi";
		
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		metadata.getCollections().add(collId);

		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempSviPropisi.xml"));
		InputStreamHandle hendle2 = new InputStreamHandle(new FileInputStream("src/data/tempPropis.xml"));

		xmlManager.write(propisi, hendle);
		xmlManager.write(propis,metadata, hendle2);
		System.err.println("ZAVRSIO!");
	}

	public Propis addGlava(Propis propis, String jsonGlava) {

		JSONObject jsonObj = new JSONObject(jsonGlava);

		String nazivGlava = jsonObj.getString("naziv");

		JSONObject clan = jsonObj.getJSONObject("clan");
		String nazivClan = clan.getString("naziv");
		String opisClan = clan.getString("opis");
		JSONObject sadrzaj = clan.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
	/*	JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

/*		Stav noviStav = new Stav();
		noviStav.setTekst(tesktStav);
		noviStav.setRedniBroj(1);*/
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));

		Glava novaGlava = new Glava();
		novaGlava.setID(new BigInteger(random.getRandom().toString()));
		novaGlava.setNaziv(nazivGlava);
		novaGlava.getClan().add(noviClan);

		((Deo) propis.getDeo().get(propis.getDeo().size() - 1)).getGlava().add(novaGlava);

		return propis;
	}

	public Propis addDeo(Propis propis, String jsonDeo) {

		JSONObject jsonObj = new JSONObject(jsonDeo);

		String nazivDeo = jsonObj.getString("naziv");
		JSONObject glava = jsonObj.getJSONObject("glava");
		String nazivGlava = glava.getString("naziv");
		JSONObject clan = glava.getJSONObject("clan");
		String nazivClan = clan.getString("naziv");
		String opisClan = clan.getString("opis");
		JSONObject sadrzaj = clan.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
	/*	JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

		Deo noviDeo = new Deo();
		noviDeo.setNaziv(nazivDeo);
		noviDeo.setID(new BigInteger(random.getRandom().toString()));

		Stav noviStav = new Stav();
//		noviStav.setTekst(tesktStav);
//		noviStav.setRedniBroj(1);
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));

		Glava novaGlava = new Glava();
		novaGlava.setID(new BigInteger(random.getRandom().toString()));
		novaGlava.setNaziv(nazivGlava);
		novaGlava.getClan().add(noviClan);

		noviDeo.getGlava().add(novaGlava);

		// ((Deo) propis.getDeo().get(propis.getDeo().size() -
		// 1)).getGlava().add(novaGlava);
		propis.getDeo().add(noviDeo);
		return propis;

	}
	
	public Propis addDeoBezGlave(Propis propis, String jsonDeo) {

		JSONObject jsonObj = new JSONObject(jsonDeo);

		String nazivDeo = jsonObj.getString("naziv");
		
		JSONObject clan = jsonObj.getJSONObject("clan");
		String nazivClan = clan.getString("naziv");
		String opisClan = clan.getString("opis");
		JSONObject sadrzaj = clan.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
	/*	JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

		Deo noviDeo = new Deo();
		noviDeo.setNaziv(nazivDeo);
		noviDeo.setID(new BigInteger(random.getRandom().toString()));

		Stav noviStav = new Stav();
//		noviStav.setTekst(tesktStav);
//		noviStav.setRedniBroj(1);
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));

	/*	Glava novaGlava = new Glava();
		novaGlava.setID(new BigInteger(random.getRandom().toString()));
		novaGlava.setNaziv(nazivGlava);
		novaGlava.getClan().add(noviClan);*/

//		noviDeo.getGlava().add(novaGlava);
		noviDeo.getClan().add(noviClan);

		// ((Deo) propis.getDeo().get(propis.getDeo().size() -
		// 1)).getGlava().add(novaGlava);
		propis.getDeo().add(noviDeo);
		return propis;

	}

	public Propis addClan(Propis propis, String jsonClan) {

		JSONObject jsonObj = new JSONObject(jsonClan);

		String nazivClan = jsonObj.getString("naziv");
		String opisClan = jsonObj.getString("opis");
		JSONObject sadrzaj = jsonObj.getJSONObject("sadrzaj");

		String tekst = sadrzaj.getString("tekst");
	/*	JSONObject stav = sadrzaj.getJSONObject("stav");

		String tesktStav = stav.getString("tekst");*/

/*		Stav noviStav = new Stav();
		noviStav.setTekst(tesktStav);
		noviStav.setRedniBroj(1);*/
		Sadrzaj noviSadrzaj = new Sadrzaj();
//		noviSadrzaj.getStav().add(noviStav);
		noviSadrzaj.getTekst().add(tekst);

		Clan noviClan = new Clan();
		noviClan.setNaziv(nazivClan);
		noviClan.setSadrzaj(noviSadrzaj);
		noviClan.setOpis(opisClan);
		noviClan.setID(new BigInteger(random.getRandom().toString()));


		Deo d = propis.getDeo().get(propis.getDeo().size() - 1);

		if (propis.getDeo().get(propis.getDeo().size() - 1).getGlava().isEmpty()) {
			propis.getDeo().get(propis.getDeo().size() - 1).getClan().add(noviClan);
		} else {
			propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1)).getClan()
					.add(noviClan);
		}
		return propis;
	}

	public Propis addStav(Propis propis, String jsonClan) {

		JSONObject jsonObj = new JSONObject(jsonClan);

		String tesktStav = jsonObj.getString("tekst");
		Long brojStava = jsonObj.getLong("redniBroj");

		Stav noviStav = new Stav();
		noviStav.setTekst(tesktStav);
		noviStav.setRedniBroj(brojStava);

		Deo d = propis.getDeo().get(propis.getDeo().size() - 1);

		if (propis.getDeo().get(propis.getDeo().size() - 1).getGlava().isEmpty()) {
			propis.getDeo().get(propis.getDeo().size() - 1).getClan().get(d.getClan().size() - 1).getSadrzaj().getStav()
					.add(noviStav);

		} else {
			Glava g = propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1));
			propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1)).getClan()
					.get(g.getClan().size() - 1).getSadrzaj().getStav().add(noviStav);
		}

		return propis;
	}

	public String generateHtmlFromXsl(Document doc, File fileForXsl)
			throws TransformerFactoryConfigurationError, TransformerException {

		// ovde se generise html kao string, koji se u stvari sastoji od
		// podataka iz xml-a spojenog sa xsl-om.
		StreamSource streamSource = new StreamSource(fileForXsl);
		Transformer transformer = TransformerFactory.newInstance().newTransformer(streamSource);
		StringWriter writer = new StringWriter();
		transformer.transform(new DOMSource(doc), new StreamResult(writer));

		return writer.getBuffer().toString();
	}

	public Document loadDocument(String file) {
		// TODO Auto-generated method stub
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document document = db.parse(new File(file));
			return document;
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (FactoryConfigurationError e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	public void izvrsiIzmenuPropisa(Propis propisById) throws JAXBException, IOException {

		// prvo upisemo u tempPropis izmenjen propis
		marshall(propisById);

	}

	public void izvrsiIzmenuUListiPropisa(Propisi propisiSvi) throws JAXBException, IOException {

		// upisujemo izmenu u propisu koji se nalazi u ListiPropisa
		marshallListuPropisa(propisiSvi);

	}

	private void marshallListuPropisa(Propisi propisiSvi) throws JAXBException, IOException {
		File allAmandmanFile = new File("src/data/tempSviPropisi.xml");
		if (!allAmandmanFile.exists()) {
			try {
				allAmandmanFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JAXBContext context2 = JAXBContext.newInstance(Propisi.class);
		FileOutputStream fos2;

		Marshaller marshaller2 = context2.createMarshaller();
		marshaller2.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		fos2 = new FileOutputStream(allAmandmanFile);
		marshaller2.marshal(propisiSvi, fos2);
		fos2.flush();
		fos2.close();
	}

	public void savePropisiBezPojedinacnog() throws JAXBException, FileNotFoundException {

		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		String propisi = "/propisi/sviPropisi.xml";
		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempSviPropisi.xml"));

		xmlManager.write(propisi, hendle);
		System.err.println("ZAVRSIO!");

	}

	private static void transform(Node node, OutputStream out) {
		try {

			// Kreiranje instance objekta zaduzenog za serijalizaciju DOM modela
			Transformer transformer = transformerFactory.newTransformer();

			// Indentacija serijalizovanog izlaza
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Nad "source" objektom (DOM stablo) vrši se transformacija
			DOMSource source = new DOMSource(node);

			// Rezultujući stream (argument metode)
			StreamResult result = new StreamResult(out);

			// Poziv metode koja vrši opisanu transformaciju
			transformer.transform(source, result);

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	public Propisi getPropisi() throws JAXBException {
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisi = "/propisi/sviPropisi.xml";
		
		xmlManager.read(propisi, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller.unmarshal(doc);
		
		return propisiSvi;
	}
	

	public void xslFOTransformer() throws SAXException, IOException {
		
		fopFactory = FopFactory.newInstance(new File("src/main/java/fop.xconf"));
		transformerFactory = new TransformerFactoryImpl();
	}

	public void kreirajPDFFilePropis(String idPropisa) throws IOException, JAXBException, FOPException, TransformerException {
		
		//u fajl upisemo bas koji nam treba propis
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propis = "/propis/" + idPropisa;
		xmlManager.read(propis, content);
		Document doc = content.get();
		JAXBContext context = JAXBContext.newInstance(Propis.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propisById = (Propis) unmarshaller.unmarshal(doc);
		marshall3drugiSetProperty(propisById);
		
		
		// Point to the XSL-FO file
		File xsltFile = new File("src/data/xsl-fo/propis_fo.xsl");
		// Create transformation source
		StreamSource transformSource = new StreamSource(xsltFile);
		// Initialize the transformation subject
		StreamSource source = new StreamSource(new File("src/data/tempPropis.xml"));
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
		String nazivFile = "src/data/gen/propis_" + propisById.getNaziv() + ".pdf";
		File pdfFile = new File(nazivFile);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
		out.write(outStream.toByteArray());

		System.out.println("[INFO] File \"" + pdfFile.getCanonicalPath() + "\" generated successfully.");
		out.close();
		System.out.println("[INFO] End.");
		
		
	}

	private void marshall3drugiSetProperty(Propis propisById) throws JAXBException, IOException {
		File propisFile = new File("src/data/tempPropis.xml");

		if (!propisFile.exists()) {
			try {
				propisFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
		JAXBContext context = JAXBContext.newInstance(Propis.class);
		// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
		Marshaller marshaller = context.createMarshaller();

		// KAKO BI UBACILI STYLESHEET za XSL
		marshaller.setProperty("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='propis_fo.xsl'?>");

		// Podešavanje marshaller-a
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		FileOutputStream fos = new FileOutputStream(propisFile);

		// Umesto System.out-a, može se koristiti FileOutputStream
		marshaller.marshal(propisById, fos);

		fos.flush();
		fos.close();
		
	}

}
