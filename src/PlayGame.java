import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import freecell.model.*;
import freecell.controller.*;
public class PlayGame {

  public static void main(String[] args) {

    Scanner myScanner = new Scanner(System.in);
    BufferedReader readIn = new BufferedReader(new InputStreamReader(System.in));
    IFreecellController c = new FreecellController(readIn, System.out);
    FreecellOperations<Card> m = new FreecellMultiMoveModel();

    System.out.println("Welcome to Freecell!\n" +
            "All inputs should be in the form:\n" +
            "[Source Pile] [Card Index] [Destination Pile] \ntype \'h\' for help.\n" );
    System.out.println("Good luck!");
    c.playGame(m.getDeck(), m, 8, 4, true);
  }
}
