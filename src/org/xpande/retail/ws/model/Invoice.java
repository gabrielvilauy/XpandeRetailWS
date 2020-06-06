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
@XmlRootElement(name = "invoice")
public class Invoice implements Serializable {

	private static final long serialVersionUID = 2446538634131319941L;

	private String serie = "";
	private String numero = "";
	private String fecha = "";
	private String moneda = "";
				
	/**
	 * 
	 */
	public Invoice() {
	}


	public String getSerie() {
		return serie;
	}

	@XmlElement
	public void setSerie(String serie) {
		this.serie = serie;
	}


	public String getNumero() {
		return numero;
	}

	@XmlElement
	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getFecha() {
		return fecha;
	}

	@XmlElement
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public String getMoneda() {
		return moneda;
	}

	@XmlElement
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	
}
