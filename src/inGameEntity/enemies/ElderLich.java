package inGameEntity.enemies;

import entity.Entity;
import main.GamePanel;
import skills.HomingOrb;

import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ElderLich extends Entity {
    private final ArrayList<HomingOrb> orbs = new ArrayList<>();
    private int cooldown = 0;
    private int castCounter = 0;
    private boolean casting = false;

    private static final int CAST_TIME = 30;
    private static final int ORB_COOLDOWN = 180;

    public ElderLich(GamePanel gp, int x, int y) {
        super(gp, "Elder Lich",
                x, y,
                70, 70,
                new Rectangle(20, 20, 28, 28),
                1,
                0,
                8,
                8,
                new boolean[4],
                new int[4],
                200);
        loadSprites();
    }

    public ElderLich(GamePanel gp) {
        this(gp, 0, 0);
    }

    private void loadSprites() {
        try {
            left1 = ImageIO.read(getClass().getResource("/res/monster/elder_lich/lich-left.png"));
            left2 = ImageIO.read(getClass().getResource("/res/monster/elder_lich/lich-left-cast.png"));
            right1 = ImageIO.read(getClass().getResource("/res/monster/elder_lich/lich-right.png"));
            right2 = ImageIO.read(getClass().getResource("/res/monster/elder_lich/lich-right-cast.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }

        worldX -= getSpeed();
        setDirection(2);

        if (cooldown <= 0) {
            orbs.add(new HomingOrb(worldX, worldY, gp));
            casting = true;
            castCounter = 0;
            cooldown = ORB_COOLDOWN;
        }

        if (casting && ++castCounter >= CAST_TIME) {
            casting = false;
            castCounter = 0;
        }

        orbs.removeIf(orb -> !orb.active);
        orbs.forEach(orb -> orb.update(gp));
        cooldown--;
    }

    @Override
    public void draw(Graphics g2) {
        if (!isAlive()) {
            return;
        }

        BufferedImage image = null;
        switch (getDirection()) {
            case 2:
                image = casting ? left2 : left1;
                break;
            case 3:
                image = casting ? right2 : right1;
                break;
        }

        drawHealthBar(g2, getScreenX(), getScreenY() - 10, getHp(), getMaxHp());
        g2.drawImage(image, getScreenX(), getScreenY(), getWidth(), getHeight(), null);
    }

    public void drawProjectiles(Graphics g2) {
        orbs.forEach(orb -> orb.draw(g2));
    }
}