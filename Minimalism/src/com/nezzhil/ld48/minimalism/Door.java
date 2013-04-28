package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door extends Element {
	
	
	public Door(float x, float y) {
		super(x, y);
		WIDTH = 32f;
		HEIGHT = 32f;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		if(used) {
			frame = ImageResources.getInstance().getRegions(-1)[5];
		}
		else {
			frame = ImageResources.getInstance().getRegions(-1)[4];
		}
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();
	}
	
}
