package com.lambdus.emailengine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateful;


//@EJB(name="batchCampaignController")
@Stateful
@Remote(IBatchCampaignController.class)
public class BatchCampaignController implements IBatchCampaignController {
	
	
	private int templateId;
	
	private int targetId;
	
	@Override
	public void startCampaign(){

        BatchRequest request = new BatchRequest();
        request.setTargetId(this.targetId);
        request.setTemplateId(this.templateId);
        
        BatchProcessor bp = new BatchProcessor(request);
        FutureTask<String> futureTask = new FutureTask<String>(bp);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(futureTask); 
		
	}
	
	@Override
    public void setTargetId(int targetId)
    {
            this.targetId = targetId;
    }
	
	@Override
    public void setTemplateId(int templateId)
    {
            this.templateId = templateId;
    }
    
    public int getTargetId()
    {
            return this.targetId;
    }
    
    public int getTemplateId()
    {
            return this.templateId;
    }
    

}
