/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.helpers;

public class Insurance {
	
	private String email;
	private int hp;
	private int birthyear;
	private float cost;
	private String subject;
	private String body;
	//if true send a submit email (first restful step)
	//else send a cost email (second restful step)
	private boolean flag;
	
	public Insurance(String email, int hp, int birthyear, boolean flag){
		this.email		= email;
		this.hp			= hp;
		this.birthyear	= birthyear;
		this.flag		= flag;
		this.subject	= null;
		this.body		= null;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getBirthyear() {
		return birthyear;
	}

	public void setBirthyear(int birthyear) {
		this.birthyear = birthyear;
	}

	public float getCost(){
		return this.cost;
	}
	
	public void setCost(float cost){
		this.cost = cost;
	}
	
	public String getSubject(){
		if(this.subject == null){
			if(this.flag){
				this.subject = "Insurance request submitted";
			} else{
				this.subject = "Insurance cost";
			}
		}
		return this.subject;
	}
	
	public String getBody(){
		if(this.body == null){
			if(this.flag){
				this.body = "The insurance for a "+this.hp+" hp car and for a person born in "+this.birthyear+" will be processed. Wait for another email while the insurance cost will be generated.";
			} else{
				this.cost = (hp*10) + (birthyear-1900);
				this.body = "The insurance cost for a "+this.hp+" hp car and for a person born in "+this.birthyear+" is "+this.cost+"$";
			}
		}
		return body;
	}
}