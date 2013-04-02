
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferStrategy;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


import javax.swing.JFrame;
import javax.swing.JViewport;
import javax.swing.Timer;

public class GameWindow extends JFrame {
	private static final long serialVersionUID = 1L; //To suppress warning
	static JFrame frame;
	BufferStrategy bufferStrat;
	Graphics graphics;
	Rectangle rec;
	int xGridMin = 40;
	int yGridMin = 70;
	int xGridMax;
	int yGridMax;
	int xMax;
	int yMax;
	int xSpacing;
	int ySpacing;
	int xBoardDim;
	int yBoardDim;
	int xClick;
	int yClick;
	int xLastCoord = -1;
	int yLastCoord = -1;
	int xLastPoint = -1;
	int yLastPoint = -1;
	int radius;
	boolean alternateColors = false;
	/* True if there was a click */
	boolean clicked = false;
	/* True if the timer was fired */
	boolean ticked = false;
	/* Used to force window updates independent of events */
	boolean forceUpdate = true;
	/* Controls the visibility of the advance and withdraw buttons */
	boolean advWithVisible = false;
	/* Controls the visibility of the board and related buttons */
	boolean gameVisible = false;
	/* Controls the visibility of the client/server/local game type */
	boolean clientServerVisible = true;
	/* Controls the visibility of the client options screen */
	boolean clientScreenVisible = false;
	/* Controls the visibility of the server options screen */
	boolean serverScreenVisible = false;
	/* Controls the visibility of the local options screen */
	boolean localScreenVisible = false;
	
	
	/* Game Variables */
	Fanorona game;
	int turn;
	int turnLimit;
	int numberOfBlackMovesThisTurn;
	boolean moveTurn;
	Piece.Type humanPlayer = Piece.Type.WHITE;
	Piece.Type otherPlayer;
	long time1;
	long time2;
	
	private enum selectionStates {
		NONE,
		FIRSTPIECE,
		SECONDCOORD;
	}
	selectionStates currentSelectState = selectionStates.NONE;
	
	private enum pieceType {
		BLACK,
		WHITE,
		SACRIFICED
	}
	
	final DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public GameWindow(int _xBoardDim, int _yBoardDim) {
		xBoardDim = _xBoardDim;
		yBoardDim = _yBoardDim;
		createWindow();
	}
	
	private final void createWindow() {
		setTitle("Fanorona");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 600);
		setLocationRelativeTo(null);
		setVisible(true);
		this.createBufferStrategy(2);
		
		/* Set window dimensions used elsewhere */
		rec = new Rectangle();
		if (getParent() instanceof JViewport) {
	        JViewport vp = (JViewport) getParent();
	        rec = vp.getViewRect();
	    } else {
	        rec = new Rectangle(0, 0, getWidth(), getHeight());
	    }
		xMax = (int) rec.getMaxX();
		yMax = (int) rec.getMaxY();

		calculateDimensions();
		if (radius > xGridMin) {
			xGridMin = (radius) + 10;
			yGridMin = (radius) + 40;
			calculateDimensions();
		}
		
        ActionListener timerListener = new ActionListener()  
        {
            public void actionPerformed(ActionEvent e)  
            {
            	ticked = true;
            	updateScreen();
            }
        };
        Timer timer = new Timer(1000, timerListener);   
        timer.start();
		
