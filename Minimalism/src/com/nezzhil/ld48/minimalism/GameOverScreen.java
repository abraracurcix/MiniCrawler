package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameOverScreen implements Screen {

	public static final int ID = 2;
	
	private boolean win;
	private SpriteBatch batch;
	private MiniCrawler game;
	
	private final static String WINWORDS = "You Win! You escaped from the maze.";
	private final static String LOSEWORDS = "You Lose! Poor little warrior.";

	public GameOverScreen(MiniCrawler game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		if(win)	MiniCrawler.font.draw(batch, WINWORDS, 30, 350);
		else MiniCrawler.font.draw(batch, LOSEWORDS, 90, 350);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		batch = new SpriteBatch();

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

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}	

}
