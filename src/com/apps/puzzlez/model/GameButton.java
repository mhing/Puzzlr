package com.apps.puzzlez.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * @author matt
 *
 */
@SuppressWarnings("unused")
public class GameButton {

	private static final String TAG = GameButton.class.getSimpleName();

	private int x;
	private int y;
	private Bitmap bitmap, pressedBitmap;
	private boolean touched;
	private boolean shuffle;
	
	public GameButton(Bitmap bitmap, Bitmap pressed, int x, int y) {
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
		pressedBitmap = pressed;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public boolean isTouched() {
		return touched;
	}
	
	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
	public Bitmap getPressedBitmap() {
		return pressedBitmap;
	}

	public void setPressedBitmap(Bitmap pressedBitmap) {
		this.pressedBitmap = pressedBitmap;
	}

	public boolean isShuffle() {
		return shuffle;
	}

	public void setShuffle(boolean shuffle) {
		this.shuffle = shuffle;
	}

	public void draw(Canvas canvas) {
		if(isTouched()) {
			canvas.drawBitmap(pressedBitmap, x, y, null);
		} else {
			canvas.drawBitmap(bitmap, x, y, null);
		}
	}
	
	public void handleActionDown(int eventX, int eventY) {
		setShuffle(false);
        if (eventX >= x && eventX <= (x + bitmap.getWidth())) { 
            if (eventY >= y && eventY <= (y + bitmap.getHeight())) {
                // button touched
            	setTouched(true);
            } else {
            	setTouched(false);
            }
        } else {
        	setTouched(false);
        }
	}
	
	public void handleActionUp(int eventX, int eventY) {
        if (isTouched()) { 
        	setTouched(false);
        	setShuffle(true);
        } else {
        	setShuffle(false);
        }
	}
}
