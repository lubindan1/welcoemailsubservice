package com.lambdus.emailengine;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;

import org.jboss.logging.Logger;

import com.lambdus.emailengine.webservices.RestDefinition;


@Stateless
@LocalBean
public class MessageQueueProducer {
	
	private static final Logger log = Logger.getLogger(MessageQueueProducer.class.getName());
	
	@Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName = "java:/queue/transactionalemail")
    private Queue queueTx;
    
    private EmailRequest request;
    private EmailMessage emailMessage = new EmailMessage();
    
    private MessageProducer messageProducer;
    private Connection connection = null;
    private Session session;
    private Destination destination;
    private MapMessage mapMessage;
    
    
    /*
    public MessageQueueProducer(EmailRequest request) {
    	log.info(request.getTemplateId());
    	this.request = request;
    }
    */
    
    public void addRequest(EmailRequest request){
    	
    	log.info(request.getTemplateId());
    	this.request = request;
    	
    }
    
    public void initialize(){
    	// resolve the message assembly
    	MessageAssembler ma = new MessageAssembler(this.request.getTemplateId(), this.request.getParameters());
    	
    	this.emailMessage.emailCreative = ma.getAssembledMessage();
    	this.emailMessage.emailAddress = this.request.getEmailAddress();
    	this.emailMessage.subjectLine = ma.getSubjectLine();
    	this.emailMessage.fromAddress = ma.getFromAddress();
    	
    	startConnection();
    	createMessage();
    	sendMessage();
    	
    	
    }
 
    public void startConnection()
    {
    	log.info("jms startConnection called");
    	try
    	{
          this.connection = this.connectionFactory.createConnection();
          log.info("after connection");
          this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          log.info("after session");
          this.destination = queueTx;
          log.info("after destination = queue");
          this.messageProducer = session.createProducer(this.destination);
          log.info("messageProducer created");
          this.connection.start();
    	}
    	catch (JMSException e) {
    		log.info("jms exception");
    	}
    	catch (Exception ex) {
    		log.info("general exception");
    	}
    	
    }
    
    public void createMessage()
    {
    	log.info("jms createMessage called");
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
      log.info("jms sendMessage called");
      try
      {
    	this.messageProducer.send(this.mapMessage);
      }
  	  catch (JMSException e) {
		
  	  }      
    }
    
	public String getInfo(){
		
		StringBuilder sb = new StringBuilder();
		sb.append(this.emailMessage.emailCreative);
		sb.append("---");
		sb.append(this.emailMessage.emailAddress);
		
		
		return sb.toString();
		
	}
	

}
