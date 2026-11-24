package inGameEntity.enemies;

import entity.Entity;
import main.GamePanel;
import skills.HomingOrb;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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
                new int[4],
                200);
        loadSprites();
    }

    public BossManticore(GamePanel gp) {
        this(gp, 0, 0);
    }

    private void loadSprites() {
        try {
            up1 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-up-1.png"));
            up2 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-up-2.png"));
            down1 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-down-1.png"));
            down2 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-down-2.png"));
            left1 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-left-1.png"));
            left2 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-left-2.png"));
            right1 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-right-1.png"));
            right2 = ImageIO.read(getClass().getResource("/res/monster/boss_manticore/manticore-right-2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }

        if (spriteCounter++ > 15) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        worldX -= getSpeed();
        setDirection(2);

        int dy = gp.player.worldY - worldY;
        if (Math.abs(dy) <= getSpeed()) {
            worldY = gp.player.worldY;
        } else if (dy < 0) {
            worldY -= getSpeed();
        } else {
            worldY += getSpeed();
        }

        if (shootCooldown <= 0) {
            shots.add(new HomingOrb(worldX, worldY, gp));
            shootCooldown = 40;
        }
        shootCooldown--;

        shots.removeIf(shot -> !shot.active);
        shots.forEach(shot -> shot.update(gp));
    }

    @Override
    public void draw(Graphics g2) {
        if (!isAlive()) {
            return;
        }

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
        }

        g2.drawImage(image, getScreenX(), getScreenY(), getWidth(), getHeight(), null);
        drawHealthBar(g2, getScreenX(), getScreenY() - 10, this.getHp(), this.getMaxHp());
    }

    public void drawProjectiles(Graphics g2) {
        shots.forEach(shot -> shot.draw(g2));
    }
}