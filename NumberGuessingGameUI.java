import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class NumberGuessingGameUI extends JFrame {

    private int numberToGuess;
    private int attemptsLeft;
    private int score = 0;
    private int round = 1;

    private final int TOTAL_ROUNDS = 3;
    private final int ATTEMPTS_PER_ROUND = 7;
    private final JLabel titleLabel;
    private final JLabel messageLabel;

    private final JLabel attemptsLabel;
    private final JLabel scoreLabel;
    private final JTextField guessField;
    private final JButton guessButton;
    private final JButton nextRoundButton;
    private final JButton restartButton;

    public NumberGuessingGameUI() {
        setTitle("🎯 Number Guessing Game");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== Header =====
        titleLabel = new JLabel("Number Guessing Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(30, 144, 255));
        add(titleLabel, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        centerPanel.setBackground(Color.WHITE);
        messageLabel = new JLabel("Guess a number between 1 and 100!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(messageLabel);

        guessField = new JTextField();
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("Segoe UI", Font.BOLD, 18));
        centerPanel.add(guessField);

        guessButton = new JButton("Submit Guess");
        styleButton(guessButton, new Color(30, 144, 255));
        centerPanel.add(guessButton);

        nextRoundButton = new JButton("Next Round");
        styleButton(nextRoundButton, new Color(0, 200, 100));
        nextRoundButton.setVisible(false);
        centerPanel.add(nextRoundButton);

        restartButton = new JButton("Restart Game");
        styleButton(restartButton, new Color(255, 100, 100));
        restartButton.setVisible(false);
        centerPanel.add(restartButton);

        add(centerPanel, BorderLayout.CENTER);

        // ===== Footer (status info) =====
        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        statusPanel.setBackground(new Color(245, 245, 245));
        attemptsLabel = new JLabel("Attempts Left: 7", SwingConstants.LEFT);
        scoreLabel = new JLabel("Score: 0", SwingConstants.RIGHT);
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusPanel.add(attemptsLabel);
        statusPanel.add(scoreLabel);
        add(statusPanel, BorderLayout.SOUTH);

        // ===== Actions =====
        guessButton.addActionListener(e -> checkGuess());
        nextRoundButton.addActionListener(e -> startNextRound());
        restartButton.addActionListener(e -> restartGame());

        startNewRound();
    }

    private void startNewRound() {
        numberToGuess = new Random().nextInt(100) + 1;
        attemptsLeft = ATTEMPTS_PER_ROUND;
        messageLabel.setText("Round " + round + ": Guess a number between 1 and 100!");
        attemptsLabel.setText("Attempts Left: " + attemptsLeft);
        guessField.setText("");
        guessField.setEditable(true);
        guessButton.setVisible(true);
        nextRoundButton.setVisible(false);
        restartButton.setVisible(false);
    }

    private void checkGuess() {
        String input = guessField.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a number!", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int guess;
        try {
            guess = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid integer!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        attemptsLeft--;
        attemptsLabel.setText("Attempts Left: " + attemptsLeft);

        if (guess == numberToGuess) {
            int points = (ATTEMPTS_PER_ROUND - attemptsLeft) * 10;
            score += (80 - points); // fewer attempts → more points
            scoreLabel.setText("Score: " + score);
            messageLabel.setText("🎉 Correct! The number was " + numberToGuess + ".");
            guessField.setEditable(false);
            guessButton.setVisible(false);

            if (round < TOTAL_ROUNDS) {
                nextRoundButton.setVisible(true);
            } else {
                endGame();
            }

        } else if (guess < numberToGuess) {
            messageLabel.setText("Too low! Try a higher number.");
        } else {
            messageLabel.setText("Too high! Try a lower number.");
        }

        if (attemptsLeft == 0 && guess != numberToGuess) {
            messageLabel.setText("❌ Out of attempts! Number was " + numberToGuess + ".");
            guessField.setEditable(false);
            guessButton.setVisible(false);

            if (round < TOTAL_ROUNDS) {
                nextRoundButton.setVisible(true);
            } else {
                endGame();
            }
        }
    }

    private void startNextRound() {
        round++;
        startNewRound();
    }

    private void restartGame() {
        score = 0;
        round = 1;
        scoreLabel.setText("Score: 0");
        startNewRound();
    }

    private void endGame() {
        messageLabel.setText("🏁 Game Over! Final Score: " + score);
        nextRoundButton.setVisible(false);
        restartButton.setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new NumberGuessingGameUI().setVisible(true);
        });
    }
}
