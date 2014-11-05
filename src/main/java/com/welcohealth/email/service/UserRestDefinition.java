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
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.Map.Entry;




import org.jboss.logging.Logger;

@Path("/service")
public class UserRestDefinition {

        
        private static final Logger log = Logger.getLogger(UserRestDefinition.class.getName());
        
        @POST
    	@Path("/adduser/json")
        @Consumes(MediaType.APPLICATION_JSON)
        @Produces(MediaType.APPLICATION_JSON)
        public Response addUser(User user, @Context UriInfo uriInfo, @Context HttpServletRequest req) {

        	ArrayList<String> response = new ArrayList<String>();
        	MultivaluedMap<String, String> paramsMap = null;
        	
        	try{
        		response.add(user.getEmail());
                paramsMap = uriInfo.getQueryParameters();   
                
                EmailDAO.loadData("dbo.HealthEmail_Add", user);
                
        	   }
        	  catch(Exception ex){
        		  log.error(ex.getMessage());
        		  return Response.serverError().build();
        	  }
                
        	  logRequest(req, "/adduser/json");
        	 
        	  return Response.status(200).entity(response).build();
        
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
        
        
        
        private void logRequest(HttpServletRequest req, String endpoint){

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
        	  try {
        		    BufferedReader reader = req.getReader();
        		    String line = null;
        		    while ((line = reader.readLine()) != null){
        		    	sb.append(line);
        		    }
        	   } catch (Exception e) { log.error(e.getMessage()); }    

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