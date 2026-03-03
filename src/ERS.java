import java.util.Collections;

class ERS extends CardGame {

    static int count = 1;

    static int numTurns = 0;

    private int lastLetterPlayer = 0;

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

    private boolean isLetterCard(Card card) {
        String v = card.value;
        return "J".equals(v) || "Q".equals(v) || "K".equals(v) || "A".equals(v);
    }

    private int turnsForLetter(Card card) {
        switch (card.value) {
            case "J":
                return 1;
            case "Q":
                return 2;
            case "K":
                return 3;
            case "A":
                return 5;
            default:
                return 0;
        }
    }

    private void collectDeckForLastLetterPlayer() {
        if (lastLetterPlayer == 1) {
            playerOneHand.getCards().addAll(0, deck);
            playerOneTurn = true;
        } else if (lastLetterPlayer == 2) {
            playerTwoHand.getCards().addAll(0, deck);
            playerOneTurn = false;
        }
        deck.clear();

        count = 1;
        numTurns = 0;
    }

    public static boolean slappable() {
        if (deck.size() < 2) {
            return false;
        }

        Card topCard = deck.get(deck.size() - 1);

        // Check for a pair (double): last two cards have the same value
        if (deck.size() >= 2) {
            Card prevCard = deck.get(deck.size() - 2);
            if (topCard.value.equals(prevCard.value)) {
                return true;
            }
        }

        // Check for a sandwich: top card matches the card two positions back
        if (deck.size() >= 3) {
            Card sandwichCard = deck.get(deck.size() - 3);
            if (topCard.value.equals(sandwichCard.value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean playCard(Card card, Hand hand) {

        if (!isValidPlay(card)) {
            System.out.println("Invalid play: " + card.value + " of " + card.suit);
            return false;
        }

        boolean currentIsP1 = playerOneTurn;

        hand.removeCard(card);
        card.setTurned(false);
        deck.add(card);
        lastPlayedCard = card;
        triggerPlayFlash();

        if (isLetterCard(card)) {

            lastLetterPlayer = currentIsP1 ? 1 : 2;
            count = turnsForLetter(card);
            numTurns = count;

            switchTurns();
        } else {
            if (numTurns > 0) {

                numTurns--;
                if (numTurns == 0) {

                    collectDeckForLastLetterPlayer();
                    deck.clear();
                } else {

                    // keep the turn with the current player (do nothing)
                }
            } else {

                switchTurns();
            }
        }

        return true;
    }

    @Override
    public void handleComputerTurn() {
        if (slappable()) {
            playerTwoHand.getCards().addAll(0, deck);
            playerOneTurn = false;
            deck.clear();
            count = 1;
            numTurns = count;
            return;
        }
        if (playerTwoHand.getSize() > 0) {
            playCard(playerTwoHand.getCard(playerTwoHand.getSize() - 1), playerTwoHand);
            if (!deck.isEmpty()) {
                deck.get(deck.size() - 1).isTop = true;
            }
            if (playerTwoHand.getSize() > 0) {
                playerTwoHand.getCard(playerTwoHand.getSize() - 1).isTop = true;
            }
        }
    }

}