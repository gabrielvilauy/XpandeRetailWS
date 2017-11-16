/**
 * 
 */
package org.xpande.retail.ws.service;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.xpande.retail.ws.model.BPartner;

/**
 * @author gabriel
 *
 */
@Path("RetailService")
public class RetailService {

	/**
	 * 
	 */
	public RetailService() {
	}
	
	@GET 
	@Path("getvendorlist")
	@Produces(MediaType.APPLICATION_XML) 
	public List<BPartner> getRecepcionPartners(){

		RetailServiceImpl impl = new RetailServiceImpl();
		return impl.getRecepcionPartners();
		
	}

	@GET 
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		return "holaaaa";
	}
	
	
}
