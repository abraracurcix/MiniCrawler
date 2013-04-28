package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ImageResources {
	
	public static final int LIME = 1;
	public static final int BOULDER = 2;
	public static final int TELEPORT = 3;
	public static final int WIZARD = 4;
	public static final int BADGUY = 9;
	
	private static ImageResources instance = null;	
	
	private TextureRegion[] players;
	private TextureRegion[] lime;
	private TextureRegion[] utils;
	private TextureRegion[] boulder;
	private TextureRegion[] wizard;
	private TextureRegion[] telever;
	
	private ImageResources() {
		Texture playerTexture = new Texture("data/player.png");
		Texture limeTexture = new Texture("data/lime.png");
		Texture boulderTexture = new Texture("data/boulder.png");
		Texture utilsTexture = new Texture("data/output/utils.png");
		Texture wizardTexture = new Texture("data/wizard.png");
		Texture teleportLeverTexture = new Texture("data/teleport.png");
		
		players = TextureRegion.split(playerTexture, 16, 16)[0];
		lime = TextureRegion.split(limeTexture, 16, 16)[0];
		utils = TextureRegion.split(utilsTexture, 16, 16)[0];
		boulder = TextureRegion.split(boulderTexture, 32, 32)[0];
		telever = TextureRegion.split(teleportLeverTexture, 16, 16)[0];
		wizard = TextureRegion.split(wizardTexture, 16, 16)[0];
	}
	
	public static ImageResources getInstance() {
		if (instance == null) {
			instance = new ImageResources();
		}
		
		return instance;
	}
	
	public TextureRegion[] getRegions(int type) {
		switch (type) {
			case Player.TYPE:
				return players;
				
			case LIME:
				return lime;
				
			case BOULDER:
				return boulder;
				
			case WIZARD:
				return wizard;
				
			case TELEPORT:
				return telever;
				
			default:
				return utils;
		}
	}
}
