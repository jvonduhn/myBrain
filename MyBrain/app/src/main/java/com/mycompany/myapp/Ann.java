package com.mycompany.myapp;


import java.util.*;


import android.content.Context;

public class Ann{
	private List<Attribute> attrList;
	private DbHelper db=null;

	public Ann( Context context ){
		attrList = new ArrayList<>();
		 db = new DbHelper( context );
	}

	public void setDb(DbHelper db){
		this.db = db;
	}

	public void addAttribute(String attributeName){

		attrList.add(new  Attribute(attributeName));
		db.addAttribute( attributeName );
	}

	public List<Attribute> getAttributes(){
		return attrList;
	}
}
