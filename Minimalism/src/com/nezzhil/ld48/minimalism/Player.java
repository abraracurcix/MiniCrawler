package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player extends Element {

	public static float MAX_VELOCITY = 100f;
	public static float DAMPING = 0.9f;
	public static final int TYPE = 0;
	public static Animation walkUP;
	public static Animation walkDOWN;

	public static Animation walkLeft;
	public static Animation walkRight;
	public static Animation standingUP;
	public static Animation standingDOWN;
	
	enum State {
		Standing,
		Walking
	}
	
	public final Vector2 velocity;
	private State state;
	private int move;
	private float stateTime;
	private float hitTime;
	private int keys;
	private int lifes;

	private boolean hit;
	
	public Player(float x, float y) {
		super(x, y);
		lifes = 5;
		keys = 0;
		velocity = new Vector2();
		state = State.Standing;
		ImageResources instance = ImageResources.getInstance();
		WIDTH = 24f;
		TextureRegion[] regions = instance.getRegions(TYPE);
		
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
	
	public void update(float delta, int key, GameScreen screen) {

		
		if(delta == 0) return;
		stateTime += delta;
		
		if(hit) {
			if(hitTime == 0f) {
				hitTime = delta;
			}
			else {
				hitTime += delta;
				if (hitTime > 1.2f) {
					hitTime = 0f;
					hit = false;
				}
			}
		}

		switch (key) {
			//DOWN
			case 2:
				velocity.y = -MAX_VELOCITY;
				state = State.Walking;
				break;
				
			//left-d
			case 1:
				velocity.x = -MAX_VELOCITY;
				velocity.y = -MAX_VELOCITY;
				state = State.Walking;
				break;
			
			//left
			case 4:
				velocity.x = -MAX_VELOCITY;
				state = State.Walking;
				break;
				
			//left-u
			case 7:
				velocity.x = -MAX_VELOCITY;
				velocity.y = MAX_VELOCITY;
				state = State.Walking;
				break;
			//up
			case 8:
				velocity.y = MAX_VELOCITY;
				state = State.Walking;
				break;
			//right-u
			case 9:
				velocity.x = MAX_VELOCITY;
				velocity.y = MAX_VELOCITY;
				state = State.Walking;
				break;
			//right
			case 6:
				velocity.x = MAX_VELOCITY;
				state = State.Walking;
				break;
			//right-d
			case 3:
				velocity.x = MAX_VELOCITY;
				velocity.y = -MAX_VELOCITY;
				state = State.Walking;
				break;
				
			default:
				if(state != state.Standing) {
					state = state.Standing;
					if(move  < 7)
						move = -1;
					else 
						move = 0;
				}
		}
		if (state != state.Standing)
			move = key;
		
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
			if(ele != this && this.hit(rect, ele) && ele.used == false) {
				if(ele.getClass() == Stairs.class) {
					SoundResources.getInstance().getItem().play(0.25f);
					screen.changeLevel(((Stairs) ele).getNextLevel());
					continue;
				}
				if(ele.getClass() == Rock.class) {
					((Rock) ele).setVelocity(velocity.x, 0);
				}
				if(ele.getClass() != Spike.class && ele.getClass() != Button.class) velocity.x = 0;
				if(ele.getClass() == Enemy.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						((Enemy) ele).setHit(true);
						lifes--;
					}
					continue;
				}
				if(ele.getClass() == Knight.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						lifes--;
					}
					continue;
				}
				if(ele.getClass() == Boulder.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						lifes-=2;
						if(lifes < 0) lifes = 0;
					}
					continue;
				}
				if(ele.getClass() == Chest.class) {
					if(ele.isUsed() == false) {
						SoundResources.getInstance().getItem().play(0.25f);
						ele.setUsed(true);
						keys++;
					}
					continue;
				}
				if(ele.getClass() == Lever.class) {
					if(ele.isUsed() == false) {
						SoundResources.getInstance().getBlip().play(0.25f);
						screen.getObjects().removeValue(((Lever)ele).setUsedElement(true), false);							
					}
					continue;
				}
				if(ele.getClass() == Door.class) {
					if(ele.isUsed() == false && keys > 0) {
						SoundResources.getInstance().getBlip().play(0.25f);
						ele.setUsed(true);
						keys--;
					}
					continue;
				}
				if(ele.getClass() == Teleport.class) {

					SoundResources.getInstance().getBlip().play(0.25f);
					position.set(((Teleport) ele).use());
				}
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
		
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele) && ele.used == false) {
				if(ele.getClass() == Stairs.class) {
					SoundResources.getInstance().getItem().play(0.25f);
					screen.changeLevel(((Stairs) ele).getNextLevel());
					continue;
				}
				if(ele.getClass() == Rock.class) {
					((Rock) ele).setVelocity(0, velocity.y);
				}
				if(ele.getClass() != Spike.class && ele.getClass() != Button.class) velocity.y = 0;
				if(ele.getClass() == Enemy.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						((Enemy) ele).setHit(true);
						lifes--;
					}
					continue;
				}
				if(ele.getClass() == Knight.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						lifes--;
					}
					continue;
				}
				if(ele.getClass() == Boulder.class) {
					if(isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						setHit(true);
						lifes-=2;
						if(lifes < 0) lifes = 0;
					}
					continue;
				}
				if(ele.getClass() == Chest.class) {
					if(ele.isUsed() == false) {
						SoundResources.getInstance().getItem().play(0.25f);
						ele.setUsed(true);
						keys++;
					}
					continue;
				}
				if(ele.getClass() == Lever.class) {
					if(ele.isUsed() == false) {
						SoundResources.getInstance().getBlip().play(0.25f);
						screen.getObjects().removeValue(((Lever)ele).setUsedElement(true), false);						
					}
					continue;
				}
				if(ele.getClass() == Door.class) {
					if(ele.isUsed() == false && keys > 0) {
						SoundResources.getInstance().getBlip().play(0.25f);
						ele.setUsed(true);
						keys--;
					}
					continue;
				}
				if(ele.getClass() == Teleport.class) {
					SoundResources.getInstance().getBlip().play(0.25f);
					position.set(((Teleport) ele).use());
				}
				break;
			}
		}
		//GETTILES TO HITS
		tiles = screen.getTiles(startX/32, startY/32, endX/32, endY/32);

		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
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
				if (move < 4) {
					frame = walkDOWN.getKeyFrame(stateTime);
				}
				else {
					if (move > 6)
						frame = walkUP.getKeyFrame(stateTime);
					else {
						if (move == 4) {
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
				batch.setColor(Color.BLACK);
		}
		else {
			batch.setColor(Color.WHITE);
		}
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT*2);
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

	/**
	 * @return the keys
	 */
	public int getKeys() {
		return keys;
	}

	/**
	 * @param keys the keys to set
	 */
	public void setKeys(int keys) {
		this.keys = keys;
	}

	/**
	 * @return the lifes
	 */
	public int getLifes() {
		return lifes;
	}

	/**
	 * @param lifes the lifes to set
	 */
	public void setLifes(int lifes) {
		this.lifes += lifes;
	}
	
	
}
