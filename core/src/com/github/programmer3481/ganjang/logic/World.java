package com.github.programmer3481.ganjang.logic;

import com.github.programmer3481.ganjang.logic.blocks.Block;
import com.github.programmer3481.ganjang.logic.blocks.Blocks;
import com.github.programmer3481.ganjang.logic.blocks.TickingBlock;

import java.util.LinkedList;

public class World { // 0:bean 1: intermedite 2: product
    public static final int ITEM_TYPE_COUNT = 3;
    //private final int[] stats = new int[ITEM_TYPE_COUNT];
    private int points = 4;
    private final Block[][] blocks;
    private final LinkedList<TickingBlock> tickingBlocks = new LinkedList<>();

    public World(int sizeX, int sizeY) {
        blocks = new Block[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                blocks[x][y] = Blocks.grass(this, x, y);
            }
        }
    }

    public void tick() {
        for (TickingBlock tickingBlock : tickingBlocks) {
            tickingBlock.reset();
        }
        for (TickingBlock tickingBlock : tickingBlocks) {
            tickingBlock.tick();
        }
    }

    public void setBlock(Block block) {
        Block toReplace = blocks[block.getxPos()][block.getyPos()];
        if (toReplace instanceof TickingBlock) {
            tickingBlocks.remove(toReplace);
        }

        blocks[block.getxPos()][block.getyPos()] = block;
        if (block instanceof TickingBlock) {
            tickingBlocks.add((TickingBlock) block);
        }
    }

    public void addStat(int item) {
        //stats[item]++;
        switch (item) {
            case 0 -> points++;
            case 1 -> points += 8;
            case 2 -> points += 32;
        }
    }

    public Block getBlock(int xPos, int yPos) {
        if (xPos < 0 || xPos >= blocks.length ||
            yPos < 0 || yPos >= blocks[0].length) {
            return null;
        }
        return blocks[xPos][yPos];
    }

    /*
    public int[] getStats() {
        return stats;
    }
     */
    public int getPoints() {
        return points;
    }

    public void takePoints(int points) {
        this.points -= points;
    }

    public LinkedList<TickingBlock> getTickingBlocks() {
        return tickingBlocks;
    }
}
