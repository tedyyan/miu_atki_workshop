package edu.mum.jobworkshop.vincheckerService.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;

public class WorkerThread implements Runnable {
	  
    private String command;
    RestTemplate restTemplate;
    String makername;
    CopyOnWriteArrayList<String> modelNames;
    CopyOnWriteArrayList<LinkedHashMap<?, ?>> modelDetails;
    
    public WorkerThread(RestTemplate restTemplate, String makername, CopyOnWriteArrayList<String> modelNames, CopyOnWriteArrayList<LinkedHashMap<?, ?>> modelDetails){
        this.restTemplate=restTemplate;
        this.makername=makername;
        this.modelNames=modelNames;
        this.modelDetails=modelDetails;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
    	JsonParser springParser = JsonParserFactory.getJsonParser();
		String resp3 = "";
    	try {
    		//TODO Faraday&Future Inc.this factory will cause problem.
    		makername = makername.replace(".", "");
    		String realurl = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/"+makername+"?format=json";   
			resp3 = restTemplate.getForObject(realurl, String.class);
    		
            System.out.println(resp3);
    	}catch(Exception e) {
            e.printStackTrace();
    		return;
    	}
    	
    	Map<String, Object> map3 = null;
        try {
            map3 = springParser.parseMap(resp3);
        }catch(JsonParseException e) {
        	e.printStackTrace();
    		return;
        }
        @SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<?, ?>> arr3 = (ArrayList<LinkedHashMap<?, ?>>) map3.get("Results");
        for (LinkedHashMap<?, ?> a : arr3) {
        	
        	modelNames.add((String) a.get("Model_Name"));
        	modelDetails.add(a);
        }
        System.out.println(modelNames);
    }

    @Override
    public String toString(){
        return this.command;
    }
}