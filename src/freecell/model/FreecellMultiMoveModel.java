package freecell.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class FreecellMultiMoveModel extends FreecellModel {


  @Override
  public void move(PileType source,
                   int pileNumber,
                   int cardIndex,
                   PileType destination,
                   int destPileNumber)
          throws IllegalArgumentException {

    // could be problematic if we add a new piletype and forget to add to the hashmap.
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

    validateBuildMove(src, dest, cardIndex, pileNumber, destPileNumber, source, destination);
    moveBuild(src, dest, cardIndex, pileNumber, destPileNumber);
  }

  private void validateBuildMove(ArrayList<LinkedList<Card>> s,
                                 ArrayList<LinkedList<Card>> d,
                                 int cardIndex, int pileNumber, int destPileNumber, PileType
                                         sourceType, PileType
                                         destType)
          throws IllegalArgumentException {

    // if the first card in a build is valid, and the build itself is valid, then the move is
    // valid so long as it's cascade -> cascade

    //check the first move
    validateMove(s, d, cardIndex, pileNumber, destPileNumber, destType);
    validateBuild(s, cardIndex, pileNumber);
    int buildLength = s.get(pileNumber).size() - cardIndex;

    //check to make sure we're not trying to move multiple cards from F pile
    if (buildLength > 1 && sourceType == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Cannot move multiple cards from foundation pile");
    }
    //additional check to make sure we're not moving more than one card to F pile
    if (buildLength > 1 && destType == PileType.FOUNDATION) {
      throw new IllegalArgumentException("Cannot move multiple cards to foundation pile");
    }
    // check to make sure we're not putting multiple cards in an open pile
    if (buildLength > 1 && destType == PileType.OPEN) {
      throw new IllegalArgumentException("Cannot move multiple cards to open pile");
    }

    if (!enoughFree(buildLength)) {
      throw new IllegalArgumentException("There are not enough free slots on the board to perform"
              + " this move");
    }
  }

  /**
   * Returns true if the rest of the pile (the cards after the specified index, inclusive) forms a
   * valid build. Valid builds have alternating suit colors and descending values.
   *
   * @param s - type of card pile to take from
   */
  private void validateBuild(ArrayList<LinkedList<Card>> s, int cardIndex, int pileNumber) {
    LinkedList<Card> source = s.get(pileNumber);
    if (cardIndex == source.size() - 1) {
      return;
    }
    LinkedList<Card> potentialBuild = new LinkedList<>();

    for (int i = cardIndex + 1; i < source.size(); i++) {
      potentialBuild.addLast(source.get(i));
    }

    Card prevCard = source.get(cardIndex);
    for (Card c : potentialBuild) {
      if (!c.differentColor(prevCard)) {
        throw new IllegalArgumentException("Invalid build: colors must be alternating");
      }
      if ((c.getValue() != prevCard.getValue() - 1)) {
        throw new IllegalArgumentException("Invalid build: card values must decrease");
      }

      //no issues thus far
      prevCard = c;
    }
  }

  /**
   * Moves the build outlined by the given piles and indices. "Build" includes single cards. DOES
   * NOT CHECK IF THE MOVE IS VALID; intended to be called after validateBuildMove
   *
   * @param src            - the cluster of piles to move from.
   * @param dest           - the cluster of piles to move to.
   * @param cardIndex      - the index of the earliest card in the build.
   * @param pileNumber     - the index of the pile from which the build is taken.
   * @param destPileNumber - the index of the pile to which we're moving the build.
   */
  private void moveBuild(ArrayList<LinkedList<Card>> src, ArrayList<LinkedList<Card>> dest, int
          cardIndex, int pileNumber, int destPileNumber) {
    LinkedList<Card> source = src.get(pileNumber);
    LinkedList<Card> destination = dest.get(destPileNumber);
    LinkedList<Card> build = new LinkedList<>();
    int buildLength = source.size() - cardIndex;

    // obtain the build
    for (int i = 0; i < buildLength; i++) {
      build.addLast(source.remove(cardIndex));
    }


    // deposit the build in the destination pile
    for (Card c : build) {
      destination.addLast(c);
    }
  }

  private boolean enoughFree(int i) {
    int k = 0;
    int n = 0;
    for (LinkedList<Card> l : this.cascadePiles) {
      if (l.isEmpty()) {
        k++;
      }
    }
    for (LinkedList<Card> l : this.openPiles) {
      if (l.isEmpty()) {
        n++;
      }
    }

    long possibleMoves = (n + 1) * (int) Math.pow(2, k);

    if (possibleMoves < 0) {
      return true; // OVERFLOW LOL
    }

    return i <= possibleMoves;
  }
}
