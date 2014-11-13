package com.welcohealth.email.service;

public class HealthSurvey {
	
    private Long sessionid;
    private String children;
    private String active;
    private String healthinsurance;
    private String lifeinsurance;
    private String allergies;
    private String rxdrugs;
    private String diabetic;
    private String pain;
    
	public Long getSessionid(){
		return sessionid;
	}
	public void setSessionid(Long sessionid){
		this.sessionid = sessionid;
	}
	
	public String getChildren(){
		return children;
	}
	public void setChildren(String children){
		this.children = children;
	}
	
	public String getActive(){
		return active;
	}
	public void setActive(String active){
		this.active = active;
	}
	
	public String getHealthinsurance(){
		return healthinsurance;
	}
	public void setHealthinsurance(String healthinsurance){
		this.healthinsurance = healthinsurance;
	}
	
	public String getLifeinsurance(){
		return lifeinsurance;
	}
	public void setLifeinsurance(String lifeinsurance){
		this.lifeinsurance = lifeinsurance;
	}
	
	public String getAllergies(){
		return allergies;
	}
	public void setAllergies(String allergies){
		this.allergies = allergies;
	}
	
	public String getRxdrugs(){
		return rxdrugs;
	}
	public void setRxdrugs(String rxdrugs){
		this.rxdrugs = rxdrugs;
	}
	
	public String getDiabetic(){
		return diabetic;
	}
	public void setDiabetic(String diabetic){
		this.diabetic = diabetic;
	}
	
	public String getPain(){
		return pain;
	}
	public void setPain(String pain){
		this.pain = pain;
	}
	
	
}
