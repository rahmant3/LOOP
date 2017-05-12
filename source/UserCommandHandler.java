/*
rahmant3
 Handles execution of user inputs
 
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

public class UserCommandHandler {
  private final int STATE_GAME = 1;
  private final int STATE_PRACTICE_MODE = 2;
  private final int STATE_WAITING = 13;

  private final int SHOW_GAME = 3;
  private final int SHOW_ABOUT = 4;
  private final int SHOW_HELP = 5;
  private final int SHOW_WINNER = 12;

  private final int CMD_RESET = 6;
  private final int CMD_FORCE = 7;
  private final int CMD_SHOW_HELP = 8;
  private final int CMD_SHOW_GAME = 9;
  private final int CMD_PRACTICE_MODE = 10;
  private final int CMD_SHOW_ABOUT = 11;

  private final int CUE_BALL_INDEX = 0;
  private final int BLACK_BALL_INDEX = 1;
  private final int P1_BALL_INDEX = 2;
  private final int P2_BALL_INDEX = 3;

  MainGame game;

  public UserCommandHandler(MainGame game) {
    this.game = game;
  }

  public void execute(Command action) {
    int cmd = action.cmd;

    switch (cmd) {
    case CMD_RESET: 
      game.reset();
      break;
    case CMD_FORCE: 
      game.applyForce(0, action.force);
      break;
    case CMD_SHOW_HELP: 
      game.show = SHOW_HELP;
      game.show_help();
      break;
    case CMD_SHOW_GAME:
      if (game.state != STATE_GAME || (game.show == SHOW_GAME || game.show == SHOW_WINNER)) {
        game.state = STATE_GAME;
        game.reset();
      }
      game.show = SHOW_GAME;
      break;
    case CMD_PRACTICE_MODE:

      if (game.state != STATE_PRACTICE_MODE || game.show == SHOW_GAME) {
        game.state = STATE_PRACTICE_MODE;
        game.reset();
      }

      game.show = SHOW_GAME;
      break;
    case CMD_SHOW_ABOUT:
      game.show = SHOW_ABOUT;
      game.show_about();
      break;
    }
  }
}