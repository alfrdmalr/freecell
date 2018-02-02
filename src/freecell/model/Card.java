package freecell.model;


import java.util.HashMap;

/**
 * Represents a playing card.
 */
public class Card {
  private int value;
  private Suit suit;

  /**
   * Creates a card with the specified value and suit.
   *
   * @param value - the ranking of the card, 1-13 (A-K).
   * @param suit  - card family (Clubs, Spades, Diamonds, Hearts).
   */
  public Card(int value, Suit suit) {
    if (validCardInput(value, suit)) {
      this.value = value;
      this.suit = suit;
    } else {
      throw new IllegalArgumentException("Invalid arguments for Card constructor");
    }
  }

  /**
   * Verifies that the input is valid for a playing card. Value should be no lower than 1 and no
   * higher than 13. Suit should not be null.
   *
   * @param value - prospective value of the card (i.e. the number that would be printed on a
   *              physical card)
   * @param suit  - one of the 4 suits, or null(considered invalid)
   * @return true if the input meets specification, false otherwise.
   */
  private static boolean validCardInput(int value, Suit suit) {
    return suit != null && (value < 14 && value > 0);
  }

  /**
   * Verifies that this card has valid value and suit fields.
   *
   * @return true if valid and false otherwise.
   */
  public boolean isValid() {
    return validCardInput(this.getValue(), this.getSuit());
  }

  /**
   * Obtains the numerical value of this card.
   *
   * @return The value as an int.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Obtains the suit of this card.
   *
   * @return The suit.
   */
  public Suit getSuit() {
    return this.suit;
  }

  /**
   * Obtains the color of this card; based on suit.
   *
   * @return "red" if suit is Hearts or Diamonds, "black" if clubs or spades.
   */
  public String getColor() {
    String color = "";
    switch (suit) {
      case CLUBS:
        color = "black";
        break;
      case SPADES:
        color = "black";
        break;
      case HEARTS:
        color = "red";
        break;
      case DIAMONDS:
        color = "red";
        break;
      default:
        throw new RuntimeException("Suit not properly initialized");
    }
    return color;
  }

  /**
   * Determines if the given card is a different color than this one. Suit does not necessarily
   * matter.
   *
   * @param o - some other card to be compared with this one.
   * @return true if different color, false if same color.
   */
  public boolean differentColor(Card o) {
    return !this.getColor().equals(o.getColor());
  }

  /**
   * Determines if the given card has the same suit as this one.
   *
   * @param o - another card, to be compared.
   * @return true if same suit, false otherwise.
   */
  public boolean sameSuit(Card o) {
    return this.suit == o.getSuit();
  }

  /**
   * Determines if the given card is the same card as this one. Different instances may be
   * considered the same if they have matching suit/value.
   *
   * @param o - other card to be compared with this one.
   * @return true if the cards have the same value and suit, false otherwise.
   */
  public boolean sameCard(Card o) {
    return this.value == o.getValue() && this.suit == o.getSuit();
  }

  /**
   * Returns the "ranking" of the card, or the symbol associated with its value. For instance, a
   * card of value 1 would be "A", while a card of value 10 would be "10."
   *
   * @return a string of length 1 or 2 ("10") representing the value.
   */
  public String toString() {
    HashMap<Integer, String> cardVals = new HashMap<Integer, String>();
    cardVals.put(1, "A");
    cardVals.put(11, "J");
    cardVals.put(12, "Q");
    cardVals.put(13, "K");
    for (int i = 2; i < 11; i++) {
      cardVals.put(i, Integer.toString(i));
    }

    String suit = "";
    switch (this.suit) {
      case DIAMONDS:
        suit = "♦";
        break;
      case HEARTS:
        suit = "♥";
        break;
      case SPADES:
        suit = "♠";
        break;
      case CLUBS:
        suit = "♣";
        break;
      default:
        throw new RuntimeException("Suit is not properly initialized");
    }

    return cardVals.get(value) + suit;
  }

}
