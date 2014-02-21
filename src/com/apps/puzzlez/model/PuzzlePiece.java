package com.apps.puzzlez.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * @author matt
 *
 */
@SuppressWarnings("unused")
public class PuzzlePiece {
	
	private static final String TAG = PuzzlePiece.class.getSimpleName();
	
	private Bitmap bitmap;
	private int x;
	private int y;
	private boolean touched;
	private boolean openPiece;
	private boolean moveable;
	private int id;
	private int position;
	private boolean inCorrectPosition;
	
	public PuzzlePiece(int id, Bitmap bitmap, int x, int y) {
		this.bitmap = bitmap;
		this.id = id;
		this.x = x;
		this.y = y;
		position = id;
	}
	
	public PuzzlePiece(int id, int x, int y) {
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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

	public boolean isTouched() {
		return touched;
	}
	
	public void setTouched(boolean touched) {
		this.touched = touched;
	}
	
	public boolean isOpenPiece() {
		return openPiece;
	}

	public void setOpenPiece(boolean openPiece) {
		this.openPiece = openPiece;
	}
	
	public boolean isMoveable() {
		return moveable;
	}

	public void setMoveable(boolean moveable) {
		this.moveable = moveable;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		
		// based on the new position, change the x,y coordinates
		if(position == 0) {
			setX(5);
			setY(5);
		} else if(position == 1) {
			setX(110);
			setY(5);
		} else if(position == 2) {
			setX(215);
			setY(5);
		} else if(position == 3) {
			setX(5);
			setY(110);
		} else if(position == 4) {
			setX(110);
			setY(110);
		} else if(position == 5) {
			setX(215);
			setY(110);
		} else if(position == 6) {
			setX(5);
			setY(215);
		} else if(position == 7) {
			setX(110);
			setY(215);
		} else {
			setX(215);
			setY(215);
		}
	}

	public boolean isInCorrectPosition() {
		return inCorrectPosition;
	}

	public void setInCorrectPosition(boolean inCorrectPosition) {
		this.inCorrectPosition = inCorrectPosition;
	}

	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, x, y, null);		
	}
	
	public void handleActionDown(int eventX, int eventY) {
        if (eventX >= x && eventX <= (x + bitmap.getWidth())) { 
            if (eventY >= y && eventY <= (y + bitmap.getHeight())) {
                // piece touched
            	setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
        	setTouched(false);
        }   
	}
	
	public void handleActionUp(int eventX, int eventY, PuzzlePiece openPiece) {
		if (isTouched()) {
			setTouched(false);
			if (isMoveable()) {
				int old_pos = getPosition();
				int open_pos = openPiece.getPosition();
				setPosition(open_pos);
				openPiece.setPosition(old_pos);
			}
		}
		
		if(id == getPosition()) {
			setInCorrectPosition(true);
		}
		else {
			setInCorrectPosition(false);
		}
	}
	
	public void handleActionMove(int eventX, int eventY, PuzzlePiece openPiece) {
		if (isMoveable()) {
			int old_pos = getPosition();
			setPosition(openPiece.getPosition());
			openPiece.setPosition(old_pos);
		}
	}
}
