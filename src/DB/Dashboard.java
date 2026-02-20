package DB;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard(String username) {

        setTitle("Digital Cafe Desk");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient Background
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        // ===== TOP SECTION =====
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0));

        JLabel title = new JLabel("Digital Cafe Desk");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(255, 140, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Smart Billing & Order Management");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcome = new JLabel("Welcome, " + username);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcome.setForeground(Color.WHITE);
        welcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(title);
        topPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        topPanel.add(subtitle);
        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        topPanel.add(welcome);

        add(topPanel, BorderLayout.NORTH);

        // ===== CENTER SECTION =====
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridLayout(3, 1, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 150, 50, 150));

        centerPanel.add(createCardButton("Manage Menu"));
        centerPanel.add(createCardButton("Take Order"));
        centerPanel.add(createCardButton("Logout"));

        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // ===== CARD BUTTON DESIGN =====
    private JPanel createCardButton(String text) {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(40, 45, 60));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        JLabel arrow = new JLabel(">");
        arrow.setFont(new Font("Segoe UI", Font.BOLD, 18));
        arrow.setForeground(Color.GRAY);

        panel.add(label, BorderLayout.WEST);
        panel.add(arrow, BorderLayout.EAST);

        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(255, 140, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                panel.setBackground(new Color(40, 45, 60));
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(text.equals("Manage Menu")) {
                    new ManageMenu();
                }
                if(text.equals("Take Order")) {
                    dispose();
                    new TakeOrder();
                }
                if(text.equals("Logout")) {
                    dispose();
                    new LoginFrame();
                }
            }
        });

        return panel;
    }

    // ===== GRADIENT BACKGROUND =====
    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();

            Color color1 = new Color(20, 25, 40);
            Color color2 = new Color(10, 10, 20);

            GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}
