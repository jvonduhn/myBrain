package com.mycompany.myapp;

import java.util.*;

public class Neuron
{
	private String neuronName;
	private int testType;
	private int threshold;
	private List<NeuronAttribute> attrList;
	
	public Neuron( String neuronName, int testType, int threshold){
		this.neuronName = neuronName;
		this.testType = testType;
		this.threshold = threshold;
		attrList = new ArrayList<>();
	}
	
	public void addAttribute( String attrName, int weight, int decAmt ){
		
	}
}
