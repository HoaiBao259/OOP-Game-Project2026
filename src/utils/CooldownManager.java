package utils;

import java.util.HashMap;
import java.util.Map;
import skills.Skill;

public class CooldownManager {
    private static CooldownManager instance = null;
    private Map<Skill, Double> cooldowns = new HashMap();

    public CooldownManager() {
    }

    public static CooldownManager getInstance() {
        if (instance == null) {
            instance = new CooldownManager();
        }
        return instance;
    }

    public boolean available(Skill var1) {
      return this.currentTime() >= (Double)this.cooldowns.getOrDefault(var1, (double)0.0F);
   }

   public void start(Skill var1) {
      this.cooldowns.put(var1, this.currentTime() + var1.cooldown);
   }

   private double currentTime() {
      return (double)System.currentTimeMillis() / (double)1000.0F;
   }
}
