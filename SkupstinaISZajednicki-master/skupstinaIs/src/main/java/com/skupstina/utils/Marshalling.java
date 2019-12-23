package com.skupstina.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import org.springframework.stereotype.Component;

import com.skupstina.model.Akt;
import com.skupstina.model.Amandman;
import com.skupstina.model.Clan;
import com.skupstina.model.Clan.Sadrzaj;
import com.skupstina.model.Clan.Sadrzaj.Stav;
import com.skupstina.model.ObjectFactory;
import com.skupstina.model.Propis;
import com.skupstina.model.Propis.Deo;
import com.skupstina.model.Propis.Deo.Glava;

@Component
public class Marshalling {

	public void test() throws Exception {
		try {

			System.out.println("[INFO] Example 2: JAXB unmarshalling/marshalling.\n");

			// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("com.skupstina.model");

			// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
			// model
			Unmarshaller unmarshaller = context.createUnmarshaller();

			Akt akt = (Akt) unmarshaller.unmarshal(new File("src/data/instance1.xml"));

			akt.setAmandman(createAmandman(new BigInteger("350"), "Novi Sad", "Stefan Halapa",
					"Obrazlozenje je da je hitno", null, 150, "Resenje broj jedan"));
			// Marshaller je objekat zadužen za konverziju iz objektnog u XML
			// model
			Marshaller marshaller = context.createMarshaller();

			// Podešavanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Umesto System.out-a, može se koristiti FileOutputStream
			marshaller.marshal(akt, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	

	private Amandman createAmandman(BigInteger id, String mesto, String podnosilac, String obrazlozenje,
			XMLGregorianCalendar datum, Integer redniBroj, String resenje) {
		
		/*
		ObjectFactory factory = new ObjectFactory();
		Amandman amandman //= factory.createAmandman();

		amandman.setID(id);
		amandman.setMesto(mesto);
		amandman.setPodnosilac(podnosilac);

		amandman.setObrazlozenje(obrazlozenje);
		amandman.setDatum(datum);
		amandman.setResenje(resenje);
		amandman.setRedniBroj(redniBroj);

		amandman.setClan(createClan(new BigInteger("150"), "Clan 1", "Clan jedan opis"));
		amandman.setPropis(createPropis(new BigInteger("200"), "Propis 1")); /////////

		return amandman;
		*/
		return null;

	}

	private Clan createClan(BigInteger id, String naziv, String opis) {

		ObjectFactory factory = new ObjectFactory();
		Clan clan = factory.createClan();

		clan.setID(id);
		clan.setNaziv(naziv);
		clan.setOpis(opis);
		clan.setSadrzaj(createClanSadrzaj()); //

		return clan;
	}

	private Propis createPropis(BigInteger id, String naziv) {

		ObjectFactory factory = new ObjectFactory();
		Propis propis = factory.createPropis();
		propis.setID(id);
		propis.setNaziv(naziv);
		propis.getDeo().add(createDeo(new BigInteger("250"), "Deo 1")); /////////////

		return propis;
	}

	private Sadrzaj createClanSadrzaj() {

		ObjectFactory factory = new ObjectFactory();
		Sadrzaj sadrzaj = factory.createClanSadrzaj();

		sadrzaj.getStav().add(createStav(1L, "Stav 1"));
		sadrzaj.getTekst().add("Tekst stava 1");

		return sadrzaj;

	}

	private Deo createDeo(BigInteger id, String naziv) {

		ObjectFactory factory = new ObjectFactory();
		Deo deo = factory.createPropisDeo();
		deo.setID(id);
		deo.setNaziv(naziv);
		deo.getGlava().add(createGlava(new BigInteger("300"), "Glava 1"));
		return deo;

	}

	private Stav createStav(Long redniBroj, String tekst) {
		ObjectFactory factory = new ObjectFactory();
		Stav stav = factory.createClanSadrzajStav();
		stav.setRedniBroj(redniBroj);
		stav.setTekst(tekst);
		return stav;

	}

	private Glava createGlava(BigInteger id, String naziv) {
		ObjectFactory factory = new ObjectFactory();
		Glava glava = factory.createPropisDeoGlava();
		glava.setID(id);
		glava.setNaziv(naziv);

		return glava;
	}
}
