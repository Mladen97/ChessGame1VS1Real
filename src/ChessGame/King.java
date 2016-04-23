package ChessGame;

import ChessGame.Board.Color;

/*
 * Subclass of a Piece specific to a King. This handles all movements the king
 * is capable of making.
 *
 */
public class King extends Piece {

	/*
	 * King constructor initializes name of piece to King. Every other parameter
	 * is initialized by superclass.
	 */
	public King(int initX, int initY, Color color, StandardBoard board) {
		super(initX, initY, board, color);
		this.nameOfPiece = "king";
	}

	/*
	 * King specific implementation of abstract method. see
	 * chessGame.Piece#isValidSpecialMove(int, int)
	 */
	@Override
	boolean isValidSpecialMove(int newX, int newY) {
		int xDisplacement = newX - xLocation;
		int yDisplacement = newY - yLocation;
		if (isValidKingMove(xDisplacement, yDisplacement))
			return true;
		else
			return false;
	}

	/*
	 * Method for King specific move check (One step in all directions) - return
	 * boolean true if valid King move
	 */
	private boolean isValidKingMove(int xDisplacement, int yDisplacement) {
		// Diagonal
		if (Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 1)
			return true;
		// Horizontal
		else if (Math.abs(xDisplacement) == 1 && Math.abs(yDisplacement) == 0)
			return true;
		// Vertical
		else if (Math.abs(xDisplacement) == 0 && Math.abs(yDisplacement) == 1)
			return true;
		else
			return false;
	}

}