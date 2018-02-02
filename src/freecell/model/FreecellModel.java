package freecell.model;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the underlying framework for the freecell game and operations.
 */
public class FreecellModel implements FreecellOperations<Card> {
  protected ArrayList<LinkedList<Card>> openPiles = new ArrayList<LinkedList<Card>>();
  protected ArrayList<LinkedList<Card>> cascadePiles = new ArrayList<LinkedList<Card>>();
  protected ArrayList<LinkedList<Card>> foundationPiles = new ArrayList<LinkedList<Card>>();


  @Override
  public List<Card> getDeck() {
    ArrayList<Card> deck = new ArrayList<Card>();
    final Suit[] SUIT_ARRAY = new Suit[]{Suit.CLUBS, Suit.SPADES, Suit.DIAMONDS, Suit
            .HEARTS};

    for (int i = 1; i < 14; i++) {
      for (int j = 0; j < 4; j++) {
        int v = i;
        Suit s = SUIT_ARRAY[j];
        Card c = new Card(v, s);
        deck.add(c);
      }
    }
    return deck;
  }

  /**
   * Determines if the given deck is valid, based on the specifications of "getDeck." First checks
   * for duplicates / invalid cards, then finally checks that the length of the deck (the number of
   * cards) is appropriate (52).
   *
   * @param d - deck to be validated.
   * @return true if deck meets specs, false otherwise.
   */
  private static boolean validateDeck(List<Card> d) {
    if (d == null) {
      throw new IllegalArgumentException("Deck must be non-null");
    }
    for (int i = 0; i < d.size(); i++) {
      int sameCards = 0;
      for (int j = 0; j < d.size(); j++) {
        Card card1 = d.get(j);
        if (card1.sameCard(d.get(i))) {
          sameCards++;
        }
        if (!card1.isValid()) {
          return false;
        }
      }
      if (sameCards > 1) {
        return false;
      }
    }
    return d.size() == 52;
  }

  private void resetGame() {
    this.cascadePiles = new ArrayList<>();
    this.foundationPiles = new ArrayList<>();
    this.openPiles = new ArrayList<>();
  }

  @Override
  public void startGame(List<Card> deck,
                        int numCascadePiles,
                        int numOpenPiles,
                        boolean shuffle)
          throws IllegalArgumentException {
    resetGame();

    //validate the deck
    if (!validateDeck(deck)) {
      throw new IllegalArgumentException("the given deck is invalid");
    }
    if (shuffle) {
      Collections.shuffle(deck);
    }
    if (numCascadePiles < 4 || numOpenPiles < 1) {
      throw new IllegalArgumentException("Cannot have negative number of piles");
    }
    // creates the cascade piles based on the input #
    for (int i = 1; i < numCascadePiles + 1; i++) {
      cascadePiles.add(new LinkedList<>());
    }

    //creates the "open" (freecell) piles
    for (int i = 1; i < numOpenPiles + 1; i++) {
      openPiles.add(new LinkedList<>());
    }

    // always adds 4 foundation piles, 1 for each suit
    for (int i = 0; i < 4; i++) {
      foundationPiles.add(new LinkedList<>());
    }

    //deal cards to the cascade piles
    dealCascade(deck);

    return;
  }

  /**
   * Deals the given deck of cards in a round-robin fashion to the existing cascade piles. Assumes
   * valid deck.
   *
   * @param deck - a valid deck of cards (as described in "getDeck")
   */
  private void dealCascade(List<Card> deck) {
    int numCascadePiles = cascadePiles.size();
    for (int i = 0; i < deck.size(); i++) {
      cascadePiles.get(i % numCascadePiles).addLast(deck.get(i));
    }
  }

  @Override
  public void move(PileType source,
                   int pileNumber,
                   int cardIndex,
                   PileType destination,
                   int destPileNumber)
          throws IllegalArgumentException {

    HashMap<PileType, ArrayList<LinkedList<Card>>> piles = new HashMap<>();
    piles.put(PileType.CASCADE, this.cascadePiles);
    piles.put(PileType.OPEN, this.openPiles);
    piles.put(PileType.FOUNDATION, this.foundationPiles);

    if (source == null || destination == null) {
      throw new IllegalArgumentException("PileType must be non-null");
    }

    ArrayList<LinkedList<Card>> src;
    ArrayList<LinkedList<Card>> dest;

    src = piles.get(source);
    dest = piles.get(destination);


    validateMove(src, dest, cardIndex, pileNumber, destPileNumber, destination);
    //card should always be the last in the pile; can't take from the middle
    if (cardIndex != src.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("The specified card is not at the end of the pile and " +
              "thus cannot be moved");
    }
    Card c = src.get(pileNumber).removeLast();
    dest.get(destPileNumber).addLast(c);

  }

