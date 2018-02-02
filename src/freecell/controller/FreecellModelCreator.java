package freecell.controller;

import freecell.model.FreecellModel;
import freecell.model.FreecellMultiMoveModel;
import freecell.model.FreecellOperations;


/**
 * Could be used to generate freecell models on the fly.
 */
public class FreecellModelCreator {
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }


  /**
   * Factory method for generating FreecellOperations objects of the specified type.
   * @param type - the type of FreecellOperations model to generate.
   * @return A freecellmodel (single move) or FreecellMultiMoveModel (multimove)
   */
  public static FreecellOperations create(GameType type) {
    switch (type) {
      case SINGLEMOVE:
        return new FreecellModel();
      case MULTIMOVE:
        return new FreecellMultiMoveModel();
      default:
        throw new IllegalArgumentException("GameType cannot be null");
    }
  }
}
