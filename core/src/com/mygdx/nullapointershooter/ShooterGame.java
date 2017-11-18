package com.mygdx.nullapointershooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class ShooterGame extends ApplicationAdapter {
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;
	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Texture background;
	private AnimatedSprite spaceshipAnimated;
	private ShotManager shotManager;
	private Music gameMusic;
	private Enemy enemy;
	private CollisionManager collisionManager;
	private boolean isGameOver = false;

	@Override
	public void create () {

		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		batch = new SpriteBatch();

		background = new Texture("space-background.png");
		Texture spaceshipTexture = new Texture("spaceship-spritesheet.png");
		Sprite spaceshipSprite = new Sprite(spaceshipTexture);
		spaceshipAnimated = new AnimatedSprite(spaceshipSprite);
		spaceshipAnimated.setPosition(SCREEN_WIDTH/2, 0);

		Texture shotTexture = new Texture("shot-spritesheet.png");
		Texture enemyShotTexture = new Texture("enemy-shot-spritesheet.png");
		shotManager = new ShotManager(shotTexture, enemyShotTexture);

		Texture enemyTexture = new Texture("enemy-spritesheet.png");
		enemy = new Enemy(enemyTexture, shotManager);

		collisionManager = new CollisionManager(spaceshipAnimated, enemy, shotManager);

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game-music.wav"));
		gameMusic.setVolume(0.25f);
		gameMusic.setLooping(true);
		gameMusic.play();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0);

		if (isGameOver) {
			BitmapFont font = new BitmapFont();
			font.draw(batch, "GAME OVER", 200, 200);
		}

		spaceshipAnimated.draw(batch);
		enemy.draw(batch);
		shotManager.draw(batch);
		batch.end();

		handleInput();

		if (!isGameOver) {
			spaceshipAnimated.move();
			enemy.update();
			shotManager.update();

			collisionManager.handleCollisions();
		}

		if (spaceshipAnimated.isDead()) {
			isGameOver = true;
		}
	}

	private void handleInput() {

		if (Gdx.input.isTouched()) {

			if (isGameOver) {
				spaceshipAnimated.setDead(false);
				isGameOver = false;
			}

			Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPosition);

			if (touchPosition.x > spaceshipAnimated.getX()) {
				spaceshipAnimated.moveRight();
			} else {
				spaceshipAnimated.moveLeft();
			}

			shotManager.firePlayerShot(spaceshipAnimated.getX());
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
