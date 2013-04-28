package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Enemy extends Element {

	public static float MAX_VELOCITY = 10f;
	public static float DAMPING = 0.9f;
	public int type;
	public static Animation walk;
	public static Animation standing;
	
	enum State {
		Standing,
		Walking
	}
	
	public final Vector2 velocity;
	private State state;
	protected boolean facesDown;
	protected boolean right;
	private float stateTime;
	private float hitTime;
	private float life = 0;
	//¿?¿?
	private boolean hit;
	
	public Enemy(int t, float x, float y) {
		super(x, y);
		type = t;
		velocity = new Vector2();
		state = State.Walking;
		ImageResources instance = ImageResources.getInstance();
		
		TextureRegion[] regions = instance.getRegions(type);
		switch (type) {
			case 1:
				life = 1;
				break;
		}
		
		standing = new Animation(0, regions[0]);
		walk = new Animation(0.15f, regions[0], regions[1], regions[2]);
		walk.setPlayMode(Animation.LOOP);
		
	}
	
	public Enemy(float x, float y) {
		super(x, y);
		velocity = new Vector2();
		
	}
	
	public void update(float delta, Player pl, GameScreen screen) {

		
		if(delta == 0) return;
		stateTime += delta;
		
		if(hit) {
			if(hitTime == 0f) {
				hitTime = delta;
			}
			else {
				hitTime += delta;
				if (hitTime > 2f) {
					hitTime = 0f;
					hit = false;
					used = true;
					position.set(-100, -100);
				}
			}
		}
		
		//MOVEMENT
		if(!hit) {
			if(Math.abs(pl.position.x - position.x) > Math.abs(pl.position.y - position.y)) {
				if(pl.position.x - position.x < 0) {
					velocity.x = -MAX_VELOCITY;
				}
				else {
					velocity.x = MAX_VELOCITY;
				}
			}
			else {
				if(pl.position.y - position.y < 0) {
					velocity.y = -MAX_VELOCITY;
				}
				else {
					velocity.y = MAX_VELOCITY;
				}
			}
		}
		//
		
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
		//GETTILES TO HITS
		Array<Rectangle> tiles = screen.getTiles(startX/32, startY/32, endX/32, endY/32);
		Array<Element> all = screen.getAll();

		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				velocity.x = 0;
				break;
			}
		}				
		
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele)) {
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
		
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele)) {
				velocity.y = 0;
				break;
			}
		}
		 
		position.add(velocity);
		if(position.x < 0) {
			position.x = 0;
		}
		if(position.y > 1100) {
			position.y = 1100;
		}
		velocity.scl(1/delta);
		
		velocity.x *= DAMPING;
		velocity.y *= DAMPING;
		
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		switch (state) {
			case Standing:
				frame = standing.getKeyFrame(stateTime);
				break;
			case Walking:
				frame = walk.getKeyFrame(stateTime);
				break;
		}		
		if(hit) {
			if(hitTime*5f%4%2 < 1)
				batch.setColor(48f,98f,48f,1f);
			else 
				batch.setColor(Color.BLACK);
		}
		else {
			batch.setColor(Color.WHITE);
		}
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.end();
	}

	/**
	 * @return the hit
	 */
	public boolean isHit() {
		return hit;
	}

	/**
	 * @param hit the hit to set
	 */
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	
}
