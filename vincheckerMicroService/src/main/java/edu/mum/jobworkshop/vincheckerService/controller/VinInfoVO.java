package edu.mum.jobworkshop.vincheckerService.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class VinInfoVO {
	
	
	public ArrayList<LinkedHashMap<?, ?>> vinDetail;
	public ArrayList<LinkedHashMap<?, ?>> allMakers;
	public CopyOnWriteArrayList<LinkedHashMap<?, ?>> allModels;
	
	public VinInfoVO(ArrayList<LinkedHashMap<?, ?>> vinDetail, ArrayList<LinkedHashMap<?, ?>> allMakers, CopyOnWriteArrayList<LinkedHashMap<?, ?>> allModels) {
		this.vinDetail = vinDetail;
		this.allMakers = allMakers;
		this.allModels = allModels;	
	}
	
	
}
