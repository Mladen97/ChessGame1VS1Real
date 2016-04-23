package ChessGame;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import ChessGame.Board.Color;
import ChessGame.King;
import ChessGame.Piece;
import ChessGame.Square;

public abstract class Piece {
	String nameOfPiece;
	public Color color;
	StandardBoard currentBoard;
	public int xLocation;
	public int yLocation;

	public Piece(int x, int y, StandardBoard board, Color color) {
		this.color = color;
		this.xLocation = x;
		this.yLocation = y;
		this.currentBoard = board;

	}

	abstract boolean isValidSpecialMove(int newX, int newY);

	/*
	 * Determines whether a piece can be moved on the board. This method checks
	 * : - Is piece within board boundaries - Is piece following it's specific
	 * movement pattern - Is destination location unoccupied or filled by enemy
	 * piece for valid move/capture - Will moving this piece place it's king in
	 * danger (check)
	 */
	public boolean canMove(int newX, int newY) {
		if (!currentBoard.inBoardBounds(newX, newY))
			return false;
		if (!isValidSpecialMove(newX, newY))
			return false;
		if (!isEnemyPieceAtDestination(newX, newY))
			return false;
		if (isKingInDanger(newX, newY)) // Check the king!
			return false;
		return true;
	}

	private boolean isEnemyPieceAtDestination(int newX, int newY) {
		Square squareToCheck = currentBoard.squaresList[newX][newY];
		if (squareToCheck.isOccupied) {
			return isEnemyPiece(this.color, squareToCheck.occupyingPiece);
		}
		return true;
	}

	public void executeCaptureOrMove(int newX, int newY) {
		movePiece(this, newX, newY);
	}

	/*
	 * Called in 2 cases: - When the move is valid and needs to be executed. -
	 * To check if making this move would put ally king in danger.
	 */
	private void movePiece(Piece pieceToMove, int newPieceX, int newPieceY) {
		Square currentSquare = currentBoard.squaresList[pieceToMove.xLocation][pieceToMove.yLocation];
		Square targetSquare = currentBoard.squaresList[newPieceX][newPieceY];
		currentSquare.isOccupied = false;
		currentSquare.occupyingPiece = null;
		targetSquare.isOccupied = true;
		targetSquare.occupyingPiece = pieceToMove;
		pieceToMove.xLocation = newPieceX;
		pieceToMove.yLocation = newPieceY;
	}

	/*
	 * Comparing colors to determine ally or enemy.
	 */
	private boolean isEnemyPiece(Color colorToCheck, Piece occupyingPiece) {
		if (colorToCheck.equals(occupyingPiece.color))
			return false;
		else
			return true;
	}

	/*
	 * Method to draw the pieces on the board.
	 */
	public void drawPiece(Graphics graphic, int squareSize, int x, int y) {
		if (this.color.equals(Color.black)) {
			String name = this.nameOfPiece.concat(".png");
			String imagePath = "PictureOfFigures/black_";
			String imageName = imagePath.concat(name);
			drawPieceHelper(graphic, squareSize, imageName, x, y);
		} else {
			String name = this.nameOfPiece.concat(".png");
			String imagePath = "PictureOfFigures/white_";
			String imageName = imagePath.concat(name);
			drawPieceHelper(graphic, squareSize, imageName, x, y);
		}
	}

	/*
	 * Draw the piece in the proper coordinates on the board.
	 */
	private void drawPieceHelper(Graphics graphic, int squareSize, String imageName, int x, int y) {
		File imageFile = new File(imageName);
		BufferedImage image = null;
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int imageHeight = image.getHeight();
		int imageWidth = image.getWidth();
		int heightPadding = (squareSize - imageHeight) / 2;
		int widthPadding = (squareSize - imageWidth) / 2;
		graphic.drawImage(image, (squareSize * x) + widthPadding, ((7 - y) * squareSize) + heightPadding, imageWidth,
				imageHeight, null);
	}

	/*
	 * IMPORTANT method which determines if the king is in a check state. Takes
	 * in the king(white or black) that we want to check and it's location.
	 */
	public boolean isKingInCheck(King kingToCheck) {
		int kingXLocation = kingToCheck.xLocation;
		int kingYLocation = kingToCheck.yLocation;
		Color colorToCheck = kingToCheck.color;
		// Iterates through the squares on the board and checks if enemy pieces
		// can attack king.
		for (int i = 0; i < currentBoard.numXSquares; i++) {
			for (int j = 0; j < currentBoard.numYSquares; j++) {
				Square squareToCheck = currentBoard.squaresList[i][j];
				if (squareToCheck.isOccupied) {
					if (isEnemyPiece(colorToCheck, squareToCheck.occupyingPiece)) {
						Piece enemyPiece = squareToCheck.occupyingPiece;
						if (enemyPiece.isValidSpecialMove(kingXLocation, kingYLocation))
							return true;
					}
				}
			}
		}
		return false;

	}

