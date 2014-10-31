package com.lambdus.emailengine.webservices;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import com.lambdus.emailengine.BatchCampaignController;
import com.lambdus.emailengine.BatchProcessor;
import com.lambdus.emailengine.BatchRequest;
import com.lambdus.emailengine.EmailRequest;
import com.lambdus.emailengine.IBatchCampaignController;
import com.lambdus.emailengine.MessageQueueProducer;


import org.jboss.logging.Logger;

@Path("/service")
public class RestDefinition {

        
        private static final Logger log = Logger.getLogger(RestDefinition.class.getName());
        
        @GET()
        @Path("/sendtx/json")
        @Produces("application/json")
        public List<String> sendTransactionalJson(@QueryParam("email") String emailAddress, @QueryParam("templateId") int templateId, @Context UriInfo uriInfo, @Context HttpServletRequest req) {

        	ArrayList<String> response = new ArrayList<String>();
        	MultivaluedMap<String, String> paramsMap = null;
        	
        	try{
        		if(emailAddress != null && templateId != 0 && !(emailAddress.equals("bademail@none.com")) ){
                paramsMap = uriInfo.getQueryParameters();
                HashMap<String,String> miscParams = collectMiscParameters(paramsMap);

                EmailRequest request = new EmailRequest();
                request.setEmailAddress(emailAddress);
                request.setTemplateId(templateId);
                request.setTokens(miscParams);
                
                MessageQueueProducer mqp = new MessageQueueProducer();
                mqp.addRequest(request);
                mqp.initialize();
                
                response.add("OK");
                response.add(emailAddress);
                response.add(String.valueOf(templateId));
        		}
        		else{
        			response.add("email and templateId are required parameters for this endpoint");
        		}
                
        	   }
        	  catch(Exception ex){
        		  response.add("Error occurred");
        		  log.error(ex.getMessage());
        	  }
                
        	  logRequest(paramsMap, req, "/sendtx/json");
        	 
              return response;
        
        }
        
        
        @GET()
        @Path("/sendtx/xml")
        @Produces("text/xml")
        public RequestResponse sendTransactionalXml(@QueryParam("email") String emailAddress, @QueryParam("templateId") int templateId, @Context UriInfo uriInfo, @Context HttpServletRequest req) {

        	RequestResponse rr = new RequestResponse();
        	MultivaluedMap<String, String> paramsMap = null;
        	 try{
        		if(emailAddress != null && templateId != 0){
                paramsMap = uriInfo.getQueryParameters();
                HashMap<String,String> miscParams = collectMiscParameters(paramsMap);

                EmailRequest request = new EmailRequest();
                request.setEmailAddress(emailAddress);
                request.setTemplateId(templateId);
                request.setTokens(miscParams);
                
                MessageQueueProducer mqp = new MessageQueueProducer();
                mqp.addRequest(request);
                mqp.initialize();
                
                rr.setResult("OK");
                rr.setEmailAddress(emailAddress);
        		}
        		else{
        			rr.setResult("email and templateId are required parameters for this endpoint");
        		}
        		
        	 }
        	 catch(Exception ex){
        		 rr.setResult("Error occurred");
        		 log.error(ex.getMessage());
        	 }
        	 
        	 logRequest(paramsMap, req, "/sendtx/xml");
        	 
             return rr;
        
        }
        
        
        
        @GET()
        @Path("/sendbatch/json")
        @Produces("application/json")
        public List<String> sendBatchJson(@QueryParam("targetId") int targetId, @QueryParam("templateId") int templateId, @Context UriInfo uriInfo) {

                MultivaluedMap<String, String> paramsMap = uriInfo.getQueryParameters();
                HashMap<String,String> miscParams = collectMiscParameters(paramsMap);

                
    		    final Hashtable jndiProperties = new Hashtable();
    	        jndiProperties.put(javax.naming.Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    	        try{
    	        final javax.naming.Context context = new InitialContext(jndiProperties);
    	        String jndi = "ejb:mailingservice/admin//BatchCampaignController!com.lambdus.emailengine.IBatchCampaignController?stateful";
    	        IBatchCampaignController campaignController = (IBatchCampaignController) context.lookup(jndi);
    	        
    			campaignController.setTargetId(targetId);
    			campaignController.setTemplateId(templateId);
    			campaignController.startCampaign();
    		     }
    	        catch(Exception e){
    	        	log.info("exception with jndi lookup");
    	        	log.error(e.getMessage());
    	        }
                
                /*
        		String uuid = UUID.randomUUID().toString();
        		String association = BatchCampaignController.resolveAssociation(targetId);
        		
                BatchRequest request = new BatchRequest();
                request.setTargetId(targetId);
                request.setTemplateId(templateId);
                request.setUuid(uuid);
              

                //FIX: Make this Separate method
                BatchProcessor bp = new BatchProcessor(request);
                FutureTask<Integer> futureTask = new FutureTask<Integer>(bp);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(futureTask);
                */
              
                ArrayList<String> response = new ArrayList<String>();
                response.add("OK");
                response.add(String.valueOf(targetId));
                response.add(String.valueOf(templateId));
                
                return response;
        
        }
        
        
        
        private HashMap<String,String> collectMiscParameters(MultivaluedMap<String, String> paramsMap)
        {
                HashMap<String,String> miscParams = new HashMap<String,String>();
                Iterator<Entry<String, List<String>>> iter = paramsMap.entrySet().iterator();
                while (iter.hasNext()) 
                {
                        MultivaluedMap.Entry<String,List<String>> kvp = (MultivaluedMap.Entry<String,List<String>>)iter.next(); 
                        if (kvp.getKey() != "email" && kvp.getKey() != "templateId")
                        {
                                miscParams.put( (String) kvp.getKey(), (String) kvp.getValue().get(0) );
                    }
                }
                
                return miscParams;
        }
        
        
        
        private void logRequest(MultivaluedMap<String, String> requestData, HttpServletRequest req, String endpoint){

        	  Date timeNow = new Date();
        	  SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd hh:mm:ss a zzz");
        	  ft.setTimeZone(TimeZone.getTimeZone("EST"));
        	  StringBuilder sb = new StringBuilder();
        	  
        	  sb.append(ft.format(timeNow))
        	  .append(" ")
        	  .append(endpoint)
        	  .append(" ")
        	  .append(req.getRemoteHost())
        	  .append(" ")
        	  .append(req.getRemoteAddr())
        	  .append(" : ");

        	   if (requestData != null){
        	      for (MultivaluedMap.Entry<String, List<String>> entry : requestData.entrySet()){
        	        sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(", ");
        	      }
        	    }
        	    sb.append("\n");
               try{
        	      File file = new File("/usr/local/share/apilogs/requests.log");
        	      FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
        	      BufferedWriter bw = new BufferedWriter(fw);
        	      bw.write(sb.toString());
        	      bw.close();
                 }catch(Exception e){log.error(e.getMessage());}

        	}
        
}