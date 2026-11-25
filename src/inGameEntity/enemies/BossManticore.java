package inGameEntity.enemies;

import entity.Entity;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import main.GamePanel;
import skills.HomingOrb;

public class BossManticore extends Entity {

    public ArrayList<HomingOrb> shots = new ArrayList<>();
    private int shootCooldown = 0;

    public BossManticore(GamePanel gp, int x, int y) {
        super(gp, "Manticore",
                x, y,
                90, 90,
                new Rectangle(24, 24, 32, 32),
                2,
                2,
                10,
                10,
                new boolean[4],
                new int[4], 200);
        loadSprites();
    }

    public BossManticore(GamePanel gp) {
        this(gp, 0, 0);
    }

    private void loadSprites() {
        try {
            up1 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-up-1.png"));
            up2 = javax.imageio.ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-up-2.png"));
            down1 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-down-1.png"));
            down2 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-down-2.png"));
            left1 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-left-1.png"));
            left2 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-left-2.png"));
            right1 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-right-1.png"));
            right2 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/boss_manticore/manticore-right-2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!isAlive())
            return;
        if (spriteCounter++ > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }
        int py = gp.player.worldY;

        worldX -= getSpeed();
        setDirection(2);
        if (py < worldY) {
            worldY -= getSpeed();
        }
        if (py > worldY) {
            worldY += getSpeed();
        }

        // ranged attack
        if (shootCooldown <= 0) {
            shots.add(new HomingOrb(worldX, worldY, gp));
            shootCooldown = 40;
        }
        shootCooldown--;

        shots.removeIf(shot -> !shot.active);
        shots.forEach(shot -> shot.update(gp));
    }

    @Override
    public void draw(java.awt.Graphics g2) {
        BufferedImage image = null;
        switch (getDirection()) {
            case 0:
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                break;
            case 1:
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                break;
            case 2:
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                break;
            case 3:
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                break;
        }
        g2.drawImage(image, getScreenX(), getScreenY(), 90, 90, null);
        drawHealthBar(g2, getScreenX(), getScreenY() - 10, this.getHp(), this.getMaxHp());
    }

    public void drawProjectiles(java.awt.Graphics g2) {
        shots.forEach(shot -> shot.draw(g2));
    }
}
