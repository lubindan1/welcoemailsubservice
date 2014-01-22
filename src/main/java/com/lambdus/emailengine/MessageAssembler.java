package com.lambdus.emailengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.lambdus.emailengine.persistence.TemplatePersist;
import com.lambdus.emailengine.persistence.SessionFactoryUtil;

import redis.clients.jedis.Jedis;


public class MessageAssembler {
	
	String template;
	String assembledMessage;
	String subjectLine;
	String fromAddress; 
	
	
	public MessageAssembler(int templateId, HashMap<String, String> tokenKVPairs)
	{
		getTemplate(templateId);
		this.assembledMessage = replaceTokens(this.template, tokenKVPairs);
	}
	
	public void getTemplate(int templateId)
	{
		checkCache(templateId);
	}
	
	public String replaceTokens(String template, HashMap<String, String> tokenKVPairs)
	{
		Set<String> keySet = tokenKVPairs.keySet();
		
		for (String k : keySet){
			  String token = new StringBuilder().append("##").append(k).append("##").toString();
			  template = template.replaceAll(token, tokenKVPairs.get(k));
			}
		
		return template;
		
	}
	
	private void checkCache(int templateId)
	{
	   //Redis in-memory cache key-value	
	   Jedis jedis = new Jedis("localhost");
	   
	   if (jedis.exists(String.valueOf(templateId)))
	   {
	       String[] templateVarargs = {"creative","subjectline","fromaddress"};
  	       List<String> templateVals  = jedis.hmget(String.valueOf(templateId), templateVarargs);
  	 	   this.template = templateVals.get(0);
  		   this.subjectLine = templateVals.get(1);
  		   this.fromAddress = templateVals.get(2);
	   }
	   else
	   {
		   TemplatePersist templateData = retrieveTemplateFromDB(templateId);
  	 	   this.template = templateData.getCreative();
  		   this.subjectLine = templateData.getSubjectline();
  		   this.fromAddress = templateData.getFromaddress();
  		   addRedisCache(templateId, templateData);
		   
	   }
  	   
  	   
	}
	
	private TemplatePersist retrieveTemplateFromDB(int templateId)
	{
		Session session = SessionFactoryUtil.getSessionFactory().getCurrentSession();
		session.getTransaction().begin();
        Criteria cb = session.createCriteria(TemplatePersist.class);
        TemplatePersist result = (TemplatePersist) cb.add(Restrictions.eq("id",templateId)).uniqueResult();
        session.getTransaction().commit();
		
        return result;
	}

	public String getAssembledMessage()
	{
		return this.assembledMessage;
	}

	public String getSubjectLine()
	{
		return this.subjectLine;
	}

	public String getFromAddress()
	{
		return this.fromAddress;
	}
	
	/*
	private ArrayList<String> collectTokenItems(HashMap<String, String> params)
	{
		Set<String> keySet = params.keySet();
		ArrayList<String> tokenList = new ArrayList<String>();
		for (String k : keySet){
		  String token = new StringBuilder().append("##").append(k).append("##").toString();
		  tokenList.add(token);
		}
		
		return tokenList;
	}
	*/
	
	private void addRedisCache(int templateId, TemplatePersist templateData)
	{
	   Jedis jedis = new Jedis("localhost");
	   HashMap<String, String> redisHashMap = new HashMap<String, String>();
	   redisHashMap.put("creative", templateData.getCreative());
	   redisHashMap.put("subjectline", templateData.getSubjectline());
	   redisHashMap.put("fromaddress", templateData.getFromaddress());
	   jedis.hmset(String.valueOf(templateId), redisHashMap);
	}
}
