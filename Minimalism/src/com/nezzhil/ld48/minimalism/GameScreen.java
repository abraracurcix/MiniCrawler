package com.nezzhil.ld48.minimalism;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class GameScreen implements Screen {

	public static final int ID = 0;
	
	private OrthographicCamera camera;
	private SpriteBatch batch, hud;
	private static Player player;
	private OrthogonalTiledMapRenderer renderer;
	private static Array<Enemy> enemies;
	private static Array<Element> objects;
	private int level;
	private MiniCrawler game;
	private TextureRegion key;
	
	private static TiledMap map;
	private static Pool<Rectangle> poolLayer = new Pool<Rectangle>() {
		@Override
		protected Rectangle newObject() {
			return new Rectangle();
		}
	};
	
	private static Array<Rectangle> tiles;
	
	public GameScreen(MiniCrawler mini) {
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 140, 140);
		camera.update();
		
		batch = new SpriteBatch();
		hud = new SpriteBatch();		
		level = 5;
		key = ImageResources.getInstance().getRegions(-1)[6];
		game = mini;
	}
	
	public void setMap() {
		Teleport aux = null;
		MapLayer layer = (MapLayer) map.getLayers().get("objects1");
		Array<Lever> levers = new Array<Lever>();
		Array<Button> buttons = new Array<Button>();
		Array<Wall> walls = new Array<Wall>();
		Array<Knight> kns = new Array<Knight>();
		for(MapObject obj : layer.getObjects()) {
			RectangleMapObject rectObj = (RectangleMapObject) obj;
			if(rectObj.getName().equals("Heroe")) {
				player.position.set(rectObj.getRectangle().x*2, rectObj.getRectangle().y*2);
				continue;
			}
			if(rectObj.getName().equals("Lime")) {
				Enemy lime1 = new Enemy(ImageResources.LIME, rectObj.getRectangle().x*2, rectObj.getRectangle().y*2);
				enemies.add(lime1);
				continue;
			}
			if(rectObj.getName().equals("Knight")) {
				MapProperties prop = rectObj.getProperties();
				Knight kn = new Knight(rectObj.getRectangle().x*2, rectObj.getRectangle().y*2,
					((String)prop.get("link")));
				enemies.add(kn);
				kns.add(kn);
				continue;
			}
			if(rectObj.getName().equals("Statue")) {
				MapProperties prop = rectObj.getProperties();	
				Statue statue = new Statue(rectObj.getRectangle().x*2, rectObj.getRectangle().y*2,
						Integer.valueOf((String)prop.get("time")), Integer.valueOf((String)prop.get("left")));
				enemies.add(statue);
				continue;
			}
			if(rectObj.getName().equals("Coffer")) {
				Chest chest = new Chest((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2);
				objects.add(chest);
				continue;
			}
			if(rectObj.getName().equals("Door")) {
				Door door = new Door((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2);
				objects.add(door);
				continue;
			}
			if(rectObj.getName().equals("Boulder")) {
				Boulder boulder = new Boulder((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2);
				enemies.add(boulder);
				continue;
			}
			if(rectObj.getName().equals("Wall")) {
				MapProperties prop = rectObj.getProperties();	
				Wall wall = new Wall((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, (String)prop.get("link"));
				objects.add(wall);
				walls.add(wall);
				continue;
			}
			if(rectObj.getName().equals("Rock")) {	
				Rock rock = new Rock((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2);
				objects.add(rock);
				continue;
			}
			if(rectObj.getName().equals("PointMage")) {
				Wizard wizard = new Wizard((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, 
						(int)rectObj.getRectangle().width, (int)rectObj.getRectangle().height);
				enemies.add(wizard);
				continue;
			}
			
			if(rectObj.getName().equals("Lever")) {
				MapProperties prop = rectObj.getProperties();				
				Lever lever = new Lever((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, (String)prop.get("link"));
				objects.add(lever);
				levers.add(lever);
				continue;
			}
			
			if(rectObj.getName().equals("Stairs")) {
				MapProperties prop = rectObj.getProperties();				
				Stairs stairs = new Stairs((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2,
						Integer.valueOf((String)prop.get("next")));
				objects.add(stairs);
				continue;
			}
			
			if(rectObj.getName().equals("Button")) {	
				MapProperties prop = rectObj.getProperties();			
				Button button = new Button((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, (String)prop.get("link"));
				objects.add(button);
				buttons.add(button);
				continue;
			}
			
			if(rectObj.getName().equals("Spikes")) {
				MapProperties prop = rectObj.getProperties();				
				Spike spike = new Spike((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2,
						Integer.valueOf((String)prop.get("init")),Integer.valueOf((String)prop.get("time")));
				objects.add(spike);
				continue;
			}
			if(rectObj.getName().equals("Teleport")) {
				if(aux == null) {
					Teleport teleport = new Teleport((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, null);
					objects.add(teleport);
					aux = teleport;
				}
				else {

					Teleport teleport = new Teleport((int)rectObj.getRectangle().x*2, (int)rectObj.getRectangle().y*2, aux);
					objects.add(teleport);
					aux.setAnother(teleport);
				}
				continue;
			}
		}
		for(Lever l : levers) {
			for(Wall w : walls) {
				if(w.getId().equals(l.getLinkID())) {
					l.setLink(w);
				}
			}
		}
		
		for(Button l : buttons) {
			for(Wall w : walls) {
				if(w.getId().equals(l.getLinkID())) {
					l.setLink(w);
				}
			}
		}
		
		for(Knight l : kns) {
			for(Wall w : walls) {
				if(w.getId().equals(l.getLinkID())) {
					l.setLink(w);
				}
			}
		}
	}
	
	public void update(float delta) {
		boolean up = false;
		boolean down = false;
		boolean right = false;
		boolean left = false;
		int key = -1;
		
		if(player.getLifes() < 1) {
			game.setWin(false);
			level = 0;
			game.changeScreen(GameOverScreen.ID);
		}
		
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			up = true;
		}
		if (Gdx.input.isKeyPressed(Keys.R)) {
			changeLevel(level);
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			down = true;
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			left = true;
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			right = true;
		}
		
		if(up) {
			key = 8;
			if (left) {
				key = 7;
			}
			else {
				if (right) {
					key = 9;
				}
			}
		}
		else {
			if (down) {
				key = 2;
				if (left) {
					key = 1;
				}
				else {
					if (right) {
						key = 3;
					}
				}
			}
			else {
				if (left) {
				key = 4;
				}
				else {
					if (right) {
						key = 6;
					}
				}				
			}				
		}
		
		for(Enemy ene : enemies) {
			if(ene.isUsed() == false)
				ene.update(delta, player, this);
		}
		for(Element obj : objects) {
			obj.update(delta);
		}
		player.update(delta, key, this);
		
		camera.position.x = player.position.x;
		camera.position.y = player.position.y+15;
		if (camera.position.x-60 < 0) {
			camera.position.x = 60;
		}
		if (camera.position.x > 950) {
			camera.position.x = 950;
		}
		if (camera.position.y-60 < 0) {
			camera.position.y = 60;
		}
		if (camera.position.y > 1050) {
			camera.position.y = 1050;
		}
		camera.update();
	}
	
	public void draw(float render) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				
		renderer.setView(camera);
		renderer.render();
		
		batch.setProjectionMatrix(camera.combined);

		for(Element obj : objects) {
			obj.draw(batch);
		}
		for(Enemy ene : enemies) {
			ene.draw(batch);
		}
		player.draw(batch);

		hud.begin();
		Pixmap pix = new Pixmap(8, 8, Format.RGBA8888);
		pix.setColor(Color.BLACK);
		pix.fillRectangle(0, 0, 8, 8);
		Texture tex = new Texture(pix);
		hud.draw(tex, 0, 540, 640, 100);
		pix.dispose();
		MiniCrawler.font.draw(hud, "LIFE", 0, 600);
		MiniCrawler.font.draw(hud, "" + player.getLifes(), 80, 600);
		MiniCrawler.font.draw(hud, "'R' to restart", 190, 620);
		MiniCrawler.font.draw(hud, "Level " + level, 240, 580);
		hud.draw(key, 430, 570, key.getRegionWidth()*3, key.getRegionHeight()*3);
		MiniCrawler.font.draw(hud, "" + player.getKeys(), 500, 600);
		hud.end();
	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		player = new Player(0, 0);
		
		map = new TmxMapLoader().load("data/world"+level+".tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 2);
		tiles = new Array<Rectangle>();
		
		enemies = new Array<Enemy>();
		objects = new Array<Element>();
		setMap();
		SoundResources.getInstance().getTheme().play();

	}

	@Override
	public void hide() {
		enemies.clear();
		tiles.clear();
		objects.clear();
		poolLayer.clear();
		SoundResources.getInstance().getTheme().stop();

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
		hud.dispose();
		renderer.dispose();
		map.dispose();
		enemies.clear();
		tiles.clear();
		objects.clear();
		poolLayer.clear();
	}
	
	public static Array<Rectangle> getTiles(int startX, int startY, int endX, int endY) {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get("block");
		
		poolLayer.freeAll(tiles);
		tiles.clear();
		for(int y = startY; y <= endY; y++) {
			for(int x = startX; x <= endX; x++) {
				Cell cell = layer.getCell(x, y);
				if (cell != null) {
					Rectangle rect = poolLayer.obtain();
					rect.set(x*32f, y*32f, 32, 32);
					tiles.add(rect);
				}
			}
		}
		
		return tiles;
	}

	public static Array<Element> getAll() {
		Array<Element> all = new Array<Element>();
		all.add(player);
		all.addAll(enemies);
		all.addAll(objects);
		return all;
	}

	/**
	 * @return the objects
	 */
	public static Array<Element> getObjects() {
		return objects;
	}

	public static Player getPlayer() {
		return player;
	}
	
	public void changeLevel(int level) {
		this.level = level;
		if(level == -1) {
			game.setWin(true);
			game.changeScreen(GameOverScreen.ID);
		}
		else {
			hide();
			show();
		}
	}
	
}
