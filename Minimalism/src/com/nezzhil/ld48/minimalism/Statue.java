package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Statue extends Enemy {
	
	private int left;
	private float time;
	private Shoot fire;
	private float stateTime;
	private Animation shootAnimationL;
	private Animation shootAnimationR;

	public Statue(float x, float y, Integer time, Integer left) {
		super(x, y);
		this.time = time;
		this.left = left;
		fire = new Shoot();
		TextureRegion[] regions = ImageResources.getInstance().getRegions(ImageResources.WIZARD);
		TextureRegion flip1, flip2;

		shootAnimationL = new Animation(0.15f, regions[4], regions[5]);
		shootAnimationL.setPlayMode(Animation.LOOP);
		flip1 = new TextureRegion(regions[4]);
		flip1.flip(true, false);
		flip2 = new TextureRegion(regions[5]);
		flip2.flip(true, false);
		shootAnimationR = new Animation(0.15f, flip1, flip2);
		shootAnimationR.setPlayMode(Animation.LOOP);
	}

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Enemy#update(float, com.nezzhil.ld48.minimalism.Player, com.nezzhil.ld48.minimalism.GameScreen)
	 */
	@Override
	public void update(float delta, Player pl, GameScreen screen) {

		if(delta == 0) return;
		stateTime += delta;
		
		if(stateTime == 0f) {
			stateTime = delta;
		}
		else {
			if(stateTime > time) {
				if(Math.max(Math.abs(pl.position.x - position.x), Math.abs(pl.position.y - position.y)) < 160) {
					SoundResources.getInstance().getShoot().play(0.15f);
				}
				stateTime = 0f;
				if(left == 0) {
					fire.position.set(position.x-16f, position.y);
					fire.velocity.x = -fire.MAX_VELOCITY;
					fire.velocity.y = 0;
				}
				else {
					fire.position.set(position.x+16f, position.y);
					fire.velocity.x = fire.MAX_VELOCITY;
					fire.velocity.y = 0;
				}
			}
			
			fire.velocity.scl(delta);
			

			Rectangle rect = new Rectangle(fire.position.x, fire.position.y, fire.WIDTH, fire.HEIGHT);
			
			rect.x += velocity.x;
			//GETTILES TO HITS
			Array<Element> all = screen.getAll();		
			
			for(Element ele : all) {
				if((ele.getClass() == Player.class || ele.getClass() == Knight.class) && this.hit(rect, ele)) {
					if(ele.getClass() == Player.class && ((Player) ele).isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						((Player) ele).setLifes(-1);
						((Player) ele).setHit(true);
					}
					if(ele.getClass() == Knight.class && ((Knight) ele).isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						((Knight) ele).setHit(true);
					}
				}
			}

			rect.x = fire.position.x;
			rect.y += fire.velocity.y;
			
			//GETTILES TO HITS			
			
			for(Element ele : all) {
				if((ele.getClass() == Player.class || ele.getClass() == Knight.class) && this.hit(rect, ele)) {
					if(ele.getClass() == Player.class && ((Player) ele).isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						((Player) ele).setLifes(-1);
						((Player) ele).setHit(true);
					}
					if(ele.getClass() == Knight.class && ((Knight) ele).isHit() == false) {
						SoundResources.getInstance().getHit().play(0.25f);
						((Knight) ele).setHit(true);
					}
				}
			}
			 
			fire.position.add(fire.velocity);
			fire.velocity.scl(1/delta);
		}
	}

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Enemy#draw(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		TextureRegion frameShoot = null;
		frame = ImageResources.getInstance().getRegions(ImageResources.WIZARD)[0];
		if(left == 0) {
			frameShoot = shootAnimationL.getKeyFrame(stateTime);
		}
		else {
			frameShoot = shootAnimationR.getKeyFrame(stateTime);
			frame.flip(true, false);
		}
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.draw(frameShoot, fire.position.x, fire.position.y, fire.WIDTH*2, fire.HEIGHT*2);
		if(left == 1) {
			frame.flip(true, false);
		}
		batch.end();
	}


}
