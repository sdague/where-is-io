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

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class Details extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		setDetails();
	}
	
	private void setDetails() {
		Bundle extras = getIntent().getExtras();
		String body = extras.getString("net.dague.astro.DetailBody");
		setTitle(body);
		
		TextView t = (TextView)findViewById(R.id.details_content);
		ImageView i = (ImageView)findViewById(R.id.details_image);

		if (body.equals("Jupiter")) {
			t.setText(R.string.jupiter_details);
			i.setImageResource(R.drawable.jupiter_128x128);
		} else if (body.equals("Callisto")) {
			t.setText(R.string.callisto_details);
			i.setImageResource(R.drawable.callisto_128x128);
			setTitleColor(getResources().getColor(
					R.color.callisto));
		} else if (body.equals("Europa")) {
			t.setText(R.string.europa_details);
			i.setImageResource(R.drawable.europa_128x128);
			setTitleColor(getResources().getColor(
					R.color.europa));
		} else if (body.equals("Ganymede")) {
			t.setText(R.string.ganymede_details);
			i.setImageResource(R.drawable.ganymede_128x128);
			setTitleColor(getResources().getColor(
					R.color.ganymede));
		} else if (body.equals("Io")) {
			t.setText(R.string.io_details);
			i.setImageResource(R.drawable.io_128x128);
			setTitleColor(getResources().getColor(
					R.color.io));
		}
	}
}