  protected static void validateMove(ArrayList<LinkedList<Card>> s,
                                     ArrayList<LinkedList<Card>> d,
                                     int cardIndex, int pileNumber, int destPileNumber, PileType
                                             destType)
          throws IllegalArgumentException {
    // pile indices should be in bounds
    if (pileNumber < 0 || destPileNumber < 0) {
      throw new IllegalArgumentException("The pile number must be non-negative");
    }
    if (pileNumber > s.size() - 1 || destPileNumber > d.size() - 1) {
      throw new IllegalArgumentException("The pile number must be a valid index.");
    }

    if (cardIndex > s.get(pileNumber).size() - 1) {
      throw new IllegalArgumentException("The card index must not exceed the number of cards in "
              + "the pile");
    }

    if (cardIndex < 0) {
      throw new IllegalArgumentException("The card index must be positive");
    }
    // source pile must be non-empty
    if (s.get(pileNumber).isEmpty()) {
      throw new IllegalArgumentException("The source pile is empty and has no cards to move");
    }

    Card c1 = s.get(pileNumber).get(cardIndex);

    switch (destType) {
      case OPEN:
        if (d.get(destPileNumber).isEmpty()) {
          return;
        } else {
          throw new IllegalArgumentException("Freecells can only have one card at a time");
        }
      case FOUNDATION:
        if (d.get(destPileNumber).isEmpty()) {
          if (c1.getValue() == 1) {
            return;
          } else {
            throw new IllegalArgumentException("Can only place Aces on empty foundation piles");
          }
        } else {
          Card c2 = d.get(destPileNumber).peekLast();
          if (c1.sameSuit(c2)) {
            if (c1.getValue() == 1 + c2.getValue()) {
              return;
            } else {
              throw new IllegalArgumentException("Card value must be 1 higher than the top card " +
                      "currently in the foundation pile");
            }
          } else {
            throw new IllegalArgumentException("Card must be the same suit as the foundation pile");
          }
        }
      case CASCADE:
        if (d.get(destPileNumber).isEmpty()) {
          return;
        } else {
          Card c2 = d.get(destPileNumber).peekLast();
          if (c1.differentColor(c2)) {
            if (c1.getValue() == c2.getValue() - 1) {
              return;
            } else {
              throw new IllegalArgumentException("The specified card must have a value that is 1 " +
                      "lower than the destination card");
            }
          } else {
            throw new IllegalArgumentException("The specified card has the same color as the " +
                    "destination card");
          }
        }
      default:
        throw new IllegalArgumentException("Illegal Move: destination type must be non-null " +
                "PileType");
    }
  }

  /**
   * Determines if the given piles are all empty.
   *
   * @param p - an arraylist of piles
   * @return true if there are no cards (all the lists in p are empty), false otherwise
   */
  private boolean noCards(ArrayList<LinkedList<Card>> p) {
    for (LinkedList<Card> l : p) {
      if (!l.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isGameOver() {

    for (LinkedList<Card> l : foundationPiles) {
      if (l.size() < 13) {
        return false;
      }
    }

    // if all cards are in the foundation pile
    return noCards(cascadePiles) && noCards(openPiles) && !noCards(foundationPiles);
  }

  /**
   * Obtains the game state for a single given pile. Same behavior as getGameState().
   *
   * @param pile       - the pile to return the games tate of.
   * @param pileLetter - the 1 character abbreviation for the pile. Cascade: C; Foundation: F; Open:
   *                   O.
   */
  private String getPileState(ArrayList<LinkedList<Card>> pile, String pileLetter) {
    String toReturn = "";
    for (int i = 0; i < pile.size(); i++) {
      LinkedList<Card> pileI = pile.get(i);
      String pileIString = pileLetter + (i + 1) + ":";
      for (int j = 0; j < pileI.size(); j++) {
        Card cardJ = pileI.get(j);
        if (j == pileI.size() - 1) {
          pileIString = pileIString + " " + cardJ.toString();
        } else {
          pileIString = pileIString + " " + cardJ.toString() + ",";
        }
      }
      if (i == pile.size() - 1) {
        toReturn = toReturn + pileIString;
      } else {
        toReturn = toReturn + pileIString + '\n';
      }
    }
    return toReturn;
  }

  @Override
  public String getGameState() {

    if (noCards(foundationPiles) && noCards(cascadePiles)) {
      return "";
    }

    //foundation piles
    String f = getPileState(foundationPiles, "F");
    String o = getPileState(openPiles, "O");
    String c = getPileState(cascadePiles, "C");

    return (f + '\n' + o + '\n' + c);
  }

}
