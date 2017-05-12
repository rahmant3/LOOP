/*
rahmant3
 Handles all in-game physics
 
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

import java.util.*;

public class CollisionHandler {

  Game_Balls gB;

  Board gameBoard;
  Vector2D hole;

  public CollisionHandler(Board gameBoard) {
    this.gameBoard = gameBoard;
    gB = new Game_Balls();
    hole = new Vector2D(gameBoard.foci[0], 0);
  }

  public void applyForce(int ball_index, Vector2D force) {
    if (gB.inPlay[ball_index]) {
      gB.balls[ball_index].applyForce(force);
    }
  }

  public void addBall(int ball_index, Ball b) {
    gB.balls[ball_index] = b;
    gB.inPlay[ball_index] = true;
  }

  public boolean inPlay(int index) {
    return (gB.inPlay[index]);
  }

  public void empty() {
    gB.balls = new Ball[4];
    gB.inPlay = new boolean[4];
  }

  public boolean roundIsOver() {
    for (int i = 0; i < gB.SIZE; i++) {
      if (gB.inPlay[i] && gB.balls[i].vel.mag() > 0) {
        return false;
      }
    }
    return true;
  }

  //Check and resolve ball to ball collision
  //Check and resolve ball to edge collision
  //Check and resolve ball to hole "collision" - IMPORTANT: DO IN A DIFFERENT LOOP
  //Update all
  public int update() {

    int pocketed = -1;

    //Ball to ball collision
    for (int i = 0; i < gB.SIZE; i++) {
      if (gB.inPlay[i]) {
        Ball a = gB.balls[i];
        for (int j = 0; j < gB.SIZE; j++) {

          if (gB.inPlay[j] && j != i) {
            Ball b = gB.balls[j];

            if (a.collides(b)) {
              a.resolveCollision(b);
            }
          }
        }
      }
    }

    for (int i = 0; i < gB.SIZE; i++) {
      if (gB.inPlay[i]) {
        Ball a = gB.balls[i];
        if (gameBoard.collides(a)) {
          gameBoard.resolveCollision(a);
        }
      }
    }

    for (int i = 0; i < gB.SIZE; i++) {
      if (gB.inPlay[i]) {
        Vector2D ball_pos = gB.balls[i].pos.copy();
        ball_pos.sub(hole);
        if (ball_pos.mag() < 1.5 * gB.balls[i].radius) {
          gB.inPlay[i] = false;
          pocketed = i;
        }
      }
    }

    for (int i = 0; i < gB.SIZE; i++) {
      if (gB.inPlay[i]) {
        gB.balls[i].update();
      }
    }

    return pocketed;
  }
}