package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Rock extends Element {
	
	private Vector2 velocity;

	public Rock(float x, float y) {
		super(x, y);
		velocity = new Vector2();
		HEIGHT = 24f;
		WIDTH = 24f;
	}	

	@Override
	public void update(float delta) {

		
		int startX, endX, startY, endY;
		Rectangle rect = new Rectangle(position.x, position.y, WIDTH, HEIGHT);
		
		if (velocity.x > 0) {
			startX = endX = (int) (position.x + WIDTH + velocity.x);
		}
		else {
			startX = endX = (int) (position.x + velocity.x);
		}
		
		startY = (int) (position.y);
		endY = (int) (position.y + HEIGHT);
		
		rect.x += velocity.x;
		//GETTILES TO HITS
		Array<Rectangle> tiles = GameScreen.getTiles(startX/32, startY/32, endX/32, endY/32);
		
		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				velocity.x = 0;
				break;
			}
		}	
		Array<Element> all = GameScreen.getAll();
		
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele) && ele.used == false) {
				if(ele.getClass() == Rock.class) {
					velocity.x = 0;
				}
			}
		}

		rect.x = position.x;
		rect.y += velocity.y;
		if (velocity.y > 0) {
			startY = endY = (int) (position.y + HEIGHT + velocity.y);
		}
		else {
			startY = endY = (int) (position.y + velocity.y);
		}		
		startX = (int) (position.x);
		endX = (int) (position.x + WIDTH);
		
		//GETTILES TO HITS
		tiles = GameScreen.getTiles(startX/32, startY/32, endX/32, endY/32);
		
		for(Element ele : all) {
			if(ele != this && this.hit(rect, ele) && ele.used == false) {
				if(ele.getClass() == Rock.class) {
					velocity.y = 0;
				}
			}
		}

		for (Rectangle tile : tiles) {
			if (rect.overlaps(tile)) {
				velocity.y = 0;
				break;
			}
		}	
		position.add(velocity);
		velocity.set(0,0);
	}

	public void setVelocity(float x, float y) {
		this.velocity.set(x, y);
	}

	@Override
	public void draw(SpriteBatch batch) {
		TextureRegion frame = null;
		frame = ImageResources.getInstance().getRegions(ImageResources.TELEPORT)[8];
		batch.begin();
		batch.setColor(Color.WHITE);
		batch.draw(frame, position.x, position.y, WIDTH, HEIGHT);
		batch.end();	

	}

}
