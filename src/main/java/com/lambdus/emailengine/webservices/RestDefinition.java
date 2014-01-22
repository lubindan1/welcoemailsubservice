package com.lambdus.emailengine.webservices;

import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.lambdus.emailengine.EmailRequest;
import com.lambdus.emailengine.MessageQueueProducer;

import org.jboss.logging.Logger;

@Path("/emailengine")
public class RestDefinition {

	
	private static final Logger log = Logger.getLogger(RestDefinition.class.getName());
	
	@GET()
	@Path("/sendtx")
	@Produces("application/json")
    public List<String> sendTransactional(@QueryParam("email") String emailAddress, @QueryParam("templateId") int templateId, @Context UriInfo uriInfo) {

		MultivaluedMap<String, String> paramsMap = uriInfo.getQueryParameters();
		HashMap<String,String> miscParams = collectMiscParameters(paramsMap);

		EmailRequest request = new EmailRequest();
		request.setEmailAddress(emailAddress);
		request.setTemplateId(templateId);
		request.setTokens(miscParams);
		
		MessageQueueProducer mqp = new MessageQueueProducer(request);
		mqp.initialize();
		
		ArrayList<String> response = new ArrayList<String>();
		response.add("OK");
		response.add(emailAddress);
		response.add(String.valueOf(templateId));
		response.add(String.valueOf(miscParams.size()));
		
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
	
}

