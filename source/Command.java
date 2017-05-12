/*
rahmant3
 Utility class to hold command for UserCommandHandler.java
 
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

public class Command {
  int cmd;
  Vector2D force;

  public Command(int cmd, Vector2D force) {
    this.cmd = cmd;
    this.force = force;
  }

  public Command(int cmd) {
    this.cmd = cmd;
    this.force = null;
  }
}