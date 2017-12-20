package com.mycompany.myapp;

import android.app.*;
import android.os.*;



public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		//Context context, String name, CursorFactory factory,int version
		
		System.out.println( "\n\n.....creating new brain ....." );
	
		Ann ann = new Ann(this);
		
		
		}
}
