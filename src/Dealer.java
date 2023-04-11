import java.util.ArrayList;
import java.util.List;



public class Dealer extends Player {
    public Dealer() {
        super("Dealer");
    }
    
    public void playTurn(Deck deck) {
        while (getScore() < 17) {
            addCard(deck.deal());
        }
    }
}
