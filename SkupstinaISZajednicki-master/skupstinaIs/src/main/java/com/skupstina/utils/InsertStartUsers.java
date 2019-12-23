package com.skupstina.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.beans.factory.annotation.Autowired;

import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;
import com.skupstina.db.DatabaseManager;
import com.skupstina.model.users.Korisnici;
import com.skupstina.model.users.Korisnici.Korisnik;

public class InsertStartUsers {

	@Autowired
	DatabaseManager database;
	
	@Autowired
	Rand random;
	
	public Korisnik createOdbornik() {
		Korisnik odbornik = new Korisnik();
		odbornik.setIme("odbornik");
		odbornik.setPassword("odbornik");
		odbornik.setPrezime("odbornik");
		odbornik.setUsername("odbornik");
		odbornik.setVrsta(UserRole.ODBORNIK.toString());
		odbornik.setID(new BigInteger("1"));
		
		return odbornik;
	}
	
	public Korisnik createGradjanin() {
		Korisnik gradjanin = new Korisnik();
		gradjanin.setIme("gradjanin");
		gradjanin.setPassword("gradjanin");
		gradjanin.setPrezime("gradjanin");
		gradjanin.setUsername("gradjanin");
		gradjanin.setVrsta(UserRole.GRADJANIN.toString());
		gradjanin.setID(new BigInteger("2"));
		return gradjanin;
	}
	
	public Korisnik createPredsednik() {
		Korisnik predsednik = new Korisnik();
		predsednik.setIme("predsednik");
		predsednik.setPassword("predsednik");
		predsednik.setPrezime("predsednik");
		predsednik.setUsername("predsednik");
		predsednik.setVrsta(UserRole.PREDSEDNIK.toString());
		predsednik.setID(new BigInteger("3"));
		
		return predsednik;
	}
	
	@PostConstruct
	public void insert () throws JAXBException, IOException {
		Korisnici korisnici = new Korisnici();
		korisnici.getKorisnik().add(createGradjanin());
		korisnici.getKorisnik().add(createOdbornik());
		korisnici.getKorisnik().add(createPredsednik());
		
		File korisniciFile = new File("src/data/tempKorisnici.xml");

		if (!korisniciFile.exists()) {
			try {
				korisniciFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		FileOutputStream fos = new FileOutputStream(korisniciFile);
		
		
		JAXBContext context = JAXBContext.newInstance(Korisnici.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		
		marshaller.marshal(korisnici, fos);
		
		String korisniciDoc = "/korisnici.xml";
		
		InputStreamHandle hendle = new InputStreamHandle(new FileInputStream("src/data/tempKorisnici.xml"));
		XMLDocumentManager xmlManager = database.getClient().newXMLDocumentManager();
		xmlManager.write(korisniciDoc, hendle);
		
		database.getClient().release();
	}
}
