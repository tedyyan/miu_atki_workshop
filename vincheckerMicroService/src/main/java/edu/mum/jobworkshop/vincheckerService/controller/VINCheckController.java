package edu.mum.jobworkshop.vincheckerService.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@CrossOrigin(value = "http://localhost:8080")
public class VINCheckController {
	
	private static final Logger logger = LoggerFactory.getLogger(VINCheckController.class);
	@GetMapping("/vinchecker/{vin}")
	public VinInfoVO getVinData(String vin,HttpServletResponse httpResponse) {
		RestTemplate restTemplate = new RestTemplate();
		 
		
		
		try {
	        //1---------------------------------
			ArrayList<String> VehicleTypes = new ArrayList<>();
			ArrayList<LinkedHashMap<?, ?>> vinInfo = findVinDetail(restTemplate,vin,VehicleTypes);
			if (VehicleTypes.size()==0) {
				httpResponse.setStatus(400);
			}else {
				//2---------------------------------
				Set<String> allMakers = new HashSet<>();
				ArrayList<LinkedHashMap<?, ?>> makers = findAllMakers(restTemplate,VehicleTypes.get(0),allMakers);				
				
				//3---------------------------------findAllModels
				Set<String> allModels = new HashSet<>();
				CopyOnWriteArrayList<LinkedHashMap<?, ?>> models = findAllModels(restTemplate,allMakers,allModels);
				httpResponse.setStatus(201);
				return new VinInfoVO(vinInfo,makers,models);
			}
		}catch(Exception e) {
			httpResponse.setStatus(400);	
			logger.error(e.toString());
		}
		return null;
		
	}
	/**
	 * 
	 * @param restTemplate
	 * @param vinNum
	 * @param VehicleTypes
	 * @return
	 */
	ArrayList<LinkedHashMap<?, ?>> findVinDetail(RestTemplate restTemplate,String vinNum, List<String> VehicleTypes) {
		String resp = restTemplate.getForObject("https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/"+vinNum+"?format=json&modelyear=2011", String.class);

		logger.debug(resp);
        
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(resp);
        
        String VehicleType  = "";
        @SuppressWarnings("unchecked")
		ArrayList<LinkedHashMap<?, ?>> arr = (ArrayList<LinkedHashMap<?, ?>>) map.get("Results");
        for (LinkedHashMap<?, ?> a : arr) {
        	//System.out.println(a);  //map2.get("VehicleType")
    		logger.debug((String) a.get("VehicleType"));
        	VehicleType = (String) a.get("VehicleType");
        	break;
        }
        if (VehicleType.length()>0)
        	VehicleTypes.add(VehicleType);
        return arr;
	}
	/**
	 * 
	 * @param restTemplate
	 * @param VehicleType
	 * @param allMakers
	 * @return
	 */
	ArrayList<LinkedHashMap<?, ?>> findAllMakers(RestTemplate restTemplate,String VehicleType, Set<String> allMakers) {
		String resp2 = restTemplate.getForObject("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/"+VehicleType+"?format=json", String.class);
        System.out.println(resp2);

        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map2 = springParser.parseMap(resp2);
        
        ArrayList<LinkedHashMap<?, ?>> arr2 = (ArrayList<LinkedHashMap<?, ?>>) map2.get("Results");
        for (LinkedHashMap<?, ?> a : arr2) {
        	allMakers.add((String) a.get("MakeName"));
        }
        logger.info(allMakers.toString());
        return  arr2;
	}
	
	/**
	 * 
	 * @param restTemplate
	 * @param makerNames
	 * @param modelNames
	 * @return
	 */	
	CopyOnWriteArrayList<LinkedHashMap<?, ?>> findAllModels(RestTemplate restTemplate,Set<String> makerNames,Set<String> modelNames) {
        
		CopyOnWriteArrayList<LinkedHashMap<?, ?>> modelDetails = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<String> modelNames1 = new CopyOnWriteArrayList<>();
        
        //---------------------------------------------------------
        ExecutorService executor = Executors.newFixedThreadPool(30);
        for (String makername: makerNames) {
            Runnable worker = new WorkerThread( restTemplate,  makername,  modelNames1,  modelDetails);
            executor.execute(worker);
        }
        executor.shutdown();
        int i = 0;
        while (!executor.isTerminated()) {
        	try {
				Thread.sleep(100);
				i++;
				if (i>20) {
					logger.error("thread timeout!");
					break;					
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        logger.info("Finished all threads");
        
        return modelDetails;
	}
	
	/**
	 * 
	 * @param restTemplate
	 * @param makername
	 * @param modelNames
	 * @param modelDetails
	 */
	private void workThead(RestTemplate restTemplate, String makername, ArrayList<String> modelNames, ArrayList<LinkedHashMap<?, ?>> modelDetails) {     		
		
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
        	//System.out.println(a);  //map2.get("VehicleType")
        	//System.out.println(a);
        	modelNames.add((String) a.get("Model_Name"));
        	modelDetails.add(a);
        }
        System.out.println(modelNames);
    	
	}
}
