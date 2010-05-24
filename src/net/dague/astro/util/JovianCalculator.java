/*
  Copyright 2010 Sean Dague

  This file is part of Where is Io

  Where is Io is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  Where is Io is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with Where is Io.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.dague.astro.util;

import java.util.List;

import net.dague.astro.jupiter.JovianSpiralView;

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
	JovianSpiralView view;
	SolarSim sim;
	
	public JovianCalculator(SolarSim sim, JovianSpiralView view, long start, long endhours) 
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
    		// view.newData();
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
