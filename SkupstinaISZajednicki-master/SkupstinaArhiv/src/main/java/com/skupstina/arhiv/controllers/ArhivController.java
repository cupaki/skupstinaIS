package com.skupstina.arhiv.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.DocumentMetadataHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.skupstina.arhiv.db.DatabaseManager;

@RestController
@RequestMapping("/api/arhiviranje")
public class ArhivController {
	
	@Autowired
	private DatabaseManager databaseManager;
	
	@CrossOrigin(origins = "http://localhost:8000", maxAge=3600)
	@RequestMapping(value="/", method=RequestMethod.POST, consumes=MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody void insertIntoArchiv(@RequestBody String xml) throws IOException {

		
		System.out.println(xml);
		File file = new File("/src/data/propis.xml");
		FileUtils.writeStringToFile(file, xml);
		
		XMLDocumentManager xmlManager = databaseManager.getClient().newXMLDocumentManager();
		DOMHandle content = new DOMHandle();
		DocumentMetadataHandle metadata = new DocumentMetadataHandle();
		
		String collId = "/arhiva/propisi";
		
		
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder; 
	    Document document;
	    Element element = null;
	    try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse( new InputSource( new StringReader( xml ) ) ); 
	        element = document.getDocumentElement();
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    } 
		
	    String id = element.getAttribute("ID");
	    String propis = "/propis/" + id;
		metadata.getCollections().add(collId);
		
		InputStream in = IOUtils.toInputStream(xml, "UTF-8");
		InputStreamHandle hendle = new InputStreamHandle(in);
		xmlManager.write(propis,metadata, hendle);

		
		

	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	public String test() {
		return "Test";
	}

}
