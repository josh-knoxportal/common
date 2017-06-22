package com.nemustech.platform.lbs.common.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.android.gcm.server.InvalidRequestException;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.nemustech.platform.lbs.ngms.service.VehicleApiService;

@Service(value = "gcmService")
public class GcmService {
	private static final Logger logger = LoggerFactory.getLogger(VehicleApiService.class);
	
	@Autowired
	PropertyService propertyService;
	
	//private static final  String GCM_API_KEY = "AIzaSyCWflYPfqM8EmxAXkK9Sc8heLfOMrtR1Wo";
	private static final  String GCM_API_KEY = "gcm.apikey";
	
	private static final int retries = 3;
	
	private List<Result> multicast(List<String> gcmRegIdList, Message msg){
		Sender sender = new Sender(propertyService.getString(GCM_API_KEY));
		
		 logger.info("gcmRegIdList : " + gcmRegIdList);
		 
		List<Result> resultList =  null;
    	try {
	        if(gcmRegIdList !=null) {
	        	MulticastResult multiResult = sender.send(msg, gcmRegIdList, retries);
	    		if(multiResult != null) {
		    		resultList = multiResult.getResults();
		    		
		    		for (Result result : resultList) {
		    			 //result.get
		    			 logger.info("Result getMessageId : " + result.getMessageId());
		    			 logger.info("Result getCanonicalRegistrationId : " + result.getCanonicalRegistrationId());
		    			 logger.info("Result getErrorCodeName : " + result.getErrorCodeName());
		    			 
		    			 //logger.info("Result " + result.getMessageId());
		    		}
	    		}
        
	        }
    	} catch (InvalidRequestException e) {
    		logger.info("Invalid Request:"+e.getMessage());
    	} catch (IOException e) {
    		logger.info("IO Exception:"+e.getMessage());
    	}
    	return resultList;
  	}
	
	
	public List<Result> pushToGcm(List<String> gcmRegIdList, Map<String,String> data){
		
		logger.info("FCM Message data : "+ data);
		
		if(data == null || gcmRegIdList == null) return null;
		
	   	Message msg = new Message.Builder().setData(data).build();	   	
	   	logger.info("FCM Message Format : "+ msg.toString());
    	List<Result> resultList =  multicast(gcmRegIdList, msg);
    	return resultList;
	}
	
	
    public List<Result> pushToGcm(List<String> gcmRegIdList,String message){
    	if(message == null) return null;

    	Message msg = new Message.Builder().addData("message",message).build();
    	List<Result> resultList =  multicast(gcmRegIdList, msg);
   	   	return resultList;
    }
    
    public List<Result>  pushToGcm(String regId,String message){
    	List<String> regIdList = new ArrayList<String>();
    	regIdList.add(regId);
    	return pushToGcm(regIdList,message);
    	//return pushToGcm(regIdList,message);
    }
    
    
    public void getTest(){
    	
    }
    
    
    
    
}
