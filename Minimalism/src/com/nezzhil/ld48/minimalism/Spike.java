package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Spike extends Element {

	private Animation spikes;
	private boolean up;
	private float timeEnd;
	private float timeUp;
	
	public Spike(float x, float y, int timeInit, int time) {
		super(x, y);
		
		stateTime = timeInit;
		timeEnd = time;
		timeUp = stateTime;
		TextureRegion[] regions = ImageResources.getInstance().getRegions(ImageResources.TELEPORT);
		spikes = new Animation(timeEnd, regions[4], regions[5]);
		spikes.setPlayMode(Animation.LOOP);
		up = false;
		WIDTH = 32f;
		HEIGHT = 32f;
	}
	
	

	/* (non-Javadoc)
	 * @see com.nezzhil.ld48.minimalism.Element#update(float)
	 */
	@Override
	public void update(float delta) {
		super.update(delta);

		timeUp +=delta;
		if(timeUp > timeEnd) {
			timeUp = (stateTime - (float)((int)stateTime));
			up = !up;
		}
		
		Player pl = GameScreen.getPlayer();
		Rectangle rect = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
		if(this.hit(rect, pl) && up) {
			if(pl.isHit() == false) {
				SoundResources.getInstance().getHit().play(0.25f);
				pl.setHit(true);
				pl.setLifes(-1);
			}
		}
	}



	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		
		frame = spikes.getKeyFrame(stateTime);
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();		
	}

}
