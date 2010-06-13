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

package net.dague.astro.util;

public class Vector3 {
	public double X;
	public double Y;
	public double Z;
	
	public Vector3()
	{
		this.X = 0.0;
		this.Y = 0.0;
		this.Z = 0.0;
	}
	
	public Vector3(double[] a)
	{
		this.X = a[0];
		this.Y = a[1];
		this.Z = a[2];
	}
	
	public Vector3(double x, double y, double z)
	{
		this.X = x;
		this.Y = y;
		this.Z = z;
	}
	
	public double mag()
	{
		return Math.sqrt(X*X + Y*Y + Z*Z);
	}
	
	public Vector3 add(Vector3 B)
	{
		Vector3 A = this;
		Vector3 C = new Vector3();
		C.X = A.X + B.X;
		C.Y = A.Y + B.Y;
		C.Z = A.Z + B.Z;
		return C;
	}
	
	public Vector3 sub(Vector3 B)
	{
		Vector3 A = this;
		Vector3 C = new Vector3();
		C.X = A.X - B.X;
		C.Y = A.Y - B.Y;
		C.Z = A.Z - B.Z;
		return C;
	}
	
	
	public Vector3 unitv()
	{
		Vector3 U = new Vector3();
		double mag = this.mag();
		U.X = X / Math.sqrt(mag);
		U.Y = Y / Math.sqrt(mag);
		U.Z = Z / Math.sqrt(mag);
		return U;
	}
	
	public Vector3 cross(Vector3 B)
	{
		Vector3 A = this;
		Vector3 C = new Vector3();
		C.X = A.Y * B.Z - A.Z * B.Y;
		C.Y = A.Z * B.X - A.X * B.Z;
		C.Z = A.X * B.Y - A.Y * B.X;
		
		return C;
	}
	
	public double dot(Vector3 B)
	{
		double prod = 0.0;
		Vector3 A = this;
		prod = A.X * B.X + A.Y * B.Y + A.Z * B.Z;
		return prod;
	}
	
	public boolean isZero()
	{
		return (X == 0.0 && Y == 0.0 && Z == 0.0);
	}
	
	public String toString()
	{
		return "[" + X + "," + Y + "," + Z + "]";
	}
}
