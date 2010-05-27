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

package net.dague.astro.jupiter;

import java.util.logging.Handler;

import net.dague.astro.Details;
import net.dague.astro.R;
import net.dague.astro.R.color;
import net.dague.astro.R.drawable;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.AstroConst;
import net.dague.astro.util.Convert;
import net.dague.astro.util.SolarCalc;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.TouchMap;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class JovianSpiralView extends SurfaceView implements SurfaceHolder.Callback {

	public final static int START_HOURS = 12;
	public final static int END_HOURS = 96;
	public final static long TIMESTEP_MINUTES = 60;
	public final static long TIMESTEP_MILS = TIMESTEP_MINUTES * 60 * 1000;
	
	Paint background;
	Paint europa;
	Paint io;
	Paint callisto;
	Paint ganymede;
	Paint jupiter;
	Paint nowline;
	Paint moon;
	
	final float strokeWidth = 3;
	
	private TouchMap map;
	
	private JovianThread thread;
	private JovianCalculator calc;

	public JovianSpiralView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		Log.i("IO","started a new spiral view");
	        // create thread only; it's started in surfaceCreated()

		calc = new JovianCalculator(getContext());
		thread = new JovianThread(holder, getContext(), calc);
		calc.setHandler(thread.getHandler());
		thread.start();
        calc.start();
        
		setFocusable(true);
	}
	
	public static long now(long time)
	{
		return TimeUtil.round2minutes(time, TIMESTEP_MINUTES);
	}

	public boolean onTouchEvent(MotionEvent me) 
	{
		String body = null;
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			
			body = thread.getTouched(me.getX(), me.getY());
		}
		if (body != null) {
			Log.i("IO", "Touched " + body);
			
			Intent detailScreen = new Intent(this.getContext(), Details.class);
			detailScreen.putExtra("net.dague.astro.DetailBody", body);
			this.getContext().startActivity(detailScreen);
		}
		return true;
	}
	
	private int computedY(double value, int width)
	{
		int Y = (int)(value);
		return Y + width / 2;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
		thread.setSurfaceSize(width, height);
		// thread.interrupt();

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		Log.i("IO", "Surface created");

		
        thread.setRunning(true);
        thread.getHandler().sendEmptyMessage(0);

        calc.setRunning(true);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
		thread.setRunning(false);
		calc.setRunning(false);
		
//		thread.getHandler().sendEmptyMessage(1);
//		Log.i("IO", "Surface destroyed!");
//		while (retry) {
//			try {
//				thread.join();
//				calc.join();
//				retry = false;
//			} catch (InterruptedException e) {
//			}
//		
//		}
	}
}
