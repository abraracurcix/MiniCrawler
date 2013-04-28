package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nezzhil.ld48.minimalism.Enemy.State;

public class Boulder extends Enemy {
	
	public static Animation upDown;
	public static Animation leftRigth;
	public static float MAX_VELOCITY = 100f;
	public static float DAMPING = 0.9f;
	
	private State state;
	private int move;
	private boolean right;
	private float stateTime;
	private float hitTime;
	private float life = 0;

	public Boulder(float x, float y) {
		super(x, y);
		
		ImageResources instance = ImageResources.getInstance();
		WIDTH = 94f;
		HEIGHT = 94f;
		TextureRegion[] regions = instance.getRegions(ImageResources.BOULDER);
		TextureRegion fliped = new TextureRegion(regions[3]);
		fliped.flip(false, true);
		upDown = new Animation(0.25f, regions[2], regions[0], regions[3], fliped, regions[3], regions[4]);
		upDown.setPlayMode(Animation.LOOP);
		TextureRegion flip0, flip1, flip2;
		flip1 = new TextureRegion(regions[5]);
		flip1.flip(true, false);
		flip0 = new TextureRegion(regions[6]);
		flip0.flip(true, false);
		flip2 = new TextureRegion(regions[1]);
		flip2.flip(true, false);
		leftRigth = new Animation(0.25f, regions[5], regions[1], flip2, flip1, flip0, regions[6]);
		leftRigth.setPlayMode(Animation.LOOP);
		move = 0; //0 left, 1 down, 2 right, 3 up
	}
	
	
	public void update(float delta, Player pl, GameScreen screen) {
		
		if(delta == 0) return;
		stateTime += delta;
				
		switch (move) {
			case 0:
				velocity.x = -MAX_VELOCITY;
				velocity.y = 0;
				break;
			case 1:
				velocity.x = 0;
				velocity.y = -MAX_VELOCITY;
				break;
			case 2:				
				velocity.x = MAX_VELOCITY;
				velocity.y = 0;
				break;
			case 3:
				velocity.x = 0;
				velocity.y = MAX_VELOCITY;
		}

		velocity.scl(delta);
		int startX, endX, startY, endY;
		Rectangle rect = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
		
		if (velocity.x > 0) {
			startX = endX = (int) (position.x + WIDTH + velocity.x);
		}
		else {
			startX = endX = (int) (position.x + velocity.x);
		}
		
		startY = (int) (position.y);
		endY = (int) (position.y + HEIGHT);
		rect.x += velocity.x;
		Array<Rectangle> tiles = screen.getTiles(startX/32, startY/32, endX/32, endY/32);
		
		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				velocity.x = 0;
				break;
			}
		}	
		
		rect.x = position.x;
		rect.y += velocity.y;
		if (velocity.y > 0) {
			startY = endY = (int) (position.y + HEIGHT + velocity.y);
		}
		else {
			startY = endY = (int) (position.y + velocity.y);
		}		
		startX = (int) (position.x);
		endX = (int) (position.x + WIDTH);
		
		//GETTILES TO HITS
		tiles = screen.getTiles(startX/32, startY/32, endX/32, endY/32);

		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				velocity.y = 0;
				break;
			}
		}
		
		switch (move) {
			case 0:
				if(velocity.x == 0) {
					if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
						SoundResources.getInstance().getRoll().play(0.15f);
					}
					position.y-=2;
					move = 1;
					leftRigth.setPlayMode(Animation.LOOP_REVERSED);
				}
				break;
			case 1:			
				if(velocity.y == 0) {
					if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
						SoundResources.getInstance().getRoll().play(0.25f);
					}
					position.x+=2;
					move = 2;
					upDown.setPlayMode(Animation.LOOP_REVERSED);
				}
				break;
			case 2:				
				if(velocity.x == 0) {
					if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
						SoundResources.getInstance().getRoll().play(0.25f);
					}
					position.y+=2;
					move = 3;
					leftRigth.setPlayMode(Animation.LOOP);
				}
				break;
			case 3:
				if(velocity.y == 0) {
					if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
						SoundResources.getInstance().getRoll().play(0.25f);
					}
					position.x-=2;
					move = 0;
					upDown.setPlayMode(Animation.LOOP);
				}
				break;
		}
		
		position.add(velocity);
		if(position.x < 0) {
			position.x = 0;
		}
		if(position.y > 1100) {
			position.y = 1100;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Enemy#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		switch (move) {
			case 0:
				frame = leftRigth.getKeyFrame(stateTime);
				break;
			case 1:			
				frame = upDown.getKeyFrame(stateTime);
				break;
			case 2:			
				frame = leftRigth.getKeyFrame(stateTime);
				break;
			case 3:
				frame = upDown.getKeyFrame(stateTime);
				break;
		}
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();
	}

	public boolean isHit() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setHit(boolean hit) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
