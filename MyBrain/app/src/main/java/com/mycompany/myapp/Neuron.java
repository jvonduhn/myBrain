package com.mycompany.myapp;

import java.util.*;

public class Neuron implements AttributeListener
{
	private String id;
	private String neuronName;
	private int testType;
	private int threshold;
	private List<NeuronAttribute> attrList;
	
	public Neuron( String id, String neuronName, int testType, int threshold){
		this.neuronName = neuronName;
		this.testType = testType;
		this.threshold = threshold;
		this.id = id;
		attrList = new ArrayList<>();
	}
	
	public boolean cycle(){
		int weightSum =0;
		for ( NeuronAttribute na : attrList ){
			weightSum += na.getWeight();
		}
		return false;
	}
	
	public void addAttribute( NeuronAttribute na){
		System.out.println( "....adding attr " +  na.getAttribute().getAttrName());
		attrList.add(na);
		na.getAttribute().addListeners(this);
	}
	
	public String getNeuronName(){
		return neuronName;
	}
	
	public String getId(){
		return this.id;
	}
	
	public int getTestType(){
		return this.testType;
	}
	
	public int getThreshold(){
		return this.threshold;
	}
	
	public void actionPerformed(AttributeEvent attributeEvent){ 
		System.out.println("++++attribute event = " + attributeEvent.getSource().getAttrName());
	}
	
}
