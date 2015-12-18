package com.ibm.twa.bluemix.samples.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONException;

import com.ibm.twa.bluemix.samples.helpers.Insurance;
import com.ibm.twa.bluemix.samples.managers.SendGridManager;
import com.sendgrid.SendGridException;

@ApplicationPath("api")
@Path("/insurance/submit")
public class InsuranceSubmit {
	
	private SendGridManager sgManager;
	
	/**
	 * submit
	 * 
	 * Method called by the first restful step
	 * of Workload Scheduler process
	 * that send a submission request email
	 * 
	 * @throws SendGridException, JSONException
	 */
	@GET
	@Produces({MediaType.TEXT_HTML})
	public Response submit(
			@QueryParam("email") String email,
			@QueryParam("hp") int hp,
			@QueryParam("birthyear") int birthyear
			) throws SendGridException, JSONException {
				this.sgManager = new SendGridManager();
				this.sgManager.initConnection();
				Insurance insurance = new Insurance(email, hp, birthyear, true);
				String response = this.sgManager.send(insurance);
				return Response.ok().entity(response).build();
	}
}