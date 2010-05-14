package net.dague.astro;

import net.dague.astro.util.JovianPoints;
import net.dague.astro.util.SolarSim;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class JovianGraphView extends View {
	
	Paint background;
	Paint europa;
	Paint io;
	Paint callisto;
	Paint ganymede;
	Paint jupiter;
	
	final float strokeWidth = 4;

	public JovianGraphView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setupPaint();
	}
	
	private void setupPaint()
	{
		background = new Paint();
		background.setColor(getResources().getColor(
		      R.color.jovian_graph_background));
		
		europa = new Paint();
		europa.setColor(getResources().getColor(
				R.color.europa));
		europa.setStrokeCap(Paint.Cap.ROUND);
		europa.setStrokeWidth(strokeWidth);
		europa.setAntiAlias(true);

		io = new Paint();
		io.setColor(getResources().getColor(
				R.color.io));
		io.setStrokeCap(Paint.Cap.ROUND);
		io.setStrokeWidth(strokeWidth);
		io.setAntiAlias(true);
			
		ganymede = new Paint();
		ganymede.setColor(getResources().getColor(
				R.color.ganymede));
		ganymede.setStrokeCap(Paint.Cap.ROUND);
		ganymede.setStrokeWidth(strokeWidth);
		ganymede.setAntiAlias(true);
		
		callisto = new Paint();
		callisto.setColor(getResources().getColor(
				R.color.callisto));
		callisto.setStrokeCap(Paint.Cap.ROUND);
		callisto.setStrokeWidth(strokeWidth);
		callisto.setAntiAlias(true);
		
		jupiter = new Paint();
		jupiter.setColor(getResources().getColor(
					R.color.jupiter));
		jupiter.setStrokeCap(Paint.Cap.ROUND);
		jupiter.setStrokeWidth(10);
	}
	
	
	public void onDraw(Canvas canvas)
	{
		SolarSim s = new SolarSim(this.getContext());
		
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		
		JovianPoints jp = s.getMoonPoints(System.currentTimeMillis(), 96);

		float[] ipoints = jp.getIoPoints(getWidth(), getHeight());
		canvas.drawPoints(ipoints, io);
		
		float[] epoints = jp.getEuropaPoints(getWidth(), getHeight());
		canvas.drawPoints(epoints, europa);
		
		float[] gpoints = jp.getGanymedePoints(getWidth(), getHeight());
		canvas.drawPoints(gpoints, ganymede);
		
		float[] cpoints = jp.getCallistoPoints(getWidth(), getHeight());
		canvas.drawPoints(cpoints, callisto);
		
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2 , getHeight(), jupiter);
	
	}
	
	public int computedY(double value, int width)
	{
		int Y = (int)(value);
		return Y + width / 2;
	}

}
