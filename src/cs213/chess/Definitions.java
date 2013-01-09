package cs213.chess;

public  class Definitions  {
	final static int Normal = 3;         	 
	final static int IfNotExist = 0;         
	final static int OnlyFirstTime = 4;
	final static int IfExist = 1;            		
	final static int DependOnPrevious = 2;   		
	final static int DependOnPreviousEnd = 5;   
	public static enum pieceIndex {
		blackrook1 ,
		blackrook2,
		blackKnight1,
		blackKnight2,
		blackBishop1,
		blackBishop2,
		blackKing,
		blackQueen,
		blackQueen2,
		blackPawn1,
		blackPawn2,
		blackPawn3,
		blackPawn4,
		blackPawn5,
		blackPawn6,
		blackPawn7,
		blackPawn8,
		whiterook1,
		whiterook2,
		whiteKnight1,
		whiteKnight2,
		whiteBishop1,
		whiteBishop2,
		whiteKing,
		whiteQueen,
		whiteQueen2,
		whitePawn1,
		whitePawn2,
		whitePawn3,
		whitePawn4,
		whitePawn5,
		whitePawn6,
		whitePawn7,
		whitePawn8,
		none
	}
}