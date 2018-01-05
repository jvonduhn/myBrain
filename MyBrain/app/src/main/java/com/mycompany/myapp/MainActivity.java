package com.mycompany.myapp;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;

import java.util.*;
import java.text.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.content.*;
import android.graphics.Bitmap.*;
import android.widget.CompoundButton.OnCheckedChangeListener;



public class MainActivity extends Activity implements OnCheckedChangeListener{


	private Map<String, ProgressBar> progressBars;
	private Ann ann;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		progressBars = new HashMap<>();

		System.out.println("\n\n.....creating new brain .....");

		ann = new Ann(this);
		ann.setLogger((TextView) this.findViewById(R.id.mainEditText1));

		LinearLayout ll = (LinearLayout) this.findViewById(R.id.sLayout);
	/*	
		ann.cleanDb();

	
		 ann.addAttribute("grey");
		 ann.addAttribute("elephant");
	//ann.addAttribute("table");
	//	 ann.addAttribute("cup");
		 ann.addAttribute("africa");

		 ann.addNeuron("elephant", 0, 55);

		 ann.addAttr2Neuron("elephant", "elephant", 100, 100);

	//*/

		List<Attribute> aList = ann.getSavedAttributes();
		addLog("aList size = " + aList.size());
		for (Attribute a : aList){
			addLog("....." + a.getAttrID() + " " + a.getAttrName());
			ll.addView(addProgressBar(a.getAttrName()));
		}

		List<Neuron> nList = ann.getSavedNeurons();
		addLog("nList size = " + nList.size());
		for (Neuron n : nList){
			addLog("....." + n.getId() + " " + n.getNeuronName());

			ll.addView(addProgressBar(n.getNeuronName(), false, true));
			List<NeuronAttribute> naList = ann.getLinkedAttr(n);

			addLog("naList size = " + naList.size());
			for (NeuronAttribute na : naList){
				Attribute attr = ann.getAttributeById(na.getAttrId());
				n.addAttribute(na);
				addLog("....." + na.getId() + " " + attr.getAttrName());
			}
		}
	}

	private View addProgressBar(String name){
		return addProgressBar(name, true, false);
	}

	private View addProgressBar(String name, boolean withCheckBox, boolean isNeuron){
		LinearLayout fl = new LinearLayout(this);
		CheckBox cb = new CheckBox(this);
		cb.setText(name);
		cb.setWidth(200);
		cb.setOnCheckedChangeListener(this);

		TextView tv = new TextView(this);
		tv.setText(name);
		tv.setWidth(200);

		ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		if(isNeuron)
		progressBars.put(name+"_NEURON", pb);
		else
			progressBars.put(name, pb);
		

		FrameLayout rl = new FrameLayout(this);

		if (withCheckBox){
			fl.addView(cb);
		}
		else{
			fl.addView(tv);
		}

		fl.addView(rl);
		fl.addView(pb);

		addLog("...5...");

		return fl;
	}

	private void addLog(String txtLine){
		TextView txt = (TextView) this.findViewById(R.id.mainEditText1);

		txt.setText(txt.getText() + "\n" + txtLine);		
	}


	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1){
		addLog("arg0 = " + arg0.getText() + " " + arg1);
		ProgressBar pb = progressBars.get(arg0.getText());

		if (arg1){
			pb.setProgress(100);
			ann.activateAttribute(arg0.getText().toString());
		}
		else{
			pb.setProgress(0);
		}
	}
}
