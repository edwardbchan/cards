import java.util.Collections;

class ERS extends CardGame {
    int count = 1;
    int numTurns;
    boolean mustPlayFaceCard;
    boolean slappable;

    @Override
    protected void dealCards(int numCards) {
        Collections.shuffle(deck);
        for (int i = 0; i < numCards; i++) {
            Card card = deck.remove(0);
            card.setTurned(true);
            playerOneHand.addCard(deck.remove(0));
            playerTwoHand.addCard(card);
        }
        if (!playerOneHand.getCards().isEmpty()) {
            playerOneHand.getCard(playerOneHand.getSize() - 1).isTop = true;
        }
        if (!playerTwoHand.getCards().isEmpty()) {
            playerTwoHand.getCard(playerTwoHand.getSize() - 1).isTop = true;
        }

        // position cards
        playerOneHand.positionCards(50, 450, 80, 120, 20);
        playerTwoHand.positionCards(50, 50, 80, 120, 20);
    }

    @Override
    public void handleDrawButtonClick(int mouseX, int mouseY) {

        if (playerOneTurn && playerOneHand.getSize() > 0) {
            playCard(playerOneHand.getCard(playerOneHand.getSize() - 1), playerOneHand);
            if (!deck.isEmpty()) {
                deck.get(deck.size() - 1).isTop = true;
            }
            playerOneHand.getCard(playerOneHand.getSize() - 1).isTop = true;
        }

    }

    @Override
    public boolean playCard(Card card, Hand hand) {
        numTurns = count;
        // Check if card is valid to play
        if (!isValidPlay(card)) {
            System.out.println("Invalid play: " + card.value + " of " + card.suit);
            return false;
        }
        numTurns--;
        hand.removeCard(card);

        card.setTurned(false);
        deck.add(card);
        if (card.value == "J") {
            count = 1;
            playerOneTurn = !playerOneTurn;
            mustPlayFaceCard = true;
        }
        if (card.value == "Q") {
            count = 2;
            playerOneTurn = !playerOneTurn;
            mustPlayFaceCard = true;
        }
        if (card.value == "K") {
            count = 3;
            playerOneTurn = !playerOneTurn;
            mustPlayFaceCard = true;
        }
        if (card.value == "A") {
            count = 4;
            playerOneTurn = !playerOneTurn;
            mustPlayFaceCard = true;
        }
        if (numTurns == 0&&playerOneTurn) {
            playerOneTurn = !playerOneTurn;
            for (Card c : deck) {
                playerOneHand.getCards().add(0, c);
            }
            deck.clear();
            
        }
        return true;
    }

    @Override
    public void handleComputerTurn() {
        numTurns = count;
        if (playerTwoHand.getSize() > 0) {
            playCard(playerTwoHand.getCard(playerTwoHand.getSize() - 1), playerTwoHand);
            numTurns--;
            if (!deck.isEmpty()) {
                deck.get(deck.size() - 1).isTop = true;
            }
            playerTwoHand.getCard(playerTwoHand.getSize() - 1).isTop = true;
        }
        if (numTurns == 0) {
            playerOneTurn = !playerOneTurn;
        }
    }

}