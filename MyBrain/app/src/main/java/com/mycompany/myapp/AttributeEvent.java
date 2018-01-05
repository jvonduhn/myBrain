package com.mycompany.myapp;

public class AttributeEvent
{
	private Attribute source;
	
	public AttributeEvent( Attribute source ){
		this.source = source;
	}
	public Attribute getSource(){
		return source;
	}
}
