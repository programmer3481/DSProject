package com.github.programmer3481.ganjang.logic.blocks;

import com.github.programmer3481.ganjang.logic.World;

public class Blocks {
    public static Block grass(World world, int xPos, int yPos) {
        return new Block(world, xPos, yPos, 0, 0, false);
    }

    public static Block beans(World world, int xPos, int yPos) {
        return new Block(world, xPos, yPos, 1, 0, false);
    }

    public static Conveyor conveyor(World world, int xPos, int yPos, int direction) {
        return new Conveyor(world, xPos, yPos, 0, 1, false, direction, 4);
    }

    //public static Conveyor fastConveyor(World world, int xPos, int yPos, int direction) {
    //    return new Conveyor(world, xPos, yPos, 3, 0, false, direction, 16);
    //}
    //3, 0, 16

    public static Output output(World world, int xPos, int yPos) {
        return new Output(world, xPos, yPos, 1,1, true);
    }

    public static Machine fermenter(World world, int xPos, int yPos) {
        return new Machine(world, xPos, yPos, 2, 1, true, 64, 0, 4, 1);
    }

    public static Machine press(World world, int xPos, int yPos) {
        return new Machine(world, xPos, yPos, 1,2, true, 32, 1, 8, 2);
    }

    public static Input harvester(World world, int xPos, int yPos) {
        return new Input(world, xPos, yPos, 2, 0, true, 128, 0);
    }
}
