package com.mygdx.nullapointershooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ShotManager {

    public static final int SHOT_Y_OFFSET = 90;
    public static final int SHOT_SPEED = 300;
    public static final float MINIMUM_TIME_BETWEEN_SHOTS = 0.5f;
    private static final float ENEMY_SHOT_Y_OFFSET = 400;
    private final Texture shotTexture;
    private Texture enemyShotTexture;
    private List<AnimatedSprite> shots = new ArrayList<AnimatedSprite>();
    private float timeSinceLastShot = 0;
    private Sound lazer = Gdx.audio.newSound(Gdx.files.internal("lazer-sound.wav"));
    private List<AnimatedSprite> enemyShots = new ArrayList<AnimatedSprite>();

    public ShotManager(Texture shotTexture, Texture enemyShotTexture) {
        this.shotTexture = shotTexture;
        this.enemyShotTexture = enemyShotTexture;
    }

    public void firePlayerShot(int shipCenterXLocation) {

        if (canFireShot()) {
            Sprite newShot = new Sprite(shotTexture);
            AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
            newShotAnimated.setPosition(shipCenterXLocation, SHOT_Y_OFFSET);
            newShotAnimated.setVelocity(new Vector2(0, SHOT_SPEED));
            shots.add(newShotAnimated);
            timeSinceLastShot = 0f;
            lazer.play();
        }
    }

    private boolean canFireShot() {
        return timeSinceLastShot > MINIMUM_TIME_BETWEEN_SHOTS;
    }

    public void update() {

        Iterator<AnimatedSprite> i = shots.iterator();

        while(i.hasNext()) {
            AnimatedSprite shot = i.next();
            shot.move();
            if (shot.getY() > ShooterGame.SCREEN_HEIGHT) {
                i.remove();
            }
        }

        Iterator<AnimatedSprite> j = enemyShots.iterator();

        while(j.hasNext()) {
            AnimatedSprite enemyShot = j.next();
            enemyShot.move();
            if (enemyShot.getY() < 0) {
                j.remove();
            }
        }

        timeSinceLastShot += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {

        for (AnimatedSprite shot : shots) {
            shot.draw(batch);
        }

        for (AnimatedSprite enemyShot : enemyShots) {
            enemyShot.draw(batch);
        }
    }

    public void fireEnemyShot(int enemyCenterXLocation) {
        Sprite newShot = new Sprite(enemyShotTexture);
        AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
        newShotAnimated.setPosition(enemyCenterXLocation, ENEMY_SHOT_Y_OFFSET);
        newShotAnimated.setVelocity(new Vector2(0, -SHOT_SPEED));
        enemyShots.add(newShotAnimated);
    }

    public boolean playerShotTouches(Rectangle boundingBox) {
        return shotTouches(shots, boundingBox);
    }

    public boolean enemyShotTouches(Rectangle boundingBox) {
        return shotTouches(enemyShots, boundingBox);
    }

    private boolean shotTouches(List<AnimatedSprite> shots, Rectangle boundingBox) {
        Iterator<AnimatedSprite> i = shots.iterator();
        while (i.hasNext()) {
            AnimatedSprite shot = i.next();
            if (Intersector.intersectRectangles(shot.getBoundingBox(), boundingBox, new Rectangle())) {
                i.remove();
                return true;
            }
        }

        return false;
    }
}
