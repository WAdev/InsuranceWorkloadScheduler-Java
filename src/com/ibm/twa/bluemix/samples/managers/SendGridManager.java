/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import org.apache.wink.json4j.JSONException;
import com.ibm.twa.bluemix.samples.helpers.Insurance;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SendGridManager extends Manager {
	
	final static String sendgridServiceName = "sendgrid";
	private SendGrid sendgrid;
	
	public SendGridManager(){
		super(sendgridServiceName);
	}
	
	/**
	 * send
	 * 
	 * Check all the submissions
	 * and send an email to those has the status not set on "completed"
	 * 
	 * @return SendGrid response
	 * 
	 * @throws JSONException
	 */
	public String send(Insurance insurance) throws JSONException{
				
		if(!this.isConnected()){
			this.initConnection();
		}
		
		String resp;
		SendGrid.Response response = null;
		
		SendGrid.Email sgemail = new SendGrid.Email();
		
		try {
			System.out.println("Sending email to: " + insurance.getEmail());
			sgemail.addTo(insurance.getEmail());
			sgemail.setFrom(insurance.getEmail());
			sgemail.setSubject(insurance.getSubject());
			sgemail.setText(insurance.getBody());
			response = this.sendgrid.send(sgemail);	
		} catch (SendGridException e) {
			e.printStackTrace();
		}
		if(response.getStatus()){
			resp = insurance.getBody();
		} else{
			resp = "Email to " + insurance.getEmail() + " not sent because: " + response.getMessage();
		}
		return resp;
	}
	
	public void connect(){
		this.sendgrid = new SendGrid(this.getUser(), this.getPassword());
	}
	
}