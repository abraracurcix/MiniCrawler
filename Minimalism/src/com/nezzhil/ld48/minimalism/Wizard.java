package com.nezzhil.ld48.minimalism;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Wizard extends Enemy {
	
	private int maxX, maxY;
	private int minX, minY;
	private float teleportTime, stateTime;
	private int move;
	private Shoot fire;
	private Animation shootAnimationU;
	private Animation shootAnimationD;
	private Animation shootAnimationL;
	private Animation shootAnimationR;
	
	public Wizard(float x, float y, float maxWidth, float maxHeight) {
		super(x, y);
		minX = (int) x;
		minY = (int) y;
		maxX = (int) maxWidth*2;
		maxY = (int) maxHeight*2;
		right = false;
		fire = new Shoot();
		TextureRegion[] regions = ImageResources.getInstance().getRegions(ImageResources.WIZARD);
		TextureRegion flip1, flip2, flip3, flip4;
		shootAnimationD = new Animation(0.15f, regions[6], regions[7]);
		shootAnimationD.setPlayMode(Animation.LOOP);

		shootAnimationL = new Animation(0.15f, regions[4], regions[5]);
		shootAnimationL.setPlayMode(Animation.LOOP);

		flip1 = new TextureRegion(regions[6]);
		flip1.flip(false, true);
		flip2 = new TextureRegion(regions[7]);
		flip2.flip(false, true);
		shootAnimationU = new Animation(0.15f, flip1, flip2);
		shootAnimationU.setPlayMode(Animation.LOOP);

		flip3 = new TextureRegion(regions[4]);
		flip3.flip(true, false);
		flip4 = new TextureRegion(regions[5]);
		flip4.flip(true, false);
		shootAnimationR = new Animation(0.15f, flip3, flip4);
		shootAnimationR.setPlayMode(Animation.LOOP);
	}

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Enemy#update(float, com.nezzhil.ld48.minimalism.Player, com.nezzhil.ld48.minimalism.GameScreen)
	 */
	@Override
	public void update(float delta, Player pl, GameScreen screen) {
		if(delta == 0) return;
		stateTime += delta;
		Random rand = new Random();
		float f = rand.nextFloat();
		int n = rand.nextInt(maxX);
		float nextX = f + (float) n + minX;
		f = rand.nextFloat();
		n = rand.nextInt(maxY);
		float nextY = f + (float) n + minY;
		
		if(teleportTime == 0f) {
			teleportTime = delta;
		}
		else {
			teleportTime += delta;
			if (teleportTime > 2f) {
				teleportTime = 0f;
				position.set(nextX, nextY);
				if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
					SoundResources.getInstance().getShoot().play(0.15f);
				}
				if(Math.abs(pl.position.x - position.x) > Math.abs(pl.position.y - position.y)) {
					if(pl.position.x - position.x < 0) {
						move = 1;
						fire.position.set(position.x-16f, position.y);
						fire.velocity.x = -fire.MAX_VELOCITY;
						fire.velocity.y = 0;
					}
					else {
						move = 3;
						fire.position.set(position.x+16f, position.y);
						fire.velocity.x = fire.MAX_VELOCITY;
						fire.velocity.y = 0;
					}
				}
				else {
					if(pl.position.y - position.y < 0) {
						move = 0;
						fire.position.set(position.x, position.y-16f);
						fire.velocity.y = -fire.MAX_VELOCITY;
						fire.velocity.x = 0;
					}
					else {
						move = 2;
						fire.position.set(position.x, position.y+16f);
						fire.velocity.y = fire.MAX_VELOCITY;
						fire.velocity.x = 0;
					}
				}
			}
		}
		
		
		fire.velocity.scl(delta);
		
		Rectangle rect = new Rectangle(fire.position.x, fire.position.y, fire.WIDTH, fire.HEIGHT);
		
		rect.x += velocity.x;
		//GETTILES TO HITS
		Array<Element> all = screen.getAll();		
		
		for(Element ele : all) {
			if(ele.getClass() == Player.class && this.hit(rect, ele)) {
				if(((Player) ele).isHit() == false) {
					SoundResources.getInstance().getHit().play(0.25f);
					((Player) ele).setLifes(-1);
					((Player) ele).setHit(true);
				}
			}
		}

		rect.x = fire.position.x;
		rect.y += fire.velocity.y;
		
		//GETTILES TO HITS			
		
		for(Element ele : all) {
			if(ele.getClass() == Player.class && this.hit(rect, ele)) {
				if(((Player) ele).isHit() == false) {
					SoundResources.getInstance().getHit().play(0.25f);
					((Player) ele).setLifes(-1);
					((Player) ele).setHit(true);
				}
			}
		}
		 
		fire.position.add(fire.velocity);
		fire.velocity.scl(1/delta);
		
	}

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Enemy#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
		
		TextureRegion frame = null;
		TextureRegion frameShoot = null;
		batch.begin();
		switch (move) {
		
			//down
			case 0:
				frame = ImageResources.getInstance().getRegions(ImageResources.WIZARD)[1];
				frameShoot = shootAnimationD.getKeyFrame(stateTime);
				break;
			//left
			case 1:
				frame = ImageResources.getInstance().getRegions(ImageResources.WIZARD)[3];
				frameShoot = shootAnimationL.getKeyFrame(stateTime);
				break;
			//up
			case 2:
				frame = ImageResources.getInstance().getRegions(ImageResources.WIZARD)[2];
				frameShoot = shootAnimationU.getKeyFrame(stateTime);
				break;
			//right
			case 3:
				frame = ImageResources.getInstance().getRegions(ImageResources.WIZARD)[3];
				frameShoot = shootAnimationR.getKeyFrame(stateTime);
				frame.flip(true, false);
				break;
		}
		
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.draw(frameShoot, fire.position.x, fire.position.y, fire.WIDTH*2, fire.HEIGHT*2);
		if(move == 3) frame.flip(true, false);
		batch.end();

	}

	
}
