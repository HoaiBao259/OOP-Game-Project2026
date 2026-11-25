package inGameEntity.enemies;

import entity.Entity;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class Warbeast extends Entity {

    public Warbeast(GamePanel gp, int x, int y) {
        super(
                gp,
                "Warbeast",
                x, y,
                64, 64, // kích thước quái
                new Rectangle(20, 20, 24, 24), // hitbox
                2, // speed
                4, // damage
                30, // hp
                30, // maxHp
                new boolean[4],
                new int[4], 100);
        loadSprites();
    }

    public Warbeast(GamePanel gp) {
        this(gp, 0, 0);
    }

    private void loadSprites() {
        try {
            up1 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-up-1.png"));
            up2 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-up-2.png"));
            down1 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-down-1.png"));
            down2 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-down-2.png"));
            left1 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-left-1.png"));
            left2 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-left-2.png"));
            right1 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-right-1.png"));
            right2 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/war_beast/beast-right-2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!isAlive())
            return;
        if (spriteCounter++ > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        worldX -= getSpeed();
        setDirection(2);
    }

    public void draw(java.awt.Graphics g2) {
        drawHealthBar(g2, getScreenX(), getScreenY() - 10, getHp(), getMaxHp());
        BufferedImage image = null;
        switch (getDirection()) {
            case 0:
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case 1:
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case 2:
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case 3:
                image = (spriteNum == 1) ? right1 : right2;
                break;
            default:
                break;
        }
        g2.drawImage(image, getScreenX(), getScreenY(), getWidth(), getHeight(), null);
    }
}
