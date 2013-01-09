package cs213.chess;

import java.util.*; 
import cs213.chess.Definitions.pieceIndex;

public class ChessOperations {
	private ArrayList<String> movesList;
	private ArrayList<Integer[]> positionList;
	ChessPiece[][] temp;
	
	public ChessOperations() {
		this.movesList = new ArrayList<String>();
		this.positionList = new ArrayList<Integer[]>();
	}
	
	public void resetMovedData()
	{
		movesList.clear();
		positionList.clear();
	}

	public ArrayList<String> getMovesList() {
		return movesList;
	}
	
	public void addToMovesList(String Move) {
		movesList.add(Move);
		saveArrayData(movesList, "game01");
	}
	
	public ArrayList<Integer[]> getPositionList()
	{
		return positionList;
	}
	
	public void removeLast() {
		movesList.remove(movesList.size()-1);
		positionList.remove(positionList.size()-1);
		saveArrayData(movesList, "game01");
	}
	
	public void setPositionList(int fromX, int fromY, int toX, int toY)
	{
		Integer[] addPosition = new Integer[4];
		addPosition[0]=fromX;
		addPosition[1]=fromY;
		addPosition[2]=toX;
		addPosition[3]=toY;
		positionList.add(addPosition);
	}
	
	public void saveArrayData(ArrayList<String> list, String filename)
	{
		String output = "";
		if (movesList.size() < 8)
		{
			for (int i = 0; i < movesList.size(); i++)
			{
				output += (movesList.get(i) + "\n");
			}
		}
		else 
		{
			for (int i = movesList.size()-7; i < movesList.size() ; i++)
			{
				output += (movesList.get(i) + "\n");
			}
		}
		ChessActivity.addToTextView(output);
	}
	
	public int checkChecker(char whoseTurn, int[] startingPoint, int[] endingPoint, ChessPiece[][] cb)
	{
		ChessPiece[][] temp = makeCopy(cb);
		String[][] cbString = getChessBoard(temp);
		return(determineOutcome(whoseTurn, startingPoint, endingPoint, cbString));
	}
	
