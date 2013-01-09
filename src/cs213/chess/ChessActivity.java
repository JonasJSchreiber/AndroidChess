package cs213.chess;

import java.util.ArrayList;
import java.util.Random;

import cs213.chess.R;
import cs213.chess.Definitions.*;
import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.view.View.*;


public class ChessActivity extends Activity {
	ChessView cv;
	ChessPiece[][] ChessBoard = new ChessPiece[8][8];
	ChessOperations co = new ChessOperations();
	ChessPiece[] pieceChar = new ChessPiece[33];
	int chooseIndex = -1;
	boolean whitesTurn = true;
	private static TextView displayText;  
	private Button newGame;
	private Button undo;
	private Button previous;
	private Button next;
	private Button draw;
	private Button resign;
	private Button ai;
	private int[] lastMove;
	private ChessPiece prevPiece;
	boolean undopressed;
	boolean started;
	private ArrayList<Integer[]> movedLog;
	private int currentPOSforReplay = 0;
	boolean wasfirstmove;
	boolean castlewaslast;	
	

	/** Called when the activity is first created. */	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.main);
		cv = new ChessView(this);
		started = false;
		whitesTurn = true;
		LinearLayout linearMain;
        displayText = (TextView) findViewById(R.id.gameLog);
        lastMove = new int[4];
        undopressed = false;
        castlewaslast = false;
        
        newGame = (Button) findViewById(R.id.newGame);
        undo = (Button) findViewById(R.id.undo);
        previous = (Button) findViewById(R.id.Previous);
        next = (Button) findViewById(R.id.Next); 
        draw= (Button) findViewById(R.id.draw);
        resign= (Button) findViewById(R.id.resign);
        ai = (Button) findViewById(R.id.ai);
        
        newGame.setEnabled(true);
        undo.setEnabled(true);
        draw.setEnabled(true);
        resign.setEnabled(true);
        ai.setEnabled(true);
        previous.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        
        newGame.setOnClickListener(new NewGameListener(this));
        undo.setOnClickListener(new undoListener());
        previous.setOnClickListener(new previousListener(this));
        next.setOnClickListener(new nextListener(this));
        draw.setOnClickListener(new drawListener(this));
        resign.setOnClickListener(new resignListener(this));
        ai.setOnClickListener(new aiListener());
        
		linearMain = (LinearLayout) findViewById(R.id.chessBoardView);
		linearMain.addView(cv);
		createPieces();

		if (null == savedInstanceState)
			boardSetup();
		else 
			ChessBoard = (ChessPiece[][]) savedInstanceState.getSerializable("chessBoard");
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("chessBoard", ChessBoard);
		outState.putInt("sss", 1);
	}
	protected void createPieces() {
		pieceChar[0] = new ChessPiece(pieceIndex.blackrook1);
		pieceChar[1] = new ChessPiece(pieceIndex.blackrook2);
		pieceChar[2] = new ChessPiece(pieceIndex.blackKnight1);
		pieceChar[3] = new ChessPiece(pieceIndex.blackKnight2);
		pieceChar[4] = new ChessPiece(pieceIndex.blackBishop1);
		pieceChar[5] = new ChessPiece(pieceIndex.blackBishop2);
		pieceChar[6] = new ChessPiece(pieceIndex.blackKing);
		pieceChar[7] = new ChessPiece(pieceIndex.blackQueen);
		pieceChar[8] = new ChessPiece(pieceIndex.blackPawn1);
		pieceChar[9] = new ChessPiece(pieceIndex.blackPawn2);
		pieceChar[10] = new ChessPiece(pieceIndex.blackPawn3);
		pieceChar[11] = new ChessPiece(pieceIndex.blackPawn4);
		pieceChar[12] = new ChessPiece(pieceIndex.blackPawn5);
		pieceChar[13] = new ChessPiece(pieceIndex.blackPawn6);
		pieceChar[14] = new ChessPiece(pieceIndex.blackPawn7);
		pieceChar[15] = new ChessPiece(pieceIndex.blackPawn8);
		
		pieceChar[16] = new ChessPiece(pieceIndex.whiterook1);
		pieceChar[17] = new ChessPiece(pieceIndex.whiterook2);
		pieceChar[18] = new ChessPiece(pieceIndex.whiteKnight1);
		pieceChar[19] = new ChessPiece(pieceIndex.whiteKnight2);
		pieceChar[20] = new ChessPiece(pieceIndex.whiteBishop1);
		pieceChar[21] = new ChessPiece(pieceIndex.whiteBishop2);
		pieceChar[22] = new ChessPiece(pieceIndex.whiteKing);
		pieceChar[23] = new ChessPiece(pieceIndex.whiteQueen);
		pieceChar[24] = new ChessPiece(pieceIndex.whitePawn1);
		pieceChar[25] = new ChessPiece(pieceIndex.whitePawn2);
		pieceChar[26] = new ChessPiece(pieceIndex.whitePawn3);
		pieceChar[27] = new ChessPiece(pieceIndex.whitePawn4);
		pieceChar[28] = new ChessPiece(pieceIndex.whitePawn5);
		pieceChar[29] = new ChessPiece(pieceIndex.whitePawn6);
		pieceChar[30] = new ChessPiece(pieceIndex.whitePawn7);
		pieceChar[31] = new ChessPiece(pieceIndex.whitePawn8);
		pieceChar[32] = new ChessPiece(pieceIndex.none);
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onStop() {
		super.onStop();
	}
	
	protected void boardSetup() {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				ChessBoard[x][y] = pieceChar[32];
			}
		}

		ChessBoard[0][0] = pieceChar[0];
		ChessBoard[1][0] = pieceChar[2];
		ChessBoard[2][0] = pieceChar[4];
		ChessBoard[3][0] = pieceChar[7];
		ChessBoard[4][0] = pieceChar[6];
		ChessBoard[5][0] = pieceChar[5];
		ChessBoard[6][0] = pieceChar[3];
		ChessBoard[7][0] = pieceChar[1];
		ChessBoard[0][1] = pieceChar[8];
		ChessBoard[1][1] = pieceChar[9];
		ChessBoard[2][1] = pieceChar[10];
		ChessBoard[3][1] = pieceChar[11];
		ChessBoard[4][1] = pieceChar[12];
		ChessBoard[5][1] = pieceChar[13];
		ChessBoard[6][1] = pieceChar[14];
		ChessBoard[7][1] = pieceChar[15];

		ChessBoard[0][7] = pieceChar[16];
		ChessBoard[1][7] = pieceChar[18];
		ChessBoard[2][7] = pieceChar[20];
		ChessBoard[3][7] = pieceChar[23];
		ChessBoard[4][7] = pieceChar[22];
		ChessBoard[5][7] = pieceChar[21];
		ChessBoard[6][7] = pieceChar[19];
		ChessBoard[7][7] = pieceChar[17];
		ChessBoard[0][6] = pieceChar[24];
		ChessBoard[1][6] = pieceChar[25];
		ChessBoard[2][6] = pieceChar[26];
		ChessBoard[3][6] = pieceChar[27];
		ChessBoard[4][6] = pieceChar[28];
		ChessBoard[5][6] = pieceChar[29];
		ChessBoard[6][6] = pieceChar[30];
		ChessBoard[7][6] = pieceChar[31];
	}
	
	protected boolean movePiece(int fromX, int fromY, int toX, int toY) {
		boolean bRtn =false;  		
		if (8 == fromX || 8 == fromY)
			return  bRtn ;

		if (8 == toX || 8 == toY) {
			ChessBoard[fromX][fromY].afterMove(false);
			return  bRtn;
		}
		
		if (ChessBoard[fromX][fromY].selected == false)
			return  bRtn;
		
		if (ChessBoard[fromX][fromY].team == whitesTurn)
			return false;
		
		for (int i = 0; i < ChessBoard[fromX][fromY].canMove.size(); i++) 
		{
			Integer movePattern[] = ChessBoard[fromX][fromY].canMove.get(i);	
			if (movePattern[0] == toX && movePattern[1] == toY)
			{
				char x = row(fromX);
				String Move = (x + "" + (8-fromY) + " " + row(toX) + (8-toY));
				char whoseTurn = 'w';
				castlewaslast = false;
				int[] startingPoint = new int[2];
				int[] endingPoint = new int[2];
				startingPoint[0] = 7-fromY;
				startingPoint[1] = fromX;
				endingPoint[0] = 7-toY;
				endingPoint[1] = toX;
				if (!whitesTurn)
					whoseTurn = 'b';
				int result = co.checkChecker(whoseTurn, startingPoint, endingPoint, ChessBoard);
				if (result == 0) {
					addToTextView("That move would cause you to be in check, try again.");
					return false;
				}
				if (result == 2)
					Move += " Check";
				if (result == 3) 
				{
					Move += " CheckMate!";
					final Context context = this;
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					String turn = "";
					if(whitesTurn)
						turn = "White";
					else
						turn = "Black";
					String message = (turn += " Wins");
					builder.setTitle("Checkmate!");
					builder.setMessage(message);
					builder.setNeutralButton("Start New Game", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							onCreate(null);	
							co = new ChessOperations();
						}
					});
					builder.setNegativeButton("Save Game", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							
							AlertDialog.Builder savePop = new AlertDialog.Builder(context);
							savePop.setTitle("Please type a file name to save.");
							
							final EditText input = new EditText(context); 
							savePop.setView(input);
							
							savePop.setNegativeButton("Ok", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialog, int whichButton) {  
							        String value = input.getText().toString();
							        ArrayList<Integer[]> movedList = co.getPositionList();
							        //co.saveArrayData(movedList, value);
							        LogControl moveData = new LogControl(context);
							        moveData.SetLog(movedList);
							        moveData.saveGame(value);
							       }  
							     });  

							    savePop.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

							        public void onClick(DialogInterface dialog, int which) {
							            return;   
							        }
							    });


							savePop.show();
						}
					});
					builder.show();
				}
				if (result == 4)
				{
					Move += " Stalemate";
					final Context context = this;
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					String message = ("Game ends in draw");
					builder.setTitle("Stalemate");
					builder.setMessage(message);
					builder.setNeutralButton("Start New Game", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							onCreate(null);	
							co = new ChessOperations();
						}
					});
					builder.setNegativeButton("Save Game", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							
							AlertDialog.Builder savePop = new AlertDialog.Builder(context);
							savePop.setTitle("Please type a file name to save.");
							
							final EditText input = new EditText(context); 
							savePop.setView(input);
							
							savePop.setNegativeButton("Ok", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialog, int whichButton) {  
							        String value = input.getText().toString();
							        ArrayList<Integer[]> movedList = co.getPositionList();
							        //co.saveArrayData(movedList, value);
							        LogControl moveData = new LogControl(context);
							        moveData.SetLog(movedList);
							        moveData.saveGame(value);
							       }  
							     });  
							    savePop.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {

							        public void onClick(DialogInterface dialog, int which) {
							            return;   
							        }
							    });
							savePop.show();
						}
					});
					builder.show();
				}
				co.addToMovesList(Move);
				prevPiece = ChessBoard[toX][toY];
				wasfirstmove = ChessBoard[fromX][fromY].mIsFirstMove;
				if ((ChessBoard[fromX][fromY].name.equals("King")) && Math.abs(fromX - toX) == 2)
				{
					if (ChessBoard[fromX][fromY].mIsFirstMove == false)
					{
						addToTextView("You cannot castle, as you have already moved your King");
						return false;
					}
					if ((toY == 7 && fromY == 7) || (toY == 0 && fromY == 0)) //whiteKing
					{
						if (fromX - toX == 2) //queenside castle
						{
							if (ChessBoard[1][toY] != pieceChar[32])
								return false;
							if (ChessBoard[2][toY] != pieceChar[32])
								return false;
							if (ChessBoard[3][toY] != pieceChar[32])
								return false;
							ChessBoard[toX][toY] = ChessBoard[fromX][fromY];
							ChessBoard[3][toY] = ChessBoard[0][toY];
							ChessBoard[0][toY] = pieceChar[32];
							ChessBoard[fromX][fromY] = pieceChar[32];
							ChessBoard[toX][toY].afterMove(true); 
							recordLastMove(fromX, fromY, toX, toY);
							castlewaslast = true;
							whitesTurn = !whitesTurn;
							undopressed = false;
							undo.setEnabled(true);
							return true;
						}
						if (toX - fromX == 2) //kingside castle
						{
							if (ChessBoard[5][toY] != pieceChar[32])
									return false;
							if (ChessBoard[6][toY] != pieceChar[32])
								return false;
							ChessBoard[toX][toY] = ChessBoard[fromX][fromY];
							ChessBoard[5][toY] = ChessBoard[7][toY];
							ChessBoard[7][toY] = pieceChar[32];
							ChessBoard[fromX][fromY] = pieceChar[32];
							ChessBoard[toX][toY].afterMove(true); 
							recordLastMove(fromX, fromY, toX, toY);
							castlewaslast = true;
							whitesTurn = !whitesTurn;
							undopressed = false;
							undo.setEnabled(true);
							return true;
						}
					}
					return false;
				}
				ChessBoard[toX][toY] = ChessBoard[fromX][fromY];
				ChessBoard[fromX][fromY] = pieceChar[32];
				if (ChessBoard[toX][toY].name.equals("Pawn") && toY == 0)
					ChessBoard[toX][toY] = new ChessPiece(pieceIndex.whiteQueen2);
				if (ChessBoard[toX][toY].name.equals("Pawn") && toY == 7)
					ChessBoard[toX][toY] = new ChessPiece(pieceIndex.blackQueen2);
				ChessBoard[toX][toY].afterMove(true); 
				recordLastMove(fromX, fromY, toX, toY);
				started = true;
				undopressed = false;
				undo.setEnabled(true);
				whitesTurn = !whitesTurn;
				bRtn = true;
			}
		} 				
		return bRtn ;
	}
	
	protected void selectPiece(int X, int Y) {
		chooseIndex = -1;
		if (X == 8 || Y == 8)
			return;
		else if (pieceIndex.none == ChessBoard[X][Y].index) 
		{
			return;
		}
		else if (ChessBoard[X][Y] == pieceChar[32])
		{
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					ChessBoard[i][j].selected = false;
				}
			}
			return;
		}
		else if (ChessBoard[X][Y].team == whitesTurn)
			return;
		else 
		{
			ChessBoard[X][Y].selected = true;
			ChessBoard[X][Y].setMovablePos(X, Y, ChessBoard);
		}
	}
		
	private class NewGameListener implements OnClickListener {
        Context context;
        int SelectedItem;
        LogControl movedData;
        public NewGameListener(Context context)
        {
                this.context = context;
        }
        public void onClick(View cv) 
        {
            movedData = new LogControl(context);
            final CharSequence[] items = movedData.getSavedFiles();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select saved log on the list\n Otherwise start new game.");
            builder.setItems(items, new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int item) 
                {
                    SelectedItem = item;
                    AlertDialog.Builder subBuilder = new AlertDialog.Builder(context);
                    subBuilder.setTitle("Select Option");
                    subBuilder.setPositiveButton("Load", new DialogInterface.OnClickListener() 
                    {
                        public void onClick(DialogInterface dialog, int which) 
                        {
                        	onCreate(null);
	                        co = new ChessOperations();
	                        movedData.loadGame(SelectedItem);
	                        movedLog = movedData.log.MoveLog;
	                        undo.setVisibility(View.GONE);
	                        draw.setVisibility(View.GONE);
	                        resign.setVisibility(View.GONE);
	                        ai.setVisibility(View.GONE);
	                        previous.setVisibility(View.VISIBLE);
	                        next.setVisibility(View.VISIBLE);
	                        previous.setEnabled(true);
	                        next.setEnabled(true);
                        }
                    });

                	subBuilder.setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            movedData.removeGame(SelectedItem);
                            return;
                        }
                    });

            		subBuilder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    subBuilder.show();
                }
            });

            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    onCreate(null);
                    co = new ChessOperations();
                }
            });

            builder.setNeutralButton("Save Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    AlertDialog.Builder savePop = new AlertDialog.Builder(context);
                    savePop.setTitle("Save as: ");
                    final EditText input = new EditText(context);
                    savePop.setView(input);
                    savePop.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = input.getText().toString();
                            ArrayList<Integer[]> movedList = co.getPositionList();
                            LogControl moveData = new LogControl(context);
                            moveData.SetLog(movedList);
                            moveData.saveGame(value);
                        }
                    });
                    savePop.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                    savePop.show();
            	}
            });
            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            builder.show();
        }
	}
	
	private class undoListener implements OnClickListener {
		public void onClick(View cv) {
			if (undopressed == true || started == false)
				return;
			ChessBoard[lastMove[0]][lastMove[1]] = ChessBoard[lastMove[2]][lastMove[3]];
			ChessBoard[lastMove[2]][lastMove[3]] = prevPiece;
			co.removeLast();
			ChessPiece x = ChessBoard[lastMove[0]][lastMove[1]];
			x.mIsFirstMove = wasfirstmove;
			if (castlewaslast == true)
			{
				if (lastMove[2] == 2) //queenside
				{
					ChessBoard[0][lastMove[1]] = ChessBoard[3][lastMove[1]];
					ChessBoard[3][lastMove[1]] = pieceChar[32];
				}
				if (lastMove[2] == 6) //kingside
				{
					ChessBoard[7][lastMove[1]] = ChessBoard[5][lastMove[1]];
					ChessBoard[5][lastMove[1]] = pieceChar[32];
				}
			}
			View CBV = findViewById (R.id.chessBoardView);
			CBV.invalidate();
			undopressed = true;
			undo.setEnabled(false);
			whitesTurn = !whitesTurn;
		}
	}
	
	private class previousListener implements OnClickListener{
		
		@SuppressWarnings("unused")
		Context context;
		public previousListener(Context context)
		{
			this.context = context;
		}
		public void onClick(View cv)
		{
			onCreate(null);	
			co = new ChessOperations();
    		
			undo.setVisibility(View.GONE);
			draw.setVisibility(View.GONE);
			resign.setVisibility(View.GONE);
			ai.setVisibility(View.GONE);
			
			previous.setVisibility(View.VISIBLE);
			next.setVisibility(View.VISIBLE);
			previous.setEnabled(true);
			next.setEnabled(true);

			if(currentPOSforReplay>0)
			{
				co.resetMovedData();
				currentPOSforReplay--;
				for(int i=0; i<currentPOSforReplay;i++)
				{
					int fromX = movedLog.get(i)[0];
					int fromY = movedLog.get(i)[1];
					int toX = movedLog.get(i)[2];
					int toY = movedLog.get(i)[3];
					selectPiece(fromX, fromY);
					if(movePiece(fromX, fromY, toX, toY) == true) 
					{
						View CBV = findViewById (R.id.chessBoardView);
						CBV.invalidate();
					}
				}
			}
		}
	}
	
	private class nextListener implements OnClickListener{
		
		@SuppressWarnings("unused")
		Context context;
		
		public nextListener(Context context)
		{
			this.context = context;
		}
		
		public void onClick(View cv)
		{
			if(currentPOSforReplay<movedLog.size())
			{
				int fromX = movedLog.get(currentPOSforReplay)[0];
				int fromY = movedLog.get(currentPOSforReplay)[1];
				int toX = movedLog.get(currentPOSforReplay)[2];
				int toY = movedLog.get(currentPOSforReplay)[3];
				selectPiece(fromX, fromY);
				if(movePiece(fromX, fromY, toX, toY) == true) 
				{
					View CBV = findViewById (R.id.chessBoardView);
					CBV.invalidate();
				}
				currentPOSforReplay++;
			}
		}
	}
	
	private class drawListener implements OnClickListener {
		Context context;
		LogControl movedData;
		public drawListener(Context context)
		{
			this.context = context;
		}
		public void onClick(View cv) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			String turn;
			String dialogBox;
			if(whitesTurn)
			{
				turn = "white";
				dialogBox = "Black";
			}
			else
			{
				turn = "Black";
				dialogBox = "white";
			}
			String message = (turn + " is asking for a draw.\nDo you wish to accept?");
			builder.setTitle(dialogBox);
			builder.setMessage(message);
			builder.setPositiveButton("Draw", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog.Builder subBuilder = new AlertDialog.Builder(context);
					@SuppressWarnings("unused")
					String turn;
					if(whitesTurn)
						turn = "White";
					else
						turn = "Black";
					subBuilder.setTitle("Game ends in draw");
					subBuilder.setPositiveButton("Save Game", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							AlertDialog.Builder savePop = new AlertDialog.Builder(context);
							savePop.setTitle("Save as: ");
							final EditText input = new EditText(context); 
							savePop.setView(input);
							
							savePop.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialog, int whichButton) {  
							        String value = input.getText().toString();
							        ArrayList<Integer[]> movedList = co.getPositionList();
							        LogControl moveData = new LogControl(context);
							        moveData.SetLog(movedList);
							        moveData.saveGame(value);
							       }  
							     });  

							    savePop.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

							        public void onClick(DialogInterface dialog, int which) {
							            return;   
							        }
							    });
							savePop.show();
						}
					});
					
					subBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							movedData = new LogControl(context);
							final CharSequence[] items = movedData.getSavedFiles();
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Select a game to replay or start a new game.");
							builder.setItems(items, new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int item) {
							    	onCreate(null);	
									co = new ChessOperations();
							    	
							    	movedData.loadGame(item);
									movedLog = movedData.log.MoveLog;
									
									undo.setVisibility(View.GONE);
									draw.setVisibility(View.GONE);
									resign.setVisibility(View.GONE);
									ai.setVisibility(View.GONE);
									
									previous.setVisibility(View.VISIBLE);
									next.setVisibility(View.VISIBLE);
									previous.setEnabled(true);
									next.setEnabled(true);
							    }
							});
							builder.setNeutralButton("New Game", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									onCreate(null);	
									co = new ChessOperations();
								}
							});
							builder.show();
						}
					});
					subBuilder.show();
				}
			});
			
			builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			builder.show();
		}
	}
	
	private class resignListener implements OnClickListener {
		
		Context context;
		LogControl movedData;
		public resignListener(Context context)
		{
			this.context = context;
		}
		public void onClick(View cv) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			String turn = "";
			if(whitesTurn)
				turn = "white";
			else
				turn = "Black";
			String message = ("Resign game?");
			builder.setTitle(turn);
			builder.setMessage(message);
			builder.setPositiveButton("Resign", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					AlertDialog.Builder subBuilder = new AlertDialog.Builder(context);
					String notTurn = "";
					if(whitesTurn)
						notTurn = "Black";
					else
						notTurn = "white";
					subBuilder.setTitle("Game Resigned");
					subBuilder.setMessage(notTurn + " Wins");
					subBuilder.setPositiveButton("Save Game", new DialogInterface.OnClickListener() {					
						public void onClick(DialogInterface dialog, int which) {
							AlertDialog.Builder savePop = new AlertDialog.Builder(context);
							savePop.setTitle("Save as: ");
							final EditText input = new EditText(context); 
							savePop.setView(input);
							savePop.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
							    public void onClick(DialogInterface dialog, int whichButton) {  
							        String value = input.getText().toString();
							        ArrayList<Integer[]> movedList = co.getPositionList();
							        LogControl moveData = new LogControl(context);
							        moveData.SetLog(movedList);
							        moveData.saveGame(value);
							       }  
							     });
							    savePop.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							        public void onClick(DialogInterface dialog, int which) {
							            return;   
							        }
							    });
							savePop.show();
						}
					});
					subBuilder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {	
							movedData = new LogControl(context); 
							final CharSequence[] items = movedData.getSavedFiles();
							AlertDialog.Builder builder = new AlertDialog.Builder(context);
							builder.setTitle("Select a game to replay or start a new game.");
							builder.setItems(items, new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog, int item) {
									onCreate(null);	
									co = new ChessOperations();
							    	movedData.loadGame(item);
									movedLog = movedData.log.MoveLog;
	
									undo.setVisibility(View.GONE);
									draw.setVisibility(View.GONE);
									resign.setVisibility(View.GONE);
									ai.setVisibility(View.GONE);
									
									previous.setVisibility(View.VISIBLE);
									next.setVisibility(View.VISIBLE);
									previous.setEnabled(true);
									next.setEnabled(true);
							    }
							});
							builder.setNeutralButton("New Game", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									onCreate(null);	
									co = new ChessOperations();
								}
							});
							builder.show();
						}
					});
					subBuilder.show();
				}
			});
			builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			});
			builder.show();
		}
	}

	private class aiListener implements OnClickListener {
		public void onClick(View cv) {
			Random rand = new Random();
			int i = rand.nextInt(7);
			{
				for (int j = 0; j < 8; j++)
				{
					ChessPiece x = new ChessPiece();
					x=ChessBoard[i][j];
					if (x.name != null)
					{
						for (int k = 0; k < 8; k++) 
						{
							for (int l = 0; l < 8; l++)
							{
								selectPiece(i, j);
								if(movePiece(i, j, k, l) == true) 
								{
									View CBV = findViewById (R.id.chessBoardView);
									CBV.invalidate();
									return;
								}
							}
						}
					x = null;
					}
				}
			}
		}
	}
	
	public static void addToTextView(String text) {
		displayText.setText(text);
	}
	
	public void recordLastMove(int prevX, int prevY, int X, int Y) {
		lastMove[0] = prevX;
		lastMove[1] = prevY;
		lastMove[2] = X;
		lastMove[3] = Y;
		co.setPositionList(prevX, prevY, X, Y);
	}
	
	public char row (int index)
	{
		switch(index)
		{
			case 0: return 'a';
			case 1: return 'b';
			case 2: return 'c';
			case 3: return 'd'; 
			case 4: return 'e';
			case 5: return 'f';
			case 6: return 'g';
			case 7: return 'h';
			default: return 'z'; // should never happen
		}
	}
}
