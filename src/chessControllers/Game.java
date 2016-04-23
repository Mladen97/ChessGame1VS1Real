package chessControllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Stack;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ChessGame.King;
import ChessGame.Piece;
import ChessGame.StandardBoard;
import ChessViews.GameDisplay;
import ChessViews.GameDisplay;;

public class Game {
	static Player whitePlayer;
	static Player blackPlayer;
	ChessGame.Board.Color gameTurn;
	StandardBoard gameBoard;
	boolean gameOver;
	static boolean gameType = false;
	int squareSize;
	JFrame window;
	JPanel gamePanel;
	JPanel sidePanel;
	JLabel whiteLabel;
	JLabel blackLabel;
	JLabel whiteScore;
	JLabel blackScore;
	JButton forfeitButton;
	JButton undoButton;
	JButton restartButton;
	Piece movingPiece;
	Stack<MoveCommand> commandStack;

	/**
	 * Method to initialize gameBoard
	 */
	public void gameInit(boolean gameType) {
		gameBoard = new StandardBoard(8, 8);
		gameBoard.populateBoardWithPieces(gameType);
		gameTurn = ChessGame.Board.Color.white;
		gameOver = false;
		squareSize = 80;
		commandStack = new Stack();
	}

	private static void getGamePlayers() {
		String whiteName = JOptionPane.showInputDialog("Please input White player name");
		if (whiteName == "" || whiteName == null)
			whiteName = "White player";
		String blackName = JOptionPane.showInputDialog("Please input Black player name");
		if (blackName == "" || blackName == null)
			blackName = "Black player";
		whitePlayer = new Player(whiteName, ChessGame.Board.Color.white);
		blackPlayer = new Player(blackName, ChessGame.Board.Color.black);
	}

	public void gameStart() {
		Thread gameThread = new Thread() {
			@Override
			public void run() {
				gameLoop();
			}
		};
		gameThread.start();

	}

	private void gameLoop() {
		while (true) {
			if (gameOver)
				break;
			gamePanel.repaint();
		}
	}

