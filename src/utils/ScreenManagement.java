package utils;

import main.Constant;
import main.GamePanel;

public class ScreenManagement {
    private static ScreenManagement instance = null;

    public static ScreenManagement getInstance(GamePanel gp) {
        if (instance == null) {
            instance = new ScreenManagement(gp);
        }
        return instance;
    }
    private GamePanel gp;

    private float lerp = 0.1f;
    private float screenX, screenY;

    private ScreenManagement(GamePanel gp) {
        this.gp = gp;
        this.screenX = 0;
        this.screenY = 0;
    }

    public void update() {
        float maxX = Constant.maxWorldCol * Constant.tileSize - Constant.screenWidth;
        float maxY = Constant.maxWorldRow * Constant.tileSize - Constant.screenHeight;
        float targetX = Math.max(0, Math.min(maxX, gp.player.worldX - Constant.screenWidth / 3f));
        float targetY = Math.max(0, Math.min(maxY, gp.player.worldY - Constant.screenHeight / 2f));
        screenX += (targetX - screenX) * lerp;
        screenY += (targetY - screenY) * lerp;
    }

    public int getScreenX() {
        return (int)screenX;
    }

    public int getScreenY() {
        return (int)screenY;
    }
}
