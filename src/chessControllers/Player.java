package chessControllers;

import ChessGame.Board.Color;

public class Player {
	public String playerName;
	Color playerColor;
	public int playerScore;

	public Player(String playerName, Color playerColor) {
		this.playerName = playerName;
		this.playerColor = playerColor;
		this.playerScore = 0;
	}

}
