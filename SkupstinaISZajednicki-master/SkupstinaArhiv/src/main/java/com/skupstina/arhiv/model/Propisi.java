package com.skupstina.arhiv.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Propisi")
public class Propisi {

	
	protected List<Propis> propis;

	@XmlElement(name="propis")
	public List<Propis> getPropis() {
		if (propis == null) {
			propis = new ArrayList<Propis>();
		}
		return this.propis;
	}


}
