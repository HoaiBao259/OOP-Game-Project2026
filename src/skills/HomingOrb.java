package skills;

import main.Constant;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics;

public class HomingOrb {
    private final GamePanel gp;
    public int x, y;
    public int speed = 6;
    public int damage = 5;
    public boolean active = true;
    private int lifeTime = 240;

    public HomingOrb(int x, int y, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.gp = gp;
    }

    public void update(GamePanel gp) {
        if (!active) {
            return;
        }

        int dx = gp.player.worldX - x;
        int dy = gp.player.worldY - y;
        double distance = Math.sqrt((double) dx * dx + (double) dy * dy);
        if (distance > 0) {
            x += (int) Math.round(dx / distance * speed);
            y += (int) Math.round(dy / distance * speed);
        }

        if (gp.player.getHitBox().contains(x, y)) {
            gp.player.damageHp(damage);
            active = false;
            return;
        }

        lifeTime--;
        int maxWorldX = Constant.maxWorldCol * Constant.tileSize;
        int maxWorldY = Constant.maxWorldRow * Constant.tileSize;
        if (lifeTime <= 0 || x < 0 || x >= maxWorldX || y < 0 || y >= maxWorldY) {
            active = false;
        }
    }

    public void draw(Graphics g2) {
        if (!active) {
            return;
        }
        g2.setColor(Color.MAGENTA);
        g2.fillOval(
                x - 8 - gp.screenManagement.getScreenX(),
                y - 8 - gp.screenManagement.getScreenY(),
                16,
                16
        );
    }
}