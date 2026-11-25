package main;

import java.awt.Color;
import java.awt.Graphics2D;
import main.GamePanel.GameState;
import utils.CardManager;
import utils.MouseManager;

public class CardChoosingState {
    private GamePanel gp;
    private static CardChoosingState instance = null;
    private boolean cardsGenerated = false;
    private CardManager cardManager = CardManager.getInstance();

    public static CardChoosingState getInstance(GamePanel var0) {
        if (instance == null) {
            instance = new CardChoosingState(var0);
        }
        return instance;
    }

    public CardChoosingState(GamePanel var1) {
        this.gp = var1;
    }

    public void update() {
        if (!this.cardsGenerated) {
            this.cardManager.generateCards();
            this.cardsGenerated = true;
        }

        if(MouseManager.getInstance().clicked){
            int var1 = MouseManager.getInstance().mouseX;
            int var2 = MouseManager.getInstance().mouseY;
            MouseManager.getInstance().clicked = false;
            if (this.cardManager.checkCardClick(var1, var2)){
                this.cardsGenerated = false;
                this.gp.gameState = GameState.PLAYING;
            }
        }
    }

    public void draw(Graphics2D var1) {
        if (this.cardsGenerated){
            var1.setColor(new Color(0, 0, 0, 150));
            var1.fillRect(0, 0, 1408, 1024);
            this.cardManager.drawCards(var1);
        }
    }
}
