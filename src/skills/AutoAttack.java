package skills;

import java.awt.Point;

import entity.Entity;
import main.Constant;
import main.GamePanel;

public class AutoAttack extends Skill {
    public AutoAttack(GamePanel gp) {
        super(gp);
        this.name = "Auto Attack";
        this.cooldown = 0.3d;
        this.manaCost = 0.0d; // no mana cost
    }

    @Override
    public void activate(Entity caster, Point target) {
        // Implementation of auto attack logic
        int damage = caster.getAttackPower() + 1;
        Point mapEnd = new Point(Constant.maxWorldCol * Constant.tileSize - 1, caster.worldY);
        Projectile projectile = new Projectile(gp, mapEnd, caster, damage, 10,
                Constant.maxWorldCol * Constant.tileSize);
        gp.projectileManager.addProjectile(projectile);
    }

    public void decreaseCooldown(double amount) {
        this.cooldown = Math.max(0.1d, this.cooldown - amount * 0.1d);
    }
}
