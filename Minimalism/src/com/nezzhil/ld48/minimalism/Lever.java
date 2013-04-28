package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Lever extends Element {
	
	private Element linked;
	private String id;

	public Lever(float x, float y, String elem) {
		super(x, y);
		id = elem;
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		batch.begin();
		if(used) {
			frame = ImageResources.getInstance().getRegions(ImageResources.TELEPORT)[3];
		}
		else {
			frame = ImageResources.getInstance().getRegions(ImageResources.TELEPORT)[2];
		}
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH*2, HEIGHT*2);
		batch.end();

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

}
