package cs213.chess;

import java.io.*;
import java.util.*;

import cs213.chess.R;
import cs213.chess.Definitions.*;

public class ChessPiece implements Serializable {
	private static final long serialVersionUID = 1L;
	String name;
	pieceIndex index;
	Boolean team;
	int image;
	ArrayList<Integer[]> movePattern = new ArrayList<Integer[]>();
	ArrayList<Integer[]> canMove = new ArrayList<Integer[]>();
	Boolean selected;
	Boolean mIsFirstMove;    
	public ChessPiece() {
		return;
	}
	public ChessPiece(pieceIndex indexOfPiece) {
		index = indexOfPiece;
		selected = false;
		mIsFirstMove = true;
		switch (indexOfPiece) {
		case blackrook1:
		case blackrook2:
			name = "Rook";
			image = R.drawable.br;
			team = true;
			break;

		case blackKnight1:
		case blackKnight2:
			name = "Knight";
			image = R.drawable.bn;
			team = true;
			break;

		case blackBishop1:
		case blackBishop2:
			name = "Bishop";
			image = R.drawable.bb;
			team = true;
			break;

		case blackKing:
			name = "King";
			image = R.drawable.bk;
			team = true;
			break;

		case blackQueen:
		case blackQueen2:
			name = "Queen";
			image = R.drawable.bq;
			team = true;
			break;

		case blackPawn1:
		case blackPawn2:
		case blackPawn3:
		case blackPawn4:
		case blackPawn5:
		case blackPawn6:
		case blackPawn7:
		case blackPawn8:
			name = "Pawn";
			image = R.drawable.bp;
			team = true;
			break;

		case whiterook1:
		case whiterook2:
			name = "Rook";
			image = R.drawable.wr;
			team = false;
			break;

		case whiteKnight1:
		case whiteKnight2:
			name = "Knight";
			image = R.drawable.wn;
			team = false;
			break;

		case whiteBishop1:
		case whiteBishop2:
			name = "Bishop";
			image = R.drawable.wb;
			team = false;
			break;

		case whiteKing:
			name = "King";
			image = R.drawable.wk;
			team = false;
			break;

		case whiteQueen:
		case whiteQueen2:	
			name = "Queen";
			image = R.drawable.wq;
			team = false;
			break;

		case whitePawn1:
		case whitePawn2:
		case whitePawn3:
		case whitePawn4:
		case whitePawn5:
		case whitePawn6:
		case whitePawn7:
		case whitePawn8:
			name = "Pawn";
			image = R.drawable.wp;
			team = false;
			break;

		default:
			break;

		}
		switch (indexOfPiece) {
		case blackrook1:
		case blackrook2:
		case whiterook1:
		case whiterook2:
			this.movePattern.add(new Integer[] { 0, 1, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { 0, i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 0, -1, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { 0, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 1, 0, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { i, 0, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, 0, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { -i, 0, Definitions.DependOnPrevious });
			}
			break;

		case blackKnight1:
		case blackKnight2:
		case whiteKnight1:
		case whiteKnight2:
			for (int i = -1; i <= 1; i+=2)
			{
				this.movePattern.add(new Integer[] { 2*i, i, Definitions.Normal });
				this.movePattern.add(new Integer[] { 2*i, -i, Definitions.Normal });
				this.movePattern.add(new Integer[] { i, 2*i, Definitions.Normal });
				this.movePattern.add(new Integer[] { -i, 2*i, Definitions.Normal });
			}
			break;

		case blackBishop1:
		case blackBishop2:
		case whiteBishop1:
		case whiteBishop2:
			this.movePattern.add(new Integer[] { 1, 1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { i, i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, -1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { -i, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 1, -1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { i, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, 1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { -i, i, Definitions.DependOnPrevious });
			}
			break;

		case blackKing:
		case whiteKing:
			for (int i = -1; i <= 1; i++)
			{
				for (int j = -1; j <= 1; j++)
				{
					if (i != 0 || j != 0)
					{
						this.movePattern.add(new Integer[] { i, j, Definitions.Normal });
					}
				}
			}
			this.movePattern.add(new Integer[] { -2, 0, Definitions.OnlyFirstTime });
			this.movePattern.add(new Integer[] { 2, 0, Definitions.OnlyFirstTime });
			break;

		case blackQueen:
		case blackQueen2:
		case whiteQueen:
		case whiteQueen2:
			this.movePattern.add(new Integer[] { 0, 1, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { 0, i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 0, -1, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { 0, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 1, 0, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { i, 0, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, 0, Definitions.Normal });
			for (int i = 2; i <= 7; i++)
			{
				this.movePattern.add(new Integer[] { -i, 0, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 1, 1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { i, i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, -1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { -i, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { 1, -1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { i, -i, Definitions.DependOnPrevious });
			}
			this.movePattern.add(new Integer[] { -1, 1, Definitions.Normal });
			for (int i = 2; i < 8; i++)
			{
				this.movePattern.add(new Integer[] { -i, i, Definitions.DependOnPrevious });
			}
			break;

		case blackPawn1:
		case blackPawn2:
		case blackPawn3:
		case blackPawn4:
		case blackPawn5:
		case blackPawn6:
		case blackPawn7:
		case blackPawn8:
			this.movePattern.add(new Integer[] { 1, 1, Definitions.IfExist });
			this.movePattern.add(new Integer[] { 0, 1, Definitions.IfNotExist });
			this.movePattern.add(new Integer[] { 0, 2, Definitions.DependOnPrevious });
			this.movePattern.add(new Integer[] { -1, 1, Definitions.IfExist });
			break;

		case whitePawn1:
		case whitePawn2:
		case whitePawn3:
		case whitePawn4:
		case whitePawn5:
		case whitePawn6:
		case whitePawn7:
		case whitePawn8:
			this.movePattern.add(new Integer[] { 1, -1, Definitions.IfExist });
			this.movePattern.add(new Integer[] { 0, -1, Definitions.IfNotExist });
			this.movePattern.add(new Integer[] { 0, -2, Definitions.DependOnPrevious });
			this.movePattern.add(new Integer[] { -1, -1, Definitions.IfExist });
			break;
		}
		return;
	}
	
	public void setMovablePos(int X, int Y, ChessPiece[][] ChessBoard) {
		canMove.clear();
		Integer movingPattern[];
		boolean isDependOnPrevious = false; 
		for (int i = 0; i < movePattern.size(); i++) {
			movingPattern = this.movePattern.get(i);

			int resultX = movingPattern[0] + X;
			int resultY = movingPattern[1] + Y;
			if (resultX < 0 || resultX > 7 || resultY < 0 || resultY > 7) 
				continue;
			switch (this.index) {
			case blackPawn1:
			case blackPawn2:
			case blackPawn3:
			case blackPawn4:
			case blackPawn5:
			case blackPawn6:
			case blackPawn7:
			case blackPawn8:
			case whitePawn1:
			case whitePawn2:
			case whitePawn3:
			case whitePawn4:
			case whitePawn5:
			case whitePawn6:
			case whitePawn7:
			case whitePawn8:
				if (ChessBoard[resultX][resultY].index == pieceIndex.none 
					&& movingPattern[2] == Definitions.IfNotExist)
				{
					canMove.add(new Integer[] { resultX, resultY });
					isDependOnPrevious = true;
				}
				else if (ChessBoard[resultX][resultY].index != pieceIndex.none)
						isDependOnPrevious = false;
				else if (movingPattern[2] == Definitions.DependOnPrevious
						&& isDependOnPrevious == true
						&& this.mIsFirstMove == true)
					canMove.add(new Integer[] { resultX, resultY });
				else if (movingPattern[2] == Definitions.IfExist 
						&& ChessBoard[resultX][resultY].index != pieceIndex.none 
						&& ChessBoard[resultX][resultY].team != this.team) 
					canMove.add(new Integer[] { resultX, resultY });
				break;
				
			case whiteKing:
			case blackKing:
				 if (ChessBoard[resultX][resultY].index == pieceIndex.none	
					&& movingPattern[2] == Definitions.OnlyFirstTime
					&& this.mIsFirstMove == true)
					 canMove.add(new Integer[] { resultX, resultY });
				 
			case blackrook1:
			case blackrook2:
			case whiterook1:
			case whiterook2:
			case blackBishop1:
			case blackBishop2:
			case whiteBishop1:
			case whiteBishop2:
			case blackQueen:
			case blackQueen2:
			case whiteQueen:
			case whiteQueen2:
				if (movingPattern[2] == Definitions.Normal) 
				{
					if (ChessBoard[resultX][resultY].index == pieceIndex.none) 
					{
						isDependOnPrevious = true;
						canMove.add(new Integer[] { resultX, resultY });
					} 
					else if (ChessBoard[resultX][resultY].index != pieceIndex.none && ChessBoard[resultX][resultY].team != this.team) {
						isDependOnPrevious = false;
						canMove.add(new Integer[] { resultX, resultY });
					} 
					else 
						isDependOnPrevious = false;
				} 
				else if (movingPattern[2] == Definitions.DependOnPrevious) 
				{
					if (isDependOnPrevious == true) 
					{
						if (ChessBoard[resultX][resultY].index == pieceIndex.none) 
							canMove.add(new Integer[] { resultX, resultY });
						else if (ChessBoard[resultX][resultY].index != pieceIndex.none && ChessBoard[resultX][resultY].team != this.team) 
						{
							canMove.add(new Integer[] { resultX, resultY });
							isDependOnPrevious = false;
						} 
						else if (ChessBoard[resultX][resultY].index != pieceIndex.none && ChessBoard[resultX][resultY].team == this.team)
							isDependOnPrevious = false;
					} 
				}
				break;

			default:
				if (movingPattern[2] == Definitions.Normal) {
					if (ChessBoard[resultX][resultY].index == pieceIndex.none)
						canMove.add(new Integer[] { resultX, resultY });
					else if (ChessBoard[resultX][resultY].index != pieceIndex.none && ChessBoard[resultX][resultY].team != this.team)
						canMove.add(new Integer[] { resultX, resultY });
				}
				break;
			}
		}
		return;
	}
	
	
	public void afterMove(boolean moved) {
		if (true == moved) {
			this.selected = false;
			this.mIsFirstMove = false;
		} 
		else
			this.selected = false;
		return;
	}
}