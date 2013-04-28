package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Knight extends Enemy {

	public static float MAX_VELOCITY = 60f;
	public static float DAMPING = 0.9f;
	public int type;
	
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
	public static Animation walkUP;
	public static Animation walkDOWN;

	public static Animation walkLeft;
	public static Animation walkRight;
	public static Animation standingUP;
	public static Animation standingDOWN;
	

	private Element linked;
	private String id;
	private int move;
	
	public Knight(float x, float y, String id) {
		super(x, y);
		velocity = new Vector2();
		state = State.Walking;
		switch (type) {
			case 1:
				life = 3;
				break;
		}
		
		this.id = id;		

		TextureRegion[] regions = ImageResources.getInstance().getRegions(Player.TYPE);
		
		standingUP = new Animation(0, regions[3]);
		standingDOWN = new Animation(0, regions[0]);
		walkUP = new Animation(0.15f, regions[3], regions[4], regions[5]);
		walkDOWN = new Animation(0.15f, regions[0], regions[1], regions[2]);
		walkUP.setPlayMode(Animation.LOOP);
		walkDOWN.setPlayMode(Animation.LOOP);

		walkLeft = new Animation(0.15f, regions[6], regions[7], regions[8]);
		TextureRegion flip1 = new TextureRegion(regions[6]);
		TextureRegion flip2 = new TextureRegion(regions[7]);
		TextureRegion flip3 = new TextureRegion(regions[8]);
		flip1.flip(true, false);
		flip2.flip(true, false);
		flip3.flip(true, false);
		walkRight = new Animation(0.15f, flip1, flip2, flip3);
		walkLeft.setPlayMode(Animation.LOOP);
		walkRight.setPlayMode(Animation.LOOP);
		
	}
	
	public void setLink(Element elem) {
		linked = elem;
	}
	
	public Element setUsedElement(boolean us) {
		used = us;
		return linked;
	}

	public String getLinkID() {
		return id;
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
					if(life < 1) {
						used = true;
						GameScreen.getObjects().removeValue(linked, false);
						position.set(-100, -100);
					}
				}
			}
		}
		
		//MOVEMENT
		if(!hit) {
			if(Math.abs(pl.position.x - position.x) > Math.abs(pl.position.y - position.y)) {
				if(pl.position.x - position.x < 0) {
					velocity.x = -MAX_VELOCITY;
					move = 1;
					
				}
				else {
					velocity.x = MAX_VELOCITY;
					move = 2;
				}
			}
			else {
				if(pl.position.y - position.y < 0) {
					velocity.y = -MAX_VELOCITY;
					move = 0;
				}
				else {
					velocity.y = MAX_VELOCITY;
					move = 3;
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
			if(ele != this && this.hit(rect, ele) && ele.getClass() != Boulder.class) {
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
			if(ele != this && this.hit(rect, ele) && ele.getClass() != Boulder.class) {
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
			if (move == -1) {
				frame = standingDOWN.getKeyFrame(stateTime);
			}
			else {
				frame = standingUP.getKeyFrame(stateTime);
			}
			break;
		case Walking:
			if (move < 1) {
				frame = walkDOWN.getKeyFrame(stateTime);
			}
			else {
				if (move > 2)
					frame = walkUP.getKeyFrame(stateTime);
				else {
					if (move == 1) {
						frame = walkLeft.getKeyFrame(stateTime);
					}
					else {
						frame = walkRight.getKeyFrame(stateTime);
					}
				}
			}
			break;
		}			
		if(hit) {
			if(hitTime*5f%4%2 < 1)
				batch.setColor(48f,98f,48f,1f);
			else 
				batch.setColor(Color.WHITE);
		}
		else {
			batch.setColor(Color.BLACK);
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
		life--;
	}
	
	
}
