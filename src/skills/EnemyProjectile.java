package skills;

import main.GamePanel;

public class EnemyProjectile {

    public int x, y;
    public int tx, ty;
    public int speed = 10;
    public int damage = 2;
    public boolean active = true;
    private GamePanel gp;
    private double dx, dy, len;

    public EnemyProjectile(int x, int y, int tx, int ty, int damage, GamePanel gp) {
        this(x, y, tx, ty, damage, gp, false);
    }

    public EnemyProjectile(int x, int y, int tx, int ty, int damage, GamePanel gp, boolean horizontalOnly) {
        this.x = x;
        this.y = y;
        this.tx = tx;
        this.ty = ty;
        this.damage = damage;
        this.gp = gp;
        dx = horizontalOnly ? -speed : tx - x;
        dy = horizontalOnly ? 0 : ty - y;
        len = Math.sqrt(dx * dx + dy * dy);
        if (len == 0) {
            len = 1;
        }
        dx = dx / len * speed;
        dy = dy / len * speed;
    }

    public void update() {
        x += dx;
        y += dy;

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
        g2.setColor(java.awt.Color.RED);
        g2.fillOval(x - 8 - gp.screenManagement.getScreenX(), y - 8 - gp.screenManagement.getScreenY(), 16, 16);
    }
}
