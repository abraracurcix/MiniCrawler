package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Button extends Element {
	
	private Element linked;
	private String id;
	private boolean pressed;

	public Button(float x, float y, String elem) {
		super(x, y);
		id = elem;
		HEIGHT = 24f;
		WIDTH = 24f;
		pressed = false;
	}

	@Override
	public void update(float delta) {		
		super.update(delta);
		
		Array<Element> all = GameScreen.getAll();
		Rectangle rect = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
		
		if(used) {
			used = false;
			GameScreen.getObjects().add(linked);
		}
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele)) {
				used = true;
				GameScreen.getObjects().removeValue(linked, false);
				if(!pressed) {
					pressed = true;
					SoundResources.getInstance().getItem().play(0.25f);
				}
				break;
			}
		}
		if(!used) {
			pressed = false;
		}
	}
	
	public void setLink(Element elem) {
		linked = elem;
	}

	public String getLinkID() {
		return id;
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		frame = ImageResources.getInstance().getRegions(ImageResources.TELEPORT)[7];
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();

	}

}
