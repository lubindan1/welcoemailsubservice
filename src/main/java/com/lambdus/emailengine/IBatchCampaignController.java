package com.lambdus.emailengine;

public interface IBatchCampaignController {
	
	void startCampaign();
	
    void setTargetId(int targetId);

    void setTemplateId(int templateId);


}
