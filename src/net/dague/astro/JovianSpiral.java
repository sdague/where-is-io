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

package net.dague.astro;

import net.dague.astro.data.SimData;
import net.dague.astro.util.JovianCalculator;
import net.dague.astro.util.JovianPoints;
import net.dague.astro.util.SolarCalc;
import net.dague.astro.util.SolarSim;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class JovianSpiral extends Activity implements OnClickListener {
	
	JovianGraphView jgv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        jgv = new JovianGraphView(this);

    	setContentView(jgv);
    	
    	precalcData();
        
        jgv.requestFocus();
    
    }
    
	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	private void precalcData() {
		SolarSim sim = new SolarSim(this);
		new Thread(new JovianCalculator(sim, jgv, JovianGraphView.startTime(), 120)).start();
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, About.class));
		    return true;
		case R.id.delete:
			deleteCache();
			return true;
		}
		
		return false;
	}

	private void deleteCache()
	{
		SimData d = new SimData(this);
		d.deleteAll();
		precalcData();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
	   super.onCreateOptionsMenu(menu);
	   MenuInflater inflater = getMenuInflater();
	   inflater.inflate(R.menu.menu, menu);
	   return true;
	}

    
//    protected void onSaveInstanceState(Bundle  outState) {
//    	if (this.jc != null && this.jc.getStatus() != AsyncTask.Status.FINISHED) {
//     		outState.putBoolean("inProgress", true);
//    	}
//    }
}