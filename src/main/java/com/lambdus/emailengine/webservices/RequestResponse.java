package com.lambdus.emailengine.webservices;

import javax.xml.bind.annotation.XmlAttribute; 
import javax.xml.bind.annotation.XmlElement; 
import javax.xml.bind.annotation.XmlRootElement;   


    @XmlRootElement(name = "request") 
    public class RequestResponse {   
	
	String emailAddress; 
	
	String result; 
   
	@XmlAttribute
	public String getEmailAddress() { return emailAddress; }   
	
	public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }   
	
	@XmlElement
	public String getResult() { return result; }   
	
	public void setResult(String result) { this.result = result; }   
	
}
