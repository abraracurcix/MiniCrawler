package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Chest extends Element {

	public Chest(float x, float y) {
		super(x, y);
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		if(used) {
			frame = ImageResources.getInstance().getRegions(-1)[3];
		}
		else {
			frame = ImageResources.getInstance().getRegions(-1)[2];
		}
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.end();
	}
}
