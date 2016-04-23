package ChessGame;
import ChessGame.Board.Color;
import ChessGame.Square;
import ChessGame.Bishop;
import ChessGame.King;
import ChessGame.Knight;
import ChessGame.Pawn;
import ChessGame.Queen;
import ChessGame.Rook;

public class StandardBoard extends Board{

	/*
	 * Trackers for the white and black kings for check, checkmate and game ending conditions.
	 */
	public King whiteKingTracker;
	public King blackKingTracker;
	
	/*
	 *Initialize the chess board.
	 */
	public StandardBoard(int xSquares, int ySquares) {

		this.numXSquares = xSquares;
		this.numYSquares = ySquares;
		this.totalSquares = this.numXSquares * this.numYSquares;
		this.squaresList = new Square[this.numXSquares][this.numYSquares];
		populateBoardWithSquares();
		this.whiteKingTracker = null;
		this.blackKingTracker = null;
	}
	
	public void populateBoardWithSquares(){
		for (int i = 0; i < numXSquares; i++) {
			for (int j = 0; j < numYSquares; j++) {
				if(i%2==0){
					if(j%2==0){
						squaresList[i][j] = new Square(false,Color.black);
					}
					else {
						squaresList[i][j]= new Square(false, Color.white);
					}
				}
				else{
					if(j%2==0){
						squaresList[i][j]= new Square(false, Color.white);
					}
						else{
						squaresList[i][j]= new Square(false, Color.black);
						}
					
				}
			}
		}
	}
	
	/*
	 * Populate chess board with standard pieces.
	 */
	public void populateBoardWithPieces(boolean special) {
			setupKnights();
			setupBishops();
		setupPawns();
		setupRooks();
		setupQueens();
		setupKings();
	}
	
	

	/*
	 * Pawns in their initial positions.
	 */
	public void setupPawns(){
		for(int i = 0; i < 8; i++){
			Pawn newWhitePawn = new Pawn(i, 1, this, Color.white);
			Pawn newBlackPawn = new Pawn(i, 6, this, Color.black);
			this.squaresList[i][1].isOccupied = true;
			this.squaresList[i][6].isOccupied = true;
			this.squaresList[i][1].occupyingPiece = newWhitePawn;
			this.squaresList[i][6].occupyingPiece = newBlackPawn;
			
		}
	}
	
	/*
	 * Rooks in their initial positions.
	 */
	public void setupRooks(){
		Rook whiteRookOne = new Rook(0, 0, Color.white, this);
		Rook whiteRookTwo = new Rook(7, 0, Color.white, this);
		Rook blackRookOne = new Rook(0, 7, Color.black, this);
		Rook blackRookTwo = new Rook(7, 7, Color.black, this);
		this.squaresList[0][0].isOccupied = true;
		this.squaresList[7][0].isOccupied = true;
		this.squaresList[0][0].occupyingPiece = whiteRookOne;
		this.squaresList[7][0].occupyingPiece = whiteRookTwo;
		this.squaresList[0][7].isOccupied = true;
		this.squaresList[7][7].isOccupied = true;
		this.squaresList[0][7].occupyingPiece = blackRookOne;
		this.squaresList[7][7].occupyingPiece = blackRookTwo;
		
	}
	
	/*
	 * Bishops in their initial positions.
	 */
	public void setupBishops(){
		Bishop whiteBishopOne = new Bishop(2, 0, this, Color.white);
		Bishop whiteBishopTwo = new Bishop(5, 0, this, Color.white);
		Bishop blackBishopOne = new Bishop(2, 7, this, Color.black);
		Bishop blackBishopTwo = new Bishop(5, 7, this, Color.black);
		this.squaresList[2][0].isOccupied = true;
		this.squaresList[5][0].isOccupied = true;
		this.squaresList[2][0].occupyingPiece = whiteBishopOne;
		this.squaresList[5][0].occupyingPiece = whiteBishopTwo;
		this.squaresList[2][7].isOccupied = true;
		this.squaresList[5][7].isOccupied = true;
		this.squaresList[2][7].occupyingPiece = blackBishopOne;
		this.squaresList[5][7].occupyingPiece = blackBishopTwo;
	}
	
	/*
	 * Knights in their initial positions.
	 */
	public void setupKnights(){
		Knight whiteKnightOne = new Knight(1, 0, this, Color.white);
		Knight whiteKnightTwo = new Knight(6, 0, this, Color.white);
		Knight blackKnightOne = new Knight(1, 7, this, Color.black);
		Knight blackKnightTwo = new Knight(6, 7, this, Color.black);
		this.squaresList[1][0].isOccupied = true;
		this.squaresList[6][0].isOccupied = true;
		this.squaresList[1][0].occupyingPiece = whiteKnightOne;
		this.squaresList[6][0].occupyingPiece = whiteKnightTwo;
		this.squaresList[1][7].isOccupied = true;
		this.squaresList[6][7].isOccupied = true;
		this.squaresList[1][7].occupyingPiece = blackKnightOne;
		this.squaresList[6][7].occupyingPiece = blackKnightTwo;
	}
	
	/*
	 * Queens in their initial positions.
	 */	
	public void setupQueens(){
		Queen whiteQueen = new Queen(3, 0, Color.white, this);
		Queen blackQueen = new Queen(3, 7, Color.black, this);
		this.squaresList[3][0].isOccupied = true;
		this.squaresList[3][7].isOccupied = true;
		this.squaresList[3][0].occupyingPiece = whiteQueen;
		this.squaresList[3][7].occupyingPiece = blackQueen;
	}
	
	/*
	 * Kings in their initial positions.
	 */
	public void setupKings(){
		King whiteKing = new King(4, 0, Color.white, this);
		King blackKing = new King(4, 7, Color.black, this);
		this.squaresList[4][0].isOccupied = true;
		this.squaresList[4][7].isOccupied = true;
		this.squaresList[4][0].occupyingPiece = whiteKing;
		this.squaresList[4][7].occupyingPiece = blackKing;
		whiteKingTracker = whiteKing;
		blackKingTracker = blackKing;
	}
	
	public boolean inBoardBounds(int newX, int newY){
		if(newX < numXSquares && newY < numYSquares && newX > -1 && newY > -1){
			return true;
		}
		else
			return false;
	}
	
	
	

}
