package com.mycompany.myapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.UUID;

public class DbHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VER=1;
	public static final String DATABASE_NAME="ANN";


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


	public DbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VER);
		System.out.println(".....created db");
	}

	public DbHelper(Context context, String name, CursorFactory factory,
					int version){
		super(context, name, factory, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db){

		System.out.println(".....Db created " + this.DATABASE_NAME + " v" + this.DATABASE_VER);

		String sqlTable = "create table " + TABLE_ATTRIBUTE + "("  + KEY_ID + " text," + KEY_ATTR_NAME + " text);"; 
		db.execSQL(sqlTable);
		sqlTable = "create table " + TABLE_NEURON + "("  + KEY_ID + " text," + KEY_NEURON_NAME + " text," + KEY_TEST_TYPE + " int," + KEY_THRESHOLD + " int);"; 
		db.execSQL(sqlTable);
		sqlTable = "create table " + TABLE_NEURON_ATTR + "("  + KEY_ID + " text," + KEY_NEURON_ID + " text," + KEY_ATTR_ID + " text," + KEY_WEIGHT + " int," + KEY_DEC_AMT + " int);"; 
		db.execSQL(sqlTable);

		System.out.println(".....created table");
		System.out.println(sqlTable);
		System.out.println();

	}


	@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTRIBUTE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEURON);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEURON_ATTR);

        // Create tables again
        onCreate(db);
    }


	public String addAttribute(String attrName){
		System.out.println(".....creating attribute " + attrName);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String uuid = UUID.randomUUID().toString();
		values.put(KEY_ID, uuid);
		values.put(KEY_ATTR_NAME, attrName);
		db.insert(TABLE_ATTRIBUTE, null, values);
		db.close();

		return uuid;
	}

	public String addNeuron(String neuronName, int testType, int threshold){
		System.out.println(".....creating neuron " + neuronName);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		String uuid = UUID.randomUUID().toString();
		values.put(KEY_ID, uuid);
		values.put(KEY_NEURON_NAME, neuronName);
		values.put(KEY_TEST_TYPE, testType);
		values.put(KEY_THRESHOLD, threshold);
		db.insert(TABLE_NEURON, null, values);
		db.close();

		return uuid;
	}


	public String getAttrUuId(String attrName){
		System.out.println(String.format(".....looking for attriibute (%s)", attrName));
		String attrId=null;

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_ATTRIBUTE + "where " + KEY_ATTR_NAME + " = ?";
        Cursor cursor = dbr.rawQuery(selectQuery, new String []{ attrName});

		if (cursor.getCount() != 0){
			cursor.moveToFirst();
			attrId = cursor.getString(0);
		}
		return attrId;
	}

	public String getNeuronUuId(String neuronName){
		System.out.println(String.format(".....looking for neuron (%s)", neuronName));
		String neuronId=null;

		SQLiteDatabase dbr = this.getReadableDatabase();
		String selectQuery = "SELECT  * FROM " + TABLE_NEURON + "where " + KEY_NEURON_NAME + " = ?";
        Cursor cursor = dbr.rawQuery(selectQuery, new String []{ neuronName});

		if (cursor.getCount() != 0){
			cursor.moveToFirst();
			neuronId = cursor.getString(0);
		}
		return neuronId;
	}

	public String linkAttr2Neuron(String attrName, String neuronName){
		System.out.println(String.format(".....linking attriibute (%s) 2 neuron (%s)", attrName, neuronName));

		String attrId = getAttrUuId(attrName);
		if (attrId == null){
			attrId = this.addAttribute(attrName);
		}


		String neuronId=getNeuronUuId(neuronName);
		if (neuronId == null){
			neuronId = this.addNeuron(neuronName, 0, 50);
		} 
		SQLiteDatabase dbw = this.getWritableDatabase();
		ContentValues values = new ContentValues();
	    String uuid = UUID.randomUUID().toString();

		values.put(KEY_ID, uuid);
		values.put(KEY_NEURON_ID, neuronId);
		values.put(KEY_ATTR_ID, attrId);
		values.put(KEY_WEIGHT, 50);
		values.put(KEY_DEC_AMT, 100);

		dbw.insert(TABLE_NEURON_ATTR, null, values);
		dbw.close();

		return uuid;		
	}

	public void adjustWeight(String neuronName, String attrName, int newWeight){
		String neuronId = getNeuronUuId(neuronName);
		String attrId = getAttrUuId(attrName);

		SQLiteDatabase dbw = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_WEIGHT, newWeight);

		dbw.update(TABLE_NEURON_ATTR, values, String.format("%s = ? and %s = ?", KEY_NEURON_ID, KEY_ATTR_ID), new String[]{neuronId,attrId});
		dbw.close();



	}

}

