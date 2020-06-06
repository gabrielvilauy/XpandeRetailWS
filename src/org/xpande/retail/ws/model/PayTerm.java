package org.xpande.retail.ws.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payterm")
public class PayTerm implements Serializable {

	private static final long serialVersionUID = 6845398322335360203L;

	
	private int ID = 0;
	private String NomTermino = "";

	
	public PayTerm() {
	}


	public int getID() {
		return ID;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}


	public String getNomTermino() {
		return NomTermino;
	}

	@XmlElement
	public void setNomTermino(String nomTermino) {
		NomTermino = nomTermino;
	}

}
