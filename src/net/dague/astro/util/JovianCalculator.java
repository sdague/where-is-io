package net.dague.astro.util;

import java.util.List;

import android.os.AsyncTask;
import android.view.View;

public class JovianCalculator implements Runnable {

	public boolean inProgress = true; 
	public boolean isInProgress() {
		synchronized(this) {
			return inProgress;
		}
	}

	public void setInProgress(boolean inProgress) {
		synchronized(this) {
			this.inProgress = inProgress;
		}
	}

	final static int incHours = 4;
	long start;
	long endHours;
	View view;
	SolarSim sim;
	
	public JovianCalculator(SolarSim sim, View view, long start, long endhours) 
	{
		this.start = start;
		this.endHours = endhours;
		this.sim = sim;
		this.view = view;
	}
    
    private void calcData() {
    	
    	long endMils = start + TimeUtil.hours2mils(endHours);
    	long incMils = TimeUtil.hours2mils(incHours);
    	
    	for (long s = start; s < endMils; s += incMils) {
    		sim.getMoonPoints(s, incHours, false);
    		// and we've calculated a chunk, so let's redraw
    		view.postInvalidate();
//    		try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
    	}
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		calcData();
	}
}
