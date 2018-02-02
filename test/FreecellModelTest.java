

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.model.PileType;
import freecell.model.Suit;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


public class FreecellModelTest {

  Card aceSpades = new Card(1, Suit.SPADES);
  Card lemmy = new Card(1, Suit.SPADES);
  Card aceClubs = new Card(1, Suit.CLUBS);
  Card fiveHearts = new Card(5, Suit.HEARTS);
  Card tenDiamonds = new Card(10, Suit.DIAMONDS);
  Card jackDiamonds = new Card(11, Suit.DIAMONDS);
  Card fourSpades = new Card(4, Suit.SPADES);
  Card fiveClubs = new Card(5, Suit.CLUBS);

  FreecellModel model = new FreecellModel();
  LinkedList<Card> emptyPile = new LinkedList<Card>();


  @Test
  public void getDeck() throws Exception {
    List<Card> d = model.getDeck();
    assertEquals(52, d.size());
    assertEquals(d.get(0).toString(), "A♣");
    assertEquals(d.get(51).toString(), "K♥");
    Collections.shuffle(d);
    assertNotEquals(d.get(0).toString(), "A♣");
    assertNotEquals("K♥", d.get(51).toString());
  }

  @Test
  public void startGame() throws Exception {
    FreecellOperations<Card> mShuffle = new FreecellModel();
    FreecellOperations<Card> mNoShuffle = new FreecellModel();

    assertNotEquals(mShuffle.getGameState(), mNoShuffle.getGameState());

    model.startGame(model.getDeck(), 4, 4, true);
    assertFalse(model.isGameOver());
    String game1 = model.getGameState();
    model.startGame(model.getDeck(), 5, 4, false);
    String game2 = model.getGameState();
    assertNotEquals(game1, game2);

  }


  @Test(expected = IllegalArgumentException.class)
  public void illegalStartGame() throws Exception {
    model.startGame(null, 4, 4, true);
    model.startGame(model.getDeck(), -1, 4, true);
    model.startGame(model.getDeck(), 4, -1, false);

    List<Card> shortDeck = model.getDeck();
    shortDeck.remove(0);
    List<Card> crowdedDeck = model.getDeck();
    crowdedDeck.add(new Card(1, Suit.SPADES));

    //a deck must have 52 cards
    model.startGame(shortDeck, 4, 4, false);
    model.startGame(crowdedDeck, 4, 4, false);

    //we allow as few as four cascade
    model.startGame(model.getDeck(), 3, 4, true);

    //we allow as few as 1 open piles
    model.startGame(model.getDeck(), 4, 0, true);
    assertEquals("", model.getGameState());
  }

  @Test(expected = IllegalArgumentException.class)
  public void illegalMoves() throws Exception {
    model.startGame(model.getDeck(), 4, 4, false);
    //can't move from an empty pile
    model.move(PileType.FOUNDATION, 0, 2, PileType.CASCADE, 3);
    model.move(PileType.OPEN, 0, 1, PileType.CASCADE, 3);

    //can't move card from the middle of a pile
    model.move(PileType.CASCADE, 0, 1, PileType.CASCADE, 3);
    model.move(PileType.CASCADE, 0, 0, PileType.CASCADE, 2);


    //can't move card if index doesn't exist
    model.move(PileType.CASCADE, 0, 25, PileType.CASCADE, 2);

    //can't move card if its the same color
    model.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 1);

    //can't move card if its not 1 value lower
    model.move(PileType.CASCADE, 0, 12, PileType.CASCADE, 2);

    //negative pile numbers
    model.move(PileType.CASCADE, -1, 12, PileType.CASCADE, 2);
    model.move(PileType.CASCADE, 0, 12, PileType.CASCADE, -3);

    //can't move to an occupied open pile
    model.move(PileType.CASCADE, 2, 12, PileType.OPEN, 1); //valid
    model.move(PileType.CASCADE, 2, 11, PileType.OPEN, 1);
  }

  //  @Test
  //  public void validMoves() {
  //    model.startGame(model.getDeck(), 52, 4, false);
  //    model.move(PileType.CASCADE, 0, 0, PileType.FOUNDATION, 0);
  //    model.move(PileType.CASCADE, 4, 0, PileType.FOUNDATION, 0);
  //  }

  @Test
  public void isGameOver() throws Exception {
    FreecellModel m = new FreecellModel();

    //game is not over since it has never begun
    assertFalse(m.isGameOver());
    m.startGame(m.getDeck(), 52, 4, false);
    assertFalse(m.isGameOver());

    for (int i = 0; i < 52; i++) {
      m.move(PileType.CASCADE, i, 0, PileType.FOUNDATION, i % 4);
    }

    assertTrue(m.isGameOver());


  }

  @Test
  public void getGameState() throws Exception {
    FreecellModel m = new FreecellModel();
    ArrayList<LinkedList<Card>> examplePiles = new ArrayList<>();
    LinkedList<Card> diamondPile = new LinkedList<>();
    diamondPile.add(new Card(1, Suit.DIAMONDS));
    diamondPile.add(new Card(2, Suit.DIAMONDS));
    diamondPile.add(new Card(3, Suit.DIAMONDS));
    examplePiles.add(emptyPile);
    examplePiles.add(diamondPile);


    //assertEquals(m.getPileState(examplePiles, "F"), "F1:\nF2: A♦, 2♦, 3♦");
    assertEquals(m.getGameState(), "");
    m.startGame(m.getDeck(), 4, 4, false);
    assertNotEquals(m.getGameState(), "");
    assertEquals(m.getGameState().substring(0, 7), "F1:\nF2:");

    assertEquals("F1:\nF2:\nF3:\nF4:\nO1:\nO2:\nO3:\nO4:\nC1: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣," +
                    " " +
                    "10♣, J♣, Q♣, K♣\n" + "C2: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, " +
                    "K♠\nC3: " +
                    "A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\nC4: A♥, 2♥, 3♥, 4♥, 5♥," +
                    " 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥",
            m.getGameState());
    FreecellModel m2 = new FreecellModel();
  }
}
