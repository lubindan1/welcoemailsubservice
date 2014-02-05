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
	 BatchRequest request;
	 
	 private static String jdbcHandle = "jdbc:mysql://localhost:3306/email_engine";
	 private static String dbusername = "dan";
	 private static String dbpassword = "lambdus2200";
	 
	 //Azure instance
	 private static String azureConnection = "jdbc:sqlserver://v8st4k97ey.database.windows.net:1433;database=email_engine;user=email_engine@v8st4k97ey;password=!Lambdus2200;encrypt=true;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
	 
	
	 public BatchProcessor(BatchRequest request)
	 {
		 this.request = request;
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
	    	 log.info("Before fetTarget JDBC conn call");
		     Connection con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);
		     log.info("DB conn catalog: " + con.getCatalog());
		     Statement st = con.createStatement();
		     String select = String.format("SELECT * FROM email_engine.targets WHERE id = %d", this.targetId);
		     log.info("Select statement for target: " + select);
		     try{
		     rs = st.executeQuery(select);
		     queryText = rs.getString("queryText");
		     }
		     catch(Exception e){
		    	log.info(e.getMessage());
		     }
		     log.info("Query Text " + queryText);	     
		     
		} catch (SQLException e) {
			
			log.info(e.getMessage());
		}
	    
		
		 return queryText;
		 
	 }
	 
	 public HashMap<String,Object> processQuery(String query, BatchTarget batchtarget)
	 {
		log.info("Query " + query);
		ResultSet rs;
		HashMap<String,Object> userData = new HashMap<String,Object>();
		 try {
			 // Use this for production
			 //Connection con = DriverManager.getConnection(jdbcHandle, dbusername, dbpassword);
			 //Azure Test Conn String
			 Connection con = DriverManager.getConnection(azureConnection);
			 Statement stmt = con.createStatement();
			 rs = stmt.executeQuery(query);

			 while (rs.next()) {
				 log.info("Result set processing row " + String.valueOf(rs.getRow()) );
				 HashMap<String,String> fieldmap = new HashMap<String,String>();
				 if (batchtarget.getFields().size() > 0){
				    for (String f: batchtarget.getFields())
				      {
					    fieldmap.put(f, rs.getString(f));
				      }
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
		    BatchQueueProducer bqp = new BatchQueueProducer();
		    bqp.initialize(batchDirectiveHash, request);
		    int processed = bqp.processBatch();
		    String batchResultInfo = String.format("%s %s",Thread.currentThread().getName(), String.valueOf(processed) );
	        return batchResultInfo;
	    }
	 
	

}