	/*
	 * Determines if moving a piece puts it's king in danger
	 */
	private boolean isKingInDanger(int newPieceX, int newPieceY) {
		int oldPieceX = this.xLocation;
		int oldPieceY = this.yLocation;
		King kingToCheck;
		Square squareToCheck = currentBoard.squaresList[newPieceX][newPieceY];
		if (squareToCheck.isOccupied) {
			Piece pieceToCheck = squareToCheck.occupyingPiece;
			if (isEnemyPieceAtDestination(newPieceX, newPieceY)) {
				Piece enemyPiece = pieceToCheck;
				if (this.color.equals(Color.white)) {
					if (currentBoard.whiteKingTracker == null)
						return false;
					kingToCheck = currentBoard.whiteKingTracker;
				} else {
					if (currentBoard.blackKingTracker == null)
						return false;
					kingToCheck = currentBoard.blackKingTracker;
				}
				movePiece(this, newPieceX, newPieceY);
				if (isKingInCheck(kingToCheck)) {
					movePiece(this, oldPieceX, oldPieceY);
					movePiece(enemyPiece, newPieceX, newPieceY);
					return true;
				}
				movePiece(this, oldPieceX, oldPieceY);
				movePiece(enemyPiece, newPieceX, newPieceY);
			}
		} else {
			if (this.color.equals(Color.white)) {
				if (currentBoard.whiteKingTracker == null)
					return false;
				kingToCheck = currentBoard.whiteKingTracker;
			} else {
				if (currentBoard.blackKingTracker == null)
					return false;
				kingToCheck = currentBoard.blackKingTracker;
			}
			movePiece(this, newPieceX, newPieceY);
			if (isKingInCheck(kingToCheck)) {
				movePiece(this, oldPieceX, oldPieceY);
				return true;
			}
			movePiece(this, oldPieceX, oldPieceY);
		}
		return false;
	}

	/*
	 * Check if the king is in checkmate!
	 */
	public boolean isKingCheckmate(King kingToCheck) {
		if (!isKingInCheck(kingToCheck))
			return false;
		Color colorToCheck = kingToCheck.color;
		for (int i = 0; i < currentBoard.numXSquares; i++) {
			for (int j = 0; j < currentBoard.numYSquares; j++) {
				Square squareToCheck = currentBoard.squaresList[i][j];
				if (squareToCheck.isOccupied) {
					if (!isEnemyPiece(colorToCheck, squareToCheck.occupyingPiece)) {
						Piece allyPiece = squareToCheck.occupyingPiece;
						if (!checkmateHelper(allyPiece, kingToCheck))
							return false;
					}
				}
			}
		}
		return true;
	}

	/*
	 * Method to iterate through the pieces to check if any move can break the
	 * check.
	 */
	private boolean checkmateHelper(Piece allyPiece, King kingToCheck) {
		int oldPieceX = allyPiece.xLocation;
		int oldPieceY = allyPiece.yLocation;
		for (int i = 0; i < currentBoard.numXSquares; i++) {
			for (int j = 0; j < currentBoard.numYSquares; j++) {
				Square squareToCheck = currentBoard.squaresList[i][j];
				if (isEnemyPieceAtDestination(i, j)) {
					if (allyPiece.isValidSpecialMove(i, j)) {
						if (squareToCheck.isOccupied) {
							Piece enemyPiece = squareToCheck.occupyingPiece;
							movePiece(allyPiece, i, j);
							if (!isKingInCheck(kingToCheck)) {
								movePiece(allyPiece, oldPieceX, oldPieceY);
								movePiece(enemyPiece, i, j);
								return false;
							}
							movePiece(allyPiece, oldPieceX, oldPieceY);
							movePiece(enemyPiece, i, j);
						} else {
							movePiece(allyPiece, i, j);
							if (!isKingInCheck(kingToCheck)) {
								movePiece(allyPiece, oldPieceX, oldPieceY);
								return false;
							}
							movePiece(allyPiece, oldPieceX, oldPieceY);
						}
					}
				}
			}
		}
		return true;
	}
}
