package com.lambdus.emailengine;

import java.util.HashMap;

public class EmailRequest {
	
	String emailAddress;
	int templateId = 0;
    HashMap<String, String> parameters;
	
	public void setEmailAddress(String emailAddress)
	{
		this.emailAddress = emailAddress;
	}
	
	public void setTemplateId(int templateId)
	{
		this.templateId = templateId;
	}
	
	public void setTokens(HashMap<String, String> parameters)
	{
		this.parameters = parameters;
	}
	
	public String getEmailAddress()
	{
		return this.emailAddress;
	}
	
	public int getTemplateId()
	{
		return this.templateId;
	}
	
	
	public HashMap<String, String> getParameters()
	{
		return this.parameters;
	}
	
	
	

}
