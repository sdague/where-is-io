package net.dague.astro.util;

import java.util.Vector;

public class JovianPoints {
	private Vector jp;
	
	double max = 0;
	double scale = 2.1;
	
	public JovianPoints()
	{
		jp = new Vector();
	}
	
	public void add(JovianMoons jm)
	{
		maxCheck(jm);

		jp.add(jm);
	}
	
	private void maxCheck(JovianMoons jm)
	{

		if (Math.abs(jm.callisto) > max) 
			max = jm.callisto;
		if (Math.abs(jm.io) > max)
			max = jm.io;
		if (Math.abs(jm.europa) > max)
			max = jm.europa;
		if (Math.abs(jm.ganymede) > max)
			max = jm.ganymede;
	}

	public float[] getIoPoints(int width, int height)
	{
		float[] results = new float[jp.size() * 2];
		
		for (int i = 0; i < jp.size(); i++) {
			JovianMoons j = (JovianMoons) jp.elementAt(i);
			float x = (float) ((j.io / max) * (width / scale) + (width / 2));
			float y = i * height / jp.size();
			results[2*i] = x;
			results[2*i + 1] = y;
		}
		
		return results;
	}
	
	public float[] getEuropaPoints(int width, int height)
	{
		float[] results = new float[jp.size() * 2];
		
		for (int i = 0; i < jp.size(); i++) {
			JovianMoons j = (JovianMoons) jp.elementAt(i);
			float x = (float) ((j.europa / max) * (width / scale) + (width / 2));
			float y = i * height / jp.size();
			results[2*i] = x;
			results[2*i + 1] = y;
		}
		
		return results;
	}
	
	public float[] getCallistoPoints(int width, int height)
	{
		float[] results = new float[jp.size() * 2];
		
		for (int i = 0; i < jp.size(); i++) {
			JovianMoons j = (JovianMoons) jp.elementAt(i);
			float x = (float) ((j.callisto / max) * (width / scale) + (width / 2));
			float y = i * height / jp.size();
			results[2*i] = x;
			results[2*i + 1] = y;
		}
		
		return results;
	}
	
	public float[] getGanymedePoints(int width, int height)
	{
		float[] results = new float[jp.size() * 2];
		
		for (int i = 0; i < jp.size(); i++) {
			JovianMoons j = (JovianMoons) jp.elementAt(i);
			float x = (float) ((j.ganymede / max) * (width / scale) + (width / 2));
			float y = i * height / jp.size();
			results[2*i] = x;
			results[2*i + 1] = y;
		}
		
		return results;
	}

	
}
