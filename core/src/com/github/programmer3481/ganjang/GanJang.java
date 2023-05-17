package com.github.programmer3481.ganjang;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.programmer3481.ganjang.logic.blocks.Block;
import com.github.programmer3481.ganjang.logic.blocks.Conveyor;
import com.github.programmer3481.ganjang.logic.blocks.TickingBlock;
import com.github.programmer3481.ganjang.logic.World;
import com.github.programmer3481.ganjang.logic.blocks.Blocks;

import java.util.ArrayList;
import java.util.Random;

public class GanJang extends ApplicationAdapter {
    //SpriteBatch batch;
    //Texture img;
    //
    //@Override
    //public void create () {
    //	batch = new SpriteBatch();
    //	img = new Texture("badlogic.jpg");
    //}

    //@Override
    //public void render () {
    //	ScreenUtils.clear(1, 0, 0, 1);
    //	batch.begin();
    //	batch.draw(img, 0, 0);
    //	batch.end();
    //}
    //
    //@Override
    //public void dispose () {
    //	batch.dispose();
    //	img.dispose();
    //}

    private TiledMap map;
    private OrthogonalTiledMapRendererSprites renderer;
    private OrthographicCamera camera;
    private TextureRegion[][] tiles;
    private TiledMapTileLayer tileMap;
    private TiledMapTileLayer tileMapTop;

    private BitmapFont font;
    private SpriteBatch batch;

    private Texture[] itemTex;

    private int frame = 0;

    private World world;

	public ShapeRenderer debugShapes; // DEBUG

    @Override
    public void create() {
		debugShapes = new ShapeRenderer(); //DEBUG

        world = new World(32, 32);
        Random random = new Random();
        world.setBlock(Blocks.output(world, 16, 16));//random.nextInt(32), random.nextInt(32)));
        for (int i = 0; i < 10; i++) {
            world.setBlock(Blocks.beans(world, random.nextInt(32), random.nextInt(32)));
        }


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 512, 512);
        camera.update();

        font = new BitmapFont();
        font.getData().setScale(3);
        batch = new SpriteBatch();

        Texture tileTex = new Texture(Gdx.files.internal("ganjangTiles.png"));
        tiles = TextureRegion.split(tileTex, 16, 16);

        itemTex = new Texture[]{
                new Texture(Gdx.files.internal("beans.png")),
                new Texture(Gdx.files.internal("fBean.png")),
                new Texture(Gdx.files.internal("soy.png"))
        };

        map = new TiledMap();
        tileMap = new TiledMapTileLayer(32, 32, 16, 16);
        tileMapTop = new TiledMapTileLayer(32, 32, 16, 16);
        map.getLayers().add(tileMap);
        map.getLayers().add(tileMapTop);

        updateTileMap();

