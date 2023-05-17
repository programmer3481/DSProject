package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

public class Block {
    private final World world;
    private final int xPos, yPos;
    private final int texX, texY;
    private final boolean topLayer;

    public Block(World world, int xPos, int yPos, int texX, int texY, boolean topLayer) {
        this.world = world;
        this.xPos = xPos;
        this.yPos = yPos;
        this.texX = texX;
        this.texY = texY;
        this.topLayer = topLayer;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getTexX() {
        return texX;
    }

    public int getTexY() {
        return texY;
    }

    public boolean isTopLayer() {
        return topLayer;
    }

    public World getWorld() {
        return world;
    }
}