		addMouseListener(new MouseAdapter() {
			@Override
            public void mousePressed(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                	clicked = true;
                	xClick = event.getX();
                	yClick = event.getY();
                	updateScreen();
                }

                if (event.getButton() == MouseEvent.BUTTON3) {

                }
			}
		});
	}
	
	private void initGame() {
		game = new Fanorona(xBoardDim, yBoardDim);
		turn = 0;
		turnLimit = yBoardDim * 10;
		numberOfBlackMovesThisTurn = 0;
		moveTurn = false;
		time1 = new Date().getTime();
		time2 = -1;
		if (humanPlayer == Piece.Type.WHITE) {
			otherPlayer = Piece.Type.BLACK; 
		} else {
			otherPlayer = Piece.Type.WHITE;
		}
	}
	
	private void handleMove() {
		if (game.activePlayer() == otherPlayer) {
			String bestMove = game.getAIMove(otherPlayer);

			System.out.println("best move: " + bestMove);

			//move = bestMove;

			numberOfBlackMovesThisTurn++;
			//move = game.getRandomMove();
			//System.out.println("Other move: " + move);

			time2 = new Date().getTime();
		} else {
			//Move selection entered here
			time2 = new Date().getTime();

			/*if (playerInput.equals("quit")) {
				
				//Quit here
			} else if(playerInput.equals("moves")) {
				System.out.println("\nwhite: " + game.board.whiteMovesFull);
				System.out.println("\nblack: " + game.board.blackMovesFull);
				moveTurn = false;
				turn--;
			} else if(playerInput.equals("reset")) {
				//Start a new game here
				initGame();
				continue;
			} else {
				try {
					//Get move
				}
				catch(Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
			}*/
		}
	}
	
	private void gameLoop() {
		initGame();
		updateScreen();

		while (true) {

			
			String move = "";
			//if (game.capturingMoveAvailable()) System.out.println("Capturing move required");

			int pieceInt1 = -1;
			int pieceInt2 = -1;

			String pieceStr1 = "";
			String pieceStr2 = "";

			moveTurn = true; //Indicates that a move as been entered


			boolean valid = true;
			if (moveTurn) {
				int row1;
				int row2;
				int col1;
				int col2;
				char moveType;

				if(move.equals("N")) {
					moveType = 'N';
					row1 = 0;
					col1 = 0;
					row2 = 0;
					col2 = 0;
				}
				else {
					if (game.activePlayer() == humanPlayer) {

						moveType = Fanorona.getMoveType(move);
						if (moveType == 'S') {
							row1 = game.board.rows-Fanorona.getFirstRowCMD(move);
							col1 = Fanorona.getFirstColumnCMD(move)-1;
							row2 = 0;
							col2 = 0;
						}
						else {
							row1 = game.board.rows-Fanorona.getFirstRowCMD(move);
							col1 = Fanorona.getFirstColumnCMD(move)-1;
							row2 = game.board.rows-Fanorona.getSecondRowCMD(move);
							col2 = Fanorona.getSecondColumnCMD(move)-1;
						}

						move = game.convertToInternalMove(move);
					}
					else{

						row1 = Fanorona.getFirstRow(move);
						col1 = Fanorona.getFirstColumn(move);
						row2 = Fanorona.getSecondRow(move);
						col2 = Fanorona.getSecondColumn(move);
						moveType = Fanorona.getMoveType(move);
					}
				}

				valid = game.validMoveSystax(move);

				if (!valid) {
					//Not a valid move
				} else {
					if(game.activePlayer() == Piece.Type.WHITE) {
						//Total time it took for the player to make the move
						game.player1Time += (time2 - time1);
					}
					else {
						game.player2Time += (time2 - time1);
					}

					if (game.capturingMoveAvailable()){
						if (game.isPossibleCapturingMove(row1, col1, row2, col2, moveType) || moveType == 'S') {
							Piece.Type previous = game.activePlayer();
							//			      			boolean successiveMove = game.move(row1, col1, row2, col2, moveType);
							//boolean successiveMove = game.move(move);
							if(previous == Piece.Type.BLACK && game.activePlayer() == Piece.Type.WHITE) {
								numberOfBlackMovesThisTurn = 0;
							}
							game.removeSacrifices(game.activePlayer());
						} else {
							System.out.println("A capturing move must be entered\n");
							valid = false;
						}
					} else {
						game.move(row1,col1,row2,col2,moveType);
					}
					if (game.board.numberRemaining(Piece.Type.WHITE) == 0) {
						//Black wins here
						break;
					} else if (game.board.numberRemaining(Piece.Type.BLACK) == 0) {
						//White wins here
						break;
					}
				}
			}
			updateScreen();
			//game.printTime();
			if(valid && moveTurn) {
				turn++;
			}
			if (turn == turnLimit) {
				//Maximum number of turns reached here
				break;
			}
		}
	}
	
	private void calculateDimensions() {
		/* TODO Add stroke size scaling coefficient */
		xSpacing = (xMax - 2 * xGridMin)/(xBoardDim - 1);
		ySpacing = ((yMax - 30) - 2 * xGridMin)/(yBoardDim - 1);
		if (xSpacing >= ySpacing) {
			radius = (int) (0.375 * (double) ySpacing);
		} else {
			radius = (int) (0.375 * (double) xSpacing);
		}
		xGridMax = xGridMin + ((xBoardDim - 1) * xSpacing);
		yGridMax = yGridMin + ((yBoardDim - 1) * ySpacing);
	}
	
	/* TODO Implement this function */
	public void processMove() {
		
	}
	
	private boolean checkButtonClicks() {
		if (gameVisible) {
			if (checkButtonClick(40, yMax - 30, 20, 80)) {
				//Reset clicked
				System.out.println("RESET");
				return true;
			} else if (checkButtonClick(480, yMax - 30, 20, 80)) {
				//Quit clicked
				System.out.println("QUIT");
				return true;
			} else if (advWithVisible) {
				if (checkButtonClick(200, yMax - 30, 20, 80)) {
					//Advance clicked
					advWithVisible = false;
					forceUpdate = true;
					return true;
				} else if (checkButtonClick(315, yMax - 30, 20, 80)) {
					//Withdraw clicked
					advWithVisible = false;
					forceUpdate = true;
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else if (clientServerVisible) {
			if (checkButtonClick(xMax / 4, yMax / 3, yMax / 6, xMax / 5)) {
				//Client clicked
				System.out.println("CLIENT");
				clientServerVisible = false;
				clientScreenVisible = true;
				forceUpdate = true;
				updateScreen();
				return true;
			} else if (checkButtonClick(xMax / 2 + xMax / 20, yMax /3, yMax / 6,
					xMax / 5)) {
				//Server clicked
				System.out.println("SERVER");
				clientServerVisible = false;
				serverScreenVisible = true;
				forceUpdate = true;
				updateScreen();
				return true;
			} else if (checkButtonClick(4 * (xMax / 10), yMax / 3 + yMax / 4,
					yMax / 6, xMax / 5)) {
				//Local clicked
				System.out.println("LOCAL");
				clientServerVisible = false;
				localScreenVisible = true;
				forceUpdate = true;
				updateScreen();
				return true;
			} else {
				return false;
			}
		} else if (clientScreenVisible) {
			if (false) {
				//Address field clicked
				
			} else if (false) {
				//Port field clicked
			} else if (checkNumberPadButtons()) {
				
			} else {
				return false;
			}
		} else if (serverScreenVisible) {
			if (false) {
				//Rows field clicked
				
			} else if (false) {
				//Columns field clicked
				
			} else if (false) {
				//Port field clicked
				
			} else if (false) {
				//Color checkbox clicked
				
			} else if (checkNumberPadButtons()) {
				
			} else {
				return false;
			}
		} else if (localScreenVisible) {
			if (false) {
				//Rows field clicked
				
			} else if (false) {
				//Columns field clicked
				
			} else if (false) {
				//Color checkbox clicked
				
			} else if (false) {
				//Player 1 type checkbox clicked
				
			} else if (false) {
				//Player 2 type checkbox clicked
				
			} else if (checkNumberPadButtons()) {
				
			} else {
				return false;
			}
		} else {
			return false;
		}
		return false;
	}
	
	private boolean checkButtonClick(int x, int y, int height, int width) {
		if ((xClick > x && xClick < (x + width))
				&& (yClick > y && yClick < y + height)) {
			return true;
		} else {
			return false;
		}
	}
	
	private void processClick(int x, int y) {
		if (checkButtonClicks()) {
			return;
		}
		if (gameVisible) {
			xClick = -1;
			yClick = -1;
			int xTemp;
			int yTemp;
			if (x <= xGridMin) {
				xTemp = 0;
			} else if (x >= xGridMax) {
				xTemp = xBoardDim - 1;
			} else {
				double xDouble = (double) x;
				xTemp = (int) Math.round(((xDouble -
						(double)xGridMin) / (double)xSpacing));
			}
			if (y <= yGridMin) {
				yTemp = 0;
			} else if (y >= yGridMax) {
				yTemp = yBoardDim - 1;
			} else {
				double yDouble = (double) y;
				yTemp = (int) Math.round(((yDouble -
						(double)yGridMin) / (double)ySpacing));
			}
			processCoordinate(xTemp, yTemp, x, y);
		}

		/*graphics.drawString("Point nearest click: " +
				Integer.toString(xTemp), 20, 40);
		graphics.drawString("Point nearest click: " +
				Integer.toString(yTemp), 20, 60);*/
	}

	private void processCoordinate(int xCoord, int yCoord, int xActual,
			int yActual) {
		int xTemp = xGridMin + xSpacing * xCoord;
		int yTemp = yGridMin + ySpacing * yCoord;

		double distance = Math.sqrt(((xTemp - xActual) * (xTemp - xActual)) +
				((yTemp - yActual) * (yTemp - yActual)));
		
		/* Is the click near a coordinate? */
		if (distance <= radius) {
			if (currentSelectState == selectionStates.NONE) {
				/* TODO Check to see if the selected coordinate has a
				 * selectable piece in it, otherwise take no action */
				xLastCoord = xCoord;
				yLastCoord = yCoord;
				currentSelectState = selectionStates.FIRSTPIECE;
				drawSelection(xTemp, yTemp);
				return;
			} else if (currentSelectState == selectionStates.FIRSTPIECE) {
				if (xCoord == xLastCoord && yCoord == yLastCoord) {
					/* Currently selected piece is selected again. This is
					 * interpreted as a deselect action */
					currentSelectState = selectionStates.NONE;
					xLastCoord = -1;
					yLastCoord = -1;
					return;
				} else {
					/* A location different from the first is selected. This
					 * is interpreted as an attempted move action. */
					currentSelectState = selectionStates.SECONDCOORD;
					drawSelection(xTemp, yTemp);
					return;
				}
			}
		} else {
			/* Clicked point not near a coordinate */
			drawSelection(-2, -2);
			return;
		}
	}
	
	private void drawSelection(int xPoint, int yPoint) {
		if (forceUpdate) {
			return;
		}
		if (xPoint == -2 || yPoint == -2) {
			xPoint = xLastPoint;
			yPoint = yLastPoint;
		} else {
			xLastPoint = xPoint;
			yLastPoint = yPoint;
		}
		if (currentSelectState == selectionStates.FIRSTPIECE) {
			graphics.setColor(Color.RED);
		} else {
			graphics.setColor(Color.BLUE);
		}
		Graphics2D graphics2D = (Graphics2D) graphics;
	    graphics2D.setStroke(new BasicStroke(2F));
	    drawBox(xPoint - radius, yPoint - radius, radius * 2, radius * 2);
		graphics.setColor(Color.BLACK);
		graphics2D.setStroke(new BasicStroke(0F));
		if (currentSelectState == selectionStates.SECONDCOORD) {
			currentSelectState = selectionStates.NONE;
			/* TODO Possibly update x/yLastCoord/Point here */
			processMove();
		}
		
		/* REMOVE THIS LINE */
		drawPiece(pieceType.WHITE, xPoint, yPoint);
	}
	
	private void clearWindow() {
		graphics.setColor(Color.lightGray);
		/* +20 is to work around the either getting bad dimensions from the
		 * viewport or rendering problems with Swing or the window manager */
        graphics.fillRect(0, 0, xMax + 20, yMax);
        graphics.setColor(Color.BLACK);
	}
	
	private void drawTime() {
		if (gameVisible) {
			graphics.setColor(Color.lightGray);
	        graphics.fillRect(xMax - 250, 0, 240, 42);
	        graphics.setColor(Color.BLACK);
			Date date = new Date();
			String time = timeFormat.format(date);
	        graphics.drawString("Remaining move time: ", xMax - 250, 40);
			graphics.drawString(time + " sec", xMax - 100, 40);
		}
	}

	private void drawGrid() {
		/* TODO Add special processing for 1xX and Xx1 board sizes */
		if ((xBoardDim == 1) || (yBoardDim == 1)) {
			return;
		}

		int xCurrent = xGridMin;
		int yCurrent = yGridMin;

		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setStroke(new BasicStroke(7F));

		boolean altLeft = true;
		boolean flipAlt = false;
		if (xBoardDim % 2 == 0) {
			flipAlt = true;
		}
		while(true) {
			if (yCurrent != yGridMax) {
				//Draw down line
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent,
						yCurrent + ySpacing);
			}
			if ((xCurrent != xGridMin) && (yCurrent != yGridMax) && altLeft) {
				//Draw left diagonal
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent - xSpacing,
						yCurrent + ySpacing);
			}
			if ((xCurrent != xGridMax) && (yCurrent != yGridMax) && altLeft) {
				//Draw right diagonal
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent + xSpacing,
						yCurrent + ySpacing);
			}
			if (xCurrent != xGridMax) {
				//Draw right line
				graphics2D.drawLine(xCurrent, yCurrent, xCurrent + xSpacing,
						yCurrent);
			}
			if ((xCurrent >= xGridMax) && (yCurrent >= yGridMax)) {
				break;
			}
			if (xCurrent == xGridMax) {
				if (flipAlt) {
					altLeft = !altLeft;
				}
				xCurrent = xGridMin;
				yCurrent += ySpacing;
			} else {
				xCurrent += xSpacing;
			}
			altLeft = !altLeft;
		}
		graphics2D.setStroke(new BasicStroke(0F));
	}

	private void drawPiece(pieceType pT, int xPoint, int yPoint) {
		if (pT == pieceType.WHITE) {
			graphics.setColor(Color.WHITE);
		} else if (pT == pieceType.BLACK) {
			graphics.setColor(Color.BLACK);
		} else {
			graphics.setColor(Color.GRAY);
		}
		Graphics2D graphics2D = (Graphics2D) graphics;
		int x = (int) (((double) xPoint) - ((double) radius));
		int y = (int) (((double) yPoint) - ((double) radius));
		Ellipse2D.Double circle = new Ellipse2D.Double(x, y,
				radius * 2, radius * 2);
		graphics2D.fill(circle);
		graphics.setColor(Color.BLACK);
	}
	
	
	public void drawPieces() {
		/* Iterate over pieces here */
		/* xCoord and yCoord points start at the top left corner with (0,0)
		 * and go to the bottom left corner with (n,m) where n and m are the
		 * x and y dimensions of the board, respectively */
		int xCoord = 4;
		int yCoord = 4;
		int xPoint = xGridMin + (xSpacing * xCoord);
		int yPoint = yGridMin + (ySpacing * yCoord);
		//drawPiece(pieceType.WHITE, xPoint, yPoint);
	}
	
	public void drawInfo() {
		graphics.drawString("Current Turn: XX", xMax - 196, 52);
		graphics.drawString("Current Player: XX", xMax - 196, 62);
	}
	
	private void drawButton(int x, int y, Color color) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setStroke(new BasicStroke(3F));
		//Reset button
		graphics.setColor(color);
		drawBox(x, y, 20, 80);
		graphics.setColor(Color.BLACK);
		graphics2D.setStroke(new BasicStroke(0F));
	}
	
	private void drawButtons() {
		if (gameVisible) {
			//Reset button
			drawButton(40, yMax - 30, Color.ORANGE);
			graphics.drawString("Reset", 60, yMax - 15);
			//Quit button
			drawButton(480, yMax - 30, Color.ORANGE);
			graphics.drawString("Quit", 505, yMax - 15);
			if (advWithVisible) {
				//Advance button
				drawButton(200, yMax - 30, Color.GRAY);
				graphics.drawString("Advance", 214, yMax - 15);
				//Withdraw button
				drawButton(315, yMax - 30, Color.GRAY);
				graphics.drawString("Withdraw", 327, yMax - 15);
			}
		}
	}
	
	private void drawFlashingBox(int x, int y, int height, int width) {
		Graphics2D graphics2D = (Graphics2D) graphics;
	    graphics2D.setStroke(new BasicStroke(1.5F));
		if (alternateColors) {
			graphics.setColor(Color.RED);
			if (ticked) {
				alternateColors = false;
			}
		} else {
			graphics.setColor(Color.YELLOW);
			if (ticked) {
				alternateColors = true;
			}
		}
		drawBox(x, y, height, width);
		graphics.setColor(Color.BLACK);
		graphics2D.setStroke(new BasicStroke(0F));
	}
	
	private void drawBox(int x, int y, int height, int width) {
		graphics.drawLine(x, y + height, x + width, y + height); //Bottom
		graphics.drawLine(x + width, y + height, x + width, y); //Right
		graphics.drawLine(x, y, x + width, y); //Top
		graphics.drawLine(x, y, x, y + height); //Left
	}
	
	private void drawClientServer() {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setStroke(new BasicStroke(3F));
		graphics.setColor(Color.BLACK);
		//Client button
		drawBox(xMax / 4, yMax / 3, yMax / 6, xMax / 5);
		graphics.drawString("Client", xMax / 4 + xMax / 10 - xMax / 30,
				yMax / 3 + yMax / 10);
		//Server button
		drawBox(xMax / 2 + xMax / 20, yMax /3, yMax / 6, xMax / 5);
		graphics.drawString("Server", xMax / 4 + 2 * xMax / 10 - xMax / 30 + 
				xMax / 5,
				yMax / 3 + yMax / 10);
		//Local button
		drawBox(4 * (xMax / 10), yMax / 3 + yMax / 4, yMax / 6, xMax / 5);
		graphics.drawString("Local", xMax / 2 - xMax / 30, 2 * yMax / 3);
	}
	
	private void drawClientScreen() {
		//Address field
		//drawBox(4 * (xMax / 10), yMax / 3 + yMax / 4, yMax / 6, xMax / 5);
		//Port field
		//drawBox(4 * (xMax / 10), yMax / 3 + yMax / 4, yMax / 6, xMax / 5);
		//Number pad
	}
	
	private void drawServerScreen() {
		//Rows field
		//Columns field
		//Port field
		//Color checkbox
		//Number pad
	}
	
	private void drawLocalScreen() {
		//Rows field
		//Columns field
		//Player 1 color checkbox
		//Player 1 player checkbox
		//Player 2 player checkbox
	}
	
	private void drawNumberPad() {
		int yOffset = yMax / 6;
		int xShift = 5;
		int yShift = -2;
		drawBox(xMax / 3, yMax / 3 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("7", xMax / 3 + xMax / 18 - xShift,
				yMax / 3 + yMax / 18 - yShift +  + yOffset);
		drawBox(xMax / 3 + xMax / 9, yMax / 3 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("8", xMax / 3 + 3 * xMax / 18 - xShift,
				yMax / 3 + yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("9", xMax / 3 + 5 * xMax / 18 - xShift,
				yMax / 3 + yMax / 18 - yShift + yOffset);
		
		drawBox(xMax / 3, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("4", xMax / 3 + xMax / 18 - xShift,
				yMax / 3 + 3 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + xMax / 9, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("5", xMax / 3 + 3 * xMax / 18 - xShift,
				yMax / 3 + 3 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("6", xMax / 3 + 5 * xMax / 18 - xShift,
				yMax / 3 + 3 * yMax / 18 - yShift + yOffset);
		
		drawBox(xMax / 3, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("1", xMax / 3 + xMax / 18 - xShift,
				yMax / 3 + 5 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + xMax / 9, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("2", xMax / 3 + 3 * xMax / 18 - xShift,
				yMax / 3 + 5 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9 , xMax / 9);
		graphics.drawString("3", xMax / 3 + 5 * xMax / 18 - xShift,
				yMax / 3 + 5 * yMax / 18 - yShift + yOffset);
		
		drawBox(xMax / 3, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString(".", xMax / 3 + xMax / 18 - xShift,
				yMax / 3 + 7 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + xMax / 9, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("0", xMax / 3 + 3 * xMax / 18 - xShift,
				yMax / 3 + 7 * yMax / 18 - yShift + yOffset);
		drawBox(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9);
		graphics.drawString("<-", xMax / 3 + 5 * xMax / 18 - xShift,
				yMax / 3 + 7 * yMax / 18 - yShift + yOffset);
	}
	
	private boolean checkNumberPadButtons() {
		int yOffset = yMax / 6;
		if (checkButtonClick(xMax / 3, yMax / 3 + yOffset, yMax / 9, xMax / 9)) {
			//7 pressed
			System.out.println("7");
			return true;
		} else if (checkButtonClick(xMax / 3 + xMax / 9, yMax / 3 + yOffset, yMax / 9, xMax / 9)) {
			//8 pressed
			System.out.println("8");
			return true;
		} else if (checkButtonClick(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + yOffset, yMax / 9, xMax / 9)) {
			//9 pressed
			System.out.println("9");
			return true;
		} else if (checkButtonClick(xMax / 3, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9)) {
			//4 pressed
			System.out.println("4");
			return true;
		} else if (checkButtonClick(xMax / 3 + xMax / 9, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9)) {
			//5 pressed
			System.out.println("5");
			return true;
		} else if (checkButtonClick(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + yMax / 9 + yOffset, yMax / 9, xMax / 9)) {
			//6 pressed
			System.out.println("6");
			return true;
		} else if (checkButtonClick(xMax / 3, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9)) {
			//1 pressed
			System.out.println("1");
			return true;
		} else if (checkButtonClick(xMax / 3 + xMax / 9, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9)) {
			//2 pressed
			System.out.println("2");
			return true;
		} else if (checkButtonClick(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + 2 * yMax / 9 - 1 + yOffset, yMax / 9 , xMax / 9)) {
			//1 pressed
			System.out.println("3");
			return true;
		} else if (checkButtonClick(xMax / 3, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9)) {
			//. pressed
			System.out.println(".");
			return true;
		} else if (checkButtonClick(xMax / 3 + xMax / 9, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9)) {
			//0 pressed
			System.out.println("0");
			return true;
		} else if (checkButtonClick(xMax / 3 + 2 * xMax / 9 - 1, yMax / 3 + 3 * yMax / 9 - 1 + yOffset, yMax / 9, xMax / 9)) {
			//Backspace pressed
			System.out.println("Bkspc");
			return true;
		} else {
			return false;
		}
	}
	
	private void quit() {
		
	}
	
	private void updateScreen() {
		bufferStrat = this.getBufferStrategy();
		graphics = null;

		try {
			graphics = bufferStrat.getDrawGraphics();
			Graphics2D graphics2D = (Graphics2D) graphics;
			RenderingHints renderHints = new RenderingHints(
					RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			graphics2D.setRenderingHints(renderHints);
			if (clicked || forceUpdate) {
				clicked = false;
				clearWindow();
				if (gameVisible) {
					drawGrid();
					drawInfo();
					drawPieces();
				}
				if (clientServerVisible) {
					drawClientServer();
				}
				if (clientScreenVisible) {
					drawClientScreen();
				}
				if (serverScreenVisible) {
					drawServerScreen();
				}
				if (localScreenVisible) {
					drawLocalScreen();
				}
				if (clientScreenVisible || serverScreenVisible ||
						localScreenVisible) {
					drawNumberPad();
				}
				drawButtons();
				processClick(xClick, yClick);
			}
			drawTime();
			if (advWithVisible) {
				drawFlashingBox(180, yMax - 40, 38, 240);
				ticked = false;
			}
			

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		} finally {

		}
		graphics.dispose();
		bufferStrat.show();
		Toolkit.getDefaultToolkit().sync();
		forceUpdate = false;
	}
}
