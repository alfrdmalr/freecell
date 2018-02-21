# freecell
*This is a slight modification to a project completed in cs3500: Object-Oriented Design*

Adaptation of freecell solitaire in java that allows single-move games as well as multiple moves at once (this is what you're used to if you've ever played solitaire, software or otherwise).

The primary gameplay loop is as follows:
the user observes the game state, decides upon a potential move, and then provides some input. 
Valid input is of the form 
\[Source Pile] \[Card Index] \[Destination Pile]

Here's a quick example:

C1: 10♥, A♦, 2♥, 7♠, 6♣, 7♣, 8♠

C2: 10♠, Q♥, 3♥, 4♦, J♣, 6♦, Q♦

C3: 5♣, 4♠, A♥, 7♥, 4♣, A♣, 8♦

C4: 3♠, 9♣, 8♥, J♦, 3♣, 6♠, 2♦

C5: 5♦, 8♣, K♥, 6♥, 5♠, Q♠

C6: 2♠, 9♦, 10♣, 2♣, A♠, J♠

C7: J♥, K♦, 3♦, 4♥, K♣, 9♥

C8: Q♣, 10♦, 5♥, K♠, 7♦, 9♠

A valid move here would be to move the 8 of diamonds from pile C3 to the 9 of hearts at the end of pile C8. The input

*C3 7 C8*

yields the following game state, which is what we wanted:

C1: 10♥, A♦, 2♥, 7♠, 6♣, 7♣, 8♠

C2: 10♠, Q♥, 3♥, 4♦, J♣, 6♦, Q♦

C3: 5♣, 4♠, A♥, 7♥, 4♣, A♣

C4: 3♠, 9♣, 8♥, J♦, 3♣, 6♠, 2♦

C5: 5♦, 8♣, K♥, 6♥, 5♠, Q♠

C6: 2♠, 9♦, 10♣, 2♣, A♠, J♠

C7: J♥, K♦, 3♦, 4♥, K♣, 9♥

C8: Q♣, 10♦, 5♥, K♠, 7♦, 9♠, 8♦

Standard freecell rules apply - cards can only be placed on empty piles or on cards of the opposite suit. The card being placed must be of rank 1 lower than the card it is being placed on. To win, a full straight of each suit (A-K) must be placed in order on each of the 4 foundation piles - these piles are the only exception to the aforementioned rules.
