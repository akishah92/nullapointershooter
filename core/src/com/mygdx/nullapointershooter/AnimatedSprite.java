package com.mygdx.nullapointershooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

public class AnimatedSprite {
    private static final int FRAMES_ROW = 2;
    private static final int FRAMES_COL = 2;

    private Sprite sprite;
    private Animation animation;
    private TextureRegion[] frames;
    private TextureRegion currentFrame;

    private float stateTime;

    public AnimatedSprite(Sprite sprite){

        this.sprite = sprite;
        Texture texture = sprite.getTexture();
        TextureRegion[][] temp = TextureRegion.split(texture, texture.getWidth()/FRAMES_COL, texture.getHeight()/FRAMES_ROW);
        frames = new TextureRegion[FRAMES_COL * FRAMES_ROW];
        int index = 0;
        for (int i = 0; i < FRAMES_ROW; i++) {
            for (int j = 0; j < FRAMES_COL; j++) {
                frames[index++] = temp[i][j];
            }
        }

        animation = new Animation(0.1f, frames);
        stateTime = 0f;
    }

    public void draw(SpriteBatch spriteBatch) {
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = (TextureRegion)animation.getKeyFrame(stateTime, true);

        spriteBatch.draw(currentFrame, sprite.getX(), sprite.getY());
    }

    public void setPosition(float x, float y) {
        float widthOffset = sprite.getWidth()/FRAMES_COL;
        sprite.setPosition(x - widthOffset/2, y);
    }
}
