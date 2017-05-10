/*
rahmant3
Main game class, handles all game-related behaviors, and some UI behaviors

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

//Useful Ratios:
//28.66 board width to ball width
//1.09 board width to board height
//Use 546 x 500 board, 20 x 20 ball

public class MainGame {
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
  
  public int state;
  public int show;
  
  public CollisionHandler physics;
  
  public final int BOARD_WIDTH = 436;
  public final int BOARD_HEIGHT = 400;
  public Board board;
  
  public final double BALL_RADIUS = 12;
  
  public int current_turn;
  public int next_turn;
  public int winner;
  public int player1_score;
  public int player2_score;
  
  public String displayText;
  
  private boolean previous_round;
  
  public MainGame() {
    state = STATE_GAME;
    show = SHOW_GAME;
    
    current_turn = 0;
    next_turn = -1;
    player1_score = 0;
    player2_score = 0;
    
    board = new Board(BOARD_WIDTH, BOARD_HEIGHT);
    physics = new CollisionHandler(board);
  }
  
  public void update() {
    if (show == SHOW_GAME) {
      boolean current_round;
      if (current_round = !physics.roundIsOver()) {
        if (previous_round != current_round) {
             next_turn = -1;
         }
        
        
        int potted = physics.update();
        //based on which ball is potted, apply the correct rules
        switch(potted) {
          case(CUE_BALL_INDEX):
            update_turn();
            break;
          case(BLACK_BALL_INDEX):
            show = SHOW_WINNER;
            if (!physics.inPlay(P1_BALL_INDEX)) {
              winner = 0;
              player1_score++;
            } else if (!physics.inPlay(P2_BALL_INDEX)) {
              winner = 1;
              player2_score++;
            } else {
              winner = (current_turn + 1) % 2;
              if (winner == 0) { player1_score++;}
              if (winner == 1) { player2_score++;}
              update_turn();
            }
            break;
         case(P1_BALL_INDEX):
           if (current_turn != 0) {
             update_turn();
           }
           break;
         case(P2_BALL_INDEX):
           if (current_turn != 1) {
             update_turn();
           }
           break;
         
        }
        
        
      } else { 
        if (!physics.inPlay(CUE_BALL_INDEX)) {
          reset_cue_ball();
        } 
        if (previous_round != current_round) {
          update_turn();
          current_turn = next_turn;
        }
      }
      previous_round = current_round;
    } else if (show == SHOW_HELP) {
      show_help(); 
    } else if (show == SHOW_ABOUT) {
      show_about();
    } else if (show == SHOW_WINNER) {
      show_winner();
    }
  }
  
  public void update_turn() {
    if (next_turn == -1) {
      next_turn = (current_turn + 1) % 2;
    }
  }
  
  public void show_help() {
    displayText = 
    "The object of LOOP is to win by potting a colored ball\n" +
    "and then the black ball.\n" +
    "Player 1 is assigned the red ball, Player 2 is assigned\n" +
    "the yellow ball.\n" +
    "Turns alternate as players take shots. Potting a colored\n"+ 
    "ball gives the current player another turn.\n" +
    "A player who pots the black with his or her colour still\n" +
    "on the table loses the game.\n\n" +
    "The key to the strategy of LOOP is to consider the focus points.\n" +
    "When the path of a ball goes through a focus point, its path\n" +
    "will always continue to the other focus point due to elliptical\n" +
    "geometry.\n\n" +
    "Move the ball by holding down the mouse and releasing it.";
    
  }
  public void show_about() {
    displayText =
    "This game is based on the real-life LOOP game\n" +
    "featured in Numberphile, presented by Alex Bellos\n\n\n" +
    "Numberphile: https://www.youtube.com/numberphile\n" +
    "LOOP:        http://www.loop-the-game.com/\n\n\n\n\n\n\n\n\n\n\n" +
    "This game is (poorly) programmed with Processing 3.2.3 (Java).";
  
  }
  public void show_winner() {
    displayText =
    "The winner is player " + (winner + 1) + "!";
  }
  
  public boolean userInputValid() {
    return (!(show == SHOW_GAME && !physics.roundIsOver()));
  }
    
  
  public void reset() { 
    physics.empty();
    physics.addBall( CUE_BALL_INDEX, cue_ball() );
    
    if (state == STATE_GAME) {
      physics.addBall( BLACK_BALL_INDEX, black_ball() );
      physics.addBall( P1_BALL_INDEX, P1_ball() );
      physics.addBall( P2_BALL_INDEX, P2_ball() );
    }
  }
 
  public void reset_cue_ball() {
    physics.addBall( CUE_BALL_INDEX, cue_ball() );
  }
   
  public void applyForce(int ball_index, Vector2D force) {
    physics.applyForce(ball_index, force);
  }
  
  private Ball cue_ball() {
    return new Ball(0, 0, 255, BALL_RADIUS);
  }
  private Ball black_ball() {
    return new Ball(board.foci[1], 0, 0, BALL_RADIUS);
  }
  private Ball P1_ball() {
    return new Ball(board.foci[1], 2*BALL_RADIUS, -1, BALL_RADIUS);
  }
  private Ball P2_ball() {
    return new Ball(board.foci[1], -2*BALL_RADIUS, -2, BALL_RADIUS);
  }
    
    
  
}