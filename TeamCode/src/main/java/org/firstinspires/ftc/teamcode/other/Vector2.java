package org.firstinspires.ftc.teamcode.other;

//Stores 2 values that you can do vector operations on.
public class Vector2 {
	// Members
	public double x;
	public double y;

	// Constructors
	public Vector2() {
		this.x = 0.0;
		this.y = 0.0;
	}

	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double distance(Vector2 other) {
		double v0 = other.x - x;
		double v1 = other.y - y;
		return Math.sqrt(v0*v0 + v1*v1);
	}

	public void normalize() {
		// sets length to 1
		double length = Math.sqrt(x*x + y*y);

		if (length != 0.0) {
			double s = 1.0 / (double)length;
			x = x*s;
			y = y*s;
		}
	}

	public void add(Vector2 other){
		x += other.x;
		y += other.y;
	}
	public void add(Vector3 other) {
		x += other.x;
		y += other.y;
	}

	public void multiply(Vector2 other){
		x *= other.x;
		y *= other.y;
	}
	public void multiply(Vector3 other){
		x *= other.x;
		y *= other.y;
	}

	public void subtract(Vector2 other){
		x -= other.x;
		y -= other.y;
	}
	public void subtract(Vector3 other){
		x -= other.x;
		y -= other.y;
	}

	public void divide(Vector2 other){
		x /= other.x;
		y /= other.y;
	}
	public void divide(Vector3 other){
		x /= other.x;
		y /= other.y;
	}

	// Compare two vectors
	public boolean equals(Vector2 other) {
		return (this.x == other.x && this.y == other.y);
	}

	public String toString(){
		return "x: " + x + ", y: " + y;
	}
}
