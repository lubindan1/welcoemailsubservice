package com.lambdus.emailengine;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.jboss.logging.Logger;

public class TaskMonitor {
	
	 private static final Logger log = Logger.getLogger(TaskMonitor.class.getName());
	
     public static void monitorTask(Future<?> futureTask, ExecutorService executor, BatchRequest batchRequest){
	   boolean status = true;
	   while (status) {
         try {
             if(futureTask.isDone()){
                 executor.shutdown();
                 status = false;
                 return;
             }
              
             if(!futureTask.isDone()){
             log.info("Task: Template " + String.valueOf(batchRequest.getTemplateId()) + " Target " + String.valueOf(batchRequest.getTargetId()) + " TaskStatus " + futureTask.get() );
             }

         } catch (Exception e) {
            log.error(e.getMessage());
         }
	 }	   
   }
}
