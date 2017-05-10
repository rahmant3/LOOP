/*
rahmant3
Instance of billiard ball, handles collision detection

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

public class Ball {

  Vector2D pos;
  Vector2D vel;
  int hue;
  double radius;
  String name;
  
  double TERMINAL = 10;
  double FRICTION = -0.005;
  double MIN_SPEED = 0.2;

  public Ball (double xPos, double yPos, int hue, double radius) {
    pos = new Vector2D(xPos, yPos);
    this.hue = hue;
    vel = new Vector2D(0, 0);
    this.radius = radius;
  }

  public void update() {
    Vector2D frictional_force = vel.copy();
    frictional_force.mult(FRICTION);
    if (MIN_SPEED > vel.mag()) {
      vel.x = 0;
      vel.y = 0;
    } else {
      vel.add(frictional_force);
    }
    
    pos.add(vel);
  }
  
  public void applyForce(Vector2D force) {
    if (force.mag() > TERMINAL) { 
      force = force.unit();
      force.mult(TERMINAL);
    }
    
    vel.add(force);
    
    if (vel.mag() > TERMINAL) {
      vel = vel.unit();
      vel.mult(TERMINAL);
    }
  }

  public boolean collides(Ball b) {
    
    double dx = pos.getX() - b.getPos().getX();
    double dy = pos.getY() - b.getPos().getY();

    double rSum = radius + b.getRadius();

    if (!((dx * dx + dy * dy) < (rSum * rSum))) {
      return false;
    } else {
      return true;
    }

  }

//reference: http://archive.ncsa.illinois.edu/Classes/MATH198/townsend/math.html
  public void resolveCollision(Ball b) {
    //hack to prevent (some) rubber banding
    if (b.vel.mag() == 0 && this.vel.mag() > 5) {
      b.vel = this.vel.copy();
      b.vel = b.vel.unit();
      b.vel.mult(0.1);
    }
    
    if (this.vel.mag() == 0 && b.vel.mag() > 5) {
      this.vel = b.vel.copy();
      this.vel = this.vel.unit();
      this.vel.mult(0.1);
    }
    
    
    Vector2D n = this.pos.copy();
    n.sub(b.pos);
    n.div(n.mag());
    
    Vector2D nminus = n.copy();
    nminus.mult(-1);
    
    Vector2D vn1 = nminus.copy();
    vn1.mult(nminus.dot(this.vel));
    
    Vector2D vn2 = n.copy();
    vn2.mult(n.dot(b.vel));
    
    
    Vector2D vt1 = vn1.copy();
    vt1.sub(this.vel);
    Vector2D vt2 = vn2.copy();
    vt2.sub(b.vel);
    
    Vector2D v1new = vt1.copy();
    v1new.sub(vn2);
    Vector2D v2new = vt2.copy();
    v2new.sub(vn1);
    
    this.vel = v1new;
    b.vel = v2new;
  
    this.pos.add(vel);
    b.pos.add(b.vel);
  
  }
  
  public Vector2D getPos() {
    return pos;
  }

  public void setPos(Vector2D pos) {
    this.pos = pos;
  }

  public Vector2D getVel() {
    return vel;
  }

  public void setVel(Vector2D vel) {
    this.vel = vel;
  }

  public int getHue() {
    return hue;
  }

  public void setHue(int hue) {
    this.hue = hue;
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
  }
}