package ChessGame;

import ChessGame.Board.Color;

public class Knight extends Piece {

	public Knight(int x, int y, StandardBoard board, Color color) {
		super(x, y, board, color);
		this.nameOfPiece = "knight";
	}

	public static boolean isValidKnightMove(int xDisplacement, int yDisplacement) {
		if (Math.abs(xDisplacement) == 2 && Math.abs(yDisplacement) == 1) {
			return true;
		} else if (Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 2) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if (isValidKnightMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}

}
