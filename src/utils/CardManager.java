package utils;
import entity.Card;
import entity.Player;
import java.awt.Color;
import java.awt.Graphics2D;

public class CardManager {
    private static CardManager instance;
    private Card[] cards = new Card[3];

    public CardManager(){
    }

    public static CardManager getInstance() {
        if (instance == null) {
            instance = new CardManager();
        }
        return instance;
    }

    public void generateCards(){
        Card[] var1 = new Card[] {
            new Card("HP", "Increase Max HP by 1", 1),
            new Card("Speed", "Increase Speed by 1", 1),
            new Card("Attack", "Increase ATK by 1", 1),
            new Card("AttackSpeed", "Decrease Auto Attack cooldown by 0.1s", 1),
            new Card("EXP Scale", "Increase EXP gain by 1.5x", 5),
            new Card("HP Regen", "Increase HP Regen rate", 25)
        
        };
        for (int var2 = 0; var2 < 3; ++var2){
            this.cards[var2] = var1[(int)(Math.random()*(double)var1.length)];
        }
    }

    public void drawCards(Graphics2D var1){
        short var2 = 300;
        short var3 = 450;
        int var4 = 704 - (var2 * 3 + 60) / 2;
        int var5 = 512 - var3 / 2;

        for (int var6 = 0; var6 < 3; ++var6){
            int var7 = var4 + var6 * (var2 + 30);
            var1.setColor(new Color(50, 50, 50, 220));
            var1.fillRoundRect(var7, var5, var2, var3, 25, 25);
            var1.setColor(Color.WHITE);
            var1.drawRoundRect(var7, var5, var2, var3, 25, 25);
            var1.setColor(Color.WHITE);
            var1.setFont(var1.getFont().deriveFont(50.0F));
            var1.drawString(this.cards[var6].name, var7 + 20, var5 + 90);
            var1.setFont(var1.getFont().deriveFont(30.0F));
            String var8 = this.cards[var6].description;
            int var9 = var2 - 40;
            String[] var10 = var8.split(" ");
            String var11 = "";
            int var12 = var5 + 200;

            for(String var16 : var10){
                if(var1.getFontMetrics().stringWidth(var11 + var16) < var9){
                    var11 = var11 + var16 + " ";
                } else {
                    var1.drawString(var11, var7 + 20, var12);
                    var11 = var16 + " ";
                    var12 += 40;
                }
            }
            var1.drawString(var11, var7 + 20, var12);
        }
    }

    public boolean checkCardClick(int var1, int var2) {
      short var3 = 300;
      short var4 = 450;
      int var5 = 704 - (var3 * 3 + 60) / 2;
      int var6 = 512 - var4 / 2;
      if (var2 >= var6 && var2 <= var6 + var4) {
         for(int var7 = 0; var7 < 3; ++var7) {
            int var8 = var5 + var7 * (var3 + 30);
            if (var1 >= var8 && var1 <= var8 + var3) {
               this.applyCardEffect(this.cards[var7]);
               return true;
            }
         }
      }

      return false;
   }

   public void applyCardEffect(Card var1) {
      switch (var1.name) {
         case "Attack" -> Player.getInstance().attackPowerUp(var1.bonus);
         case "HP" -> Player.getInstance().maxHpUp(var1.bonus);
         case "Speed" -> Player.getInstance().speedUp(var1.bonus);
         case "AttackSpeed" -> Player.getInstance().attackSpeedUp(var1.bonus);
         case "EXP Scale" -> Player.getInstance().increaseExpScale((float)var1.bonus * 0.1F);
         case "HP Regen" -> Player.getInstance().decreaseHpRegenCooldown(var1.bonus);
      }

   }
}
