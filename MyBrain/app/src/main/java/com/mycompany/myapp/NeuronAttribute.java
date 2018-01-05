package com.mycompany.myapp;

public class NeuronAttribute
{
	private String id;
	private String neuronId;
	private String attrId;
	private Attribute attribute;
	private int attrWeight;	
	private int decCounter;
	private boolean isActive;
	
	public NeuronAttribute( String id, String neuronId, Attribute attribute, int attrWeight, int decCounter){
		this.id = id;
		this.neuronId = neuronId;
		this.attrId = attribute.getAttrID();
		this.attribute = attribute;
		this.attrWeight = attrWeight;
		this.decCounter = decCounter;
		isActive = false;
	}
	
	public String getId(){
		return id;
	}
	
	public void setAsActive(){
		isActive = true;
	}
	
	public String getAttrId(){
		return attrId;
	}
	
	public String getNeuronId(){
		return neuronId;
	}
	
	public int getWeight(){
		return this.attrWeight;
	}
	
	public Attribute getAttribute(){
		return this.attribute;
	}
	
	public int getDecCount(){
		return this.decCounter;
	}
	
	public void decCounter(){
		if(--this.decCounter < 0){
			this.decCounter =0;
			isActive = false;
		}
	}
}
