package com.github.programmer3481.ganjang.logic;

import com.github.programmer3481.ganjang.logic.blocks.Blocks;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        World world = new World(8, 8);
        world.setBlock(Blocks.harvester(world, 2, 0));
        world.setBlock(Blocks.conveyor(world, 2,1, 0));
        //world.setBlock(Blocks.conveyor(world, 2,2, 0));
        world.setBlock(Blocks.fermenter(world, 2, 2));
        world.setBlock(Blocks.conveyor(world, 2,3, 0));
        world.setBlock(Blocks.output(world, 2, 4));
        for (int i = 0; i < 100; i++) {
            world.tick();
        }
    }
}