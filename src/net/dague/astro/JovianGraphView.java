package net.dague.astro;

import net.dague.astro.util.AstroConst;
import net.dague.astro.util.Convert;
import net.dague.astro.util.JovianCalculator;
import net.dague.astro.util.JovianMoons;
import net.dague.astro.util.JovianPoints;
import net.dague.astro.util.SolarCalc;
import net.dague.astro.util.SolarSim;
import net.dague.astro.util.TimeUtil;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class JovianGraphView extends View {
	
	private final static int START_HOURS = 12;
	private final static int END_HOURS = 96;
	
	Paint background;
	Paint europa;
	Paint io;
	Paint callisto;
	Paint ganymede;
	Paint jupiter;
	Paint nowline;
	Paint moon;
	
	final float strokeWidth = 3;

	public JovianGraphView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setupPaint();
	}
	
	public static long startTime()
	{
		return System.currentTimeMillis() - TimeUtil.hours2mils(START_HOURS);
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
		jupiter.setStrokeWidth(scale(10));
		
		nowline = new Paint();
		nowline.setColor(getResources().getColor(
					R.color.nowline));
		nowline.setStrokeCap(Paint.Cap.ROUND);
		nowline.setStrokeWidth(1);
		
		moon = new Paint();
		moon.setColor(getResources().getColor(
					R.color.moon));
		moon.setStrokeCap(Paint.Cap.ROUND);
		moon.setAntiAlias(true);
		moon.setStrokeWidth(1);
	}
	
	private float scale(float in) {
		float ratio = (float)getWidth() / (float)getHeight();
		if (ratio > 1) {
			return in * ratio * ratio;
		} else {
			return in;
		}
	}
	
	private int scale(int in) {
		float ratio = (float)getWidth() / (float)getHeight();
		if (ratio > 1) {
			return (int)(in * ratio * ratio);
		} else {
			return in;
		}
	}
	
	
	private int end_hours()
	{
		float ratio = (float)getHeight() / (float)getWidth();
		if (ratio < 1) {
			return (int)(END_HOURS * ratio * ratio);
		} else {
			return END_HOURS;
		}
	}
	
	private void drawBackground(Canvas canvas)
	{
		
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
	}
	
	private float au2x(float auWidth, float au)
	{
		return (getWidth() / 2) * (au / (auWidth / 2))  + (getWidth() / 2);
	}
	
	private void drawJupiter(Canvas canvas, float auWidth)
	{
		// calculate the jupiter stroke size.  This is actually twice what it really is, but 
		// it looks better that way on screen
		float stroke = AstroConst.JUPITER_DIAMETER / Convert.au2km(auWidth) * getWidth() * 2;
		
		jupiter.setStrokeWidth((int)stroke);
		canvas.drawLine(getWidth() / 2, 0, getWidth() / 2 , getHeight(), jupiter);
	}
	
	private void drawJupiterDisc(Canvas canvas, float auWidth)
	{
		float delta = AstroConst.JUPITER_DIAMETER / Convert.au2km(auWidth) * getWidth();
		RectF jrect = new RectF((getWidth() / 2) - delta, nowPos() - delta, (getWidth() / 2) + delta, nowPos() + delta );
		BitmapDrawable d = (BitmapDrawable)getResources().getDrawable(R.drawable.jupiter);
		canvas.drawBitmap(d.getBitmap(), null, jrect, background);
	}
	
	private void drawMoonTracks(Canvas canvas, SolarSim s)
	{
		JovianPoints jp = s.getMoonPoints(startTime() , end_hours(), true);
		
		if (jp.percent == 0)
			return;
		
		float[] ipoints = jp.getMoonLines(JovianMoons.IO, getWidth(), getHeight());
		canvas.drawLines(ipoints, io);
		
		float[] epoints = jp.getMoonLines(JovianMoons.EUROPA, getWidth(), getHeight());
		canvas.drawLines(epoints, europa);
		
		float[] gpoints = jp.getMoonLines(JovianMoons.GANYMEDE, getWidth(), getHeight());
		canvas.drawLines(gpoints, ganymede);
		
		float[] cpoints = jp.getMoonLines(JovianMoons.CALLISTO, getWidth(), getHeight());
		canvas.drawLines(cpoints, callisto);
	}
	
	private float nowPos()
	{
		return scale((float) START_HOURS / (float)END_HOURS * getHeight()); 
	}
	
	private void drawMoons(Canvas canvas, SolarSim s)
	{
		JovianMoons jm = s.getMoonsAt(System.currentTimeMillis());
		float width = JovianPoints.screenWidth();
		
		for (int i = 0; i < 4; i++) {
			float moonpos = (float) jm.get(i);
			float x = au2x(width, moonpos);
			canvas.drawCircle(x, nowPos(), 3, moon);
		}		
	}
	
	private void drawNowLine(Canvas canvas)
	{
		canvas.drawLine(0, nowPos(), getWidth(), nowPos(), nowline);
	}
	
	public void onDraw(Canvas canvas)
	{
		SolarSim s = new SolarSim(this.getContext());
		drawBackground(canvas);
		drawJupiter(canvas, JovianPoints.screenWidth());
		drawMoonTracks(canvas, s);
		drawNowLine(canvas);
		drawMoons(canvas, s);
		drawJupiterDisc(canvas, JovianPoints.screenWidth());
	}
	
	public int computedY(double value, int width)
	{
		int Y = (int)(value);
		return Y + width / 2;
	}

}
