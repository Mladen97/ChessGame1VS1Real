package chessControllers;

import ChessGame.Piece;

public class MoveCommand {

	Piece movingPiece;
	Piece enemyPiece;
	int xDestination;
	int yDestination;
	int xOrigin;
	int yOrigin;

	public MoveCommand(Piece movingPiece, Piece enemyPiece, int xDestination, int yDestination) {
		this.movingPiece = movingPiece;
		this.enemyPiece = enemyPiece;
		this.xOrigin = movingPiece.xLocation;
		this.yOrigin = movingPiece.yLocation;
		this.xDestination = xDestination;
		this.yDestination = yDestination;
	}

	public void undo() {
		this.movingPiece.executeCaptureOrMove(xOrigin, yOrigin);
		if (this.enemyPiece != null)
			this.enemyPiece.executeCaptureOrMove(xDestination, yDestination);
	}

	public void execute() {
		movingPiece.executeCaptureOrMove(xDestination, yDestination);
	}

}
