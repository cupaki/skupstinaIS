package com.skupstina.contoller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

import org.apache.fop.apps.FopFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.Amandman;
import com.skupstina.model.Clan.Sadrzaj;
import com.skupstina.model.Propis;
import com.skupstina.model.Propis.Deo;
import com.skupstina.model.Propis.Deo.Glava;
import com.skupstina.model.Propisi;
import com.skupstina.service.AmandmanService;
import com.skupstina.service.PropisService;
import com.skupstina.utils.Marshalling;
import com.skupstina.utils.Unmarshalling;

@RestController
@RequestMapping(value = "/api/propis")
public class PropisContoller {

	@Autowired
	Unmarshalling unmarshaler;
	@Autowired
	DatabaseManager database;

	@Autowired
	PropisService propisService;

	@Autowired
	Marshalling marshaller;

	@Autowired
	AmandmanService amandmanService;
	
	

	@RequestMapping(value = "/novi", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insert(@RequestBody String json) throws JAXBException, IOException {

		Propis propis = null;

		propis = propisService.createPropis(json);

		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/noviBezGlave", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertBezGlave(@RequestBody String json)
			throws JAXBException, IOException {

		Propis propis = null;

		propis = propisService.createPropisBezGlave(json);

		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/novi/dodajGlavu", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertGlavu(@RequestBody String jsonDeo)
			throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propis = propisService.addGlava(propis, jsonDeo);
		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/novi/dodajDeo", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertDeo(@RequestBody String jsonDeo)
			throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propis = propisService.addDeo(propis, jsonDeo);
		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/novi/dodajDeoBezGlave", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertDeoBezGlave(@RequestBody String jsonDeo)
			throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propis = propisService.addDeoBezGlave(propis, jsonDeo);
		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/novi/dodajClan", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertClan(@RequestBody String jsonClan)
			throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propis = propisService.addClan(propis, jsonClan);
		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/novi/dodajStav", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> insertStav(@RequestBody String jsonClan)
			throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propis = propisService.addStav(propis, jsonClan);
		propisService.marshall(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/novi/prikaziPropis", method = RequestMethod.GET)
	public @ResponseBody Propis getPropis() throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		return propis;
	}

	@RequestMapping(value = "/novi/save", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> savePropis() throws JAXBException, IOException {

		JAXBContext context = JAXBContext.newInstance(Propis.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		propisService.marshallAllPropis(propis);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/propisi", method = RequestMethod.GET)
	public @ResponseBody Propisi getPropisi() throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisi = "/propisi/sviPropisi.xml";

		xmlManager.read(propisi, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller.unmarshal(doc);

		return propisiSvi;
	}

	@RequestMapping(value = "/propisiProcedura", method = RequestMethod.GET)
	public @ResponseBody Propisi getPropisiUProceduri() throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisi = "/propisi/sviPropisi.xml";

		xmlManager.read(propisi, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller.unmarshal(doc);

		Propisi propisiUProceduri = new Propisi();

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getStatus() != null) {
				if (propisiSvi.getPropis().get(i).getStatus().equals("u proceduri")) {
					propisiUProceduri.getPropis().add(propisiSvi.getPropis().get(i));
				}
			}
		}

		return propisiUProceduri;
	}

	@RequestMapping(value = "/propisiUsvojeni", method = RequestMethod.GET)
	public @ResponseBody Propisi getPropisiUsvojeni() throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisi = "/propisi/sviPropisi.xml";

		xmlManager.read(propisi, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller.unmarshal(doc);

		Propisi propisiUsvojeni = new Propisi();

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getStatus() != null) {
				if (propisiSvi.getPropis().get(i).getStatus().equals("usvojen u celini")) {
					System.err.println("USAO JEDNOM");
					propisiUsvojeni.getPropis().add(propisiSvi.getPropis().get(i));
				}
			}
		}

		return propisiUsvojeni;
	}

	@RequestMapping(value = "/propisiUsvojeniUNacelu", method = RequestMethod.GET)
	public @ResponseBody Propisi getPropisiUsvojeniUNacelu() throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propisi = "/propisi/sviPropisi.xml";

		xmlManager.read(propisi, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller.unmarshal(doc);

		Propisi propisiUsvojeniUNacelu = new Propisi();

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getStatus() != null) {
				if (propisiSvi.getPropis().get(i).getStatus().equals("usvojen u nacelu")) {
					propisiUsvojeniUNacelu.getPropis().add(propisiSvi.getPropis().get(i));
				}
			}
		}

		return propisiUsvojeniUNacelu;
	}

