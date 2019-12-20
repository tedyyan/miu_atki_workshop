package edu.mum.jobworkshop.vincheckerService.controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import edu.mum.jobworkshop.vincheckerService.controller.VINCheckController;

public class VINCheckControllerTest {
	public static void main(String[] args) {
		VINCheckController v = new VINCheckController();
		System.out.print(v.getVinData("5UXWX7C5*BA", null));
	}
	
	@Test
	public void multiplicationOfZeroIntegersShouldReturnZero() {
		VINCheckController tester = new VINCheckController(); // MyClass is tested

        RestTemplate rest  = new RestTemplate();;
		List<String> k = new ArrayList<>();
		// assert statements
		tester.findVinDetail(rest,"5UXWX7C5*BA",k );
        assertEquals(k.get(0).trim() , "MULTIPURPOSE PASSENGER VEHICLE (MPV)" );
    }
}
