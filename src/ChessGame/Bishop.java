package ChessGame;

import ChessGame.Board.Color;
import ChessGame.Square;

public class Bishop extends Piece {

	public Bishop(int x, int y, StandardBoard board, Color color) {
		super(x, y, board, color);
		this.nameOfPiece = "bishop";
	}

	public static boolean isValidBishopMove(int xDisplacement, int yDisplacement) {
		if ((Math.abs(xDisplacement) == Math.abs(yDisplacement)) && xDisplacement != 0)
			return true;
		return false;
	}

	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if (isValidBishopMove(xDisplacement, yDisplacement)) {
			int steps = Math.abs(xDisplacement);
			int xDirection = xDisplacement / steps;
			int yDirection = yDisplacement / steps;
			// Check for obstacles in path of Bishop.
			for (int i = 1; i < steps; i++) {
				Square squareToCheck = currentBoard.squaresList[xLocation + i * xDirection][yLocation + i * yDirection];
				if (squareToCheck.isOccupied)
					return false;
			}
			return true;
		}
		return false;

	}

}
