package com.github.programmer3481.ganjang;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class DirectionalSprite extends Sprite {
    private final int direction;

    public DirectionalSprite (Texture texture, int direction) {
        super(texture);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }
}
