package controller;


import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import model.Card;
import model.FreecellOperations;
import model.PileType;

/**
 * Represents a controller for the game of freecell.
 */
public class FreecellController implements IFreecellController<Card> {

  private Readable in;
  private Appendable out;
  private Command expected;
  private String[] commands = {"", "", ""};


  /**
   * Initializes a new FreecellController with the given input/output objects.
   *
   * @param rd - Readable object to represent input
   * @param ap - Appendable object to represent output
   */
  public FreecellController(Readable rd, Appendable ap) {
    this.in = rd;
    this.out = ap;
    this.expected = Command.SOURCE;
  }

  @Override
  public void playGame(List<Card> deck, FreecellOperations<Card> model, int numCascades, int
          numOpens, boolean shuffle) {

    // also validates that rd/ap are initialized
    validate(deck, model);
    try {
      try {
        model.startGame(deck, numCascades, numOpens, shuffle);
      } catch (IllegalArgumentException i) {
        this.out.append("Could not start game.");
        return;
      }

      Scanner myScanner = new Scanner(in);
      while (!model.isGameOver()) {
        if (expected == Command.SOURCE) {
          out.append("\n" + model.getGameState() + "\n");
        }

        // no more moves
        if (!myScanner.hasNext()) {
          out.append("\nNo more moves");
          return;
        }

        String n = myScanner.next();
        switch(n.toLowerCase()) {
          case "h": out.append("Commands:\n\th - provides list of commands"
                  + "\n\tq - quits the game\n\tr - resets the game\n\ts - shows the current game state\n"
                  + "Input Example: C1 7 O3");
            break;
          case "q": out.append("Game quit prematurely.");
          return;
          case "r":
            out.append("reset not supported, sorry!");
            //out.append("Game resetting...");
            //this.reset();
            //out.append("Game reset");
            //out.append(model.getGameState());
            break;
          case "s": out.append(model.getGameState());
            break;
          default: inputHandler(n);
            try {
              tryMove(model);
            } catch (IllegalArgumentException e) {
              out.append("\nInvalid move. Try again:" + e.getMessage());
            }
            break;
        }
      }
      if (model.isGameOver()) {
        out.append("\n" + model.getGameState() + "\n");
        out.append("\nGame over.");
        return;
      }
    } catch (IOException e) {
      return;
    }
  }

  /**
   * Verifies that the given model and deck are somewhat valid (non-null). Additionally, checks that
   * the Readable and Appendable fields have been initialized.
   *
   * @param deck  - list of cards representing a deck.
   * @param model - FreecellModel implementation used to run a game
   */
  private void validate(List<Card> deck, FreecellOperations<Card> model) {
    if (deck == null) {
      throw new IllegalArgumentException("Deck must be non-null");
    }
    if (model == null) {
      throw new IllegalArgumentException("Model must be non-null");
    }
    if (in == null || out == null) {
      throw new IllegalStateException("I/O not initialized");
    }
  }

  /**
   * Given a single command from the user, deals with it appropriately.
   *
   * @param s - a single command, not necessarily a valid one.
   */
  private void inputHandler(String s) throws IOException {
    String pile;
    String index;

    // should abstract this (source and dest)
    switch (this.expected) {
      case SOURCE:
        if (s.length() < 1) {
          out.append("\nSource pile cannot be a single character");
          return;
        } else if (s.charAt(0) == 'F' || s.charAt(0) == 'C' || s.charAt(0) == 'O') {
          pile = s.substring(0, 1);
          try {
            int i = Integer.parseInt(s.substring(1));
            index = "" + i;
            this.commands[0] = pile + index;
            this.expected = Command.CARD;
          } catch (NumberFormatException e) {
            out.append("\nCannot parse source pile number. Try again.");
            return;
          }
        } else {
          out.append("\nInvalid source pile. Try again.");
          return;
        }
        break;
      case CARD:
        try {
          int i = Integer.parseInt(s);
          index = "" + i;
          this.commands[1] = index;
          this.expected = Command.DEST;
        } catch (NumberFormatException e) {
          out.append("\nCannot parse card index. Try again.");
        }
        break;
      case DEST:
        if (s.length() < 1) {
          out.append("\nDestination pile cannot be a single character");
          return;
        } else if (s.charAt(0) == 'F' || s.charAt(0) == 'C' || s.charAt(0) == 'O') {
          pile = s.substring(0, 1);
          try {
            int i = Integer.parseInt(s.substring(1));
            index = "" + i;
            this.commands[2] = pile + index;
          } catch (NumberFormatException e) {
            out.append("\nCannot parse dest pile number. Try again.");
            return;
          }
        } else {
          out.append("\nInvalid dest pile. Try again.");
          return;
        }
        break;
      default:
        out.append("\nDid not expect any of the command sequences");
    }
  }

  private void tryMove(FreecellOperations<Card> m) {
    for (String s : this.commands) {
      if (s.equals("")) {
        return; //not all commands have been input yet
      }
    }

    // determine what the inputs are for move() based on the commands
    PileType source;
    int pileNumber = Integer.parseInt(this.commands[0].substring(1)) - 1;
    int cardIndex = Integer.parseInt(this.commands[1]) - 1;
    PileType dest;
    int destPileNumber = Integer.parseInt(this.commands[2].substring(1)) - 1;

    switch (this.commands[0].charAt(0)) {
      case 'C':
        source = PileType.CASCADE;
        break;
      case 'O':
        source = PileType.OPEN;
        break;
      case 'F':
        source = PileType.FOUNDATION;
        break;
      default:
        throw new IllegalStateException("Illegal source type; input handler error.");
    }
    switch (this.commands[2].charAt(0)) {
      case 'C':
        dest = PileType.CASCADE;
        break;
      case 'O':
        dest = PileType.OPEN;
        break;
      case 'F':
        dest = PileType.FOUNDATION;
        break;
      default:
        throw new IllegalStateException("Illegal dest type, input handler error.");
    }

    // ask the model to perform the move
    m.move(source, pileNumber, cardIndex, dest, destPileNumber);

    //reset the command sequence
    this.commands[0] = "";
    this.commands[1] = "";
    this.commands[2] = "";

    //reset the expected input
    this.expected = Command.SOURCE;
  }
}
