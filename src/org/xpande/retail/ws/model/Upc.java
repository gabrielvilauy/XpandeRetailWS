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
@XmlRootElement(name = "upc")
public class Upc implements Serializable {

	private static final long serialVersionUID = 4345490130466690898L;

	private int ID = 0;
	private String codigoBarra = "";
	
	/**
	 * 
	 */
	public Upc() {
	}

	public int getID() {
		return ID;
	}

	@XmlElement
	public void setID(int iD) {
		ID = iD;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	@XmlElement
	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}
	
}
