package net.dague.astro.jupiter;

import net.dague.astro.R;
import net.dague.astro.sim.SolarSim;
import net.dague.astro.util.AstroConst;
import net.dague.astro.util.Convert;
import net.dague.astro.util.TimeUtil;
import net.dague.astro.util.TouchMap;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

public class JovianThread extends Thread {
	
	private boolean running = false;
	// the height and width of the canvas
	private int height;
	private int width;
	
	private SurfaceHolder surfaceHolder;
	private Handler handler;
	private Context context;
	
	// All the different paints
	Paint backgroundPaint;
	Paint europa;
	Paint io;
	Paint callisto;
	Paint ganymede;
	Paint jupiter;
	Paint nowline;
	Paint moon;
	Paint marker;
	
	Bitmap jupiterBitmap;
	SolarSim sim;
	
	float strokeWidth = 2.5f;
	
	TouchMap map;
	
	// stuff for animations
	long lastFrameTime;
	final long frameDuration = 33;
	
	final int START = 0;
	final int RUNNING = 1;
	final int DONE = 2;
	
	
	final int stepSize = 1;
	int progress;
	
	int state;
	private JovianCalculator calc;
	private Paint timeLine;
	private Paint timeText;
	long now;
	
    public JovianThread(SurfaceHolder surfaceHolder, Context context, JovianCalculator calc) {
//            Handler handler) {
        // get handles to some important objects
        this.surfaceHolder = surfaceHolder;
        //this.handler = handler;
        this.context = context;
        this.calc = calc;
        
        handler = new Handler() {
        	public void handleMessage(Message m) {
        		this.removeMessages(0);
        		if (running) {
        			Log.i("IO","About to draw");
        			draw();
        			if (state == DONE) {
        				sendEmptyMessageDelayed(0, 30000);
        			} else {
        				sendEmptyMessageDelayed(0, sleepTime());
        			}
        		}
        }};
        
        // bogus size, this gets reset when we draw
        map = new TouchMap(1);
        
		state = START;
		progress = stepSize;
		now = System.currentTimeMillis();
		lastFrameTime = now;
		setupPaint();
    }
    
	private void setupPaint()
	{
		backgroundPaint = new Paint();
		backgroundPaint.setColor(context.getResources().getColor(
		      R.color.jovian_graph_background));
		
		europa = new Paint();
		europa.setColor(context.getResources().getColor(
				R.color.europa));
		europa.setStrokeCap(Paint.Cap.ROUND);
		
		europa.setStrokeWidth(strokeWidth);
		europa.setAntiAlias(true);

		io = new Paint();
		io.setColor(context.getResources().getColor(
				R.color.io));
		io.setStrokeCap(Paint.Cap.ROUND);
		io.setStrokeWidth(strokeWidth);
		io.setAntiAlias(true);
			
		ganymede = new Paint();
		ganymede.setColor(context.getResources().getColor(
				R.color.ganymede));
		ganymede.setStrokeCap(Paint.Cap.ROUND);
		ganymede.setStrokeWidth(strokeWidth);
		ganymede.setAntiAlias(true);
		
		callisto = new Paint();
		callisto.setColor(context.getResources().getColor(
				R.color.callisto));
		callisto.setStrokeCap(Paint.Cap.ROUND);
		callisto.setStrokeWidth(strokeWidth);
		callisto.setAntiAlias(true);
		
		jupiter = new Paint();
		jupiter.setColor(context.getResources().getColor(
					R.color.jupiter));
		jupiter.setStrokeCap(Paint.Cap.ROUND);
		jupiter.setStrokeWidth(scale(10));
		
		nowline = new Paint();
		nowline.setColor(context.getResources().getColor(
					R.color.nowline));
		nowline.setStrokeCap(Paint.Cap.ROUND);
		nowline.setStrokeWidth(1);
		
		timeLine = new Paint();
		timeLine.setColor(context.getResources().getColor(
					R.color.timeline));
		timeLine.setStrokeWidth(0);
		
		timeText = new Paint();
		timeText.setColor(context.getResources().getColor(
				R.color.moon));
		timeText.setAntiAlias(true);
		timeText.setTypeface(Typeface.SANS_SERIF);
		timeText.setTextSize(10);

		marker = new Paint();
		marker.setColor(0xffff00ff);
		marker.setAntiAlias(true);
		marker.setStrokeWidth(4);
		
		moon = new Paint();
		moon.setColor(context.getResources().getColor(
					R.color.moon));
		// moon.setStrokeCap(Paint.Cap.ROUND);
		moon.setAntiAlias(true);
		// moon.setStrokeWidth(1);
		moon.setStyle(Paint.Style.FILL);
		
		BitmapDrawable d = (BitmapDrawable)context.getResources().getDrawable(R.drawable.jupiter);
		jupiterBitmap = d.getBitmap();
	}
	
