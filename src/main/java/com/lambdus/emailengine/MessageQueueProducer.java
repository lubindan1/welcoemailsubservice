package com.lambdus.emailengine;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;



public class MessageQueueProducer {
	
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/transactionalemail")
    private Queue queueTx;
    
    private EmailRequest request;
    private EmailMessage emailMessage;
    
    private MessageProducer messageProducer;
    private Connection connection = null;
    private Session session;
    private Destination destination;
    private MapMessage mapMessage;
    
    public MessageQueueProducer(EmailRequest request) {
    	this.request = request;
    }
    
    public void initialize(){
    	// resolve the message assembly
    	MessageAssembler ma = new MessageAssembler(this.request.getTemplateId(), this.request.getParameters());
    	
    	this.emailMessage.emailCreative = ma.getAssembledMessage();
    	this.emailMessage.emailAddress = this.request.getEmailAddress();
    	this.emailMessage.subjectLine = ma.getSubjectLine();
    	this.emailMessage.fromAddress = ma.getFromAddress();
    }
 
    public void startConnection()
    {
    	try
    	{	
          this.connection = this.connectionFactory.createConnection();
          this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          this.messageProducer = session.createProducer(destination);
          this.connection.start();
    	}
    	catch (JMSException e) {
    		
    	}
    	
    }
    
    public void createMessage()
    {
    	try
    	{
    	  mapMessage = this.session.createMapMessage(); 
    	  this.mapMessage.setString("emailAddress", emailMessage.emailAddress);
    	  this.mapMessage.setString("emailCreative", emailMessage.emailCreative);
    	  this.mapMessage.setString("subjectLine", emailMessage.subjectLine);
    	  this.mapMessage.setString("fromAddress", emailMessage.fromAddress);
    	  
    	}
    	catch (JMSException e) {
    		
    	}
    }
    
    public void sendMessage()
    {
      try
      {
    	this.messageProducer.send(this.mapMessage);
      }
  	  catch (JMSException e) {
		
  	  }      
    }
    
	
	

}
