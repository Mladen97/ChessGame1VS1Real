package ChessGame;

import ChessGame.Board.Color;

/*
 * Subclass of a Piece specific to a Queen. This handles all movements the queen is capable
 * of making.
 */
public class Queen extends Piece {

	/*
	 * Queen constructor initializes name of piece to Queen. Every other
	 * parameter is initialized by superclass.
	 */
	public Queen(int initX, int initY, Color color, StandardBoard board) {
		super(initX, initY, board, color);
		this.nameOfPiece = "queen";
	}

	/*
	 * Queen specific implementation of abstract method. Check -
	 * chessGame.Piece#isValidSpecialMove(int, int)
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if (isValidQueenMove(xDisplacement, yDisplacement)) {
			int steps = Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement));
			int xDirection = xDisplacement / steps;
			int yDirection = yDisplacement / steps;
			// Check for obstacles in path of Queen.
			for (int i = 1; i < steps; i++) {
				Square squareToCheck = currentBoard.squaresList[xLocation + i * xDirection][yLocation + i * yDirection];
				if (squareToCheck.isOccupied)
					return false;
			}
			return true;
		}
		return false;
	}

	/*
	 * Method for Queen specific moves (Diagonal + Vertical + Horizontal)
	 */
	private boolean isValidQueenMove(int xDisplacement, int yDisplacement) {
		// Diagonal movement.
		if ((Math.abs(xDisplacement) == Math.abs(yDisplacement)) && xDisplacement != 0)
			return true;
		else {
			// Horizontal movement
			if (xDisplacement != 0 && yDisplacement == 0)
				return true;
			// Vertical movement
			else if (xDisplacement == 0 && yDisplacement != 0)
				return true;
			else
				return false;
		}
	}

}
