/**
 * 
 */
package org.xpande.retail.ws.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author gabriel
 *
 */
@XmlRootElement(name = "bpartner")
public class BPartner implements Serializable {

	private int ID = 0;
	private String RUT = "";
	private String RazonSocial = "";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5801679662830286795L;

	/**
	 * 
	 */
	public BPartner() {
	}

	public int getID() {
		return ID;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}

	public String getRUT() {
		return RUT;
	}

	@XmlElement
	public void setRUT(String rUT) {
		RUT = rUT;
	}

	public String getRazonSocial() {
		return RazonSocial;
	}

	@XmlElement
	public void setRazonSocial(String razonSocial) {
		RazonSocial = razonSocial;
	}
	
	
	

}
