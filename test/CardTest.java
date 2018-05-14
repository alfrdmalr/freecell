

import org.junit.Test;


import freecell.model.Card;
import freecell.model.Suit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;


/**
 * To test the card class.
 */
public class CardTest {

  Card aceSpades = new Card(1, Suit.SPADES);
  Card lemmy = new Card(1, Suit.SPADES);
  Card aceClubs = new Card(1, Suit.CLUBS);
  Card fiveHearts = new Card(5, Suit.HEARTS);
  Card tenDiamonds = new Card(10, Suit.DIAMONDS);
  Card jackDiamonds = new Card(11, Suit.DIAMONDS);
  Card fourSpades = new Card(4, Suit.SPADES);
  Card fiveClubs = new Card(5, Suit.CLUBS);

  @Test
  public void testIsValid() throws Exception {

    assertTrue(aceSpades.isValid());
    assertTrue(lemmy.isValid());
    assertTrue(tenDiamonds.isValid());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardConstructor() throws Exception {
    Card nullCard = null;
    Card invalidValTooLow = new Card(0, Suit.CLUBS);
    Card invalidValTooHigh = new Card(14, Suit.CLUBS);
    Card nullSuit = new Card(7, null);
  }

  @Test
  public void testSameSuit() throws Exception {
    assertTrue(aceSpades.sameSuit(lemmy));
    assertTrue(tenDiamonds.sameSuit(jackDiamonds));

    assertFalse(fiveHearts.sameSuit(jackDiamonds));
    assertFalse(aceSpades.sameSuit(aceClubs));
  }

  @Test
  public void testToString() throws Exception {
    assertEquals("A♠", aceSpades.toString());
    assertEquals("4♠", fourSpades.toString());
    assertEquals("5♥", fiveHearts.toString());
    assertEquals("10♦", tenDiamonds.toString());
    assertEquals(aceSpades.toString(), lemmy.toString());

    assertNotEquals(aceSpades.toString(), aceClubs.toString());
    assertNotEquals("11♦", jackDiamonds.toString());
  }


  @org.junit.Test
  public void testGetValue() throws Exception {
    // basic behavior
    assertEquals(fiveHearts.getValue(), 5);
    assertEquals(tenDiamonds.getValue(), 10);

    assertNotEquals(aceClubs.getValue(), 6);
    assertNotEquals(aceSpades.getValue(), 0);

    // suit has no impact on value
    assertEquals(aceSpades.getValue(), 1);
    assertEquals(aceClubs.getValue(), 1);

    assertNotEquals(tenDiamonds.getValue(), jackDiamonds.getValue());
  }

  @org.junit.Test
  public void testGetSuit() throws Exception {
    assertEquals(Suit.CLUBS, aceClubs.getSuit());
    assertEquals(Suit.DIAMONDS, tenDiamonds.getSuit());
    assertEquals(Suit.SPADES, lemmy.getSuit());

    //different values can have the same suit
    assertEquals(aceSpades.getSuit(), fourSpades.getSuit());

    assertNotEquals(Suit.SPADES, fiveHearts.getSuit());
    assertNotEquals(Suit.CLUBS, aceSpades.getSuit());

    //value has no impact on suit
    assertNotEquals(aceClubs.getSuit(), aceSpades.getSuit());

  }

  @org.junit.Test
  public void testGetColor() throws Exception {
    assertEquals("black", aceSpades.getColor());
    assertEquals("red", fiveHearts.getColor());

    //different suits can be the same color
    assertEquals(fiveHearts.getColor(), tenDiamonds.getColor());
    assertEquals(aceClubs.getColor(), fourSpades.getColor());

    //different suits can be different colors
    assertNotEquals(fiveHearts.getColor(), fourSpades.getColor());
    assertNotEquals(tenDiamonds.getColor(), fiveClubs.getColor());

    //same suit is same color
    assertEquals(lemmy.getColor(), fourSpades.getColor());

    //value has no impact on color
    assertNotEquals(fiveHearts.getColor(), fiveClubs.getColor());
  }

  @org.junit.Test
  public void testDifferentColor() throws Exception {
    assertTrue(aceClubs.differentColor(tenDiamonds));
    assertTrue(fourSpades.differentColor(fiveHearts));


    assertFalse(aceSpades.differentColor(lemmy));
    assertFalse(fourSpades.differentColor(fiveClubs));
    assertFalse(tenDiamonds.differentColor(fiveHearts));
  }

  @org.junit.Test
  public void testSameCard() throws Exception {
    assertTrue(aceSpades.sameCard(aceSpades));
    assertTrue(aceSpades.sameCard(lemmy));

    assertFalse(aceSpades.sameCard(aceClubs));
    assertFalse(aceClubs.sameCard(fiveHearts));
    assertFalse(tenDiamonds.sameCard(fourSpades));
    assertFalse(jackDiamonds.sameCard(tenDiamonds));
    assertFalse(fiveClubs.sameCard(fiveHearts));

  }

}