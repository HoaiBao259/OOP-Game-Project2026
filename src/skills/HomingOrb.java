package skills;

import main.GamePanel;

public class HomingOrb {
    private GamePanel gp;
    public int x, y;
    public int speed = 6;
    public int damage = 5;
    public boolean active = true;

    public HomingOrb(int x, int y, GamePanel gp) {
        this.x = x;
        this.y = y;
        this.gp = gp;
    }

    public void update(GamePanel gp) {
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
        }
        int maxWorldX = main.Constant.maxWorldCol * main.Constant.tileSize;
        int maxWorldY = main.Constant.maxWorldRow * main.Constant.tileSize;
        if (x < 0 || x >= maxWorldX || y < 0 || y >= maxWorldY) {
            active = false;
        }
    }

    public void draw(java.awt.Graphics g2) {
        g2.setColor(java.awt.Color.MAGENTA);
        g2.fillOval(x - 8 - gp.screenManagement.getScreenX(), y - 8 - gp.screenManagement.getScreenY(), 16, 16);
    }
}
