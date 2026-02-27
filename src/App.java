import processing.core.PApplet;

public class App extends PApplet {

    CardGame cardGame = new ERS();
    private int timer;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    @Override
    public void settings() {
        size(600, 600);
    }

    @Override
    public void draw() {
        background(255);
        // Draw player hands
        for (Card card : cardGame.playerOneHand.getCards()) {
            if (card.isTop) {
                card.setPosition(260, 450, 80, 120);
                card.draw(this);
            }
        }
        text("Cards:" + cardGame.playerOneHand.getSize(), 300, 580);
        text("Cards:" + cardGame.playerTwoHand.getSize(), 300, 40);
        for (Card card : cardGame.playerTwoHand.getCards()) {
          //  if (card.isTop) {
                card.setPosition(260, 50, 80, 120);
                card.draw(this);
          //  }
        }
        for (Card card : cardGame.deck) {
          //  if (card.isTop) {
                card.setPosition(260, height / 2 - 60, 80, 120);
                card.draw(this);
          //  }
        }
        // cardGame.playerOneHand.draw(this);
        // Draw computer hand

        // Draw draw button
        fill(200);
        cardGame.drawButton.draw(this);
        fill(0);
        textAlign(CENTER, CENTER);
        text("Play", cardGame.drawButton.x + cardGame.drawButton.width / 2,
                cardGame.drawButton.y + cardGame.drawButton.height / 2);

        // Display current player
        fill(0);
        textSize(16);
        text("Current Player: " + cardGame.getCurrentPlayer(), width / 2, 20);

        // Display deck size
        text("Deck Size: " + cardGame.getDeckSize(), width / 2 - 120,
                height / 2);
        // Display last played card
        if (cardGame.getLastPlayedCard() != null) {
            cardGame.getLastPlayedCard().setPosition(width / 2 - 40, height / 2 - 60, 80, 120);
            cardGame.getLastPlayedCard().draw(this);
        }
        if (cardGame.getCurrentPlayer() == "Player Two") {
            fill(0);
            textSize(16);
            text("Computer is thinking...", width / 2, height / 2 + 80);
            timer++;
            if (timer == 100) {
                cardGame.handleComputerTurn();
                timer = 0;
            }
        }

        cardGame.drawChoices(this);
    }

    @Override
    public void mousePressed() {
        cardGame.handleDrawButtonClick(mouseX, mouseY);
        cardGame.handleCardClick(mouseX, mouseY);
    }

}
