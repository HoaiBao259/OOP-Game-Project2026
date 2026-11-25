package utils;

import entity.Crep;
import entity.Entity;
import inGameEntity.enemies.BossManticore;
import inGameEntity.enemies.ElderLich;
import inGameEntity.enemies.MiasmaMage;
import inGameEntity.enemies.Warbeast;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;
import main.Constant;
import main.GamePanel;

public class EnemiesManager {
    GamePanel gp;
    private static EnemiesManager instance = null;
    private Random random = new Random();
    private int spawnCountdown = 45;
    private int spawnInterval = 45;
    private int maxActiveEnemies = 8;
    private static final int MAX_SPAWN_DISTANCE = 200;

    public static EnemiesManager getInstance(GamePanel gp) {
        if (instance == null) {
            instance = new EnemiesManager(gp);
        }
        return instance;
    }

    private ArrayList<Entity> enemies = new ArrayList<>();

    private EnemiesManager(GamePanel gp) {
        this.gp = gp;
    }

    public void enemiesGrowth() {
        for (Entity enemy : enemies) {
            enemy.growth();
        }

        maxActiveEnemies++;
        spawnInterval = Math.max(20, spawnInterval - 4);
    }

    public void proccessEnemiesOutOfBound() {
        for (Entity enemy : enemies) {
            int minY = (Constant.laneTopWallRow + 1) * Constant.tileSize + enemy.getHeight() / 2;
            int maxY = Constant.laneBottomWallRow * Constant.tileSize - enemy.getHeight() / 2;
            enemy.worldY = Math.max(minY, Math.min(maxY, enemy.worldY));
        }
    }

    public void updateEnemies() {
        enemies.removeIf(enemy -> !enemy.isAlive()
                || enemy.worldX < gp.player.worldX - Constant.screenWidth);

        spawnCountdown--;
        if (spawnCountdown <= 0 && enemies.size() < maxActiveEnemies) {
            spawnEnemy();
            spawnCountdown = spawnInterval + random.nextInt(spawnInterval);
        }

        for (Entity enemy : enemies) {
            enemy.update();
        }
        proccessEnemiesOutOfBound();
    }

    private void spawnEnemy() {
        int spawnX = gp.screenManagement.getScreenX() + Constant.screenWidth + Constant.tileSize
                + random.nextInt(MAX_SPAWN_DISTANCE + 1);
        int maxX = Constant.maxWorldCol * Constant.tileSize - Constant.tileSize;
        spawnX = Math.min(spawnX, maxX);
        int minY = (Constant.laneTopWallRow + 1) * Constant.tileSize + Constant.tileSize / 2;
        int maxY = Constant.laneBottomWallRow * Constant.tileSize - Constant.tileSize / 2;
        int spawnY = minY + random.nextInt(maxY - minY + 1);

        Entity enemy;
        switch (random.nextInt(6)) {
            case 0:
            case 1:
                enemy = new Crep(gp, spawnX, spawnY);
                break;
            case 2:
                enemy = new MiasmaMage(gp, spawnX, spawnY);
                break;
            case 3:
                enemy = new Warbeast(gp, spawnX, spawnY);
                break;
            case 4:
                enemy = new ElderLich(gp, spawnX, spawnY);
                break;
            default:
                enemy = new BossManticore(gp, spawnX, spawnY);
                break;
        }
        enemy.growth();
        enemies.add(enemy);
    }

    public void drawEnemies(Graphics2D g2) {
        for (Entity enemy : enemies) {
            if (!enemy.isAlive()) {
                continue;
            }
            if (enemy.isInFrame()) {
                enemy.draw(g2);
            }
            if (enemy instanceof MiasmaMage) {
                ((MiasmaMage) enemy).drawProjectiles(g2);
            } else if (enemy instanceof BossManticore) {
                ((BossManticore) enemy).drawProjectiles(g2);
            }
        }
    }

    public ArrayList<Entity> getEnemies() {
        return enemies;
    }
}
