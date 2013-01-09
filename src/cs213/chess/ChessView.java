package cs213.chess;

import cs213.chess.R;
import cs213.chess.Definitions.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.view.*;

public class ChessView extends View {
	final static int aniTimerHandle = 100;
	ChessActivity parentObject;
	public int State;
	int mViewWidth;
	int mViewHeight;
	int mPosX[] = new int[8];
	int mPosY[] = new int[8];
	int mTileSize; 
	int mPointingPosXY[] = new int[2];

	public ChessView(Context context)  
	{
		super(context);
		parentObject = (ChessActivity) context;
		mPointingPosXY[0] = 8;
		mPointingPosXY[1] = 8;
	}

	public boolean onTouchEvent(MotionEvent event) 
	{
		boolean bRtn;
		bRtn = super.onTouchEvent(event);
		switch (event.getAction()) 
		{
			case MotionEvent.ACTION_DOWN:
			int previousPosX = mPointingPosXY[0];
			int previousPosY = mPointingPosXY[1];
			determinePiece(event.getX(), event.getY());			
			parentObject.selectPiece(mPointingPosXY[0], mPointingPosXY[1]);
			boolean bIsmoved = parentObject.movePiece(previousPosX, previousPosY, mPointingPosXY[0], mPointingPosXY[1]);
			if (true == bIsmoved)
				mAniHandler.sendEmptyMessage(aniTimerHandle);
			invalidate();
			break;
			case MotionEvent.ACTION_UP: 
			break;
		}
		return bRtn;
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHeight = h;
		mTileSize = Math.min(mViewWidth, mViewHeight) / 8;
		for (int i = 0; i < 8; i++) {
			mPosX[i] = i * mTileSize;
			mPosY[i] = i * mTileSize;
		}

	}
	
	public void refresh() {
		mAniHandler.sendEmptyMessage(aniTimerHandle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Resources r = getResources();
		BitmapDrawable tile_white_drawable = (BitmapDrawable) r.getDrawable(R.drawable.tile_white);
		Bitmap tilewhite = tile_white_drawable.getBitmap();
		BitmapDrawable tileDarkDrawable = (BitmapDrawable) r.getDrawable(R.drawable.tile_dark);
		Bitmap tileDark = tileDarkDrawable.getBitmap();
		canvas.drawColor(Color.BLACK);
		Rect drawRec = new Rect();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				drawRec.set(mPosX[x], mPosY[y], mPosX[x] + mTileSize, mPosY[y] + mTileSize);
				if ((x + y) % 2 == 0) 
					canvas.drawBitmap(tilewhite, null, drawRec, null);
				else 
					canvas.drawBitmap(tileDark, null, drawRec, null);
			}
		}
		Rect pieceSquare = new Rect();
		BitmapDrawable pieceBitmap;
		Bitmap pieceImg;
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (pieceIndex.none != parentObject.ChessBoard[x][y].index) 
				{
					pieceBitmap = (BitmapDrawable) r.getDrawable(parentObject.ChessBoard[x][y].image);
					pieceImg = pieceBitmap.getBitmap();
					pieceSquare.set(mPosX[x], mPosY[y], mPosX[x] + mTileSize, mPosY[y] + mTileSize);
					canvas.drawBitmap(pieceImg, null, pieceSquare, null);
				}
			}
		}
		if (mPointingPosXY[0] != 8 && mPointingPosXY[1] != 8) 
		{
			Paint Pnt = new Paint();
			if (parentObject.ChessBoard[mPointingPosXY[0]][mPointingPosXY[1]].selected == true) 
			{
				pieceSquare.set(mPosX[mPointingPosXY[0]], mPosY[mPointingPosXY[1]], mPosX[mPointingPosXY[0]] + mTileSize, mPosY[mPointingPosXY[1]] + mTileSize);
				Pnt.setColor(0x400000ff);
				canvas.drawRect(pieceSquare, Pnt);
				int cntOfmovablePos = parentObject.ChessBoard[mPointingPosXY[0]][mPointingPosXY[1]].canMove.size();
				for (int i = 0; i < cntOfmovablePos; i++) {
					Integer movePattern[] = parentObject.ChessBoard[mPointingPosXY[0]][mPointingPosXY[1]].canMove.get(i);
					pieceSquare.set(mPosX[movePattern[0]], mPosY[movePattern[1]], mPosX[movePattern[0]] + mTileSize, mPosY[movePattern[1]] + mTileSize);
					Pnt.setColor(0x4000ff00);
					canvas.drawRect(pieceSquare, Pnt);
				}
			}
		}
	}
	
	
	private void determinePiece(float x, float y) {
		mPointingPosXY[0] = 8; 
		mPointingPosXY[1] = 8;
		for (int i = 7; i >= 0; i--) {
			if (x > (float) mPosX[i] && x < (float) (mPosX[i] + mTileSize)) 
			{
				mPointingPosXY[0] = i;
				break;
			}
		}

		for (int i = 7; i >= 0; i--) {
			if (y > (float) mPosY[i] && y < (float) (mPosY[i] + mTileSize)) 
			{
				mPointingPosXY[1] = i;
				break;
			}
		}
	}
	
	Handler mAniHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) 
			{
				case aniTimerHandle:				
					break;
				default:
					break;
			}

		}

	};

}
