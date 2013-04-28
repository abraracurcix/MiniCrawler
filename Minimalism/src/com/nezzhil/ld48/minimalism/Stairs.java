package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Stairs extends Element {

	private int nextLevel;
	
	public Stairs(float x, float y, int next) {
		super(x, y);
		HEIGHT = 32f;
		WIDTH = 32f;
		nextLevel = next;
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		frame = ImageResources.getInstance().getRegions(ImageResources.TELEPORT)[9];
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();			
	}

	public int getNextLevel() {
		return nextLevel;
	}

}