        renderer = new OrthogonalTiledMapRendererSprites(map, 1);
    }

    public void updateTileMap() {
        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Block block = world.getBlock(x, y);
                Cell cell = new Cell();
                cell.setTile(new StaticTiledMapTile(tiles[block.getTexX()][block.getTexY()]));
                if (!block.isTopLayer()) {
                    tileMap.setCell(x, y, cell);
                    tileMapTop.setCell(x, y, null);
                } else {
                    tileMapTop.setCell(x, y, cell);
                    tileMap.setCell(x, y, null);
                }
            }
        }
    }

    @Override
    public void render() {
		ScreenUtils.clear(100f / 255f, 100f / 255f, 250f / 255f, 1f);

		if (frame > -1) {
			renderer.getSprites().clear();
			world.tick();

			for (TickingBlock tickingBlock : world.getTickingBlocks()) {
				if (tickingBlock instanceof Conveyor conveyor) {
					for (Conveyor.ConveyorItem item : conveyor.getItems()) {
						Sprite sprite = new Sprite(itemTex[item.itemId]);
						sprite.setScale(0.8f, 0.8f);
						switch (conveyor.getDirection()) {
							case 0:
								sprite.setPosition((conveyor.getxPos() - item.hPosition / 256.0f) * 16,
										(conveyor.getyPos() - (item.position - 128) / 256.0f) * 16);
								break;
							case 1:
								sprite.setPosition((conveyor.getxPos() - (item.position - 128) / 256.0f) * 16,
										(conveyor.getyPos() + item.hPosition / 256.0f) * 16);
								break;
							case 2:
								sprite.setPosition((conveyor.getxPos() + item.hPosition / 256.0f) * 16,
										(conveyor.getyPos() + (item.position - 128) / 256.0f) * 16);
								break;
							case 3:
								sprite.setPosition((conveyor.getxPos() + (item.position - 128) / 256.0f) * 16,
										(conveyor.getyPos() - item.hPosition / 256.0f) * 16);
								break;

						}
						renderer.getSprites().add(sprite);
					}
				}
			}
		}

        renderer.setView(camera);
        renderer.render();

		//DEBUG
		if (Gdx.input.isKeyPressed(Input.Keys.E)) {
			debugShapes.begin(ShapeRenderer.ShapeType.Filled);
			for (TickingBlock tickingBlock : world.getTickingBlocks()) {
				if (tickingBlock instanceof Conveyor conveyor) {
					debugShapes.setColor((conveyor.color & 4) > 0 ? 1.0f : 0.0f, (conveyor.color & 2) > 0 ? 1.0f : 0.0f, conveyor.color & 1, 1.0f);
					debugShapes.circle(conveyor.getxPos() * 48 + 24, conveyor.getyPos() * 48 + 24, 10);
				}
			}
			debugShapes.end();
		}

        batch.begin();
        font.draw(batch, "Arrow keys: Place conveyor; 1: Harvestor 2: Fermenter 3: Press; X: Delete\nPoints: " + world.getPoints(), 10, 120);
        batch.end();

        int x = Math.max(0, Math.min(31, (int) (Gdx.input.getX() * 32.0f / Gdx.graphics.getWidth())));
        int y = Math.max(0, Math.min(31, (int) ((Gdx.graphics.getHeight() - Gdx.input.getY()) * 32.0f / Gdx.graphics.getHeight())));

        //System.out.println(Gdx.input.getX());
        //System.out.println(x);
        if (!(world.getBlock(x, y).getTexX() == 1 && world.getBlock(x, y).getTexY() == 0) &&
			!(world.getBlock(x, y).getTexX() == 1 && world.getBlock(x, y).getTexY() == 1)) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                world.setBlock(Blocks.grass(world, x, y));
                updateTileMap();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                world.setBlock(Blocks.conveyor(world, x, y, 0));
                updateTileMap();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                world.setBlock(Blocks.conveyor(world, x, y, 1));
                updateTileMap();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                world.setBlock(Blocks.conveyor(world, x, y, 2));
                updateTileMap();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
                world.setBlock(Blocks.conveyor(world, x, y, 3));
                updateTileMap();
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
				if (world.getPoints() >= 64) {
					world.takePoints(64);
					world.setBlock(Blocks.press(world, x, y));
					updateTileMap();
				}
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
				if (world.getPoints() >= 16) {
					world.takePoints(16);
					world.setBlock(Blocks.fermenter(world, x, y));
					updateTileMap();
				}
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
                if ((world.getBlock(x, y + 1) != null && world.getBlock(x, y + 1).getTexX() == 1 && world.getBlock(x, y + 1).getTexY() == 0) ||
                        (world.getBlock(x + 1, y) != null && world.getBlock(x + 1, y).getTexX() == 1 && world.getBlock(x + 1, y).getTexY() == 0) ||
                        (world.getBlock(x, y - 1) != null && world.getBlock(x, y - 1).getTexX() == 1 && world.getBlock(x, y - 1).getTexY() == 0) ||
                        (world.getBlock(x - 1, y) != null && world.getBlock(x - 1, y).getTexX() == 1 && world.getBlock(x - 1, y).getTexY() == 0)) {
					if (world.getPoints() >= 4) {
						world.takePoints(4);
						world.setBlock(Blocks.harvester(world, x, y));
						updateTileMap();
					}
                }
            }
        }


        frame++;
        frame %= 64;
        //((TiledMapTileLayer) map.getLayers().get(0)).setCell(new Random().nextInt(32), new Random().nextInt(32), shell);
    }
}
