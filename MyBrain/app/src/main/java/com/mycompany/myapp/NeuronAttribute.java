package com.mycompany.myapp;

public class NeuronAttribute
{
	private String attrName;
	private int attrWeight;	
	private int decCounter;
	
	public NeuronAttribute( String attrName, int attrWeight, int decCounter){
		this.attrName = attrName;
		this.attrWeight = attrWeight;
		this.decCounter = decCounter;
	}
	
	public String getAttr(){
		return attrName;
	}
	
	public int getWeight(){
		return this.attrWeight;
	}
	
	public int getDecCount(){
		return this.decCounter;
	}
	
	public void decCounter(){
		if(--this.decCounter < 0){
			this.decCounter =0;
		}
	}
	
}
