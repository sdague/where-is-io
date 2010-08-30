package net.dague.astro.views;

import net.dague.astro.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class Diarama extends View {

	
	public Diarama(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Diarama(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Diarama(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	protected void onDraw(Canvas c) {
		Paint p = new Paint();
		p.setColor(this.getContext().getResources().getColor(R.color.io));
		p.setStrokeCap(Paint.Cap.BUTT);
		p.setStrokeWidth(4);
		p.setAntiAlias(true);
		int h = this.getHeight();
		int w = this.getWidth();
		Log.i("DIARAMA", "Height: " + h + ", Width: " + w);
		c.drawCircle(w/2, h/2, h/4, p);
	}

}
