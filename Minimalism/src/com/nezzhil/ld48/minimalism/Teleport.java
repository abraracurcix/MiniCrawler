package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Teleport extends Element {
	
	private Teleport another;
	private Animation teleport;

	public Teleport(float x, float y, Teleport portB) {
		super(x, y);
		another = portB;
		ImageResources instance = ImageResources.getInstance();
		
		TextureRegion[] regions = instance.getRegions(ImageResources.TELEPORT);
		teleport = new Animation(0.15f, regions[0], regions[1]);
		teleport.setPlayMode(Animation.LOOP);
	}
	
	public Vector2 use() {
		Vector2 pos = new Vector2(another.position);
		pos.x+=32f;
		return pos;
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		frame = teleport.getKeyFrame(stateTime);
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.end();
	}
	/**
	 * @param another the another to set
	 */
	public void setAnother(Teleport another) {
		this.another = another;
	}
	
	
}
