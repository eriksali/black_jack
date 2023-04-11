import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {

    private Deck deck;
    private Player player;
    private Player dealer;
    private Label playerScoreLabel;
    private Label dealerScoreLabel;
    private List<Label> playerCardsLabels = new ArrayList<>();
    private List<Label> dealerCardsLabels = new ArrayList<>();
    private Button hitButton;
    private Button standButton;
    private Button playAgainButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Blackjack");

        // Create the UI elements
        Label playerNameLabel = new Label("Player Name:");
        TextField playerNameField = new TextField();
        Button startButton = new Button("Start");
        playerScoreLabel = new Label("Player Score: ");
        dealerScoreLabel = new Label("Dealer Score: ");
        hitButton = new Button("Hit");
        standButton = new Button("Stand");
        playAgainButton = new Button("Play Again");

        // Set up the grid pane layout for the initial screen
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
        gridPane.add(playerNameLabel, 0, 0);
        gridPane.add(playerNameField, 1, 0);
        gridPane.add(startButton, 2, 0);

        // Set up the VBox layout for the game screen
        VBox gameLayout = new VBox();
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setSpacing(20);
        gameLayout.setPadding(new Insets(25, 25, 25, 25));
        HBox playerCardsLayout = new HBox();
        playerCardsLayout.setAlignment(Pos.CENTER);
        playerCardsLayout.setSpacing(10);
        HBox dealerCardsLayout = new HBox();
        dealerCardsLayout.setAlignment(Pos.CENTER);
        dealerCardsLayout.setSpacing(10);
        gameLayout.getChildren().addAll(playerScoreLabel, playerCardsLayout, dealerScoreLabel, dealerCardsLayout,
                hitButton, standButton, playAgainButton);

        // Set up the scene and show the initial screen
        Scene scene = new Scene(gridPane, 400, 150);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set up the event handlers for the buttons
        startButton.setOnAction(event -> {
            String playerName = playerNameField.getText();
            startGame(playerName);
            primaryStage.setScene(new Scene(gameLayout, 600, 400));
        });

        hitButton.setOnAction(event -> {
            player.addCard(deck.deal());
            updateGameScreen();
            if (player.isBust()) {
                endGame();
            }
        });

        standButton.setOnAction(event -> {
            while (dealer.getScore() < 17) {
                dealer.addCard(deck.deal());
                updateGameScreen();
            }
            endGame();
        });

        playAgainButton.setOnAction(event -> {
            startGame(player.getName());
            updateGameScreen();
        });
    }

    private void startGame(String playerName) {
        deck = new Deck();
        deck.shuffle();
        player = new Player(playerName);
        dealer = new Player("Dealer");

        // Deal the initial cards
        player.addCard(deck.deal());
        dealer.addCard(deck.deal());
        player.addCard(deck.deal());
        dealer.addCard(deck.deal());
    
        // Update the game screen
        updateGameScreen();
    }
    
    private void updateGameScreen() {
        // Update the player cards labels
        playerCardsLabels.clear();
        for (Card card : player.getCards()) {
            Label label = new Label(card.toString());
            playerCardsLabels.add(label);
        }
    
        // Update the dealer cards labels
        dealerCardsLabels.clear();
        for (Card card : dealer.getCards()) {
            Label label = new Label(card.toString());
            dealerCardsLabels.add(label);
        }
    
        // Update the player score label
        playerScoreLabel.setText("Player Score: " + player.getScore());
    
        // Update the dealer score label
        if (dealer.getCards().size() == 1) {
            dealerScoreLabel.setText("Dealer Score: ?");
        } else {
            dealerScoreLabel.setText("Dealer Score: " + dealer.getScore());
        }
    
        // Disable the hit and stand buttons if the game has ended
        if (player.isBust() || dealer.isBust()) {
            hitButton.setDisable(true);
            standButton.setDisable(true);
        }
    
        // Update the game layout
        HBox playerCardsLayout = (HBox) ((VBox) playerScoreLabel.getParent()).getChildren().get(1);
        playerCardsLayout.getChildren().clear();
        playerCardsLayout.getChildren().addAll(playerCardsLabels);
    
        HBox dealerCardsLayout = (HBox) ((VBox) playerScoreLabel.getParent()).getChildren().get(3);
        dealerCardsLayout.getChildren().clear();
        dealerCardsLayout.getChildren().addAll(dealerCardsLabels);
    }
    
    private void endGame() {
        // Reveal the dealer's face-down card
        dealerScoreLabel.setText("Dealer Score: " + dealer.getScore());
    
        // Determine the winner
        if (player.isBust()) {
            showAlert("You are bust! The dealer wins.");
        } else if (dealer.isBust()) {
            showAlert("The dealer is bust! You win.");
        } else if (player.getScore() > dealer.getScore()) {
            showAlert("You win!");
        } else if (dealer.getScore() > player.getScore()) {
            showAlert("The dealer wins.");
        } else {
            showAlert("It's a tie!");
        }
    
        // Enable the play again button
        playAgainButton.setDisable(false);
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Blackjack");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
    
       