	private ChessPiece[][] makeCopy(ChessPiece[][] cb)
	{
		ChessPiece[][] temp = new ChessPiece[8][8];
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j < 8; j++)
			{
				temp[i][j] = cb[i][j];
			}
		}
		return temp;
	}
	
	public String[][] getChessBoard(ChessPiece[][] cb)
	{
		String[][] board = new String[8][8];
		for (int i = 0; i < 8; i++) 
		{
			for (int j = 0; j < 8; j++)
			{
				board[i][j] = "  ";
			}
		}
		for (int i = 0; i < 8; i++)
		{
			for (int j = 0; j < 8; j++)
			{
				if (cb[i][j].index != pieceIndex.none)
				{
					String str = cb[i][j].name;
					if (str.equals("Pawn"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "p");
					}
					else if (str.equals("Bishop"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "B");
					}
					else if (str.equals("Rook"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "R");
					}
					else if (str.equals("Knight"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "N");
					}
					else if (str.equals("Queen"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "Q");
					}
					else if (str.equals("King"))
					{
						board[7-j][i]= (cb[i][j].index.name().charAt(0) + "K");
					}
				}
			}
		}
		return board;
	}
	public int determineOutcome(char whoseTurn, int[] startingPoint, int[] endingPoint, String[][] board)
	{
		/**
		 * Return Values:
		 * 0: Cannot Move There, Would Put Player Into Check
		 * 1. Can Move There, No Check
		 * 2. Can Move There, Check
		 * 3. Can Move There, CheckMate
		 * 4. Can Move There, StaleMate
		 * 
		 */
		String[][] tempBoard = new String[8][8];
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				tempBoard[i][j] = board[i][j];
			}
		}
		tempBoard = movePiece(startingPoint, endingPoint, board);
		if (whoseTurn == 'w')
		{
			if (isThisCheck(whoseTurn, tempBoard) == true)
				return 0;
			else if (isThisCheck('b', tempBoard))
			{
				if (isThisCheckmate('b', endingPoint, tempBoard) == true)
					return 3;
				else
					return 2;
			}
			else if (isThisStalemate('b', tempBoard) == true)
				return 4;
			else
				return 1;
		}
		else //if (whoseTurn == 'b')
		{
			if (isThisCheck(whoseTurn, tempBoard) == true)
				return 0;
			else if (isThisCheck('w', tempBoard))
			{
				if (isThisCheckmate('w', endingPoint, tempBoard) == true)
					return 3;
				else
					return 2;
			}
			else if (isThisStalemate('w', tempBoard) == true)
				return 4;
			else
				return 1;
		}
	}
	public int[] findKing(char whichKing, String[][] board)
	{
		int[] kingsLocation = new int[2];
		char[] kingLocale = {whichKing, 'K'};
		String king = new String(kingLocale);
		
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				if (board[i][j].equals(king))
				{
					kingsLocation[0] = i;
					kingsLocation[1] = j;
					return kingsLocation;
				}
			}
		}
		return kingsLocation;
	}
	public boolean isThisCheck(char whichKing, String[][] board)
	{
		int[] kingLocation = findKing(whichKing, board);
		int kingsI = kingLocation[0];
		int kingsJ = kingLocation[1];
		int[] startingPoint = new int[2];
		char piece;
		if (whichKing == 'w')
		{
			if (kingsI > 0)
			{
				if ((kingsJ -1 >= 0 && board[kingsI+1][kingsJ-1].equals("bp")) || (kingsJ+1 <= 7 && board[kingsI+1][kingsJ+1].equals("bp")))
					return true;
				else
				{
					for (int i = -1; i <= 1; i++)
					{
						for (int j = -1; j <= 1; j++)
						{
							if (kingsI + i >= 0 && kingsI + i <= 7 && kingsJ + j >= 0 && kingsJ + j <= 7)
								if (board[kingsI + i][kingsJ + j].equals("bK"))
										return true;
						}
					}
				}
			}
			for (int i = 7; i >= 0; i--)
			{
				for(int j = 0; j <= 7; j++)
				{
					if(board[i][j].equals("bR") || board[i][j].equals("bB") || board[i][j].equals("bN") || board[i][j].equals("bQ"))
					{
						startingPoint[0] = i;
						startingPoint[1] = j;
						piece = board[i][j].charAt(1);
						if (isThisMoveLegal(startingPoint, kingLocation, piece, board) == true)
							return true;
					}
				}
			}
			return false;
		}
		if (whichKing == 'b')
		{
			if (kingsI > 0)
			{
				if ((kingsJ - 1 >= 0 && board[kingsI-1][kingsJ-1].equals("wp")) || (kingsJ + 1 <= 7 && board[kingsI-1][kingsJ+1].equals("wp")))
					return true;
				for (int i = -1; i <= 1; i++)
				{
					for (int j = -1; j <= 1; j++)
					{
						if (kingsI + i >= 0 && kingsI + i <= 7 && kingsJ + j >= 0 && kingsJ + j <= 7)
							if (board[kingsI + i][kingsJ + j].equals("wK"))
									return true;
					}
				}
			}
			for (int i = 0; i <= 7; i++)
			{
				for(int j = 0; j <= 7; j++)
				{
					if(board[i][j].equals("wR") || board[i][j].equals("wB") || board[i][j].equals("wN") || board[i][j].equals("wQ"))
					{
						startingPoint[0] = i;
						startingPoint[1] = j;
						piece = board[i][j].charAt(1);
						if (isThisMoveLegal(startingPoint, kingLocation, piece, board) == true)
							return true;
					}
				}
			}
			return false;
		}
		return false;
	}
	public boolean isThisCheckmate(char whichKing, int[] lastMove, String[][] board)
	{
		int[] kingLocation = findKing(whichKing, board);
		int kingsI = kingLocation[0];
		int kingsJ = kingLocation[1];
		int[] startingPoint = new int[2];
		int[] endingPoint = new int[2];
		char piece;
		int[] direction;
		String[][] tempBoard = new String[8][8];
		String[][] protectedTempBoard = new String[8][8];
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				protectedTempBoard[i][j] = board[i][j];
			}
		}
		/*Can King Maneuver Out of Check)*/
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				if (kingsI + i >= 0 && kingsI + i <= 7 && kingsJ + j >= 0 && kingsJ + j <= 7)
				{
					if (protectedTempBoard[kingsI + i][kingsJ + j].charAt(0) != whichKing)
					{
						endingPoint[0] = kingLocation[0] + i;
						endingPoint[1] = kingLocation[1] + j;
						tempBoard = movePiece(kingLocation, endingPoint, protectedTempBoard);
						if (isThisCheck(whichKing, tempBoard) == false)
						{
							return false;
						}
					}
				}
			}
		}
		/*Can a piece take the piece threatening the King*/
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				if (protectedTempBoard[i][j].charAt(0) == whichKing)
				{
					startingPoint[0] = i;
					startingPoint[1] = j;
					piece = board[i][j].charAt(1);
					if (isThisMoveLegal(startingPoint, lastMove, piece, protectedTempBoard) == true)
					{
						tempBoard = movePiece(startingPoint, lastMove, protectedTempBoard);
						if (isThisCheck(whichKing, tempBoard) == false)
						{
							return false;
						}
					}
				}
			}
		}/* Can Pawns Get in Between */
		char[] tester = {whichKing, 'p'};
		String toTest = new String(tester);
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				if (toTest.equals(protectedTempBoard[i][j]))
				{
					startingPoint[0] = i;
					startingPoint[1] = j;
					if (whichKing == 'w')
						endingPoint[0] = i+1;
					if (whichKing == 'b')
						endingPoint[0] = i-1;
					if (i >= 1 && i <= 6)
					{
						for (int k = -1; k <= 1; k++)
						{
							if (j+k >= 0 && j+k <= 7)
							{
								endingPoint[1] = j+k;
								if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard) == true)
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck(whichKing, tempBoard) == false)
									{
										return false;
									}
								}
							}
						}
					}
					if ((i == 6 && whichKing == 'b') || (i == 1 && whichKing == 'w'))
					{
						endingPoint[1] = j;
						if (whichKing == 'w')
							endingPoint[0] = i + 2;
						if (whichKing == 'b')
							endingPoint[1] = i - 2;
						if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard) == true)
						{
							tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
							if (isThisCheck(whichKing, tempBoard) == false)
							{
								return false;
							}
						}
					}
				}
			}
		}
		/*determine if threat is from a Bishop, Queen, or Rook. If so, see if a piece can block this threat*/
		piece = getPiece(lastMove, protectedTempBoard);
		if (piece == 'B' || piece == 'Q' || piece == 'R')
		{
			if (Math.abs(lastMove[0] - kingsI) >= 2 || Math.abs(lastMove[1] - kingsJ) >= 2)
			{
				direction = getDirectionOfThreat(lastMove, kingLocation);
				if (canPiecesGetBetween(direction, kingLocation, protectedTempBoard) == true)
				{
					return false;
				}
			}
		}
		return true;
	}
	private char getPiece(int[] location, String[][] board)
	{
		char piece = ' ';
		int i = location[0];
		int j = location[1];
		if (i >= 0 && i <= 7 && j >= 0 && j <= 7)
			return board[i][j].charAt(1);
		else
			return piece;
	}
	private int[] getDirectionOfThreat (int[] lastMove, int kingLocation[])
	{
		int[] direction = new int[2];
		int i = lastMove[0];
		int j = lastMove[1];
		int kingsI = kingLocation[0];
		int kingsJ = kingLocation[1];
		if (i == kingsI)
		{
			if (j > kingsJ)
			{
				direction[0] = 1;
				direction[1] = 0;
				return direction;
			}
			else
			{
				direction[0] = -1;
				direction[1] = 0;
				return direction;
			}
		}
		else if (j == kingsJ)
		{
			if (i > kingsI)
			{
				direction[0] = 0;
				direction[1] = 1;
				return direction;
			}
			else
			{
				direction[0] = 0;
				direction[1] = -1;
				return direction;
			}
		}
		else if (Math.abs(i-kingsI) == Math.abs(j-kingsJ))
		{
			if (i > kingsI && j > kingsJ)
			{
				direction[0] = 1;
				direction[1] = 1;
				return direction;
			}
			else if (i > kingsI && j < kingsJ)
			{
				direction[0] = -1;
				direction[1] = 1;
				return direction;
			}
			else if (i < kingsI && j > kingsJ)
			{
				direction[0] = 1;
				direction[1] = -1;
				return direction;
			}
			else //if (i < kingsI && j < kingsJ)
			{
				direction[0] = -1;
				direction[1] = -1;
				return direction;
			}
			
		}
		return direction;
	}
	private boolean canPiecesGetBetween(int[] direction, int[] kingLocation, String[][] board)
	{
		int kingsI = kingLocation[0];
		int kingsJ = kingLocation[1];
		int k = kingsI;
		int l = kingsJ;
		int[] startingPoint = new int[2];
		int[] endingPoint = new int[2];
		char piece;
		String pieceExamined;
		char kingThreatened = board[kingsI][kingsJ].charAt(0); 
		if (direction[0] == 0)
		{
			if (direction[1] == 1)
			{
				k = k + 1;
				if (kingThreatened == 'w')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[1] = kingsJ;
								piece = board[i][j].charAt(1);
								while (k < 7)
								{
									endingPoint[0] = k;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									k += 1;
								}
							}
						}
					}
				}
				if (kingThreatened == 'b')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[1] = kingsJ;
								piece = board[i][j].charAt(1);
								while (k < 7)
								{
									endingPoint[0] = k;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									k += 1;
								}
							}
						}
					}
				}
				else
					return false;
			}
			if (direction[1] == -1)
			{
				k = k - 1;
				if (kingThreatened == 'w')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[1] = kingsJ;
								piece = board[i][j].charAt(1);
								while (k > 0)
								{
									endingPoint[0] = k;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									k -= 1;
								}
							}
						}
					}
				}
				if (kingThreatened == 'b')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[1] = kingsJ;
								piece = board[i][j].charAt(1);
								while (k > 0)
								{
									endingPoint[0] = k;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									k -= 1;
								}
							}
						}
					}
				}
				else
					return false;
			}
		}
		else if (direction[1] == 0)
		{
			l = l + 1;
			if (direction[0] == 1)
			{
				if (kingThreatened == 'w')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[0] = kingsI;
								piece = board[i][j].charAt(1);
								while (l < 7)
								{
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									l += 1;
								}
							}
						}
					}
				}
				if (kingThreatened == 'b')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[0] = kingsI;
								piece = board[i][j].charAt(1);
								while (l < 7)
								{
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									l += 1;
								}
							}
						}
					}
				}
				else
					return false;
			}
			if (direction[1] == -1)
			{
				l = l - 1;
				if (kingThreatened == 'w')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[0] = kingsI;
								piece = board[i][j].charAt(1);
								while (l > 0)
								{
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									l -= 1;
								}
							}
						}
					}
				}
				if (kingThreatened == 'b')
				{
					for (int i = 0; i <= 7; i++)
					{
						for (int j = 0; j <= 7; j++)
						{
							pieceExamined = board[i][j]; 
							if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
							{
								startingPoint[0] = i;
								startingPoint[1] = j;
								endingPoint[0] = kingsI;
								piece = board[i][j].charAt(1);
								while (l > 0)
								{
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
										return true;
									l -= 1;
								}
							}
						}
					}
				}
				else
					return false;
			}
			else if (direction[0] == 1)
			{
				k = k + 1;
				if (direction[1] == 1)
				{
					l = l + 1;	
					if (kingThreatened == 'w')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k < 7 && l < 7)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k += 1;
										l += 1;
									}
								}
							}
						}
					}
					if (kingThreatened == 'b')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k < 7 && l < 7)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k += 1;
										l += 1;
									}
								}
							}
						}
					}
					else
						return false;
				}
				if (direction[1] == -1)
				{
					l = l - 1;
					if (kingThreatened == 'w')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k < 7 && l > 0)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k += 1;
										l -= 1;
									}
								}
							}
						}
					}
					if (kingThreatened == 'b')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k < 7 && l > 0)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k += 1;
										l -= 1;
									}
								}
							}
						}
					}
					else
						return false;
				}
			}
			else if (direction[0] == -1)
			{
				k = k - 1;
				if (direction[1] == 1)
				{
					l = l + 1;	
					if (kingThreatened == 'w')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k > 0 && l < 7)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k -= 1;
										l += 1;
									}
								}
							}
						}
					}
					if (kingThreatened == 'b')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k > 0 && l < 7)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k -= 1;
										l += 1;
									}
								}
							}
						}
					}
					else
						return false;
				}
				if (direction[1] == -1)
				{
					l = l - 1;
					if (kingThreatened == 'w')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("wR") || pieceExamined.equals("wB") || pieceExamined.equals("wQ") || pieceExamined.equals("wN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k > 0 && l > 0)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k -= 1;
										l -= 1;
									}
								}
							}
						}
					}
					if (kingThreatened == 'b')
					{
						for (int i = 0; i <= 7; i++)
						{
							for (int j = 0; j <= 7; j++)
							{
								pieceExamined = board[i][j]; 
								if (pieceExamined.equals("bR") || pieceExamined.equals("bB") || pieceExamined.equals("bQ") || pieceExamined.equals("bN"))
								{
									startingPoint[0] = i;
									startingPoint[1] = j;
									piece = board[i][j].charAt(1);
									while (k > 0 && l > 0)
									{
										endingPoint[0] = k;
										endingPoint[1] = l;
										if (isThisMoveLegal(startingPoint, endingPoint, piece, board) == true)
											return true;
										k -= 1;
										l -= 1;
									}
								}
							}
						}
					}
					else
						return false;
				}
			}
		}
		return false;
	}
	public boolean isThisStalemate(char whichSide, String[][] board)
	{
		String[][] protectedTempBoard = new String[8][8];
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				protectedTempBoard[i][j] = board[i][j];
			}
		}
		String[][] tempBoard = new String[8][8];
		int[] startingPoint = new int[2];
		int[] endingPoint = new int[2];
		if (whichSide == 'w')
		{
			for (int i = 0; i <= 7; i++)
			{
				for (int j = 0; j <= 7; j++)
				{
					if (board[i][j].charAt(0) == 'w')
					{
						startingPoint[0] = i;
						startingPoint[1] = j;
						if (board[i][j].charAt(1) == 'p')
						{
							if (i == 1)
							{
								endingPoint[0] = i+2;
								endingPoint[1] = j;
								if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('w', tempBoard) == false)
										return false;
								}
								for (int k = -1; k <= 1; k++)
								{
									endingPoint[0] = i+k;
									endingPoint[1] = j+1;
									if (i + k > 0 && i + k <= 7 && j + 1 <= 7)
									{
										if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard))
										{
											tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
											if (isThisCheck('w', tempBoard) == false)
												return false;
										}
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'R' || board[i][j].charAt(1) == 'Q')
						{
							for (int k = 0; k <= 7; k++)
							{
								endingPoint[0] = i;
								endingPoint[1] = k;
								if (isThisMoveLegal(startingPoint, endingPoint, 'R', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('w', tempBoard) == false)
										return false;
								}
							}
							for (int k = 0; k <= 7; k++)
							{
								endingPoint[0] = k;
								endingPoint[1] = j;
								if (isThisMoveLegal(startingPoint, endingPoint, 'R', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('w', tempBoard) == false)
										return false;
								}
							}
						}
						if (board[i][j].charAt(1) == 'K')
						{
							for (int k = -1; k <= 1; k++)
							{
								for (int l = -1; l <= 1; l++)
								{
									if (k != 0 || l != 0)
									{
										if (i+k >= 0 && i+k <= 7 && j+l >= 0 && j+l <= 7)
										{
											endingPoint[0] = i+k;
											endingPoint[1] = j+l;
											if (board[i+k][j+l].charAt(0) != 'w')
											{
												tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
												if (isThisCheck('w', tempBoard) == false)
													return false;
											}
										}
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'B' || board[i][j].charAt(1) == 'Q')
						{
							for (int k = 0; k <= 7; k++)
							{
								for (int l = 0; l <= 7; l++)
								{
									endingPoint[0] = k;
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, 'B', protectedTempBoard))
									{
										tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
										if (isThisCheck('w', tempBoard) == false)
											return false;
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'N')
						{
							for (int k = -2; k <= 2; k++)
							{
								for (int l = -2; l <= 2; l++)
								{
									if (Math.abs(l + k) % 2 != 0 && k != 0 && l != 0)
									{
										if (i+k >= 0 && i+k <= 7 && j+l >= 0 && j+l <= 7)
										{
											endingPoint[0] = i+k;
											endingPoint[1] = j+l;
											if (isThisMoveLegal(startingPoint, endingPoint, 'N', protectedTempBoard))
											{
												tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
												if (isThisCheck('w', tempBoard) == false)
													return false;
											}
										}
									}
								}
							}
						}
						
					}
				}
			}
		}
		if (whichSide == 'b')
		{
			for (int i = 0; i <= 7; i++)
			{
				for (int j = 0; j <= 7; j++)
				{
					if (board[i][j].charAt(0) == 'b')
					{
						startingPoint[0] = i;
						startingPoint[1] = j;
						if (board[i][j].charAt(1) == 'p')
						{
							if (i == 6)
							{
								endingPoint[0] = i-2;
								endingPoint[1] = j;
								if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('b', tempBoard) == false)
										return false;
								}
								for (int k = -1; k <= 1; k++)
								{
									endingPoint[0] = i+k;
									endingPoint[1] = j-1;
									if (i + k > 0 && i + k <= 7 && j - 1 >= 0)
									{
										if (isThisMoveLegal(startingPoint, endingPoint, 'p', protectedTempBoard))
										{
											tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
											if (isThisCheck('b', tempBoard) == false)
												return false;
										}
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'R' || board[i][j].charAt(1) == 'Q')
						{
							for (int k = 0; k <= 7; k++)
							{
								endingPoint[0] = i;
								endingPoint[1] = k;
								if (isThisMoveLegal(startingPoint, endingPoint, 'R', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('b', tempBoard) == false)
										return false;
								}
							}
							for (int k = 0; k <= 7; k++)
							{
								endingPoint[0] = k;
								endingPoint[1] = j;
								if (isThisMoveLegal(startingPoint, endingPoint, 'R', protectedTempBoard))
								{
									tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
									if (isThisCheck('b', tempBoard) == false)
										return false;
								}
							}
						}
						if (board[i][j].charAt(1) == 'K')
						{
							for (int k = -1; k <= 1; k++)
							{
								for (int l = -1; l <= 1; l++)
								{
									if (i+k >= 0 && i+k <= 7 && j+l >= 0 && j+l <= 7)
									{
										endingPoint[0] = i+k;
										endingPoint[1] = j+l;
										if (board[i+k][j+l].charAt(0) != 'b')
										{
											tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
											if (isThisCheck('b', tempBoard) == false)
												return false;
										}
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'B' || board[i][j].charAt(1) == 'Q')
						{
							for (int k = 0; k <= 7; k++)
							{
								for (int l = 0; l <= 7; l++)
								{
									endingPoint[0] = k;
									endingPoint[1] = l;
									if (isThisMoveLegal(startingPoint, endingPoint, 'B', protectedTempBoard))
									{
										tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
										if (isThisCheck('b', tempBoard) == false)
											return false;
									}
								}
							}
						}
						if (board[i][j].charAt(1) == 'N')
						{
							for (int k = -2; k <= 2; k++)
							{
								for (int l = -2; l <= 2; l++)
								{
									if (Math.abs(l + k) % 2 != 0 && k != 0 && l != 0)
									{
										if (i+k >= 0 && i+k <= 7 && j+l >= 0 && j+l <= 7)
										{
											endingPoint[0] = i+k;
											endingPoint[1] = j+l;
											if (isThisMoveLegal(startingPoint, endingPoint, 'N', protectedTempBoard))
											{
												tempBoard = movePiece(startingPoint, endingPoint, protectedTempBoard);
												if (isThisCheck('b', tempBoard) == false)
													return false;
											}
										}
									}
								}
							}
						}
						
					}
				}
			}
		}
		return true;
	}
	public boolean isThisMoveLegal(int[] startingPoint, int[] endingPoint, char piece, String[][] board)
	{
		int i = startingPoint[0];
		int j = startingPoint[1];
		int i2 = endingPoint[0];
		int j2 = endingPoint[1];
		if (board[i2][j2].charAt(0) == board[i][j].charAt(0))
			return false;
		if (piece == 'p') 
		{
			if (board[i][j].equals("bp"))
			{
				if (j == j2)
					if (i-i2 == 1 || i-i2 == 2)
					{
						if (board[i2][j2].charAt(0) == ' ' || board[i2][j2].charAt(0) == '#')
							if (board[i-1][j].charAt(0) != 'w' && board[i-1][j].charAt(0) != 'b')
								return true;
						else {
							return false;
						}
					}
					else {
						return false;
					}
				else if ((Math.abs(j - j2)) == 1)
					if (i-i2 == 1)
						if (board[i2][j2].charAt(0) == 'w')
							return true;
					return false;
			}
			if (board[i][j].equals("bp"))
			{
				if (j == j2)
					if (i2-i == 1 || i2-i == 2)
					{
						if (board[i2][j2].charAt(0) == ' ' || board[i2][j2].charAt(0) == '#')
						{
							if (board[i+1][j].charAt(0) != 'w' && board[i+1][j].charAt(0) != 'b')
								return true;
						}
						else 
							return false;
					}
					else 
						return false;
				else if ((Math.abs(j - j2)) == 1)
					if (i2-i == 1)
						if (board[i2][j2].charAt(0) == 'b')
							return true;						
					return false;
			}
			return false;
		}
		else if (piece == 'B') 
		{
			if (Math.abs(i - i2) == Math.abs(j - j2))
			{
				int spacesCovered = Math.abs(i-i2);
				if (i-i2 < 0)
				{
					if (j-j2 < 0)
					//direction = "NE";
					{
						for (int k = 1; k < spacesCovered; k++)
						{
							if (board[i+k][j+k].charAt(0) == 'w' || board[i+k][j+k].charAt(0) == 'b')
								return false;
						}
						return true;
					}
					else
					//direction = "NW";
					{
						for (int k = 1; k < spacesCovered; k++)
						{
							if (board[i+k][j-k].charAt(0) == 'w' || board[i+k][j-k].charAt(0) == 'b')
								return false;
						}
						return true;
					}
				}
				else if (i - i2 > 0)
				{
					if (j-j2 < 0)
					//direction = "SE";
					{
						for (int k = 1; k < spacesCovered; k++)
						{
							if (board[i-k][j+k].charAt(0) == 'w' || board[i-k][j+k].charAt(0) == 'b')
								return false;
						}
						return true;
					}
					else 
					//direction = "SW";
					{
						for (int k = 1; k < spacesCovered; k++)
						{
							if (board[i-k][j-k].charAt(0) == 'w' || board[i-k][j-k].charAt(0) == 'b')
								return false;
						}
						return true;
					}
				}
				else 
					return false;
			}
		}
		else if (piece == 'R') 
		{
			if (i == i2 || j == j2)
			{
				int spacesCovered;
				if (i-i2 < 0)
				{
					//direction = 'N';
					spacesCovered = i2-i;
					for (int k = 1; k < spacesCovered; k++)
					{
						if (board[i+k][j].charAt(0) == 'w' || board[i+k][j].charAt(0) == 'b')
							return false;
					}
					return true;
					
				}
				else if (i - i2 > 0)
				{
					//direction = 'S';
					spacesCovered = i-i2;
					for (int k = 1; k < spacesCovered; k++)
					{
						if (board[i-k][j].charAt(0) == 'w' || board[i-k][j].charAt(0) == 'b')
							return false;
					}
					return true;
				}
				else if (j - j2 < 0)
				{
					//direction = 'E';
					spacesCovered = j2-j;
					for (int k = 1; k < spacesCovered; k++)
					{
						if (board[i][j+k].charAt(0) == 'w' || board[i][j+k].charAt(0) == 'b')
							return false;
					}
					return true;
				}
				else if (j - j2 > 0)
				{
					//direction = 'W';
					spacesCovered = j-j2;
					for (int k = 1; k < spacesCovered; k++)
					{
						if (board[i][j-k].charAt(0) == 'w' || board[i][j-k].charAt(0) == 'b')
							return false;
					}
					return true;
				}
				else 
					return false;
			}
			else
				return false;
		}
		else if (piece == 'Q') 
		{
			if (Math.abs(i - i2) == Math.abs(j - j2))
				return isThisMoveLegal(startingPoint, endingPoint, 'B', board);
			else if (i == i2 || j == j2)
				return isThisMoveLegal(startingPoint, endingPoint, 'R', board);
			else
				return false;
		}
		else if (piece == 'K') {
			if ((Math.abs(i - i2) <2) && (Math.abs(j - j2) <2))
				if (i == i2 && j == j2)
					return false;
				else
					return true;
			else
				return false;		
		}
		if (piece == 'N') {
			if (Math.abs(i - i2) == 1)
				if (Math.abs(j - j2) == 2)
					return true;
			if (Math.abs(i - i2) == 2)
				if (Math.abs(j - j2) == 1)
					return true;
			return false;
		}
		return false;
	}
	private String[][] movePiece(int[] startingPoint, int[] endingPoint, String[][] board)
	{
		String[][] returnboard = new String[8][8];
		for (int i = 0; i <= 7; i++)
		{
			for (int j = 0; j <= 7; j++)
			{
				returnboard[i][j] = board[i][j];
			}
		}
		returnboard[endingPoint[0]][endingPoint[1]] = returnboard[startingPoint[0]][startingPoint[1]];
		returnboard[startingPoint[0]][startingPoint[1]] = "  ";
		return returnboard;
	}
}