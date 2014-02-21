/**
 * 
 */
package com.apps.puzzlez;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.apps.puzzlez.model.GameButton;
import com.apps.puzzlez.model.PuzzlePiece;

import com.apps.puzzlez.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * @author matt
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private static int NUM_PIECES = 9;
	
	private MainThread thread;
	private PuzzlePiece piece0,piece1,piece2,piece3,piece4,piece5,piece6,piece7,piece8;
	private GameButton shuffle;
	private WindowManager wm;
	private Display display;
	private List<PuzzlePiece> pieces;
	
	public MainGamePanel(Context context) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();
		
		pieces = new ArrayList<PuzzlePiece>();
		
		@SuppressWarnings("deprecation")
		int screen_width = display.getWidth();
		int width_spacer = (screen_width - 300) / 4;
		int height_spacer = width_spacer;
		
		Log.d(TAG, "SpaceW: " + width_spacer + ", SpaceH: " + height_spacer);
		
		// Initialize the puzzle pieces
		piece0 = new PuzzlePiece(0,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_0), 5, 5);
		piece1 = new PuzzlePiece(1,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_1), 110, 5);
		piece2 = new PuzzlePiece(2,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_2), 215, 5);
		piece3 = new PuzzlePiece(3,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_3), 5, 110);
		piece4 = new PuzzlePiece(4,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_4), 110, 110);
		piece5 = new PuzzlePiece(5,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_5), 215, 110);
		piece6 = new PuzzlePiece(6,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_6), 5, 215);
		piece7 = new PuzzlePiece(7,BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_7), 110, 215);
		piece8 = new PuzzlePiece(8, 215, 215);
		
		piece8.setOpenPiece(true);
		
		pieces.add(piece0);
		pieces.add(piece1);
		pieces.add(piece2);
		pieces.add(piece3);
		pieces.add(piece4);
		pieces.add(piece5);
		pieces.add(piece6);
		pieces.add(piece7);
		pieces.add(piece8);
		
		// Initialize the shuffle button
		shuffle = new GameButton(BitmapFactory.decodeResource(getResources(), R.drawable.shuffle_button),
								 BitmapFactory.decodeResource(getResources(), R.drawable.shuffle_button_pressed), 
								 75, 350);
		
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
	}

	public void shufflePuzzle() {
		Random randInt = new Random();
		List<Integer> positions = new ArrayList<Integer>();
		int pos;
		int free_pos;
		
		for (int i = 0; i < NUM_PIECES; i++) {
			// Get a random position between 0 and 8
			do {
				pos = randInt.nextInt(9);
			} while(positions.contains(pos));
			
			// set new position
			pieces.get(i).setPosition(pos);
			positions.add(pos);
		}
		
		free_pos = pieces.get(8).getPosition();
		findMoveablePieces(free_pos);
	}
	
	private void findMoveablePieces(int free_pos) {
		int rows = (int) Math.sqrt(NUM_PIECES);
		int free_row = free_pos / rows;
		int free_col = free_pos % rows;
		int row,col,pos;
		for (int i = 0; i < NUM_PIECES; i++) {
			pos = pieces.get(i).getPosition();
			row = pos / rows;
			col = pos % rows;
			
			if (row == free_row) {
				if (free_pos + 1 == pos || free_pos - 1 == pos) {
					pieces.get(i).setMoveable(true);
				} else {
					pieces.get(i).setMoveable(false);
				}
			} else if (col == free_col) {
				if (free_pos + rows == pos || free_pos - rows == pos) {
					pieces.get(i).setMoveable(true);
				} else {
					pieces.get(i).setMoveable(false);
				}
			} else {
				pieces.get(i).setMoveable(false);
			}
		}
	}
	
	private void checkVictory() {
		boolean victory = pieces.get(0).isInCorrectPosition() &&
				pieces.get(1).isInCorrectPosition() &&
				pieces.get(2).isInCorrectPosition() &&
				pieces.get(3).isInCorrectPosition() &&
				pieces.get(4).isInCorrectPosition() &&
				pieces.get(5).isInCorrectPosition() &&
				pieces.get(6).isInCorrectPosition() &&
				pieces.get(7).isInCorrectPosition() &&
				pieces.get(8).isInCorrectPosition();
		
		if (victory) {
			victory();
		}
	}
	
	private void victory() {
		Log.d(TAG, "VICTORY!!!!");
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		thread.setRunning(true);
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// delegating event handling to the puzzlePiece
			piece0.handleActionDown((int)event.getX(), (int)event.getY());
			piece1.handleActionDown((int)event.getX(), (int)event.getY());
			piece2.handleActionDown((int)event.getX(), (int)event.getY());
			piece3.handleActionDown((int)event.getX(), (int)event.getY());
			piece4.handleActionDown((int)event.getX(), (int)event.getY());
			piece5.handleActionDown((int)event.getX(), (int)event.getY());
			piece6.handleActionDown((int)event.getX(), (int)event.getY());
			piece7.handleActionDown((int)event.getX(), (int)event.getY());	
			
			shuffle.handleActionDown((int)event.getX(), (int)event.getY());
			
			// check if in the lower part of the screen we exit
			if (event.getY() > 450) {
				thread.setRunning(false);
				((Activity)getContext()).finish();
			}
		} if (event.getAction() == MotionEvent.ACTION_UP) {
			// touch was released
			piece0.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece1.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece2.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece3.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece4.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece5.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece6.handleActionUp((int)event.getX(), (int)event.getY(), piece8);
			piece7.handleActionUp((int)event.getX(), (int)event.getY(), piece8);	
			
			shuffle.handleActionUp((int)event.getX(), (int)event.getY());
			
			checkVictory();
			
			// Update the moveable pieces based on new open space
			findMoveablePieces(piece8.getPosition());
		}
		
		if(shuffle.isShuffle()) {
			shufflePuzzle();
		}
		
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// fills the canvas with black
		canvas.drawColor(Color.BLACK);
		piece0.draw(canvas);
		piece1.draw(canvas);
		piece2.draw(canvas);
		piece3.draw(canvas);
		piece4.draw(canvas);
		piece5.draw(canvas);
		piece6.draw(canvas);
		piece7.draw(canvas);	
		
		shuffle.draw(canvas);
	}

}
