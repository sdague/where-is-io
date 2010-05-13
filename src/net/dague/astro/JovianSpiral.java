package net.dague.astro;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class JovianSpiral extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("IO", "got past create");
        
        JovianGraphView jgv = new JovianGraphView(this);
        setContentView(jgv);
        jgv.requestFocus();
    
//        SolarSim s = new SolarSim();
//        JovianMoons jv = s.getMoons(System.currentTimeMillis());
//        Log.i("IO", jv.toString());
//        setContentView(R.layout.main);
    }
}