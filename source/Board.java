/*
rahmant3
Elliptical board, handles collision detection

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

public class Board {
	private int height;
	private int width;

  private double c_x;
  private double c_y;
  
  public double[] foci; 
  
	public Board(int width, int height) {
		this.height = height;
		this.width = width;

    this.c_x = 0;
    this.c_y = 0;
    
    foci = new double[2];
    foci[0] = this.c_x - foci_dist();
    foci[1] = this.c_x + foci_dist();
	}

  public double foci_dist() {
    return Math.sqrt((this.width/2) * (this.width/2) - (this.height/2) * (this.height/2));
  }

  public boolean collides(Ball b) {
    double r = b.radius;
    double xPos = b.pos.x;
    double yPos = b.pos.y;
  
    Vector2D closest = ellarc_closest_point(xPos, yPos, 0, 2*Math.PI);
    Vector2D temp = closest.copy();
    
    temp.sub(xPos, yPos);
    
    return (temp.mag() <= r);
    
  }
  
  //reference: http://stackoverflow.com/questions/36260793/algorithm-for-shortest-distance-from-a-point-to-an-elliptic-arc
  public Vector2D ellarc_closest_point(double x_in,double y_in, double a0, double a1)
    {
    int e,i;
    double ll,l,aa,a,da,x,y,b0,b1;
    while (a0>=a1) a0-= 2 * Math.PI;                 // just make sure a0<a1
    b0=a0; b1=a1; da=(b1-b0)/10000.0;          // 25 sample points in first iteration
    ll=-1; aa=a0;                           // no best solution yet
    for (i=0;i<3;i++)                       // recursions more means more accurate result
        {
        // sample arc a=<b0,b1> with step da
        for (e=1,a=b0; e != 0;a+=da)
            {
            if (a>=b1) { a=b1; e=0; }
            // elliptic arc sampled point
            x=c_x + width/2 * Math.cos(a);
            y=c_y - height/2 * Math.sin(a);                 // y axis is in reverse order
            // distance^2 to x_in,y_in
            x-=x_in; x*=x;
            y-=y_in; y*=y; l=x+y;
            // remember best solution
            if ((ll<0.0)||(ll>l)) { aa=a; ll=l; }
            }
        // use just area near found solution aa
        b0=aa-da; if (b0<a0) b0=a0;
        b1=aa+da; if (b1>a1) b1=a1;
        // 10 points per area stop if too small area already
        da=0.1*(b1-b0); if (da<1e-6) break;
        }
    double x_out = c_x + width/2 * Math.cos(aa);
    double y_out = c_y - height/2 * Math.sin(aa);    // y axis is in reverse order
    
    return new Vector2D(x_out, y_out);
    }
    

//reference: https://math.stackexchange.com/questions/13261/how-to-get-a-reflection-vector
//https://www.gamedev.net/topic/304720-calculating-the-perpendicular-bisector-of-a-line/
	public void resolveCollision(Ball b) {
  
		double r = b.getRadius();
		double xPos = b.getPos().getX();
		double yPos = b.getPos().getY();
		Vector2D vel = b.getVel();

    Vector2D closest = ellarc_closest_point(xPos, yPos, 0, 2*Math.PI);
    Vector2D closest2 = ellarc_closest_point(xPos + 0.5, yPos + 0.5, 0, 2*Math.PI);
    
    closest2.sub(closest);
    closest2 = closest2.unit();
 
    Vector2D n = new Vector2D(-1.0 * closest2.y, closest2.x);

    n = n.unit();
    
    double velDOTn = vel.dot(n);
    Vector2D result = vel.copy();
    
    n.mult(velDOTn);
    n.mult(2.0);
    
    result.sub(n);
    
    
    //To prevent multiple collisions with the board
    //Logic: if this new velocity would cause the ball to be further away from the center of the board, don't update the velocity
    Vector2D mag1 = closest.copy();
    mag1.sub(c_x, c_y);
    
    Vector2D mag2 = b.pos.copy();
    mag2.sub(c_x, c_y);
    
    Vector2D mag3 = b.pos.copy();
    mag3.add(result);
    mag3.sub(c_x, c_y);
    
    if ((mag1.mag() > mag2.mag() && mag3.mag() < mag2.mag()) || 
         mag1.mag() < mag2.mag() && mag3.mag() > mag2.mag()) {
    
      b.setVel(result);
    }
	}

  public int getHeight() {
    return height;
  }
  public int getWidth() {
    return width;
  }
	
}