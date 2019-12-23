package com.skupstina.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Amandmani")
public class Amandmani {

	protected List<Amandman> amandman;
	
	@XmlElement(name="amandman")
	public List<Amandman> getAmandman() {
		if (amandman == null) {
			amandman = new ArrayList<Amandman>();
		}
		return this.amandman;
	}
}
