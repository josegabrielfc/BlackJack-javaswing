import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class BlackJack {

    ArrayList<Card> deck;
    Random random = new Random();

    // Dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // Player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // Window
    int boardWidth = 975;
    int boardHeight = 750;
    int cardWidth = 145;
    int cardHeight = 200;

    JFrame frame = new JFrame("Black jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            try {

                // draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 30 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }
                // draw player's hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5) * i, 450, cardWidth, cardHeight, null);
                }

                //  Hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();

                    String message = "";
                    if (playerSum > 21)
                        message = "You Lose :C";
                    else if (dealerSum > 21 || playerSum > dealerSum)
                        message = "You Win :D";
                    else if (playerSum == dealerSum && playerHand.size() < dealerHand.size())
                        message = "You Win :D";
                    else if (playerSum == dealerSum && playerHand.size() == dealerHand.size())
                        message = "    Tie   ";
                    else{
                        message = "You lose :C";
                    }
                    
                    g.setFont(new Font("Arial", Font.PLAIN, 40));
                    g.setColor(Color.white);
                    g.drawString(message, 390, 345);

                    g.setFont(new Font("Arial", Font.PLAIN, 20));
                    g.setColor(Color.white);
                    g.drawString("Dealer: "+dealerSum, 825, 35);
                    g.drawString("Player: "+playerSum, 825, 650);
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
    JButton resetButton = new JButton("Reset");

    BlackJack() {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        resetButton.setFocusable(false);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();// frame.setVisible(false);
                new BlackJack();
            }
        });
        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() >= 21) { // A + 3 + K = 1 + 3 + 10
                    hitButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });
    }

    public void startGame() {
        // Deck
        buildDeck();
        shuffleDeck();

        // Dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); // Remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

        System.out.println("Dealer:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        // Player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

        System.out.println("Player:");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };
        String[] types = { "C", "D", "H", "S" }; // C = Clovers, D = Diamonds, H = Hearts, S = Spades
        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }
        System.out.println("Build Deck:\n" + deck);
    }

    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }
        System.out.println("After Shuffle:\n" + deck);
    }

    private int reduceAces(int sum, int aceCount) {
        while (sum > 21 && aceCount > 0) {
            sum -= 10;
            aceCount--;
        }
        return sum;
    }

    public int reducePlayerAce() {
        playerSum = reduceAces(playerSum, playerAceCount);
        return playerSum;
    }

    public int reduceDealerAce() {
        dealerSum = reduceAces(dealerSum, dealerAceCount);
        return dealerSum;
    }
}
