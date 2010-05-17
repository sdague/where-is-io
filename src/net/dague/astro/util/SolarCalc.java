package net.dague.astro.util;

import android.view.View;

public class SolarCalc {
	public SolarSim s;
	public long start;
	public long endhours;
	public View view;
	
	public SolarCalc(SolarSim s, long start, long endhours, View view) {
		this.s = s;
		this.start = start;
		this.endhours = endhours;
		this.view = view;
	}
}
