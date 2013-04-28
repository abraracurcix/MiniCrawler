package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen implements Screen {
	
	public static final int ID = 1;
	private MiniCrawler game;
	private Texture background;
	private SpriteBatch batch;
	private BitmapFont font;
	private float time;
	private int color;
	
	public MenuScreen(MiniCrawler game) {
		this.game = game;
		color = 0;
	}

	@Override
	public void render(float delta) {
		if(delta == 0f) return;
		time += delta;
		
		if(time > 0.3f) {
			time = 0;
			switch (color) {
			case 0:
				font.setColor(139f, 172f, 15f, 1f);
				color = 1;
				break;
			case 1:
				font.setColor(48f, 98f, 48f, 1f);
				color = 2;
				break;
			case 2:
				font.setColor(15f, 56f, 15f, 1f);
				color = 0;
				break;
			}
		}
		if (Gdx.input.isKeyPressed(Keys.UP) ||
				Gdx.input.isKeyPressed(Keys.DOWN) ||
				Gdx.input.isKeyPressed(Keys.LEFT) ||
				Gdx.input.isKeyPressed(Keys.RIGHT)) {

			SoundResources.getInstance().getItem().play(0.25f);
			game.changeScreen(GameScreen.ID);
		}
		
		batch.begin();
		batch.draw(background, 0, 0, 640, 640);
		font.draw(batch, "Press any arrow to begin.", 20, 200);
		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		background = new Texture("data/menu.png");
		batch = new SpriteBatch();
		font =  new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false, false);
		font.setScale(1.5f);

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		batch.dispose();

	}

}
