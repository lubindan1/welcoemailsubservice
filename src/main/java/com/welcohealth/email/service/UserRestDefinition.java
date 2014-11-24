package com.welcohealth.email.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Map.Entry;

import org.jboss.logging.Logger;

import redis.clients.jedis.Jedis;

@Path("/service")
public class UserRestDefinition {

        
        private static final Logger log = Logger.getLogger(UserRestDefinition.class.getName());
        private static final String CONFIG_PATH = "/etc/welcoservice.conf"; 
        private static final String sprocKeyPrefix = "SPKEY"; 
        private static final String dataObjectPrefix = "DAOKEY"; 
       
        
        @POST
    	@Path("/adduser/json")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response addUser(User user, @Context UriInfo uriInfo,  @Context HttpServletRequest req) {

        	ArrayList<String> response = new ArrayList<String>();
        	MultivaluedMap<String, String> paramsMap = null;
        	
        	try{
        		response.add(user.getEmail());
                paramsMap = uriInfo.getQueryParameters();   
                
                EmailDAO.loadData("dbo.HealthEmail_Add", user, "welco");
                EmailDAO.loadData("dbo.HealthEmail_Add", user, "azure");
                
        	   }
        	  catch(Exception ex){
        		  log.error(ex.getMessage());
        		  return Response.serverError().build();
        	  }
              
        	        	  
        	logRequest(user.getEmail(), req.getRemoteHost(), req.getRemoteAddr(), "/adduser/json");
        	
        	return Response.status(200).entity(response).build();
        
        }
        
        @POST
    	@Path("/adduser/dailyjackpot/json")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response addDailyJackpotUser(TheDailyJackpotUser user, @Context UriInfo uriInfo,  @Context HttpServletRequest req) {

        	ArrayList<String> response = new ArrayList<String>();
        	MultivaluedMap<String, String> paramsMap = null;
        	
        	try{
        		response.add(user.getEmail());
                paramsMap = uriInfo.getQueryParameters();   
                
                EmailDAO.loadData("TheDailyJackpot_Email_Add", user);
                
        	   }
        	  catch(Exception ex){
        		  log.error(ex.getMessage());
        		  return Response.serverError().build();
        	  }
              
        	        	  
        	logRequest(user.getEmail(), req.getRemoteHost(), req.getRemoteAddr(), "/adduser/dailyjackpot/json");
        	
        	return Response.status(200).entity(response).build();
        
        }
        
        
        @POST
    	@Path("/addsurvey/json")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response addSurvey(HealthSurvey survey, @Context UriInfo uriInfo,  @Context HttpServletRequest req) {

        	ArrayList<String> response = new ArrayList<String>();
        	MultivaluedMap<String, String> paramsMap = null;
        	
        	try{
        		response.add("OK");
                paramsMap = uriInfo.getQueryParameters();   
                
                EmailDAO.loadData("dbo.HealthSurvey_Add", survey);
                
        	   }
        	  catch(Exception ex){
        		  log.error(ex.getMessage());
        		  return Response.serverError().build();
        	  }
              
        	        	  
        	logRequest(String.valueOf(survey.getSessionid()), req.getRemoteHost(), req.getRemoteAddr(), "/addsurvey/json");
        	
        	return Response.status(200).entity(response).build();
        
        }
        
        
        
        private HashMap<String,String[]> getConfig(){
        	
        	 HashMap<String,String[]> hm = new HashMap<String,String[]>();
			 try {
				FileReader fr = new FileReader(CONFIG_PATH);
        	    BufferedReader br = new BufferedReader(fr);
        	    String sLine = "";
        	    while((sLine = br.readLine()) != null){
        		    String[] configs = sLine.split("::");
        		    String[] sarr = new String[2];
        		    sarr[0] = configs[1];
        		    sarr[1] = configs[2];
        		    hm.put(configs[0], sarr);
        	    }
        	  
			  } catch (Exception e) {
					e.printStackTrace();
			  }
        	  
        	 return hm;
        }
        
    	private String fetchFromRedis(String configKey){
    		
    		Jedis redis = new Jedis("localhost");
    		String rval = "";
    		
    		try{
    		  rval  = redis.get(configKey);
    		  return rval;
    		}
    		catch (Exception e){
    			return rval;
    		}
    		
    	}	
    	
        private static void addToRedis(String key, String value)
        {
            try{	
               Jedis jedis = new Jedis("localhost");
               jedis.set(key, value);
               jedis.expire(key, 3600);
               }
            catch (Exception e){
               
            }
          
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
        
        
        
        private void logRequest(String userEmail, String remoteHost, String remoteIp, String endpoint){

        	  Date timeNow = new Date();
        	  SimpleDateFormat ft = new SimpleDateFormat ("E yyyy.MM.dd hh:mm:ss a zzz");
        	  ft.setTimeZone(TimeZone.getTimeZone("EST"));
        	  StringBuilder sb = new StringBuilder();
        	  
        	  sb.append(ft.format(timeNow))
        	  .append(" ")
        	  .append(endpoint)
        	  .append(" ")
        	  .append(remoteHost)
        	  .append(" ")
        	  .append(remoteIp)
        	  .append(" : ")
        	  .append(userEmail);

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