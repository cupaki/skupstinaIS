package com.skupstina.contoller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

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
import com.skupstina.model.Amandmani;
import com.skupstina.model.Propis;
import com.skupstina.model.Propisi;
import com.skupstina.service.AmandmanService;
import com.skupstina.utils.Marshalling;
import com.skupstina.utils.Unmarshalling;

@RestController
@RequestMapping("/api/amandman")
public class AmandmanController {

	@Autowired
	Unmarshalling unmarshaler;
	@Autowired
	DatabaseManager database;
	@Autowired
	Marshalling marshaller;
	@Autowired
	AmandmanService amandmanService;
	
	@RequestMapping(value = "/novi", method = RequestMethod.POST)
	public @ResponseBody Amandman insert(@RequestBody Propis propis) throws JAXBException, IOException {

		Amandman amandman = null;
		amandman = amandmanService.createAmandmanAddPropis(propis);
		amandmanService.marshall(amandman);
		return amandman;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public @ResponseBody Amandman getAmandmanById(@PathVariable("id") String id) throws JAXBException, IOException, TransformerFactoryConfigurationError, TransformerException {
		//MENJAO TIP SA Propis u responseEntity
		
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandman = "/amandman/" + id;
		
		xmlManager.read(amandman, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandman.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandman amandmanById = (Amandman) unmarshaller.unmarshal(doc);
		
		
//		String htmlResult = propisService.generateHtmlFromXsl(doc, new File("src/data/propis.xsl"));
//		System.err.println(htmlResult);
//		return new ResponseEntity<String>(htmlResult, HttpStatus.OK);
		return amandmanById;

	}
	
	@RequestMapping(value = "/modifikovanAmandman", method = RequestMethod.POST)
	public @ResponseBody Amandman modifyAmandman(@RequestBody Amandman amandman) throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		String amandmanZaBazu = "/amandman/" + amandman.getID();
		//obrisao iz baze
		xmlManager.delete(amandmanZaBazu);
		
		//cuvanje u fajl, i ponovno cuvanje u bazu
		amandmanService.marshall2(amandman);
		
		return amandman;
	}
	
	@RequestMapping(value = "/brojAmandmana", method = RequestMethod.GET)
	public @ResponseBody Integer getNumbersOfAmandmans() throws JAXBException, IOException, TransformerFactoryConfigurationError, TransformerException {
		//MENJAO TIP SA Propis u responseEntity
		
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandmani = "/amandmani/sviAmandmani.xml";
		
		xmlManager.read(amandmani, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandmani.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandmani amandmaniSvi = (Amandmani) unmarshaller.unmarshal(doc);
		
		
		return amandmaniSvi.getAmandman().get(amandmaniSvi.getAmandman().size() -1).getRedniBroj() + 1;

	}
	
	@RequestMapping(value = "/amandmani", method = RequestMethod.GET)
	public @ResponseBody Amandmani getAmandmani() throws JAXBException, IOException {

		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		String amandmani = "/amandmani/sviAmandmani.xml";
		
		xmlManager.read(amandmani, content);
		Document doc = content.get();
		
		JAXBContext context = JAXBContext.newInstance(Amandmani.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		Amandmani amandmaniSvi = (Amandmani) unmarshaller.unmarshal(doc);
		
		return amandmaniSvi;
	}
	
	@RequestMapping(value = "/kreirajPDFFileAmandman/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> kreirajPDFFilePropis(@PathVariable("id") String idAmandmana) throws JAXBException, IOException, SAXException, TransformerException {

		amandmanService.xslFOTransformer();
		amandmanService.kreirajPDFFileAmandman(idAmandmana);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
