/*
rahmant3
Utility class used throughout the project

github: https://github.com/rahmant3/LOOP

References:
numberphile:
  http://youtube.com/numberphile
LOOP's official website:
  http://www.loop-the-game.com/
  
Closest point to an ellipse algorithm by user Spektre: 
  http://stackoverflow.com/questions/36260793/algorithm-for-shortest-distance-from-a-point-to-an-elliptic-arc
Simple ball to ball collision, NCSA at University of Illinois:
  http://archive.ncsa.illinois.edu/Classes/MATH198/townsend/math.html

*/

public class Vector2D {
	double x;
	double y;
	
	public Vector2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void add(Vector2D v) {
		this.x = x + v.getX();
		this.y = y + v.getY();
	}
	
	public void mult(double n) {
		this.x = x * n;
		this.y = y * n;
	}

  public void div(double n) {
    this.x = x / n;
    this.y = y / n;
  }
	
	public void sub(Vector2D v) {
		this.x = x - v.getX();
		this.y = y - v.getY();
	}

  public void sub(double x_t, double y_t) {
    this.x = x - x_t;
    this.y = y - y_t;
  }
	
	public double mag() {
		return Math.sqrt(this.x * this.x + this.y * this.y);
	}
	
	public double dot(Vector2D v) {
		return (this.x * v.getX() + this.y * v.getY());
	}

  public Vector2D unit() {    
    Vector2D result = this.copy();
    
    if (result.mag() != 0) {
      result.div(result.mag());
    }
    return result;
  }
  
  public Vector2D copy() {
    return new Vector2D(this.x, this.y);
  }

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}


}