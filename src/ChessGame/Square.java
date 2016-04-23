package ChessGame;

import ChessGame.Board.Color;

public class Square {
	public boolean isOccupied;
	public Color color;
	public Piece occupyingPiece;

	public Square(boolean isOccupied, Color color) {
		this.isOccupied = isOccupied;
		this.color = color;
		this.occupyingPiece = null;

	}

}
