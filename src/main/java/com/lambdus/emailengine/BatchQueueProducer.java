package com.lambdus.emailengine;

import java.util.HashMap;

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
import javax.naming.InitialContext;

import org.jboss.logging.Logger;

import com.lambdus.emailengine.webservices.RestDefinition;


@Stateless
@LocalBean
public class BatchQueueProducer {
        
    private static final Logger log = Logger.getLogger(BatchQueueProducer.class.getName());
        
    private ConnectionFactory connectionFactory;

    private Queue batchQueue;
    
    private HashMap<String,Object> batchData; 
    private BatchRequest request;
    private EmailMessage emailMessage = new EmailMessage();
    
    private MessageProducer messageProducer;
    private Connection connection = null;
    private Session session;
    private Destination destination;
    private MapMessage mapMessage;
    
    public BatchQueueProducer(HashMap<String,Object> batchData){
    	this.batchData = batchData; 	
    }
    
    //DO MAIN STUFF HERE
    public int processBatch(){
    	
    	return 0;
    }
    
    public void initialize(){
            // resolve the message assembly
            MessageAssembler ma = new MessageAssembler(this.request.getTemplateId(), this.request.getParameters());
            
            this.emailMessage.emailCreative = ma.getAssembledMessage();
            this.emailMessage.emailAddress = this.request.getEmailAddress();
            this.emailMessage.subjectLine = ma.getSubjectLine();
            this.emailMessage.fromAddress = ma.getFromAddress();
            this.emailMessage.fromName = ma.getFromName();
            
            startConnection();
            createMessage();
            sendMessage();
            
            
    }
 
    public void startConnection()
    {
          log.info("jms startConnection called");
          try
          {
          InitialContext ctx = new InitialContext();
          connectionFactory = (ConnectionFactory) ctx.lookup("java:/ConnectionFactory");
          batchQueue = (Queue) ctx.lookup("java:/queue/batchemail");
          this.connection = this.connectionFactory.createConnection();
          this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
          this.destination = batchQueue;
          this.messageProducer = session.createProducer(this.destination);
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
            try
            {
              mapMessage = this.session.createMapMessage(); 
              this.mapMessage.setString("emailAddress", emailMessage.emailAddress);
              this.mapMessage.setString("emailCreative", emailMessage.emailCreative);
              this.mapMessage.setString("subjectLine", emailMessage.subjectLine);
              this.mapMessage.setString("fromAddress", emailMessage.fromAddress);
              this.mapMessage.setString("fromName", emailMessage.fromName);
              
            }
            catch (JMSException e) {
                    
            }
    }
    
    public void sendMessage()
    {
      try
      {
            this.messageProducer.send(this.mapMessage);
            this.messageProducer.close();
            this.session.close();
            this.connection.close();
      }
      catch (JMSException jmse) {
            log.info(jmse.getMessage());
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