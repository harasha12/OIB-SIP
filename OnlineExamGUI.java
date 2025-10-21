import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class OnlineExamGUI extends JFrame implements ActionListener {
    CardLayout card;
    JPanel mainPanel, loginPanel, menuPanel, examPanel;
    JTextField usernameField;
    JPasswordField passwordField;
    JLabel timerLabel, questionLabel, messageLabel;
    JButton loginBtn, updateBtn, startExamBtn, logoutBtn, submitBtn;
    JRadioButton[] options = new JRadioButton[4];
    ButtonGroup bg;
    int current = 0, score = 0, timeLeft = 60;
    Timer timer;

    // Sample user
    String username = "student", password = "1234";

    // Questions
    String[] questions = {
            "1. What is the capital of India?",
            "2. 2 + 2 = ?",
            "3. Java is a ___ language?",
            "4. Which keyword is used to inherit a class in Java?"
    };
    String[][] choices = {
            {"Delhi", "Mumbai", "Chennai", "Kolkata"},
            {"3", "4", "5", "6"},
            {"Markup", "Programming", "Query", "Scripting"},
            {"extends", "implement", "inherits", "import"}
    };
    int[] answers = {0, 1, 1, 0};

    public OnlineExamGUI() {
        setTitle("Online Examination System");
        setSize(520, 380);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center on screen

        card = new CardLayout();
        mainPanel = new JPanel(card);
        add(mainPanel);

        Font fontTitle = new Font("Segoe UI", Font.BOLD, 20);
        Font fontLabel = new Font("Segoe UI", Font.PLAIN, 14);
        Font fontButton = new Font("Segoe UI", Font.BOLD, 13);

        // --- LOGIN PANEL ---
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 242, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setPreferredSize(new Dimension(320, 240));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel loginTitle = new JLabel("Login to Exam Portal");
        loginTitle.setFont(fontTitle);
        loginTitle.setForeground(new Color(40, 53, 147));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginCard.add(loginTitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(fontLabel);
        loginCard.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(12);
        usernameField.setFont(fontLabel);
        loginCard.add(usernameField, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(fontLabel);
        loginCard.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(12);
        passwordField.setFont(fontLabel);
        loginCard.add(passwordField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginBtn = new JButton("Login");
        loginBtn.setFont(fontButton);
        loginBtn.setBackground(new Color(40, 53, 147));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(this);
        loginCard.add(loginBtn, gbc);

        gbc.gridy = 4;
        messageLabel = new JLabel("", JLabel.CENTER);
        messageLabel.setForeground(Color.RED);
        loginCard.add(messageLabel, gbc);

        loginPanel.add(loginCard);

        // --- MENU PANEL ---
        menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));
        menuPanel.setBackground(new Color(250, 250, 250));

        updateBtn = new JButton("Update Password");
        startExamBtn = new JButton("Start Exam");
        logoutBtn = new JButton("Logout");

        JButton[] btns = {updateBtn, startExamBtn, logoutBtn};
        for (JButton b : btns) {
            b.setFont(fontButton);
            b.setBackground(new Color(63, 81, 181));
            b.setForeground(Color.WHITE);
            b.setFocusPainted(false);
            b.addActionListener(this);
            b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            menuPanel.add(b);
        }

        // --- EXAM PANEL ---
        examPanel = new JPanel();
        examPanel.setLayout(null);
        examPanel.setBackground(new Color(247, 249, 252));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        questionLabel.setBounds(30, 30, 440, 30);
        examPanel.add(questionLabel);

        bg = new ButtonGroup();
        int y = 70;
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(fontLabel);
            options[i].setBackground(new Color(247, 249, 252));
            options[i].setBounds(50, y, 300, 25);
            bg.add(options[i]);
            examPanel.add(options[i]);
            y += 35;
        }

        submitBtn = new JButton("Next");
        submitBtn.setBounds(190, 210, 100, 30);
        submitBtn.setFont(fontButton);
        submitBtn.setBackground(new Color(33, 150, 243));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.addActionListener(this);
        examPanel.add(submitBtn);

        timerLabel = new JLabel("Time Left: 60s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        timerLabel.setForeground(new Color(198, 40, 40));
        timerLabel.setBounds(380, 10, 150, 20);
        examPanel.add(timerLabel);

        mainPanel.add(loginPanel, "Login");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(examPanel, "Exam");

        card.show(mainPanel, "Login");
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            String u = usernameField.getText();
            String p = new String(passwordField.getPassword());
            if (u.equals(username) && p.equals(password)) {
                messageLabel.setText("");
                card.show(mainPanel, "Menu");
            } else {
                messageLabel.setText("Invalid username or password!");
            }
        } else if (e.getSource() == updateBtn) {
            String newPass = JOptionPane.showInputDialog(this, "Enter new password:");
            if (newPass != null && !newPass.isEmpty()) {
                password = newPass;
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
            }
        } else if (e.getSource() == startExamBtn) {
            startExam();
        } else if (e.getSource() == logoutBtn) {
            card.show(mainPanel, "Login");
        } else if (e.getSource() == submitBtn) {
            checkAnswer();
            current++;
            if (current < questions.length) {
                loadQuestion();
            } else {
                endExam();
            }
        }
    }

    void startExam() {
        score = 0;
        current = 0;
        timeLeft = 60;
        loadQuestion();
        card.show(mainPanel, "Exam");
        startTimer();
    }

    void loadQuestion() {
        bg.clearSelection();
        questionLabel.setText(questions[current]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(choices[current][i]);
        }
    }

    void checkAnswer() {
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected() && i == answers[current]) {
                score++;
            }
        }
    }

    void startTimer() {
        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    timerLabel.setText("Time Left: " + timeLeft + "s");
                    if (timeLeft <= 0) {
                        timer.cancel();
                        JOptionPane.showMessageDialog(null, "⏰ Time's up! Auto-submitting exam.");
                        endExam();
                    }
                    timeLeft--;
                });
            }
        }, 0, 1000);
    }

    void endExam() {
        if (timer != null) timer.cancel();
        JOptionPane.showMessageDialog(this, "Exam Over!\nYour Score: " + score + "/" + questions.length);
        card.show(mainPanel, "Menu");
    }

    public static void main(String[] args) {
        new OnlineExamGUI();
    }
}
