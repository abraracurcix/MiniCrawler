package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundResources {

	private Sound hit;
	private Sound blip;
	private Sound item;
	private Sound roll;
	private Sound shoot;
	private Music theme;
	
	private static SoundResources instance;
	
	private SoundResources() {
		theme = Gdx.audio.newMusic(Gdx.files.internal("data/sound/theme.wav"));
		theme.setVolume(0.5f);
		theme.setLooping(true);
		hit = Gdx.audio.newSound(Gdx.files.internal("data/sound/hit.wav"));		
		blip = Gdx.audio.newSound(Gdx.files.internal("data/sound/blip.wav"));
		item = Gdx.audio.newSound(Gdx.files.internal("data/sound/item.wav"));
		roll = Gdx.audio.newSound(Gdx.files.internal("data/sound/roll.wav"));
		shoot = Gdx.audio.newSound(Gdx.files.internal("data/sound/shoot.wav"));
	}
	
	public static SoundResources getInstance() {
		if (instance == null) {
			instance = new SoundResources();
		}
		
		return instance;
	}

	public Sound getHit() {
		return hit;
	}

	public void setHit(Sound hit) {
		this.hit = hit;
	}

	public Sound getBlip() {
		return blip;
	}

	public void setBlip(Sound blip) {
		this.blip = blip;
	}

	public Sound getItem() {
		return item;
	}

	public void setItem(Sound item) {
		this.item = item;
	}

	public Sound getRoll() {
		return roll;
	}

	public void setRoll(Sound roll) {
		this.roll = roll;
	}

	public Sound getShoot() {
		return shoot;
	}

	public void setShoot(Sound shoot) {
		this.shoot = shoot;
	}

	public Music getTheme() {
		return theme;
	}

	public void setTheme(Music theme) {
		this.theme = theme;
	}
	
	
}
