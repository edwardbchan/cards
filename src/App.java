import processing.core.PApplet;

public class App extends PApplet {

    CardGame cardGame = new ERS();
    private int timer;
    private int slapTimer; // timer used specifically for computer slap reaction
    static String slap = "hello";
    static String win = "";

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
        stroke(0);
        text(win, width / 2 + 150, height / 2 + 20);
        text(slap, width / 2 + 150, height / 2);
        if (ERS.slappable()) {
            slap = "Slap!";

            slapTimer++;
            if (slapTimer >= 100) {
                ((ERS) cardGame).handleComputerTurn();
                slapTimer = 0;
            }
        } else {
            slap = "Not Slappable";
            slapTimer = 0;
        }
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
            // if (card.isTop) {
            card.setPosition(260, 50, 80, 120);
            card.draw(this);
            // }
        }
        if (cardGame.playerTwoHand.getSize() > 0) {
            rect (260, 50, 80, 120);
        }
        for (Card card : cardGame.deck) {
            // if (card.isTop) {
            card.setPosition(260, height / 2 - 60, 80, 120);
            card.draw(this);
            // }
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
        text(ERS.numTurns, width / 2 + 100, 40);
        text(ERS.count, width / 2 + 100, 60);

        text("Deck Size: " + cardGame.getDeckSize(), width / 2 - 120,
                height / 2);

        int totalCards = cardGame.playerOneHand.getSize() + cardGame.playerTwoHand.getSize()
                + cardGame.getDeckSize();
        if (totalCards > 0) {
            if (cardGame.playerOneHand.getSize() == totalCards) {
                win = "Player One wins the game!";
            } else if (cardGame.playerTwoHand.getSize() == totalCards) {
                win = "Player Two wins the game!";
            }
        }

        // Display last played card
        if (cardGame.getLastPlayedCard() != null) {
            cardGame.getLastPlayedCard().setPosition(width / 2 - 40, height / 2 - 60, 80, 120);
            cardGame.getLastPlayedCard().draw(this);
        }

        if (cardGame.flashTimer > 0 && cardGame.getLastPlayedCard() != null) {
            pushStyle();
            float alpha = map(cardGame.flashTimer, 0, cardGame.flashDuration, 0, 255);
            stroke(255, 0, 0, alpha);
            noFill();
            strokeWeight(6);
            float cx = width / 2;
            float cy = height / 2;

            ellipse(cx, cy, 160, 160);
            popStyle();
            cardGame.flashTimer--;
        }

        if (!CardGame.playerOneTurn) {
            fill(0);
            textSize(16);
            text("Computer is thinking...", width / 2, height / 2 + 80);

            timer++;
            if (timer >= 100) {
                ((ERS) cardGame).handleComputerTurn();
                timer = 0;
            }
        }

        cardGame.drawChoices(this);
    }

    @Override
    public void keyPressed() {
        if (keyPressed && key == ' ') {
            // pass current turn information into slap handler
            handleSlap(cardGame.playerOneTurn);
        }
    }

    @Override
    public void mousePressed() {
        cardGame.handleDrawButtonClick(mouseX, mouseY);
        cardGame.handleCardClick(mouseX, mouseY);
    }

    public void handleSlap(boolean currentPlayerOne) {
        if (ERS.slappable()) {
            if (currentPlayerOne) {
                ERS.playerOneHand.getCards().addAll(0, ERS.deck);
                ERS.playerOneTurn = true;
                win = "Player One Gets " + ERS.deck.size() + " cards!";
            } else {
                ERS.playerTwoHand.getCards().addAll(0, ERS.deck);
                win = "Player Two Gets " + ERS.deck.size() + " cards!";
                ERS.playerOneTurn = false;
            }
        } else {

            if (currentPlayerOne) {
                ERS.playerTwoHand.getCards().addAll(0, ERS.deck);
                win = "Player Two Gets " + ERS.deck.size() + " cards!";
                ERS.playerOneTurn = false;
            } else {
                ERS.playerOneHand.getCards().addAll(0, ERS.deck);
                win = "Player One Gets " + ERS.deck.size() + " cards!";
                ERS.playerOneTurn = true;
            }
        }
        ERS.deck.clear();
    }
}
