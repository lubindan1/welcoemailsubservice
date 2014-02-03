package com.lambdus.emailengine;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.jboss.logging.Logger;

public class BatchProcessor implements Callable<String> {
	
	 private static final Logger log = Logger.getLogger(BatchProcessor.class.getName());
	
	 private int targetId = 0;
	 private int templateId = 0;
	 BatchTarget batchtarget;
	 
	 private String jdbcHandle = "jdbc:mysql://localhost:3306/email_engine";
	 private String dbusername = "dan";
	 private String dbpassword = "lambdus2200";
	 
	
	 public BatchProcessor(BatchRequest request)
	 {
	     this.targetId = request.getTargetId();
	     this.templateId = request.getTemplateId();
	     // HACK: Do BatchTarget config
	     this.batchtarget = new BatchTarget("email");
	 }
	
	 public String fetchTarget()
	 {
		ResultSet rs;
		String queryText = "";
	    try {	 
		     Connection con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);  
		     Statement st = con.createStatement();
		     String select = String.format("SELECT * FROM email_engine.targets WHERE id = %d", this.targetId);
		     rs = st.executeQuery(select);
		     queryText = rs.getString("queryText");
		} catch (SQLException e) {
			
			log.info(e.getMessage());
		}
	    
		
		 return queryText;
		 
	 }
	 
	 public HashMap<String,Object> processQuery(String query, BatchTarget batchtarget)
	 {
			ResultSet rs;
		    HashMap<String,Object> userData = new HashMap<String,Object>();
		 try {	 
			 Connection con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);  
			 Statement stmt = con.createStatement();
			 rs = stmt.executeQuery(query);

			 while (rs.next()) {
				 HashMap<String,String> fieldmap = new HashMap<String,String>();
				 for (String f: batchtarget.getFields()){
					 fieldmap.put(f, rs.getString(f));
				  }
				 userData.put(rs.getString(batchtarget.getEmailAddress()), fieldmap);
			 }
			 
		 } 
		 catch (SQLException e) {
				log.info(e.getMessage());
			}
	 
		 return userData;
	 }
	 
	 
	   @Override
	   public String call() throws Exception {
		    String targetQueryText = fetchTarget();
		    HashMap<String,Object> batchDirectiveHash = processQuery(targetQueryText, batchtarget);
		    //pass batchDirectiveHash to MessageQueueProducer
	        return Thread.currentThread().getName();
	    }
	 
	

}
