package com.skupstina.utils;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.stereotype.Component;

import com.skupstina.model.Akt;
import com.skupstina.model.Amandman;
import com.skupstina.model.Clan;
import com.skupstina.model.Clan.Sadrzaj;
import com.skupstina.model.Clan.Sadrzaj.Stav;
import com.skupstina.model.Propis;
import com.skupstina.model.Propis.Deo;

@Component
public class Unmarshalling {
	
	
	public void run() {
		try {

			System.out.println("[INFO] Example 1: JAXB unmarshalling.\n");

			// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("com.skupstina.model");

			// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
			// model
			Unmarshaller unmarshaller = context.createUnmarshaller();

			// Unmarshalling generiše objektni model na osnovu XML fajla
			Akt akt = (Akt) unmarshaller.unmarshal(new File("src/data/instance1.xml"));

			// Prikazuje unmarshallovan objekat
			getAkt(akt);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public void getAkt(Akt akt) {

		// Prikaz naziva fakulteta (getter metoda)
		if(akt.getAmandman()!=null) {
			getAmandman(akt.getAmandman());
		} else if (akt.getPropis() != null) {
			getPropis(akt.getPropis());
		}

	}
	
	public void getAmandman(Amandman amandman) {
		System.out.println("----------------------AMANDMANT-----------------------");
		System.out.println("Amandman atributi");
		System.out.println("Mesto " + amandman.getMesto());
		System.out.println("Obrazlozenje " + amandman.getObrazlozenje());
		System.out.println("Podnosilac " + amandman.getPodnosilac());
		System.out.println("resenje " + amandman.getResenje());
		System.out.println("Redni br:" + amandman.getRedniBroj());
		
		getClan(amandman.getClan());
		getPropis(amandman.getPropis());
		System.out.println("------------------------END AMANDMANT-----------------");
	}
	
	public void getClan(Clan clan) {
		System.out.println("-------------------------CLAN---------------------------");
		System.out.println("Clan amandmana je");
		System.out.println(clan.getNaziv());
		System.out.println(clan.getOpis());
		System.out.println(clan.getID());
		
		getSadrzaj(clan.getSadrzaj());
		System.out.println("-------------------------END CLAN-------------------------");
	}
	
	public void getSadrzaj(Sadrzaj sadrzaj) {
		System.out.println("------------------------------SADRZAJ--------------------------");
		System.out.println("Sadrzaj clana je ");
		System.out.println(sadrzaj.getTekst());
		
		getStav(sadrzaj.getStav());
		
		System.out.println("------------------------------END SADRZAJ-----------------------");
	}
	
	public void getStav(List<Stav> stavovi) {
		System.out.println("-----------------------STAVOVI----------------------------------");
		for (Stav st : stavovi){
			System.out.println("Stav: ");
			System.out.println(st.getRedniBroj());
			System.out.println(st.getTekst());
		}
		System.out.println("-----------------------END STAVOVI--------------------------------");
	}
	
	public void getPropis(Propis propis) {
		System.out.println("-------------------PROPIS-------------------------------------------");
		System.out.println("propis");
		System.out.println(propis.getNaziv());
		System.out.println(propis.getID());
		
		getDeo(propis.getDeo());
		System.out.println("---------------------END PROPIS------------------------------");
	}
	
	public void getDeo(List<Deo> delovi) {
		System.out.println("--------------------------DELOVI*----------------------------");
		for (Deo d : delovi) {
			System.out.println("Deo");
			System.out.println(d.getNaziv());
			getClanovi(d.getClan());
		}
		System.out.println("-------------------------END DELVI-------------------------");
		
	}
	public void getClanovi(List<Clan> clanovi) {
		System.out.println("-----------------------CLAN---------------------------------");
		System.out.println("Clanovi");
		for (Clan c : clanovi) {
			System.out.println(c.getNaziv());
			System.out.println(c.getOpis());
			System.out.println(c.getID());
			getSadrzaj(c.getSadrzaj());
		}
		System.out.println("-----------------------END CLANOVI-------------------------");
	}


}
