package com.mycompany.myapp;

import java.util.*;
import android.content.Context;
import android.widget.*;

public class Ann implements AttributeListener{
	private List<Attribute> attrList=null;
	private List<Neuron> neuronList;
	private DbHelper db=null;
	private TextView  logger = null;
	

	public Ann(Context context){

		neuronList = new ArrayList<>();
		db = new DbHelper(context);
		System.out.println(".....ann ctor complete");

	}

	public void setLogger(TextView logger){
		this.logger  = logger;
		log("...from ann");
		db.setLogger(logger);
	}

	public void log(String txt){
		if (logger != null)
			logger.setText(logger.getText() + "\n" + txt);	
	}

	public void setDb(DbHelper db){
		this.db = db;
	}
	
	public void cleanDb(){
		db.cleanDb();
	}

	public Attribute addAttribute(String attributeName){
		if (attrList == null)
			attrList = new ArrayList<>();
		Attribute attribute = db.getAttributeByName(attributeName);
		
		if (attribute == null){
			attribute = db.addAttribute(attributeName);
		}
		attribute.addListeners(this);
		attrList.add(attribute);
		logger.setText("\n------attr size  " + attrList.size());
		return attribute;
	}

	public void actionPerformed(AttributeEvent attributeEvent){ 
		logger.setText("attribute event = " + attributeEvent.getSource().getAttrName());
	}


	public void activateAttribute(String name){
		logger.setText("attr size  " + attrList.size());
		logger.setText(logger.getText() +  "\nlooking for " + name);
		for (Attribute a : attrList){
			logger.setText(logger.getText() + "\n..looking at " + a.getAttrName());

			if (a.getAttrName().equalsIgnoreCase(name)){
				a.setActive();
				logger.setText(a.getAttrName() + " is active");
			}
		}
	}

	public List<Attribute> getAttributes(){
		return attrList;
	}

	public List<Attribute> getSavedAttributes(){
		List<Attribute> savedAttributes =  db.getAllAttributes();

		if (attrList == null){
			attrList = new ArrayList<>();
			for (Attribute a : savedAttributes){
				attrList.add(a);
				a.addListeners(this);
			}
		}
		return savedAttributes;
	}

	public Attribute getAttributeById(String id){
		return db.getAttributeById(id);
	}

	public List<Neuron> getSavedNeurons(){
		return db.getAllNeurons();
	}


	public List<Neuron> getNeurons(){
		return neuronList;
	}

	public List<NeuronAttribute> getLinkedAttr(Neuron neuron){
		return db.getAllLinkedAttr(neuron);
	}

	public void addNeuron(String neuronName, int testType, int threshold){
		Neuron neuron=db.getNeuron(neuronName);
		if (neuron == null){
			neuron = db.addNeuron(neuronName, testType, threshold);
		}

		this.neuronList.add(neuron);
	}

	public NeuronAttribute addAttr2Neuron(String attrName, String neuronName, int weight, int decAmt){
		Neuron neuron = db.getNeuron(neuronName);
		Attribute attr = db.getAttributeByName(attrName);

		NeuronAttribute neuronAttribute  = db.getLinkedAttribue(attr, neuron);
		if (neuronAttribute == null){
			neuronAttribute =  db.linkAttr2Neuron(attr, neuron, weight, decAmt);
		}

		return neuronAttribute;
	}
}
