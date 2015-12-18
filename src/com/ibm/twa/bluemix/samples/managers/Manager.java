/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import org.apache.wink.json4j.JSON;
import org.apache.wink.json4j.JSONArray;
import org.apache.wink.json4j.JSONException;
import org.apache.wink.json4j.JSONObject;


public abstract class Manager {
	
	private String serviceName;
	private boolean connected 	= false;
	private String url			= null;
    private String user 		= null;
    private String password 	= null;
	private String host			= null;
	private int port;
		
	public Manager(String serviceName){
		this.serviceName = serviceName;
	}
	
	/**
	 * initConnection
	 * 
	 * Connects and authenticates to a specific service on server, exploring the content of
	 * VCAP_SERVICES content.
	 * 
	 * @throws JSONException
	 * 
	 */
	public void initConnection() throws JSONException {
		if (System.getenv("VCAP_SERVICES") == null) 
		{
			switch(this.serviceName){
			case "WorkloadScheduler":
					this.user 		= "your user";
					this.password	= "your password";
					this.url		= "your url";
					break;
			case "sendgrid":
					this.user 		= "your user";
					this.password	= "your password";
					this.host		= "your host";
					break;
			}
		} 
		else 
		{
			System.out.println("Looking for "+ this.serviceName +" service..");

			// Retrieve the VCAP_SERVICES variable
			String vcapJSONString 	= System.getenv("VCAP_SERVICES");
			Object jsonObject 		= JSON.parse(vcapJSONString);
			JSONObject json 		= (JSONObject) jsonObject;

			String key;
			JSONArray twaServiceArray = null;
			
			// Reading the content of the VCAP_SERVICES variable
			for (Object k : json.keySet()) 
			{	
				key = (String) k;
				if (key.startsWith(this.serviceName)) 
				{	
					//  Service found
					twaServiceArray = (JSONArray) json.get(key);
					System.out.println(this.serviceName +" service found!");
					break;
				}
			}
			if (twaServiceArray == null) 
			{
				// Service not found
				System.out.println("Could not connect: I was not able to find the "+this.serviceName+" service!");
				System.out.println("This is your VCAP services content");
				System.out.println(vcapJSONString);
				return;
			}
			
			JSONObject twaService 	= (JSONObject) twaServiceArray.get(0);
			JSONObject credentials 	= (JSONObject) twaService.get("credentials");
			switch(this.serviceName){
				case "WorkloadScheduler":
						this.user 		= (String) credentials.get("userId");
						this.password	= (String) credentials.get("password");
						this.url		= (String) credentials.get("url");
						break;
				case "sendgrid":
						this.user 		= (String) credentials.get("username");
						this.password	= (String) credentials.get("password");
						this.host		= (String) credentials.get("hostname");
						break;
			}

			// Connect to service
			System.out.println("Starting " + this.serviceName + " service connection..");
			try 
			{
				this.connect();
			} 
			catch (Exception e) 
			{
				System.out.println("Could not connect to the service: " + e.getClass().getName() + " " + e.getMessage());
				return;
			}
			this.setConnected(true);
			System.out.println("Connection obtained. You are connected to " + this.serviceName + " service instance.");
		}
	}
	
	public abstract void connect();
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
}
