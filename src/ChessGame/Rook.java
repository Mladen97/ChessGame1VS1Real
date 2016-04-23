package ChessGame;

import ChessGame.Board.Color;

public class Rook extends Piece {

	public Rook(int initX, int initY, Color color, StandardBoard board) {
		super(initX, initY, board, color);
		this.nameOfPiece = "rook";
	}

	/*
	 * Rook specific implementation of abstract method.
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if (isValidRookMove(xDisplacement, yDisplacement)) {
			// Total number of steps the piece has to take. Either x = 0 or y =
			// 0.
			int steps = Math.max(Math.abs(xDisplacement), Math.abs(yDisplacement));
			int xDirection = xDisplacement / steps;
			int yDirection = yDisplacement / steps;
			// Check for obstacles in path of Rook.
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
	 * Method for Rook specific move check (Vertical + Horizontal)
	 */
	public static boolean isValidRookMove(int xDisplacement, int yDisplacement) {
		// Vertical
		if (xDisplacement != 0 && yDisplacement == 0)
			return true;
		// Horizontal
		else if (xDisplacement == 0 && yDisplacement != 0)
			return true;
		else
			return false;
	}

}
