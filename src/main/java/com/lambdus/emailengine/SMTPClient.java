package com.lambdus.emailengine;

import java.security.Security;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SMTPClient {
	
	
	private Properties properties = System.getProperties();
	private Session session;
	private String toAddress;
	private String fromAddress;
	private String subjectLine;
	private String emailCreative;
	
	public SMTPClient(String emailAddress, String emailCreative, String subjectLine, String fromAddress) 
	{
	
	this.properties.setProperty("mail.smtp.host", "localhost");
	this.session = Session.getDefaultInstance(this.properties);
	this.toAddress = emailAddress;
	this.emailCreative = emailCreative;
	this.fromAddress = fromAddress;
	this.subjectLine = subjectLine;
	
	}
	
	
	public void sendmail()
	{
	   try{
          MimeMessage message = new MimeMessage(this.session);
          message.setFrom(new InternetAddress(this.fromAddress));
          message.addRecipient(Message.RecipientType.TO,
                                  new InternetAddress(this.toAddress));
          message.setSubject(this.subjectLine);
          message.setContent(this.emailCreative,
        		            "text/html");
          Transport.send(message);
          
      }catch (MessagingException mex) {
         mex.printStackTrace();
      }
	}

}
