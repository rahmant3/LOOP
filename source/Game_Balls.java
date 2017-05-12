/*
rahmant3
 Utility to hold all 4 billiards for CollisionHandler.java
 
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
public class Game_Balls {
  private final int CUE_BALL_INDEX = 0;
  private final int BLACK_BALL_INDEX = 1;
  private final int P1_BALL_INDEX = 2;
  private final int P2_BALL_INDEX = 3;

  public Ball[] balls;
  public boolean[] inPlay;

  public final int SIZE = 4;

  public Game_Balls() {
    balls = new Ball[SIZE];
    inPlay = new boolean[SIZE];
  }

  public void add(int index, Ball b) {
    balls[index] = b;
    inPlay[index] = true;
  }
}