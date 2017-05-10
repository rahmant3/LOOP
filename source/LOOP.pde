/*
rahmant3
Main Class: Handles all UI behaviors

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

private final int STATE_GAME = 1;
private final int STATE_PRACTICE_MODE = 2;

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

private final String[] headers = {"LOOP", "LOOP - Practice Mode", "LOOP - How to Play", "LOOP - About"};
private final String[] options = {"New\nGame", "Practice\nMode", "How to\nPlay", "About"};

int BORDER = 5; 
int WHITESP = 20;
  
Vector2D startVector;

MainGame game;
UserCommandHandler usercommandhandler;
Board ellipse;

void setup() {
  game = new MainGame();
  ellipse = game.board;
  
  usercommandhandler = new UserCommandHandler(game);
  
  //usercommandhandler.execute(new Command(CMD_RESET));
  startVector = null;
  
  game.reset();
  
  size(700, 650);
  colorMode(HSB);
}

void draw() {
  background(0);
  noStroke();
  
  drawHeader();
  drawButtons();
  game.update();
  
  if(game.show == SHOW_GAME) {
    if (game.state == STATE_GAME) { 
      displayTurn();
      displayScore();
    }
    translate(width/2, height/2);
    
    drawBoard(ellipse);
    drawFoci();
    
    for (int i = 0; i < game.physics.gB.SIZE; i++) {
      if (game.physics.gB.inPlay[i]) {
        Ball b = game.physics.gB.balls[i];
        drawBall(b);
      }
    }
    drawIndicator();
    
    
  } else if (game.show == SHOW_WINNER) {
    
    displayWinner();
    displayScore();
    
    translate(width/2, height/2);
    
    drawBoard(ellipse);
    drawFoci();
    
    for (int i = 0; i < game.physics.gB.SIZE; i++) {
      if (game.physics.gB.inPlay[i]) {
        Ball b = game.physics.gB.balls[i];
        drawBall(b);
      }
    }
  } else if (game.show == SHOW_HELP || game.show == SHOW_ABOUT) {
    drawTextBox(game.displayText);
  }
  
}

void displayWinner() {
  noStroke();
  fill(255);
  textSize(15);
  text((game.displayText) + "\nClick anywhere to \ncontinue", width-200, 100);
}
void displayTurn() {
  noStroke();
  fill(255);
  textSize(20);
  text("Player " + (1 + game.current_turn) + "'s turn.", width-200, 100);
}

void displayScore() {
  noStroke();
  fill(255);
  textSize(15);
  
  fill(360, 240, 250);
  text("Player 1: " + game.player1_score, width-100, height - 50);
  fill(40, 230, 255);
  text("\nPlayer 2: " + game.player2_score, width-100, height - 50);
}

void drawTextBox(String text) {
  noStroke();
  fill(75, 100, 150);
  rect(100, 100, width-200, height-200, 100);
  fill(75, 100, 200);
  rect(120, 120, width-240, height-240, 80);
  fill(255);
  text(text, 150, 150);
}

void drawButtons() {
  pushMatrix();
  noStroke();
  fill(75, 100, 150);
  translate(0, height-100);
  rect(WHITESP + 0, WHITESP + 0, 100 - WHITESP, 100 - 2 * WHITESP, 7);
  rect(WHITESP + 100, WHITESP + 0, 100 - WHITESP, 100 - 2 * WHITESP, 7);
  rect(WHITESP + 200, WHITESP + 0, 100 - WHITESP, 100 - 2 * WHITESP, 7);
  rect(WHITESP + 300, WHITESP + 0, 100 - WHITESP, 100 - 2 * WHITESP, 7);
  fill(75, 100, 200);
  rect(BORDER + WHITESP + 0, BORDER + WHITESP + 0, 100 - 1.2* (WHITESP + BORDER), 100 - 2 * (WHITESP + BORDER));
  rect(BORDER + WHITESP + 100, BORDER + WHITESP + 0, 100 - 1.2* (WHITESP + BORDER), 100 - 2 * (WHITESP + BORDER));
  rect(BORDER + WHITESP + 200, BORDER + WHITESP + 0, 100 - 1.2* (WHITESP + BORDER), 100 - 2 * (WHITESP + BORDER));
  rect(BORDER + WHITESP + 300, BORDER + WHITESP + 0, 100 - 1.2* (WHITESP + BORDER), 100 - 2 * (WHITESP + BORDER));
  
  fill(255);
  stroke(0);
  strokeWeight(10);
  textSize(13);
  text(options[0], 27, 47);
  text(options[1], 127, 47);
  text(options[2], 227, 47);
  text(options[3], 327, 53);
  popMatrix();
}
void drawHeader() {
    String text = "";
    int show = game.show;
    int state = game.state;
    if ( (show == SHOW_GAME || show == SHOW_WINNER ) && state == STATE_GAME) { 
      text = headers[0];
    } else if (show == SHOW_GAME && state == STATE_PRACTICE_MODE) {
      text = headers[1];
    } else if (show == SHOW_HELP) {
      text = headers[2];
    } else if (show == SHOW_ABOUT) {
      text = headers[3];
    }
    fill(255);
    stroke(1);
    textSize(60);
    text(text, 10, 50);
}

void drawFoci() {
  //Draw the border around the hole
  //Draw the hole
  //Draw the other foci
  
  noStroke();
  fill(10, 200, 200);
  ellipse((float)ellipse.foci[0], 0, 30, 30);
  fill(0);
  ellipse((float)ellipse.foci[0], 0, 20, 20);
  fill(75, 100, 150);
  ellipse((float)ellipse.foci[1], 0, 10, 10);
}

void drawIndicator() {
  if (startVector != null) {
    
    //draw indicator line
    Vector2D direction = startVector.copy();
    direction.sub(mouseX, mouseY);
    
    Ball cueBall = game.physics.gB.balls[0];
    
    direction.sub(cueBall.pos);
    //direction = direction.unit();
    if (direction.mag() > 250) {
      direction = direction.unit();
      direction.mult(250);
    }
    direction.mult(-1);
    
    fill(255);
    stroke(250, 200, 150);
    strokeWeight(3);
    line((float) cueBall.pos.x, (float) cueBall.pos.y, (float) direction.x, (float) direction.y);
    
    Vector2D mid = cueBall.pos.copy();
    mid = mid.unit();
    mid.mult(cueBall.pos.mag()/2);
    
    mid.sub(direction);
    mid.mult(-0.5);
    
    //draw indicator text
    double magD = 100 * direction.mag()/250;
    magD *= 100;
    int magI = (int) magD;
    magD = (double) magI;
    magD /= 100;
    
    fill(0);
    textSize(20);
    text(magD + "", (float) mid.x, (float) mid.y); 
  }
}


void drawBall(Ball b) {
  float xPos = (float) b.pos.x;
  float yPos = (float) b.pos.y;
  
  noStroke();
  
  double hue = b.getHue();
  
  if (hue == -1) {
    fill(360, 240, 250);
  } else if (hue== -2) {
    fill(40, 230, 255);
  } else {
    fill((int) hue);
  }
  ellipse(xPos, yPos, (int) b.radius * 2, (int) b.radius * 2);
  
}

void drawBoard(Board br) {
  noStroke();
  
  int h = br.getHeight();
  int w = br.getWidth();
  
  fill(75, 100, 150);
  ellipse(0, 0, w+50, h+50);
  
  fill(75, 100, 200);
  ellipse(0, 0, w, h);
}

void mousePressed() {
  
  if (withInRect(WHITESP + 0, WHITESP + height - 100, 100 - WHITESP,  height - 100 + 100 - 2 * WHITESP)) {
    usercommandhandler.execute(new Command(CMD_SHOW_GAME));
  } else if (withInRect(WHITESP + 100, WHITESP + height - 100, 100 - WHITESP, height - 100 + 100 - 2 * WHITESP)) {
    usercommandhandler.execute(new Command(CMD_PRACTICE_MODE));
  } else if (withInRect(WHITESP + 200, WHITESP + height - 100, 100 - WHITESP, height - 100 + 100 - 2 * WHITESP)) {
    usercommandhandler.execute(new Command(CMD_SHOW_HELP));
  } else if (withInRect(WHITESP + 300, WHITESP + height - 100, 100 - WHITESP, height - 100 + 100 - 2 * WHITESP)) {
    usercommandhandler.execute(new Command(CMD_SHOW_ABOUT));
  } else if (game.show == SHOW_GAME && game.userInputValid()) {
    Ball cueBall = game.physics.gB.balls[0];
    startVector = cueBall.pos.copy();
    startVector.add(new Vector2D(width/2, height/2));
  }  else if (game.show == SHOW_WINNER) {
    usercommandhandler.execute(new Command(CMD_SHOW_GAME));
  }
}

void mouseReleased() {
  if (startVector != null) {
    
    startVector.sub(mouseX, mouseY);
    startVector.div(20);
    
    usercommandhandler.execute(new Command(CMD_FORCE, startVector));
    startVector = null;
  }
  
  System.out.println(mouseX + " " + mouseY);
}

void keyPressed() {
  
}


public boolean withInRect (double xin, double yin, double w, double h) {
    return (mouseX > xin && mouseX < xin + w && mouseY > yin && mouseY < yin + h);
}