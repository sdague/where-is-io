package net.dague.astro;

import net.dague.astro.data.SimData;
import net.dague.astro.util.JovianCalculator;
import net.dague.astro.util.JovianPoints;
import net.dague.astro.util.SolarCalc;
import net.dague.astro.util.SolarSim;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class JovianSpiral extends Activity {
	
	AsyncTask<SolarCalc,View,Void> jc;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("IO", "got past create");
        
        // TODO: remove once progressive rendering works

        JovianGraphView jgv = new JovianGraphView(this);

        // SimData d = new SimData(this);
        // d.deleteAll();
        SolarSim sim = new SolarSim(this);
        // JovianPoints jp = sim.getMoonPoints(JovianGraphView.startTime(), 120, true);
        
        new Thread(new JovianCalculator(sim, jgv, JovianGraphView.startTime(), 120)).start();
//        if(!jp.complete()) {
//        		this.jc = new JovianCalculator().execute(new SolarCalc(sim, JovianGraphView.startTime(), 120, jgv));
//        	}
//      }
        
    	setContentView(jgv);
        
        jgv.requestFocus();
    
//        SolarSim s = new SolarSim();
//        JovianMoons jv = s.getMoons(System.currentTimeMillis());
//        Log.i("IO", jv.toString());
//        setContentView(R.layout.main);
    }
//    protected void onPause() {
//    	super.onPause();
//    }
    
//    protected void onSaveInstanceState(Bundle  outState) {
//    	if (this.jc != null && this.jc.getStatus() != AsyncTask.Status.FINISHED) {
//     		outState.putBoolean("inProgress", true);
//    	}
//    }
}