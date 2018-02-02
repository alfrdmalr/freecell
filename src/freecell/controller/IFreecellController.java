package freecell.controller;

import java.util.List;

import freecell.model.FreecellOperations;

public interface IFreecellController<K> {

  /**
   * Starts a new game of Freecell using the provided model, number of cascade and open piles and
   * the provided deck. If "shuffle" is set to false, the deck must be used as-is, else the deck
   * should be shuffled.
   * @param deck - valid deck of 52 unique cards
   * @param model - FreecellOperations model used to run the game
   * @param numCascades - number of cascade piles
   * @param numOpens - number of open piles
   * @param shuffle - determines whether or not to shuffle the deck before starting the game
   */
  void playGame(List<K> deck, FreecellOperations<K> model, int numCascades, int numOpens,
                boolean shuffle);
}
