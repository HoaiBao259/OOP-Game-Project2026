package inGameEntity.enemies;

import entity.Entity;
import java.awt.Rectangle;
import java.util.ArrayList;
import main.Constant;
import main.GamePanel;
import skills.HomingOrb;

public class ElderLich extends Entity {
    private final ArrayList<HomingOrb> orbs = new ArrayList<>();
    private int cooldown;
    private int castCounter;
    private boolean casting;
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
                new int[4], 200);
        loadSprites();
    }

    public ElderLich(GamePanel gp) {
        this(gp, 0, 0);
    }

    private void loadSprites() {
        try {
            left1 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/elder_lich/lich-left.png"));
            left2 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/elder_lich/lich-left-cast.png"));
            right1 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/elder_lich/lich-right.png"));
            right2 = javax.imageio.ImageIO
                    .read(getClass().getResource("/res/monster/elder_lich/lich-right-cast.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if (!isAlive())
            return;
        worldX -= getSpeed();
        setDirection(2);
        if (cooldown <= 0) {
            orbs.add(new HomingOrb(worldX, worldY, gp));
            casting = true;
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
    public void draw(java.awt.Graphics g2) {
        if (!isAlive())
            return;
        orbs.forEach(orb -> orb.draw(g2));
        java.awt.image.BufferedImage image = null;
        switch (getDirection()) {
            case 2:
                image = casting ? left2 : left1;
                break;
            case 3:
                image = casting ? right2 : right1;
                break;
        }
        drawHealthBar(g2, getScreenX(), getScreenY() - 10, getHp(), getMaxHp());
        g2.drawImage(image, getScreenX(), getScreenY(), Constant.tileSize, Constant.tileSize, null);
    }
}
