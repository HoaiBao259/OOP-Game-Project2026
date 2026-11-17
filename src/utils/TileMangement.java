package utils;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.InputStream;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;

import inGameEntity.Tile;
import main.Constant;
import main.GamePanel;

public class TileMangement {
    private static TileMangement instance = null;

    private GamePanel gp;

    protected Tile[] tile;
    protected int[][] mapTileNum;
    private BufferedImage[] floorVariants;

    private TileMangement(GamePanel gp) {
        this.gp = gp;
        mapTileNum = new int[Constant.maxWorldCol][Constant.maxWorldRow];
        loadMap("/res/maps/map1.txt");
        loadTile();
        buildFloorVariants();
    }

    public static TileMangement getInstance(GamePanel gp) {
        if (instance == null) {
            instance = new TileMangement(gp);
        }
        return instance;
    }

    private void loadTile() {
        tile = new Tile[4];
        tile[0] = new Tile(0);
        tile[1] = new Tile(1);
        tile[2] = new Tile(2);
        tile[3] = new Tile(3);
    }

    private void loadMap(String filename) {
        try (InputStream is = getClass().getResourceAsStream(filename)) {
            if (is == null) {
                throw new IllegalArgumentException("Map file not found: " + filename);
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                for (int row = 0; row < Constant.maxWorldRow; row++) {
                    String line = br.readLine();
                    if (line == null) {
                        throw new IllegalArgumentException("Map has too few rows: " + filename);
                    }
                    String[] numbers = line.trim().split("\\s+");
                    for (int col = 0; col < Constant.maxWorldCol; col++) {
                        if (row < Constant.laneTopWallRow || row > Constant.laneBottomWallRow) {
                            mapTileNum[col][row] = 3;
                        } else if (row == Constant.laneTopWallRow
                                || row == Constant.laneBottomWallRow
                                || (col == 0 && row > Constant.laneTopWallRow
                                        && row < Constant.laneBottomWallRow)) {
                            mapTileNum[col][row] = 1;
                        } else if (col == 0) {
                            mapTileNum[col][row] = 3;
                        } else if (col < numbers.length - 1) {
                            mapTileNum[col][row] = Integer.parseInt(numbers[col]);
                        } else {
                            mapTileNum[col][row] = 0;
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unable to load map: " + filename, e);
        }
    }

    private void buildFloorVariants() {
        BufferedImage floorSheet = tile[0].getTileImage();
        int subTileSize = floorSheet.getWidth() / 2;
        floorVariants = new BufferedImage[] {
                floorSheet.getSubimage(0, 0, subTileSize, subTileSize),
                floorSheet.getSubimage(subTileSize, 0, subTileSize, subTileSize),
                floorSheet.getSubimage(0, subTileSize, subTileSize, subTileSize),
                floorSheet.getSubimage(subTileSize, subTileSize, subTileSize, subTileSize)
        };
        for (int row = 0; row < Constant.maxWorldRow; row++) {
            for (int col = 0; col < Constant.maxWorldCol; col++) {
                if (mapTileNum[col][row] == 2) {
                    StatueManager.getInstance(gp).addStatue(new Point(col, row));
                }
            }
        }
    }

    public void draw(Graphics2D g2d) {
        int screenX = gp.screenManagement.getScreenX();
        int screenY = gp.screenManagement.getScreenY();
        int startCol = Math.max(0, screenX / Constant.tileSize);
        int endCol = Math.min(Constant.maxWorldCol,
                (screenX + Constant.screenWidth) / Constant.tileSize + 1);
        int startRow = Math.max(0, screenY / Constant.tileSize);
        int endRow = Math.min(Constant.maxWorldRow,
                (screenY + Constant.screenHeight) / Constant.tileSize + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int tileId = mapTileNum[col][row];
                BufferedImage image = tileId == 0
                        ? floorVariants[Math.floorMod(col * 31 + row * 17, floorVariants.length)]
                        : tile[tileId].getTileImage();
                g2d.drawImage(image, col * Constant.tileSize - screenX,
                        row * Constant.tileSize - screenY, Constant.tileSize, Constant.tileSize, null);
            }
        }
    }

    public int getMapTileNum(int x, int y) {
        return mapTileNum[x][y];
    }

    public Tile getTile(int index) {
        return tile[index];
    }
}
