package com.mygdx.nullapointershooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public class ShotManager {

    public static final int SHOT_Y_OFFSET = 90;
    public static final int SHOT_SPEED = 300;
    public static final float MINIMUM_TIME_BETWEEN_SHOTS = 0.5f;
    private final Texture shotTexture;
    private List<AnimatedSprite> shots = new ArrayList<AnimatedSprite>();
    private float timeSinceLastShot = 0;
    private Sound lazer = Gdx.audio.newSound(Gdx.files.internal("lazer-sound.wav"));

    public ShotManager(Texture shotTexture) {
        this.shotTexture = shotTexture;
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

        Iterator<AnimatedSprite> iterator = shots.iterator();

        while(iterator.hasNext()) {
            AnimatedSprite shot = iterator.next();
            shot.move();
            if (shot.getY() > ShooterGame.SCREEN_HEIGHT) {
                iterator.remove();
            }
        }
        timeSinceLastShot += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {

        for (AnimatedSprite shot : shots) {
            shot.draw(batch);
        }
    }
}
