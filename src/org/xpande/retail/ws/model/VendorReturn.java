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
@XmlRootElement(name = "vendorreturn")
public class VendorReturn implements Serializable {

	private int productID = 0;
	private int partnerID = 0;
	private String motivo = "";
	private double cantidad = 0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5539018574299176247L;

	
	/**
	 * 
	 */
	public VendorReturn() {
	}

	public int getProductID() {
		return productID;
	}

	@XmlElement
	public void setProductID(int productID) {
		this.productID = productID;
	}

	public int getPartnerID() {
		return partnerID;
	}

	@XmlElement
	public void setPartnerID(int partnerID) {
		this.partnerID = partnerID;
	}

	public String getMotivo() {
		return motivo;
	}

	@XmlElement
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public double getCantidad() {
		return cantidad;
	}

	@XmlElement
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	
}
