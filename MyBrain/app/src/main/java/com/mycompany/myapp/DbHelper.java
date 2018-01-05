package com.mycompany.myapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;
import android.widget.*;


import java.util.*;


public class DbHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VER=1;
	public static final String DATABASE_NAME="ANN.db";


	private final String TABLE_ATTRIBUTE="attribute";
	private final String TABLE_NEURON="neuron";
	private final String TABLE_NEURON_ATTR="neuronAttr";

	private final String KEY_NEURON_NAME="neuronName";
	private final String KEY_ATTR_NAME ="attrName";
	private final String KEY_WEIGHT="weight";
	private final String KEY_DEC_AMT="decAmt";
	private final String KEY_ATTR_ID="attrID";
	private final String KEY_NEURON_ID="neuronID";
	private final String KEY_ID="ID";

	private final String KEY_TEST_TYPE="testType";
	private final String KEY_THRESHOLD="threshold";

	private TextView logger=null;

	private Map<String, Attribute> attributeCache;
	private Map<String, Neuron> neuronCache;
	private Map<String, NeuronAttribute> neuronAttrCache;

	public void log(String txt){
		if (logger != null)
			logger.setText(logger.getText() + "\n" + txt);	
		else System.out.println(txt);
	}

	public void setLogger(TextView logger){
		this.logger = logger;
	}


	public DbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VER);
		createCache();
		log(".....created db : " + this.getDatabaseName());
	}

	public DbHelper(Context context, String name, CursorFactory factory,
					int version){
		super(context, name, factory, version);
		createCache();
	}

	private void createCache(){
		attributeCache = new HashMap<>();
		neuronCache = new HashMap<>();
		neuronAttrCache = new HashMap<>();	
	}

	@Override
	public void onCreate(SQLiteDatabase db){

		log(".....Db created " + this.DATABASE_NAME + " v" + this.DATABASE_VER);

		String sqlTable = "create table " + TABLE_ATTRIBUTE + "("  + KEY_ID + " text," + KEY_ATTR_NAME + " text);"; 
		log(".....created table");
		log(sqlTable);
		db.execSQL(sqlTable);
		sqlTable = "create table " + TABLE_NEURON + "("  + KEY_ID + " text," + KEY_NEURON_NAME + " text," + KEY_TEST_TYPE + " int," + KEY_THRESHOLD + " int);"; 
		log(".....created table");
		log(sqlTable);
		db.execSQL(sqlTable);
		sqlTable = "create table " + TABLE_NEURON_ATTR + "("  + KEY_ID + " text," + KEY_NEURON_ID + " text," + KEY_ATTR_ID + " text," + KEY_WEIGHT + " int," + KEY_DEC_AMT + " int);"; 
		log(".....created table");
		log(sqlTable);
		db.execSQL(sqlTable);

		log(".....created table");
		log(sqlTable);
	}


	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		System.out.println(".....onUpgrade");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRIBUTE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEURON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEURON_ATTR);

        // Create tables again
        onCreate(db);
    }


	public Attribute addAttribute(String attrName){
		Attribute rAttribute=null;
		System.out.println(".....creating attribute " + attrName);

		rAttribute = attributeCache.get(attrName);
		if (rAttribute == null){

			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			String uuid = UUID.randomUUID().toString();
			values.put(KEY_ID, uuid);
			values.put(KEY_ATTR_NAME, attrName);
			db.insert(TABLE_ATTRIBUTE, null, values);
			db.close();

			rAttribute = new Attribute(attrName, uuid);
			attributeCache.put(attrName, rAttribute);
		}

		return rAttribute;
	}

	public Neuron addNeuron(String neuronName, int testType, int threshold){
		Neuron rNeuron=null;
		System.out.println(".....creating neuron " + neuronName);

		rNeuron = neuronCache.get(neuronName);
		if (rNeuron == null){

			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			String uuid = UUID.randomUUID().toString();
			values.put(KEY_ID, uuid);
			values.put(KEY_NEURON_NAME, neuronName);
			values.put(KEY_TEST_TYPE, testType);
			values.put(KEY_THRESHOLD, threshold);
			db.insert(TABLE_NEURON, null, values);
			db.close();

			rNeuron =  new Neuron(uuid, neuronName, testType, threshold);
			neuronCache.put(neuronName, rNeuron);
		}

		return rNeuron;
	}

 	public List<Attribute> getAllAttributes(){
		Attribute attribute;
		log(".....reading all attriibutes");
		List<Attribute> rData = new ArrayList<>();

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUTE;
        Cursor cursor = dbr.rawQuery(selectQuery, null);
		log(".....data size : " + cursor.getCount());
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				log("rData : " + cursor.getString(1));
				attribute = attributeCache.get(cursor.getString(1));
				if (attribute == null){
					attribute = new Attribute(cursor.getString(1), cursor.getString(0));
				}
				rData.add(attribute);

			}while(cursor.moveToNext());
		}
		return rData;
	}

	public List<Neuron> getAllNeurons(){
		Neuron neuron;
		System.out.println(".....reading all neuron");
		List<Neuron> rData = new ArrayList<>();

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NEURON;
        Cursor cursor = dbr.rawQuery(selectQuery, null);
		log(".....data size : " + cursor.getCount());
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				neuron = neuronCache.get(cursor.getString(1));
				if (neuron == null){
					neuron = new Neuron(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
					neuronCache.put(cursor.getString(1),neuron);
				}
				rData.add(neuron);
			}while(cursor.moveToNext());
		}
		return rData;
	}

	public List<NeuronAttribute> getAllLinkedAttr(Neuron neuron){
		NeuronAttribute neuronAttribute; 
		log(String.format(".....reading all linked attributes for %s", neuron.getNeuronName()));
		List<NeuronAttribute> rData = new ArrayList<>();

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NEURON_ATTR + " where " + KEY_NEURON_ID + " = ?";
        Cursor cursor = dbr.rawQuery(selectQuery, new String []{neuron.getId()});
		log(".....data size : " + cursor.getCount());
		if (cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				Attribute attribute=this.getAttributeById(cursor.getString(2));
				neuronAttribute = neuronAttrCache.get(attribute.getAttrID() + cursor.getString(1));
				if (neuronAttribute == null){
					neuronAttribute = new NeuronAttribute(cursor.getString(0), cursor.getString(1), attribute, cursor.getInt(3), cursor.getInt(4));
					neuronAttrCache.put(attribute.getAttrID() + cursor.getString(1), neuronAttribute);
				}
				rData.add(neuronAttribute);
			}while(cursor.moveToNext());
		}
		return rData;
	}

	public Attribute getAttributeByName(String attrName){
		System.out.println(String.format(".....looking for attriibute [(%s)]", attrName));
		Attribute attribute=null;

		attribute = attributeCache.get(attrName);
		if (attribute == null){
			SQLiteDatabase dbr = this.getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUTE + " where " + KEY_ATTR_NAME + " = ?";
			Cursor cursor = dbr.rawQuery(selectQuery, new String []{ attrName});

			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				attribute = new Attribute(cursor.getString(1), cursor.getString(0));
				attributeCache.put(attrName.toLowerCase(), attribute);
			}
		}
		return attribute;
	}

	public Attribute getAttributeById(String id){
		System.out.println(String.format(".....looking for attriibute (%s)", id));
		Attribute attribute=null;

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUTE + " where " + KEY_ID + " = ?";
        Cursor cursor = dbr.rawQuery(selectQuery, new String []{ id});

		if (cursor.getCount() != 0){
			cursor.moveToFirst();
			System.out.println("....looking for by id " +  cursor.getString(1));
			
			for(Object a : attributeCache.keySet()){
				System.out.println( "a = " + a );
			}
			attribute = attributeCache.get(cursor.getString(1));
			System.out.println("....get == " + attribute);
			if (attribute == null){
				attribute = new Attribute(cursor.getString(1), cursor.getString(0));
				attributeCache.put(cursor.getString(1).toLowerCase(), attribute);
			}
		}
		return attribute;
	}



	public Neuron getNeuron(String neuronName){
		System.out.println(String.format(".....looking for neuron (%s)", neuronName));
		Neuron neuron=null;

		neuron = neuronCache.get(neuronName);
		if (neuron == null){
			SQLiteDatabase dbr = this.getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + TABLE_NEURON + " where " + KEY_NEURON_NAME + " = ?";
			Cursor cursor = dbr.rawQuery(selectQuery, new String []{ neuronName});

			if (cursor.getCount() != 0){
				cursor.moveToFirst();
				neuron = new Neuron(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
				neuronCache.put(neuronName.toLowerCase(), neuron);
			}
		}
		return neuron;
	}


	public NeuronAttribute getLinkedAttribue(Attribute attribute, Neuron neuron){
		NeuronAttribute neuronAttribute = null;

		neuronAttribute = neuronAttrCache.get(attribute.getAttrID() + neuron.getId());
		if (neuronAttribute == null){
			SQLiteDatabase dbr = this.getReadableDatabase();
			String selectQuery = "SELECT  * FROM " + TABLE_NEURON_ATTR + " where " + KEY_ATTR_ID + " = ? and " + KEY_NEURON_ID + " = ?";
			Cursor cursor = dbr.rawQuery(selectQuery, new String []{ attribute.getAttrID(), neuron.getId()});

			if (cursor.getCount() != 0){
				cursor.moveToFirst();

				neuronAttribute = new NeuronAttribute(cursor.getString(0), cursor.getString(1), attribute, cursor.getInt(3), cursor.getInt(4));
				neuronAttrCache.put(attribute.getAttrID() + neuron.getId(), neuronAttribute);
			}
		}
		return neuronAttribute;
	}

	public void delNeuron(){
		log(".......delNt");
		SQLiteDatabase dbw = this.getWritableDatabase();
		String selectQuery = "delete FROM " + TABLE_NEURON;
        dbw.rawQuery(selectQuery, null);
		
		dbw.delete(TABLE_NEURON, null, null);
		dbw.close();		
	}
	
	public void delAttr(){
		log(".......delAttr");
		SQLiteDatabase dbw = this.getWritableDatabase();
		String selectQuery = "delete FROM " + TABLE_ATTRIBUTE;
        dbw.rawQuery(selectQuery, null);

		dbw.delete(TABLE_ATTRIBUTE, null, null);
		dbw.close();	
		
	}
	
	public void delNA(){
		log(".......delNA");
		SQLiteDatabase dbw = this.getWritableDatabase();
		String selectQuery = "delete FROM " + TABLE_NEURON_ATTR;
        dbw.rawQuery(selectQuery, null);

		dbw.delete(TABLE_NEURON_ATTR, null, null);
		dbw.close();	
		
	}
	
	public void cleanDb(){
		this.delNA();
		this.delNeuron();
		this.delAttr();
	}

	public NeuronAttribute linkAttr2Neuron(Attribute attribute, Neuron neuron, int weight, int decAmt){
		NeuronAttribute rNeuronAttribute = null;
		log(String.format(".....linking attriibute (%s:%s) 2 neuron (%s:%s)", attribute.getAttrID(), attribute.getAttrName(), neuron.getId(), neuron.getNeuronName()));

		if (rNeuronAttribute == null){

			SQLiteDatabase dbw = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			String uuid = UUID.randomUUID().toString();

			values.put(KEY_ID, uuid);
			values.put(KEY_NEURON_ID, neuron.getId());
			values.put(KEY_ATTR_ID, attribute.getAttrID());
			values.put(KEY_WEIGHT, weight);
			values.put(KEY_DEC_AMT, decAmt);

			dbw.insert(TABLE_NEURON_ATTR, null, values);
			dbw.close();

			rNeuronAttribute =  new NeuronAttribute(uuid, neuron.getId(), attribute, weight, decAmt) ;	
			neuronAttrCache.put(attribute.getAttrID() + neuron.getId() , rNeuronAttribute);
		}
		return rNeuronAttribute;
	}

	public void adjustWeight(String neuronName, String attrName, int newWeight){
		Neuron neuron = getNeuron(neuronName);
		Attribute attribute= getAttributeByName(attrName);

		SQLiteDatabase dbw = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_WEIGHT, newWeight);

		dbw.update(TABLE_NEURON_ATTR, values, String.format("%s = ? and %s = ?", KEY_NEURON_ID, KEY_ID), new String[]{neuron.getId(),attribute.getAttrID()});
		dbw.close();
	}

}

