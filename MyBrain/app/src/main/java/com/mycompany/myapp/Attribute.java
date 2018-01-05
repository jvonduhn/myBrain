package com.mycompany.myapp;

import java.util.*;

public class Attribute{
	private String id=null;
	private String attrName;
	private boolean isActive;
	private List<AttributeListener> listeners;



	public Attribute(String attrName){
		this.attrName = attrName;
		this.isActive = false;
		listeners = new ArrayList<>();
	}

	public Attribute(String attrName, String id){
		setAttrID(id);
		setAttrName(attrName);
		listeners = new ArrayList<>();
	}

	public String getAttrName(){
		return attrName;
	}

	public void setActive(){
		this.isActive = true;
		sendToListeners();
	}

	public void addListeners(AttributeListener attributeListener){
		System.out.println( "2....adding listener " + "::"+this.attrName+"=="+this.getAttrID()+" " + attributeListener );
		System.out.println( "...Listener size add  " + listeners.size());
		listeners.add(attributeListener);
	}

	private void sendToListeners(){
		System.out.println( "...Listener size  " + listeners.size());
		for (AttributeListener al: listeners){	
			System.out.println( "...al  " + al.toString());
			al.actionPerformed(new AttributeEvent(this));
		}
	}

	public boolean isActive(){
		return isActive;
	}

	public String getAttrID(){
		return this.id;
	}

	public void setAttrName(String attrName){
		this.attrName = attrName;
	}

	public void setAttrID(String id){
		this.id = id;
	}	
}
