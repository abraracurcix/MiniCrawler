package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


public class MiniCrawler extends Game {
	
	private GameScreen gameScreen;
	private GameOverScreen gameover;
	private MenuScreen menu;
	private boolean win;
	public int continues;
	
	public static BitmapFont font;

	@Override
	public void create() {
		SoundResources.getInstance();
		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false, false);
		changeScreen(MenuScreen.ID);
	}	
	
	public void changeScreen(int id) {
		
		switch(id) {
			case MenuScreen.ID:
				continues = 3;
				if (menu == null) {
					menu = new MenuScreen(this);
				}
				setScreen(menu);
				break;
			case GameScreen.ID:
				if (gameScreen == null) {
					gameScreen = new GameScreen(this);
				}
				setScreen(gameScreen);
				break;
			case GameOverScreen.ID:
				if (gameover == null) {
					gameover = new GameOverScreen(this);					
				}
				if(!win) continues--;
				gameover.setWin(win);
				setScreen(gameover);
				break;
		}
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}	
	
}
