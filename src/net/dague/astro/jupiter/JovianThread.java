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
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

public class JovianThread extends Thread {
	
	private boolean running;
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
	Bitmap jupiterBitmap;
	SolarSim sim;
	
	float strokeWidth = 3;
	
	TouchMap map;
	
	// stuff for animations
	long lastFrameTime;
	final long frameDuration = 100;
	
	final int START = 0;
	final int RUNNING = 1;
	final int DONE = 2;
	
	
	final int stepSize = 4;
	int progress;
	
	int state;
	private JovianCalculator calc;
	
    public JovianThread(SurfaceHolder surfaceHolder, Context context, JovianCalculator calc) {
//            Handler handler) {
        // get handles to some important objects
        this.surfaceHolder = surfaceHolder;
        //this.handler = handler;
        this.context = context;
        this.calc = calc;

		state = START;
		progress = stepSize;
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
		
		moon = new Paint();
		moon.setColor(context.getResources().getColor(
					R.color.moon));
		moon.setStrokeCap(Paint.Cap.ROUND);
		moon.setAntiAlias(true);
		moon.setStrokeWidth(1);
		
		BitmapDrawable d = (BitmapDrawable)context.getResources().getDrawable(R.drawable.jupiter);
		jupiterBitmap = d.getBitmap();
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
        while (running) {
            Canvas c = null;
            try {
                c = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    draw(c);
                }

			} finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    surfaceHolder.unlockCanvasAndPost(c);
                }
            }
			try {
//				if (state == DONE) {
//					sleep(120000);
//				} else {
					sleep (120000); // sleepTime());
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.i("IO", "thread sleep interupted... I don't whink we care");
			}
        }
    }
    
    private long sleepTime()
    {
    	long now = System.currentTimeMillis();
    	long delta = frameDuration - (now - lastFrameTime );
    	while(delta < 1) {
    		delta += frameDuration;
    		incrementProgress();
    	}
    	return delta;
    }
    
	private void drawMoons(Canvas canvas)
	{
		JovianMoons jm = calc.getMoonsAt(System.currentTimeMillis());
		float width = JovianMoonSet.screenWidth();
		
		for (int i = 0; i < 4; i++) {
			float moonpos = (float) jm.get(i);
			float x = au2x(width, moonpos);
			canvas.drawCircle(x, nowPos(), 3, moon);
		}		
	}
	
	private void drawNowLine(Canvas canvas)
	{
		canvas.drawLine(0, nowPos(), width, nowPos(), nowline);
	}
	
	private void drawBackground(Canvas canvas)
	{
		
		canvas.drawRect(0, 0, width, height, backgroundPaint);
	}
	
	private float au2x(float auWidth, float au)
	{
		return (width / 2) * (au / (auWidth / 2))  + (width / 2);
	}
	
	private void drawJupiter(Canvas canvas, float auWidth)
	{
		// calculate the jupiter stroke size.  This is actually twice what it really is, but 
		// it looks better that way on screen
		float stroke = AstroConst.JUPITER_DIAMETER / Convert.au2km(auWidth) * width * 2;
		
		jupiter.setStrokeWidth((int)stroke);
		canvas.drawLine(width / 2, 0, width / 2 , height, jupiter);
	}
	
	private void drawJupiterDisc(Canvas canvas, float auWidth)
	{
		float delta = AstroConst.JUPITER_DIAMETER / Convert.au2km(auWidth) * width;
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
		return scale((float) JovianSpiralView.START_HOURS / (float)JovianSpiralView.END_HOURS * height); 
	}
	
	public static long startTime()
	{
		return System.currentTimeMillis() - TimeUtil.hours2mils(JovianSpiralView.START_HOURS);
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
	

	private void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		map = new TouchMap(width);
		Log.i("IO","drew background");
		drawBackground(canvas);
		Log.i("IO", "draw jupiter");
		drawJupiter(canvas, JovianMoonSet.screenWidth());
//		if (state == START) {
//			state = RUNNING;
//			lastFrameTime = System.currentTimeMillis();
//		} else {
		Log.i("IO", "draw moon tracks");
			drawMoonTracks(canvas, end_hours());
			Log.i("IO", "draw now line");
			drawNowLine(canvas);
			Log.i("IO", "draw moons");
			drawMoons(canvas);
//			incrementProgress();
//		}
			Log.i("IO", "draw disc");
		drawJupiterDisc(canvas, JovianMoonSet.screenWidth());
		Log.i("IO", "drawing done");
	}
	
	private void incrementProgress() {
		if (progress < end_hours()) {
			progress += stepSize;
		}
		if (progress > end_hours()) {
			progress = end_hours();
		}
		
	}

	public String getTouched(float x, float y) {
		// TODO Auto-generated method stub
		String body;
		int jupiterWidth = (int)jupiter.getStrokeWidth() / 2;
		int minX = (width / 2) - jupiterWidth;
		int maxX = (width / 2) + jupiterWidth;
		
		if (x > minX && y < maxX ) {
			body = "Jupiter";
		} else {
			body = map.getPoint((int)x, (int)y);
		}
		return body;
	}
}
