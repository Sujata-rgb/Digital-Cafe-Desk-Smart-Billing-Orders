package DB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {

    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginFrame() {

        setTitle("Cafe Management - Login");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient Background
        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 24, 40),
                        0, height, new Color(10, 12, 25));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        };

        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        // Glass Effect Panel
        JPanel loginPanel = new JPanel();
        loginPanel.setPreferredSize(new Dimension(380, 380));
        loginPanel.setBackground(new Color(255, 255, 255, 30));
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,40),1));
        backgroundPanel.add(loginPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Coffee Icon
        JLabel icon = new JLabel("☕", SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(icon, gbc);

        // Title
        JLabel title = new JLabel("Cafe Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(255, 170, 90));
        gbc.gridy++;
        loginPanel.add(title, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel userLabel = new JLabel("Username");
        userLabel.setForeground(Color.WHITE);
        loginPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        styleTextField(usernameField);
        loginPanel.add(usernameField, gbc);

        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel passLabel = new JLabel("Password");
        passLabel.setForeground(Color.WHITE);
        loginPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        styleTextField(passwordField);
        loginPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        loginButton = new JButton("Login") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255,140,60),
                        0, getHeight(), new Color(180,90,40));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g);
            }
        };

        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        loginPanel.add(loginButton, gbc);

        loginButton.addActionListener(e -> loginUser());

        setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(new Color(35,40,0));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    public void loginUser() {

        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String url = "jdbc:mysql://localhost:3306/cafe_db";
        String dbUser = "root";
        String dbPass = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);

            String query = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                dispose();
                new Dashboard(username);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password");
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