	public void setupDisplay() {
		window = new JFrame("Chess 1V1");
		gamePanel = initializeGamePanel(gameBoard);
		Container contentPanel = window.getContentPane();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.LINE_AXIS));
		sidePanel = initializeSidePanel();
		contentPanel.add(gamePanel);
		contentPanel.add(sidePanel);
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.validate();
		window.pack();

	}

	private JPanel initializeGamePanel(StandardBoard gameBoard) {
		GameDisplay gameDisplay = new GameDisplay(gameBoard, squareSize);

		gameDisplay.setPreferredSize(new Dimension(640, 640));
		gameDisplay.setLayout(new BorderLayout());
		return gameDisplay;
	}

	private JPanel initializeSidePanel() {
		JPanel sideDisplay = new JPanel();
		undoButton = new JButton("Undo Move");
		restartButton = new JButton("Restart Game");
		forfeitButton = new JButton("Forfeit Game");
		setupButtonListeners();
		whiteLabel = new JLabel("WHITE PLAYER : ".concat(whitePlayer.playerName) + " ");
		whiteLabel.setForeground(Color.BLUE);
		blackLabel = new JLabel("BLACK PLAYER : ".concat(blackPlayer.playerName) + " ");
		whiteScore = new JLabel(whitePlayer.playerName + " Score : " + whitePlayer.playerScore);
		blackScore = new JLabel(blackPlayer.playerName + " Score : " + blackPlayer.playerScore);
		sideDisplay.setLayout(new BoxLayout(sideDisplay, BoxLayout.PAGE_AXIS));
		sideDisplay.add(whiteLabel);
		sideDisplay.add(blackLabel);
		sideDisplay.add(undoButton);
		sideDisplay.add(forfeitButton);
		sideDisplay.add(restartButton);
		sideDisplay.add(whiteScore);
		sideDisplay.add(blackScore);
		return sideDisplay;
	}

	private void setupButtonListeners() {
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				undoMove();
			}
		});
		restartButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});
		forfeitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				forfeitGame();
			}
		});
	}

	public void mouseActions() {
		gamePanel.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent me) {
				int xOrigin = me.getX();
				int yOrigin = me.getY();
				xOrigin = xOrigin / squareSize;
				yOrigin = yOrigin / squareSize;
				yOrigin = 7 - yOrigin;
				movingPiece = gameBoard.squaresList[xOrigin][yOrigin].occupyingPiece;
			}

			public void mouseReleased(MouseEvent me) {
				int xDestination = me.getX();
				int yDestination = me.getY();
				xDestination = xDestination / squareSize;
				yDestination = yDestination / squareSize;
				yDestination = 7 - yDestination;
				if (movingPiece.color == gameTurn && movingPiece.canMove(xDestination, yDestination)) {
					Piece enemyPiece = null;
					if (gameBoard.squaresList[xDestination][yDestination].isOccupied)
						enemyPiece = gameBoard.squaresList[xDestination][yDestination].occupyingPiece;
					MoveCommand newCommand = new MoveCommand(movingPiece, enemyPiece, xDestination, yDestination);
					commandStack.add(newCommand);
					newCommand.execute();
					if (movingPiece.color.equals(ChessGame.Board.Color.white)) {
						gameTurn = gameTurn.opposite();
						blackLabel.setForeground(Color.BLUE);
						whiteLabel.setForeground(Color.BLACK);
						checkKingStatus(gameBoard.blackKingTracker);
					} else {
						gameTurn = gameTurn.opposite();
						whiteLabel.setForeground(Color.BLUE);
						blackLabel.setForeground(Color.BLACK);
						checkKingStatus(gameBoard.whiteKingTracker);
					}

				} else {
					messageBox("This is an Illegal Move!", "Illegal move message");
				}
			}
		});
	}

	protected void checkKingStatus(King kingToCheck) {
		Player currentPlayer;
		Player otherPlayer;
		if (kingToCheck.color == ChessGame.Board.Color.white) {
			currentPlayer = whitePlayer;
			otherPlayer = blackPlayer;
		} else {
			currentPlayer = blackPlayer;
			otherPlayer = whitePlayer;
		}
		if (kingToCheck.isKingInCheck(kingToCheck)) {
			if (kingToCheck.isKingCheckmate(kingToCheck)) {
				messageBox(
						currentPlayer.playerName
								+ " ,Your King is in Checkmate\nYou Lost\nPlease Click Restart to Play again",
						"GAME OVER!!");
				gameOver = true;
				otherPlayer.playerScore++;
				whiteScore.setText(whitePlayer.playerName + " Score : " + whitePlayer.playerScore);
				blackScore.setText(blackPlayer.playerName + " Score : " + blackPlayer.playerScore);
				return;
			}
			messageBox(currentPlayer.playerName + " ,Your King is in Check", "King in Check!!");
		}
	}

	private void undoMove() {
		if (!commandStack.isEmpty()) {
			MoveCommand move = commandStack.pop();
			move.undo();
			if (gameTurn == ChessGame.Board.Color.white) {
				blackLabel.setForeground(Color.BLUE);
				whiteLabel.setForeground(Color.BLACK);
			} else {
				whiteLabel.setForeground(Color.BLUE);
				blackLabel.setForeground(Color.BLACK);
			}
			gameTurn = gameTurn.opposite();
		}
	}

	private void restartGame() {
		String player;
		if (gameTurn.equals(ChessGame.Board.Color.white))
			player = blackPlayer.playerName;
		else
			player = whitePlayer.playerName;
		int response = JOptionPane.showConfirmDialog(null, player + " , would you like to restart?", "Restart",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			gameOver = true;
			window.setVisible(false);
			startNewGame();
		}
	}

	private void forfeitGame() {
		Player currentPlayer;
		Player otherPlayer;
		if (gameTurn == ChessGame.Board.Color.white) {
			currentPlayer = whitePlayer;
			otherPlayer = blackPlayer;
		} else {
			currentPlayer = blackPlayer;
			otherPlayer = whitePlayer;
		}
		int response = JOptionPane.showConfirmDialog(null,
				currentPlayer.playerName + " , Are you sure you want to forfeit", "Forfeit", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			gameOver = true;
			otherPlayer.playerScore++;
			whiteScore.setText(whitePlayer.playerName + " Score : " + whitePlayer.playerScore);
			blackScore.setText(blackPlayer.playerName + " Score : " + blackPlayer.playerScore);
			messageBox(currentPlayer.playerName + " ,You Lost\nPlease Click Restart to Play again", "GAME OVER!!");
		}
	}

	public static void main(String args[]) {
		getGamePlayers();
		startNewGame();
	}

	private static void startNewGame() {
		Game newGame = new Game();
		newGame.gameInit(false);
		newGame.setupDisplay();
		newGame.gameStart();
		newGame.mouseActions();

	}

	public static void messageBox(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
	}

}
