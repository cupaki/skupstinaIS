package com.skupstina.service;

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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.users.Korisnici;
import com.skupstina.model.users.Korisnici.Korisnik;
import com.skupstina.utils.Rand;

@Service
public class KorisnikService {

	@Autowired
	Rand random;

	@Autowired
	DatabaseManager databaseManager;

	private static TransformerFactory transformerFactory;

	static {
		transformerFactory = TransformerFactory.newInstance();
	}

	/**
	 * Kreira korisnika od prosledjenog json stringa
	 * 
	 * @param json
	 * @return
	 */
	public Korisnik createKorisnik(String json) {
		JSONObject jsonObj = new JSONObject(json);

		String ime = jsonObj.getString("ime");
		String prezime = jsonObj.getString("prezime");
		String username = jsonObj.getString("username");
		String password = jsonObj.getString("password");
		String vrsta = jsonObj.getString("vrsta");

		Korisnik korisnik = new Korisnik();
		korisnik.setIme(ime);
		korisnik.setPrezime(prezime);
		korisnik.setUsername(username);
		korisnik.setPassword(password);
		korisnik.setVrsta(vrsta);
		korisnik.setID(new BigInteger(random.getRandom().toString()));

		return korisnik;
	}

	/**
	 * Cuva proslednjenog korisnika u bazu podataka
	 * 
	 * @param korisnik
	 * @throws JAXBException
	 * @throws IOException
	 */
	public boolean saveToDB(Korisnik korisnik) throws JAXBException, IOException {
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		JAXBContext context = JAXBContext.newInstance(Korisnici.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		// A handle to receive the document's content.
		DOMHandle content = new DOMHandle();

		// A metadata handle for metadata retrieval
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();

		// A document URI identifier.
		String docId = "/korisnici.xml";

		File korisniciFile = new File("src/data/tempKorisnici.xml");
		File toBeSaved = new File("src/data/toBeSaved.xml");
		if (!korisniciFile.exists()) {
			try {
				korisniciFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (!toBeSaved.exists()) {
			try {
				toBeSaved.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		FileOutputStream fos = new FileOutputStream(korisniciFile);
		FileOutputStream toBeSavedStream = new FileOutputStream(toBeSaved);
		
		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/toBeSaved.xml"));
		if (xmlManager.exists(docId) != null) {		//ako ne postoji na bazi
			xmlManager.read(docId, metadata, content);
			// Retrieving a document node form DOM handle.
			Document doc = content.get();
			transform(doc, fos);

			Korisnici korisnici = (Korisnici) unmarshaller.unmarshal(korisniciFile);
			
			if (!userExits(korisnici, korisnik)) {
				korisnici.getKorisnik().add(korisnik);
			} else {
				toBeSaved.delete();
				fos.flush();
				fos.close();
				toBeSavedStream.flush();
				toBeSavedStream.close();
				hendle.close();
				return false;
			}

			
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(korisnici, toBeSavedStream);
			xmlManager.write(docId, hendle);
		} else {
			Korisnici korisnici = null;
			
			try {
				korisnici = (Korisnici) unmarshaller.unmarshal(korisniciFile);
			} catch (JAXBException e) {
				korisnici = new Korisnici();
				
				korisnici.getKorisnik().add(korisnik);

				Marshaller marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				marshaller.marshal(korisnici, fos);
				xmlManager.write(docId, hendle);
				return true;
			}

			
			korisnici.getKorisnik().add(korisnik);

			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(korisnici, fos);
			xmlManager.write(docId, hendle);
			
			
		}
		//databaseManager.getClient().release();
		toBeSaved.delete();
		fos.flush();
		fos.close();
		toBeSavedStream.flush();
		toBeSavedStream.close();
		return true;

	}

	/**
	 * Serializes DOM tree to an arbitrary OutputStream.
	 *
	 * @param node
	 *            a node to be serialized
	 * @param out
	 *            an output stream to write the serialized DOM representation to
	 * 
	 */
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
	
	private boolean userExits(Korisnici korisnici, Korisnik k) {
		for (Korisnik kk : korisnici.getKorisnik()){
			if (kk.getUsername().equals(k.getUsername())) {
				return true;
			} 
		}
		return false;
	
	}
	
	public Korisnik getUserFromDB(String username, String password) throws JAXBException, IOException {
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();

		JAXBContext context = JAXBContext.newInstance(Korisnici.class);

		Unmarshaller unmarshaller = context.createUnmarshaller();

		// A handle to receive the document's content.
		DOMHandle content = new DOMHandle();

		// A metadata handle for metadata retrieval
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();

		// A document URI identifier.
		String docId = "/korisnici.xml";
		
		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempKorisnici.xml"));
		File korisniciFile = new File("src/data/tempKorisnici.xml");
		FileOutputStream fos = new FileOutputStream(korisniciFile);
		
		xmlManager.read(docId, metadata, content);
		// Retrieving a document node form DOM handle.
		Document doc = content.get();
		transform(doc, fos);

		Korisnici korisnici = (Korisnici) unmarshaller.unmarshal(korisniciFile);
		
		for (Korisnik k : korisnici.getKorisnik()) {
			if (k.getUsername().equals(username) && k.getPassword().equals(password)) {
				fos.close();
				korisniciFile.delete();
				hendle.close();
				return k;
			} else {
				
			}
		}
		fos.close();
		korisniciFile.delete();
		hendle.close();
		return null;
	}
}
