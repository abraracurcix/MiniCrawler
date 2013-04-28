package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Element {

	public float WIDTH = 12f;
	public float HEIGHT = 12f;
	public final Vector2 position;

	protected float stateTime;
	protected boolean used;
	
	public Element(float x, float y) {
		position = new Vector2(x, y);
	}
	
	public boolean hit(Element pl) {
		return pl.position.x < position.x+WIDTH && pl.position.y < position.y+HEIGHT
				&& pl.position.x+pl.WIDTH > position.x && pl.position.y+pl.HEIGHT > position.y;
	}
	
	public boolean hit(Rectangle rect, Element pl) {
		return pl.position.x < rect.x+WIDTH && pl.position.y < rect.y+HEIGHT
				&& pl.position.x+pl.WIDTH > rect.x && pl.position.y+pl.HEIGHT > rect.y;
	}
	
	public void update(float delta) {
		if(delta == 0) return;
		stateTime += delta;
	}
	
	public abstract void draw(SpriteBatch batch);
	
	public boolean isUsed() {
		return used;
	}
	
	public void setUsed(boolean us) {
		used = us;
	}
}
