package skills;

import entity.Entity;
import main.Constant;
import main.GamePanel;
import utils.ScreenManagement;
import utils.Sound;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;


public class Projectile {
    private GamePanel gp;
    private Entity caster;
    private ScreenManagement screenM;
    private int damage;
    private int speed;
    private boolean expired = false;
    private int range; // max distance the projectile can travel
    private int[] cor = new int[4];
    private final int startX;
    private final int startY;
    private Sound hitSound = new Sound();

    public Projectile(GamePanel gp, Point target, Entity caster, int damage, int speed, int range) {
        this.gp = gp;
        this.screenM = ScreenManagement.getInstance(gp);
        this.caster = caster;
        this.damage = damage;
        this.speed = speed;
        this.range = range;
        cor[0] = caster.worldX;
        cor[1] = caster.worldY;
        cor[2] = target.x;
        cor[3] = target.y;
        startX = cor[0];
        startY = cor[1];
        hitSound.loadSound("src/res/Sounds/hit.wav");

    }
    
    public int getTravelledDistance() {
        int dx = cor[0] - startX;
        int dy = cor[1] - startY;
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    public void update() {
        int dx = cor[2] - cor[0];
        int dy = cor[3] - cor[1];
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            cor[0] = cor[2];
            cor[1] = cor[3];
            expired = true;
            return;
        }

        double ratio = speed / distance;
        cor[0] += (int) (dx * ratio);
        cor[1] += (int) (dy * ratio);
        checkIfExpired();
    }

    public void checkIfExpired() {
        checkIfWalled();
        checkIfHitEntity();
        int maxWorldX = Constant.maxWorldCol * Constant.tileSize;
        int maxWorldY = Constant.maxWorldRow * Constant.tileSize;
        if (getTravelledDistance() >= range || cor[0] < 0 || cor[0] >= maxWorldX
                || cor[1] < 0 || cor[1] >= maxWorldY) {
            expired = true;
        }
    }

    public void checkIfHitEntity() {
        for (Entity enemy : gp.enemiesManager.getEnemies()) {
            if (expired)
                return;
            // Simple bounding box collision detection
            if (!enemy.isAlive())
                continue;
            int entityX = enemy.worldX;
            int entityY = enemy.worldY;
            if (cor[0] >= entityX - enemy.getWidth()/2 && cor[0] <= entityX + enemy.getWidth()/2 &&
            cor[1] >= entityY - enemy.getHeight()/2 && cor[1] <= entityY + enemy.getHeight()/2) {
                expired = true;
                hitSound.playWithRandomPitch();
                enemy.takeDamage(damage);
                enemy.knockBack(20, cor[2] - cor[0], cor[3] - cor[1]);
                return;
            }
        }
    }

    public void checkIfWalled() {
        int col = cor[0] / Constant.tileSize;
        int row = cor[1] / Constant.tileSize;
        if (col < 0 || row < 0 || col >= Constant.maxWorldCol || row >= Constant.maxWorldRow
                || gp.tileM.getTile(gp.tileM.getMapTileNum(col, row)).isCollision()) {
            expired = true;
        }
    }


    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillOval(cor[0] - screenM.getScreenX(), cor[1] - screenM.getScreenY(), 15, 15); // Example
    }

    public void setCaster(Entity caster) {
        this.caster = caster;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int[] getCor() {
        return cor;
    }

    public void setCor(int[] cor) {
        this.cor = cor;
    }

}
