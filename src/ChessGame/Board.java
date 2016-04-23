package ChessGame;

public abstract class Board {
	public int numXSquares;
	public int numYSquares;
	public int totalSquares;
	public Square squaresList[][];

	abstract boolean inBoardBounds(int xLocation, int yLocation);

	public enum Color {
		white, black;

		public Color opposite() {
			if (this == white)
				return black;
			else
				return white;
		}
	}

}
