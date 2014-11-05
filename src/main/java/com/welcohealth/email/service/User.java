package com.welcohealth.email.service;

import java.io.Serializable;
import java.sql.Timestamp;

public class User implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	
	private String email;
	private String firstname;
	private String lastname;
	private Long registrationId;
	private Long sessionId;
	private Timestamp timestamp;
    private String address;
    private String address2;
    private String city;
    private String state;
    private Integer postalcode;
    private String ipaddress;
    private Timestamp dob;
    private Integer subcampaignId;
    private Integer campaignId;
    private String dnsname;
    private Timestamp daterec;
    

	public String getEmail(){
		return email;
	}
	public void setEmail(String email){
		this.email = email;
	}
	public String getFirstname(){
		return firstname;
	}
	public void setFirstname(String firstname){
		this.firstname = firstname;
	}
	public String getLastname(){
		return lastname;
	}
	public void setLastname(String lastname){
		this.lastname = lastname;
	}
	public Long getRegistrationId(){
		return registrationId;
	}
	public void setRegistrationId(Long registrationId){
		this.registrationId = registrationId;
	}
	public Long getSessionId(){
		return sessionId;
	}
	public void setSessionId(Long sessionId){
	    this.sessionId = sessionId;
	}
	public Timestamp getTimestamp(){
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp){
		this.timestamp = timestamp;
	}
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address = address;
	}
	public String getAddress2(){
		return address2;
	}
	public void setAddress2(String address2){
		this.address2 = address2;
	}
	public String getCity(){
		return city;
	}
	public void setCity(String city){
		this.city = city;
	}
	public String getState(){
		return state;
	}
	public void setState(String state){
		this.state = state;
	}
	public Integer getPostalcode(){
		return postalcode;
	}
	public void setPostalcode(Integer postalcode){
		this.postalcode = postalcode;
	}
	public String getIpaddress(){
		return ipaddress;
	}
	public void setIpaddress(String ipaddress){
		this.ipaddress = ipaddress;
	}
	public Timestamp getDob(){
		return dob;
	}
	public void setDob(Timestamp dob){
		this.dob = dob;
	}
	public Integer getSubcampaignId(){
		return subcampaignId;
	}
	public void setSubcampaignId(Integer subcampaignId){
		this.subcampaignId = subcampaignId;
	}
	public Integer getCampaignId(){
		return campaignId;
	}
	public void setCampaignId(Integer campaignId){
		this.campaignId = campaignId;
	}
	public String getDnsname(){
		return dnsname;
	}
	public void setDnsname(String dnsname){
		this.dnsname = dnsname;
	}
	public Timestamp getDaterec(){
		return daterec;
	}
	public void setDaterec(Timestamp daterec){
		this.daterec = daterec;
	}
	
}
