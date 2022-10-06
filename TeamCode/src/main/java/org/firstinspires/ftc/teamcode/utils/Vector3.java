package org.firstinspires.ftc.teamcode.utils;

public class Vector3 extends Vector2{

	public double z;

	public Vector3() {
		super();
		this.z = 0.0;
	}

	public Vector3(double x, double y, double z){
		super(x, y);
		this.z = z;
	}

	@Override
	public void normalize() {
		double length = Math.sqrt(x*x + y*y + z*z);

		if (length != 0.0) {
			double s = 1.0 / (double)length;
			x = x*s;
			y = y*s;
			z = z*s;
		}
	}

	@Override
	public void add(Vector3 other) {
		x += other.x;
		y += other.y;
		z += other.z;
	}
	@Override
	public void multiply(Vector3 other){
		x *= other.x;
		y *= other.y;
		z *= other.z;
	}
	@Override
	public void subtract(Vector3 other){
		x -= other.x;
		y -= other.y;
		z -= other.z;
	}
	@Override
	public void divide(Vector3 other){
		x /= other.x;
		y /= other.y;
		z /= other.z;
	}
	
	public String toString() {
		return "x: " + x + ", y: " + y  + ", z: " + z;
	}
}
