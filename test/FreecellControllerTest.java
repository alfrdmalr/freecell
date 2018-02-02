

import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import freecell.model.Card;
import freecell.model.FreecellModel;
import freecell.model.FreecellOperations;
import freecell.controller.FreecellController;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FreecellControllerTest {

  FreecellOperations<Card> model = new FreecellModel();
  List<Card> deck = model.getDeck();
  FreecellController brokenController = new FreecellController(null, new StringBuilder());
  FreecellController otherBrokenController = new FreecellController(new StringReader("hello"),
          null);
  FreecellController reallyBrokenController = new FreecellController(null, null);


  // tests the conditions under which playGame throws an error
  @Test(expected = IllegalStateException.class)
  public void breakPlayGame() throws IllegalStateException {

    brokenController.playGame(deck, model, 4, 4, false);
    otherBrokenController.playGame(deck, model, 4, 4, false);
    reallyBrokenController.playGame(deck, model, 4, 4, false);
  }


  // tests the conditions under which playGame outputs some error message
  @Test
  public void playGameWrong() throws Exception {
    StringBuilder out = new StringBuilder();

    // game should quit
    FreecellController c0 = new FreecellController(new StringReader("C1\nq\n"), out);
    c0.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Game quit prematurely."));

    // game should quit again
    FreecellController c1 = new FreecellController(new StringReader("q\n"), out);
    c1.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Game quit prematurely."));
    out.delete(0, out.length());

    // game should also quit
    FreecellController c2 = new FreecellController(new StringReader("Q\n"), out);
    c2.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Game quit prematurely."));
    out.delete(0, out.length());

    // game should ask for the source pile
    FreecellController c3 = new FreecellController(new StringReader("jh\n"), out);
    c3.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Invalid source pile. Try again."));
    assertTrue(!out.toString().contains("Game quit prematurely."));

    // game should ask for card index
    FreecellController c4 = new FreecellController(new StringReader("C1\nhh"), out);
    c4.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Cannot parse card index. Try again."));

    // game should ask for dest pile
    FreecellController c6 = new FreecellController(new StringReader("C1\n12\nBB"), out);
    c6.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Invalid dest pile. Try again."));

    // invalid moves (from model)
    FreecellController c7 = new FreecellController(new StringReader("C1\n15\nO2"), out);
    c7.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Invalid move. Try again"));

    out.delete(0, out.length());
    FreecellController c8 = new FreecellController(new StringReader("F1\n1\nO2"), out);
    c8.playGame(deck, model, 4, 4, true);
    assertTrue(out.toString().contains("Invalid move. Try again"));

  }

  // test the conditions under which playGame functions normally (win)
  @Test
  public void playGame() throws Exception {
    String s = "";
    for (int i = 1; i < 53; i++) {
      s = s + "C" + i + '\n' + 1 + '\n' + 'F' + ((i % 4) + 1) + '\n';
    }

    StringBuilder out = new StringBuilder();


    FreecellController c = new FreecellController(new StringReader(s), out);
    c.playGame(deck, new FreecellModel(), 52, 6, false);

    // game over is the last thing output by the controller
    assertEquals(out.indexOf("Game over."), out.length() - 10);

    // last "getGameState()" is the won game
    assertEquals("F1: A♥, 2♥, 3♥, 4♥, 5♥, 6♥, 7♥, 8♥, 9♥, 10♥, J♥, Q♥, K♥\n" +
                    "F2: A♣, 2♣, 3♣, 4♣, 5♣, 6♣, 7♣, 8♣, 9♣, 10♣, J♣, Q♣, K♣\n" +
                    "F3: A♠, 2♠, 3♠, 4♠, 5♠, 6♠, 7♠, 8♠, 9♠, 10♠, J♠, Q♠, K♠\n" +
                    "F4: A♦, 2♦, 3♦, 4♦, 5♦, 6♦, 7♦, 8♦, 9♦, 10♦, J♦, Q♦, K♦\n" +
                    "O1:\nO2:\nO3:\nO4:\nO5:\nO6:\nC1:\nC2:\nC3:\nC4:\nC5:\nC6:\nC7:\nC8:\nC9" +
                    ":\nC10:\nC11" +
                    ":\nC12:\nC13:\nC14:\nC15:\nC16:\nC17:\nC18:\nC19:\nC20:\nC21:\nC22:\nC23" +
                    ":\nC24:\nC25" +
                    ":\nC26:\nC27:\nC28:\nC29:\nC30:\nC31:\nC32:\nC33:\nC34:\nC35:\nC36:\nC37" +
                    ":\nC38:\nC39" +
                    ":\nC40:\nC41:\nC42:\nC43:\nC44:\nC45:\nC46:\nC47:\nC48:\nC49:\nC50:\nC51" +
                    ":\nC52" +
                    ":\nGame over.",
            out.delete(0, out.length() - 509).toString());
  }
}