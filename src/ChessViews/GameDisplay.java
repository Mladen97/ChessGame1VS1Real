package ChessViews;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import ChessGame.Board;
import ChessGame.Piece;
import ChessGame.Square;
import ChessGame.StandardBoard;

public class GameDisplay extends JPanel {
	StandardBoard board;
	int squareSize;

	public GameDisplay(StandardBoard gameBoard, int squareSize) {
		board = gameBoard;
		this.squareSize = squareSize;
	}

	/*
	 * Overrides Jpanel's paintComponent function to draw custom components in
	 * the Panel.
	 */
	@Override
	public void paintComponent(Graphics graphic) {
		for (int i = 0; i < board.numXSquares; i++) {
			for (int j = 0; j < board.numYSquares; j++) {
				Square squareToDraw = board.squaresList[i][j];
				if (squareToDraw.color.equals(Board.Color.black)) {
					graphic.setColor(new Color(58, 95, 205));
					graphic.fillRect((squareSize * i), (7 - j) * squareSize, squareSize, squareSize);
					if (squareToDraw.isOccupied)
						squareToDraw.occupyingPiece.drawPiece(graphic, squareSize, i, j);
				} else {
					graphic.setColor(new Color(230, 230, 250));
					graphic.fillRect((squareSize * i), (7 - j) * squareSize, squareSize, squareSize);
					if (squareToDraw.isOccupied)
						squareToDraw.occupyingPiece.drawPiece(graphic, squareSize, i, j);
				}
			}
		}
	}

}