	public Handler getHandler()
	{
		return handler;
	}

	public void setRunning(boolean b) {
        running = b;
    }
    
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (surfaceHolder) {
            this.width = width;
            this.height = height;
        }
    }
    
    public void run() {
    	Looper.prepare();
    	handler.sendEmptyMessage(0);
    	Looper.loop();
    }
    
    private long sleepTime()
    {
    	long delta = frameDuration - (now - lastFrameTime );
    	if (delta < 1) {
    		return 1;
    	}
    	return delta;
    }
    
	private void drawMoons(Canvas canvas)
	{
		float moonpos;
		float x;
		
		JovianMoons jm = calc.getMoonsAtInterpolate(now);
		JovianMoons jstart = calc.getMoonsAt(now);
		JovianMoons jnext = calc.getMoonsNext(now);
		
		float width = JovianMoonSet.screenWidth();
		
		/*
		 * This is frustrating.  If I actually do the interpolation, the circles 
		 * don't show up visibly in the same place.  So there is lots of fudging here
		 */
		
		for (int i = 0; i < 4; i++) {
			moonpos = (float) jstart.get(i);
			x = (float) au2xpos(moonpos);
			canvas.drawCircle(x, nowPos(), 3, moon);
			// canvas.drawPoint(x, nowPos(), marker);
			
//			moonpos = (float) jnext.get(i);
//			x = (float) au2xpos(moonpos);
//			canvas.drawCircle(x, time2y(TimeUtil.JD2mils(jnext.jd)), 3, marker);
//	
//			moonpos = (float) jm.get(i);
//			x = (float) au2xpos(moonpos);
//			canvas.drawPoint(x, nowPos(), marker);
			
		}		
	}
	
	private void drawNowLine(Canvas canvas)
	{
		canvas.drawLine(0, nowPos(), width, nowPos(), nowline);
	}
	
	private void drawBackground(Canvas canvas)
	{
		int hourInc = 12;
		canvas.drawRect(0, 0, width, height, backgroundPaint);
		
		for (int hours = hourInc; hours < (end_hours() - hourInc); hours += hourInc ) {
			float y = time2y(now + TimeUtil.hours2mils(hours));
			float x = width - 40;
			String text = "+" + hours + "h";

			canvas.drawLine(0, y, width, y, timeLine);
			Rect r = new Rect();
			moon.getTextBounds(text, 0, text.length(), r);
			canvas.drawText(text, width - 30, y - r.centerY() - 1, timeText);
		}
	}
	
	private float time2y(long mils)
	{
		long zero = startTime();
		long total = totalMils();
		
		float pos = (mils - zero) * height / total;
		
		if(pos < 0) 
			pos = 0;
		
		return pos;
	}
	
	private double au2x(double au)
	{
		double auWidth = JovianMoonSet.screenWidth();
		double fraction = au / (auWidth / 2);
		double x = fraction * (width / 2);
		return x;
	}
	
	private double au2xpos(float au)
	{
		return au2x(au) + (width / 2);
	}
	
	private void drawJupiter(Canvas canvas)
	{
		// calculate the jupiter stroke size.  This is actually twice what it really is, but 
		// it looks better that way on screen
		double stroke = au2x(AstroConst.JUPITER_DIAMETER_AU) * 2;
		
		jupiter.setStrokeWidth((int)stroke);
		canvas.drawLine(width / 2, 0, width / 2 , height, jupiter);
	}
	
	private void drawJupiterDisc(Canvas canvas)
	{
		float delta = (float)au2x(AstroConst.JUPITER_DIAMETER_AU);
		RectF jrect = new RectF((width / 2) - delta, nowPos() - delta, (width / 2) + delta, nowPos() + delta );
		canvas.drawBitmap(jupiterBitmap, null, jrect, backgroundPaint);
	}
	
	// Get the moon tracks out of the calculation thread 
	
	private void drawMoonTracks(Canvas canvas, int hours)
	{
		JovianMoonSet jp = calc.getMoonPoints(startTime(), hours);
		
		if (jp.percent == 0)
			return;
		
		if (hours >= end_hours()  && jp.percent > 99) 
			state = DONE;
		
		int drawHeight = height * hours / end_hours();
		
		float[] ipoints = jp.getMoonLines(JovianMoons.IO, width, drawHeight);

		canvas.drawLines(ipoints, io);
		addTouchPoints(ipoints, "Io");
		
		float[] epoints = jp.getMoonLines(JovianMoons.EUROPA, width, drawHeight);
		canvas.drawLines(epoints, europa);
		addTouchPoints(epoints, "Europa");

		float[] gpoints = jp.getMoonLines(JovianMoons.GANYMEDE, width, drawHeight);
		canvas.drawLines(gpoints, ganymede);
		addTouchPoints(gpoints, "Ganymede");
	
		float[] cpoints = jp.getMoonLines(JovianMoons.CALLISTO, width, drawHeight);
		canvas.drawLines(cpoints, callisto);
		addTouchPoints(cpoints, "Callisto");
	}
	
	private void addTouchPoints(float[] points, String value)
	{
		int x = 0;
		int y = 1;
		for (; x < points.length; x += 4, y += 4) {
			map.addPoint((int)points[x], (int)points[y], value);
		}
	}
	
	private float nowPos()
	{
		return time2y(now);
	}
	
	public long startTime()
	{
		return now - TimeUtil.hours2mils(JovianSpiralView.START_HOURS);
	}
	
	public long totalMils()
	{
		int end = JovianSpiralView.END_HOURS;
		float ratio = (float)height / (float)width;
		if (ratio < 1) {
			end = (int)(JovianSpiralView.END_HOURS * ratio * ratio);
		}
		return TimeUtil.hours2mils(end);
	}
	

	
	private float scale(float in) {
		float ratio = (float)width / (float)height;
		if (ratio > 1) {
			return in * ratio * ratio;
		} else {
			return in;
		}
	}
	
	private int scale(int in) {
		float ratio = (float)width / (float)height;
		if (ratio > 1) {
			return (int)(in * ratio * ratio);
		} else {
			return in;
		}
	}
	
	
	private int end_hours()
	{
		float ratio = (float)height / (float)width;
		if (ratio < 1) {
			return (int)(JovianSpiralView.END_HOURS * ratio * ratio);
		} else {
			return JovianSpiralView.END_HOURS;
		}
	}
	

	private void draw() {
		Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            now = System.currentTimeMillis();
            synchronized (surfaceHolder) {
            	
            	
            	map.resetWidth(width);
            	drawBackground(canvas);

            	drawJupiter(canvas);
            	if (state == RUNNING) {
            		drawMoonTracks(canvas, progress);
            		incrementProgress();
            	} else if (state == DONE) {
            		drawMoonTracks(canvas, end_hours());
            	} else {
            		state = RUNNING;
            	}
            	drawNowLine(canvas);
            	drawMoons(canvas);
            	drawJupiterDisc(canvas);
            }

        } finally {
            // do this in a finally so that if an exception is thrown
            // during the above, we don't leave the Surface in an
            // inconsistent state
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
	}
	
	private void incrementProgress() {
		if (progress < end_hours()) {
			progress += stepSize;
		}
		if (progress > end_hours()) {
			progress = end_hours();
			state = DONE;
		}
		
	}

	public String getTouched(float x, float y) {
		// TODO Auto-generated method stub
		String body;
		int jupiterWidth = (int)jupiter.getStrokeWidth() / 2;
		int minX = (width / 2) - jupiterWidth - 10;
		int maxX = (width / 2) + jupiterWidth + 10;
		
		if (x > minX && x < maxX ) {
			body = "Jupiter";
		} else {
			body = map.getPoint((int)x, (int)y);
		}
		return body;
	}
}
