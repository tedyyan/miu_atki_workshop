package edu.mum.jobworkshop.vincheckerService.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class TestVinChecker {
	public static void main(String[] args) {
		 
        RestTemplate restTemplate = new RestTemplate();
 
        //1---------------------------------
        String resp = restTemplate.getForObject("https://vpic.nhtsa.dot.gov/api/vehicles/decodevinvalues/5UXWX7C5*BA?format=json&modelyear=2011", String.class);
 
        System.out.println(resp);
        
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = springParser.parseMap(resp);

//        String mapArray[] = new String[map.size()];
//        System.out.println("Items found: " + mapArray.length);
//
//        int i = 0;
//        for (Map.Entry<String, Object> entry : map.entrySet()) {
//        		System.out.println(entry.getKey() + " = " + entry.getValue());        		
//        		i++;
//        }
        String VehicleType  = "";
        ArrayList<LinkedHashMap<?, ?>> arr = (ArrayList<LinkedHashMap<?, ?>>) map.get("Results");
        for (LinkedHashMap<?, ?> a : arr) {
        	//System.out.println(a);  //map2.get("VehicleType")
        	System.out.println(a.get("VehicleType"));
        	VehicleType = (String) a.get("VehicleType");
        }
        
        //2---------------------------------
        String resp2 = restTemplate.getForObject("https://vpic.nhtsa.dot.gov/api/vehicles/GetMakesForVehicleType/"+VehicleType+"?format=json", String.class);
        System.out.println(resp2);
        
        Map<String, Object> map2 = springParser.parseMap(resp2);
        Set<String> makerNames  = new HashSet<String>();
        ArrayList<LinkedHashMap<?, ?>> arr2 = (ArrayList<LinkedHashMap<?, ?>>) map2.get("Results");
        for (LinkedHashMap<?, ?> a : arr2) {
        	//System.out.println(a);  //map2.get("VehicleType")
        	//System.out.println(a);
        	makerNames.add((String) a.get("MakeName"));
        }
        System.out.println(makerNames);
        
        
        //3----------------------------------
        //https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/honda?format=json

        Set<String> modelNames  = new HashSet<String>();
        for (String makername: makerNames) {
        	String resp3 = "";
        	try {
        		//http://{enpointUrl}?method=logout&session={sessionId}
        		makername = makername.replace(".", "");
        		String realurl = "https://vpic.nhtsa.dot.gov/api/vehicles/getmodelsformake/"+makername+"?format=json";        		
        		resp3 = restTemplate.getForObject(realurl, String.class);
        		
                System.out.println(resp3);
        	}catch(Exception e) {
                e.printStackTrace();
        		continue;
        	}
        	Map<String, Object> map3 = null;
            try {
                map3 = springParser.parseMap(resp3);
            }catch(JsonParseException e) {
            	e.printStackTrace();
            	continue;
            }
            @SuppressWarnings("unchecked")
			ArrayList<LinkedHashMap<?, ?>> arr3 = (ArrayList<LinkedHashMap<?, ?>>) map3.get("Results");
            for (LinkedHashMap<?, ?> a : arr3) {
            	//System.out.println(a);  //map2.get("VehicleType")
            	//System.out.println(a);
            	modelNames.add((String) a.get("Model_Name"));
            }
            System.out.println(modelNames);
        }
    }

	private static URI encodePath(String path,String para) {
		UriComponents uriComponents =
        	    UriComponentsBuilder.fromUriString(path).build()
        	        .expand(para)
        	        .encode();
        URI uri = uriComponents.toUri();
        return uri;
	}
	
	
}
