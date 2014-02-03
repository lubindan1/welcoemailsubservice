package com.lambdus.emailengine;

import java.util.ArrayList;

public class BatchTarget {
	
	private String emailAddress;
	private ArrayList<String> fields;
	
	public BatchTarget(String emailAddress){
		this.emailAddress = emailAddress;
	}
		
	public String getEmailAddress(){
		return this.emailAddress;
	}
	
	public void addField(String field){
		this.fields.add(field);
	}
	
	public ArrayList<String> getFields(){
		return this.fields;
	}

}
