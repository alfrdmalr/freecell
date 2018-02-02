

import org.junit.Test;

import java.util.List;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;
import freecell.model.FreecellMultiMoveModel;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * To test Multimove freecell model functionality.
 */
public class FreecellMultiMoveModelTest {
  FreecellOperations<Card> m = new FreecellMultiMoveModel();
  FreecellOperations<Card> m2 = new FreecellModel();
  List<Card> d = m.getDeck();
  List<Card> d2 = m2.getDeck();

  @Test(expected = IllegalArgumentException.class)
  public void startGameNullDeck() {
    m.startGame(null, 4, 4, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameNegativeCascadePiles() {
    m.startGame(d, -1, 4, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void startGameNegativeOpenPiles() {
    m.startGame(d, 4, -1, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNullSourceType() {
    m.startGame(d, 4, 4, false);
    m.move(null, 0, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNullDestType() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 0, 0, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNegativeSourceIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, -1, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNegativeCardIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 1, -1, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveNegativeDestIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 1, 0, PileType.OPEN, -10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveTooBigSourceIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 11, 0, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveTooBigCardIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 1, 110, PileType.OPEN, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveTooBigDestIndex() {
    m.startGame(d, 4, 4, false);
    m.move(PileType.CASCADE, 1, 0, PileType.OPEN, 100);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveInvalidBuild() {
    m.startGame(d, 4, 4, true);
    m.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 2);
  }

  @Test
  public void testGameOver() {
    FreecellModel m = new FreecellMultiMoveModel();

    //game is not over since it has never begun
    assertFalse(m.isGameOver());
    m.startGame(d, 4, 4, true);
    m.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);
    m.move(PileType.CASCADE, 1, 12, PileType.OPEN, 1);
    m.move(PileType.CASCADE, 2, 12, PileType.OPEN, 2);
    assertFalse(m.isGameOver());

    m.startGame(m.getDeck(), 52, 4, false);
    assertFalse(m.isGameOver());

    for (int i = 0; i < 52; i++) {
      m.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, i % 4);
    }

    assertTrue(m.isGameOver());
  }

  @Test
  public void testStartGame() {
    m.startGame(d, 4, 4, false);
    m2.startGame(d, 4, 4, false);
    assertEquals(m.getGameState(), m2.getGameState());

    //test that startGame() resets game
    m.move(PileType.CASCADE, 0, 12, PileType.OPEN, 2);
    assertNotEquals(m.getGameState(), m2.getGameState());

    m.startGame(d, 4, 4, false);
    assertEquals(m.getGameState(), m2.getGameState());
  }

  @Test
  public void regressionMove() {
    m.startGame(d, 4, 4, false);
    m2.startGame(d, 4, 4, false);
    assertEquals(m.getGameState(), m2.getGameState());

    m.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    m2.move(PileType.CASCADE, 1, 12, PileType.OPEN, 0);
    assertEquals(m.getGameState(), m2.getGameState());

    m.move(PileType.CASCADE, 2, 12, PileType.OPEN, 1);
    m2.move(PileType.CASCADE, 2, 12, PileType.OPEN, 1);
    assertEquals(m.getGameState(), m2.getGameState());


    m.move(PileType.CASCADE, 3, 12, PileType.OPEN, 2);
    m2.move(PileType.CASCADE, 3, 12, PileType.OPEN, 2);
    assertEquals(m.getGameState(), m2.getGameState());


    m.move(PileType.CASCADE, 0, 12, PileType.OPEN, 3);
    m2.move(PileType.CASCADE, 0, 12, PileType.OPEN, 3);
    assertEquals(m.getGameState(), m2.getGameState());
  }

  @Test
  public void moveTest() {
    m.startGame(d, 52, 4, false);
    m.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 7);
    // System.out.print(m.getGameState());
    m.move(PileType.CASCADE, 7, 0, PileType.CASCADE, 0);
    // System.out.print(m.getGameState());
    m.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 9);
    // System.out.print(m.getGameState());
    m.move(PileType.CASCADE, 9, 2, PileType.FOUNDATION, 0);

    m.startGame(d, 4, 4, false);
    //System.out.print(m.getGameState());
    m.move(PileType.CASCADE, 0, 12, PileType.OPEN, 0);

    m.startGame(d, 52, 4, false);
    System.out.print(m.getGameState());
    assertFalse(m.isGameOver()); //to make bottlenose happy
  }
}

