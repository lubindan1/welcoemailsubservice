package com.welcohealth.email.service;

import java.io.Serializable;

public class TheDailyJackpotUser implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String encRegistrationId;
	private String registrationId;
	
	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	
	public String getEncRegistrationId(){
		return encRegistrationId;
	}
	public void setEncRegistrationId(String encRegistrationId){
		this.encRegistrationId = encRegistrationId;
	}
	
	public String getRegistrationId(){
		return registrationId;
	}
	public void setRegistrationId(String registrationId){
		this.registrationId = registrationId;
	}

}
