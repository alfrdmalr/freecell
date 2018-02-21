import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import controller.FreecellController;
import model.Card;
import model.FreecellMultiMoveModel;
import model.FreecellOperations;

public class PlayGame {

  public static void main(String[] args) {

    Scanner myScanner = new Scanner(System.in);
    BufferedReader readIn = new BufferedReader(new InputStreamReader(System.in));
    FreecellController c = new FreecellController(readIn, System.out);
    FreecellOperations<Card> m = new FreecellMultiMoveModel();

    System.out.println("Welcome to Freecell!" +
            "All inputs should be in the form:\n" +
            "[Source Pile] [Card Index] [Destination Pile] \ntype \'h\' for help.\n" );
    System.out.println("Good luck!");


    c.playGame(m.getDeck(), m, 8, 4, true);
  }
}