	@RequestMapping(value = "/propisUsvoj/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> usvojPropis(@PathVariable("id") String id)
			throws JAXBException, IOException {

		// IZMENA SAMOG PROPISA
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propis = "/propis/" + id;

		xmlManager.read(propis, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propis.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propisById = (Propis) unmarshaller.unmarshal(doc);

		propisById.setStatus("usvojen u celini");

		// IZMENA FAJLA SVIH PROPISA
		String propisi = "/propisi/sviPropisi.xml";
		xmlManager.read(propisi, content);
		Document doc2 = content.get();
		JAXBContext context2 = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller2 = context2.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller2.unmarshal(doc2);

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getID().equals(propisById.getID())) {
				propisiSvi.getPropis().get(i).setStatus("usvojen u celini");
				break;
			}

		}

		// poziv menjanja fajlova i cuvanja u bazu
		propisService.izvrsiIzmenuPropisa(propisById);
		propisService.izvrsiIzmenuUListiPropisa(propisiSvi);
		propisService.savePropisi();

		String strURL = "http://localhost:8080/api/arhiviranje/";
		String strXMLFilename = "src/data/tempPropis.xml";
		File input = new File(strXMLFilename);

		// Prepare HTTP post
		PostMethod post = new PostMethod(strURL);

		post.setRequestEntity(new InputStreamRequestEntity(new FileInputStream(input), input.length()));

		// Specify content type and encoding
		// If content encoding is not explicitly specified
		// ISO-8859-1 is assumed
		post.setRequestHeader("Content-type", "application/xml");

		// Get HTTP client
		HttpClient httpclient = new HttpClient();

		try {

			int result = httpclient.executeMethod(post);

			// Display status code
			System.out.println("Response status code: " + result);

			// Display response
			System.out.println("Response body: ");
			System.out.println(post.getResponseBodyAsString());

		} finally {
			// Release current connection to the connection pool
			// once you are done
			post.releaseConnection();
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/propisUsvojUNacelu/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> usvojPropisUNacelu(@PathVariable("id") String id)
			throws JAXBException, IOException {

		// IZMENA SAMOG PROPISA
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propis = "/propis/" + id;

		xmlManager.read(propis, content);
		Document doc = content.get();

		JAXBContext context = JAXBContext.newInstance(Propis.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propisById = (Propis) unmarshaller.unmarshal(doc);

		propisById.setStatus("usvojen u nacelu");

		// IZMENA FAJLA SVIH PROPISA
		String propisi = "/propisi/sviPropisi.xml";
		xmlManager.read(propisi, content);
		Document doc2 = content.get();
		JAXBContext context2 = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller2 = context2.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller2.unmarshal(doc2);

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getID().equals(propisById.getID())) {
				propisiSvi.getPropis().get(i).setStatus("usvojen u nacelu");
				break;
			}

		}

		// poziv menjanja fajlova i cuvanja u bazu
		propisService.izvrsiIzmenuPropisa(propisById);
		propisService.izvrsiIzmenuUListiPropisa(propisiSvi);
		propisService.savePropisi();

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/propisUsvojAmandman/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> usvojAmandmanNaPropis(@PathVariable("id") String idAmandmana)
			throws JAXBException, IOException {

		// pronalazenje amandmana
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandman = "/amandman/" + idAmandmana;
		xmlManager.read(amandman, content);
		Document docA = content.get();
		JAXBContext context0 = JAXBContext.newInstance(Amandman.class);
		Unmarshaller unmarshaller0 = context0.createUnmarshaller();
		Amandman amandmanById = (Amandman) unmarshaller0.unmarshal(docA);

		String idPropisa = amandmanById.getPropis().getID().toString();

		// IZMENA SAMOG PROPISA
		String propis = "/propis/" + idPropisa;
		xmlManager.read(propis, content);
		Document doc = content.get();
		JAXBContext context = JAXBContext.newInstance(Propis.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propisById = (Propis) unmarshaller.unmarshal(doc);

		// preuzimanje izmenjenog propisa
		propisById.getDeo().clear();
		propisById.getDeo().addAll(amandmanById.getPropis().getDeo());
		//

		// IZMENA FAJLA SVIH PROPISA
		String propisi = "/propisi/sviPropisi.xml";
		xmlManager.read(propisi, content);
		Document doc2 = content.get();
		JAXBContext context2 = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller2 = context2.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller2.unmarshal(doc2);

		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			if (propisiSvi.getPropis().get(i).getID().equals(propisById.getID())) {

				propisiSvi.getPropis().get(i).getDeo().clear();
				propisiSvi.getPropis().get(i).getDeo().addAll(amandmanById.getPropis().getDeo());

				break;
			}
		}

		// poziv menjanja fajlova i cuvanja u bazu
		propisService.izvrsiIzmenuPropisa(propisById);
		propisService.izvrsiIzmenuUListiPropisa(propisiSvi);
		propisService.savePropisi();

		// brisanje amandmana iz baze, jer nije vise potreban
		xmlManager.delete(amandman);
		amandmanService.izbaciAmandmanIzListe(amandmanById);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/propisOdbijAmandman/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> odbijAmandmanNaPropis(@PathVariable("id") String idAmandmana)
			throws JAXBException, IOException {

		// pronalazenje amandmana
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandman = "/amandman/" + idAmandmana;
		xmlManager.read(amandman, content);
		Document docA = content.get();
		JAXBContext context0 = JAXBContext.newInstance(Amandman.class);
		Unmarshaller unmarshaller0 = context0.createUnmarshaller();
		Amandman amandmanById = (Amandman) unmarshaller0.unmarshal(docA);

		// brisanje amandmana iz baze, jer nije vise potreban
		xmlManager.delete(amandman);
		amandmanService.izbaciAmandmanIzListe(amandmanById);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	
	@RequestMapping(value = "/kreirajPDFFilePropis/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> kreirajPDFFilePropis(@PathVariable("id") String idPropisa) throws JAXBException, IOException, SAXException, TransformerException {

		propisService.xslFOTransformer();
		propisService.kreirajPDFFilePropis(idPropisa);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	

	@RequestMapping(value = "/propisOdbij/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> odbijPropis(@PathVariable("id") String id)
			throws JAXBException, IOException {

		// BRISANJE PROPISA
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propis = "/propis/" + id;

		xmlManager.delete(propis);

		System.out.println(id + " ID! ");

		Integer idPropisa = Integer.parseInt(id);
		BigInteger Bi = BigInteger.valueOf(idPropisa.intValue());

		// IZMENA FAJLA SVIH PROPISA
		String propisi = "/propisi/sviPropisi.xml";
		xmlManager.read(propisi, content);
		Document doc2 = content.get();
		JAXBContext context2 = JAXBContext.newInstance(Propisi.class);
		Unmarshaller unmarshaller2 = context2.createUnmarshaller();
		Propisi propisiSvi = (Propisi) unmarshaller2.unmarshal(doc2);

		System.out.println(propisiSvi.getPropis().size() + " PRE FORA");
		for (int i = 0; i < propisiSvi.getPropis().size(); i++) {
			System.out.println(propisiSvi.getPropis().get(i).getID() + " ostali id" + " == " + Bi);
			if (propisiSvi.getPropis().get(i).getID().equals(Bi)) {
				System.out.println(" USAO U FOR");
				propisiSvi.getPropis().remove(i);
				break;
			}
		}
		System.out.println(propisiSvi.getPropis().size() + " POSLE FORA");

		// poziv menjanja fajlova i cuvanja u bazu
		propisService.izvrsiIzmenuUListiPropisa(propisiSvi);
		propisService.savePropisiBezPojedinacnog();

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public @ResponseBody ResponseEntity<String> getPropisById(@PathVariable("id") String id)
			throws JAXBException, IOException, TransformerFactoryConfigurationError, TransformerException {
		// MENJAO TIP SA Propis u responseEntity

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String propis = "/propis/" + id;

		xmlManager.read(propis, content);
		Document doc = content.get();

		// JAXBContext context = JAXBContext.newInstance(Propis.class);
		// Unmarshaller unmarshaller = context.createUnmarshaller();
		// Propis propisById = (Propis) unmarshaller.unmarshal(doc);

		String htmlResult = propisService.generateHtmlFromXsl(doc, new File("src/data/propis.xsl"));
		System.err.println(htmlResult);
		// return propisById;
		return new ResponseEntity<String>(htmlResult, HttpStatus.OK);
	}

	@RequestMapping(value = "/brojStava", method = RequestMethod.GET)
	public @ResponseBody Integer getBrojStava()
			throws JAXBException, IOException, TransformerFactoryConfigurationError, TransformerException {
		// MENJAO TIP SA Propis u responseEntity

		JAXBContext context = JAXBContext.newInstance("com.skupstina.model");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Propis propis = (Propis) unmarshaller.unmarshal(new File("src/data/tempPropis.xml"));

		Deo d = propis.getDeo().get(propis.getDeo().size() - 1);

		int redniBrojStava = 0;

		if (propis.getDeo().get(propis.getDeo().size() - 1).getGlava().isEmpty()) {
			Sadrzaj s1 = propis.getDeo().get(propis.getDeo().size() - 1).getClan().get(d.getClan().size() - 1)
					.getSadrzaj();
			
				if(propis.getDeo().get(propis.getDeo().size() - 1).getClan().get(d.getClan().size() - 1)
						.getSadrzaj().getStav().size() == 0){
					
					redniBrojStava = 0;
				}
				else{
				redniBrojStava = (int) propis.getDeo().get(propis.getDeo().size() - 1).getClan().get(d.getClan().size() - 1)
						.getSadrzaj().getStav().get(s1.getStav().size() - 1).getRedniBroj();
				}
			
		} else {
			Glava g = propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1));
			Sadrzaj s2 = propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1))
					.getClan().get(g.getClan().size() - 1).getSadrzaj();

				if(propis.getDeo().get(propis.getDeo().size() - 1).getGlava().get((d.getGlava().size() - 1)).getClan().get(g.getClan().size() - 1).getSadrzaj().getStav().size() == 0){
					redniBrojStava = 0;
				}
				else
				{
				redniBrojStava = (int) propis.getDeo().get(propis.getDeo().size() - 1).getGlava()
						.get((d.getGlava().size() - 1)).getClan().get(g.getClan().size() - 1).getSadrzaj().getStav()
						.get(s2.getStav().size() - 1).getRedniBroj();
				}
		}
		return redniBrojStava + 1;

	}

	@RequestMapping(value = "/arhiva", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Propisi getPropisiZaArhiv() throws JAXBException {

		return null;
	}
}
