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

import net.dague.astro.data.JupiterData;
import net.dague.astro.jupiter.JovianCalculator;
import net.dague.astro.jupiter.JovianMoonSet;
import net.dague.astro.jupiter.JovianSpiralView;
import net.dague.astro.jupiter.JovianThread;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.SolarCalc;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
	
	static final int DIALOG_LOADING = 1;
	
	JovianSpiralView jgv;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.spiral);
        
        // jgv = (JovianSpiralView)findViewById(R.id.jovian_spiral);

        jgv = new JovianSpiralView(this);

    	setContentView(jgv);
    	
        jgv.requestFocus();
    }
    
    protected Dialog onCreateDialog(int id) {
        ProgressDialog dialog;
        switch(id) {
        case DIALOG_LOADING:
        	dialog = new ProgressDialog(this);
        	dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        	dialog.setMessage("Loading...");
        	dialog.setCancelable(false);
            // do the work to define the pause Dialog
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			startActivity(new Intent(this, About.class));
		    return true;
		}
		
		return false;
	}

	private void deleteCache()
	{
		JupiterData d = new JupiterData(this);
		d.deleteAll();
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