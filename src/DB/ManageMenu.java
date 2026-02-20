package DB;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class ManageMenu extends JFrame {

    JTextField nameField, priceField, categoryField;
    JButton addButton, updateButton, deleteButton;
    JTable table;
    DefaultTableModel model;

    String url = "jdbc:mysql://localhost:3306/cafe_db";
    String dbUser = "root";
    String dbPass = "1234";

    public ManageMenu() {

        setTitle("Manage Menu");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Gradient Background
        JPanel background = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(20, 24, 40),
                        0, getHeight(), new Color(10, 12, 25));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        background.setLayout(new BorderLayout(30, 30));
        background.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        add(background);

        // LEFT GLASS PANEL
        JPanel formPanel = new JPanel();
        formPanel.setPreferredSize(new Dimension(320, 400));
        formPanel.setBackground(new Color(255, 255, 255, 25));
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,40),1));

        background.add(formPanel, BorderLayout.WEST);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Manage Menu");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(255,170,90));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(title, gbc);

        gbc.gridwidth = 1;

        // Name
        gbc.gridy++;
        formPanel.add(createLabel("Item Name"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField();
        styleField(nameField);
        formPanel.add(nameField, gbc);

        // Price
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Price"), gbc);

        gbc.gridx = 1;
        priceField = new JTextField();
        styleField(priceField);
        formPanel.add(priceField, gbc);

        // Category
        gbc.gridy++;
        gbc.gridx = 0;
        formPanel.add(createLabel("Category"), gbc);

        gbc.gridx = 1;
        categoryField = new JTextField();
        styleField(categoryField);
        formPanel.add(categoryField, gbc);

        // Buttons
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        addButton = createButton("Add");
        updateButton = createButton("Update");
        deleteButton = createButton("Delete");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel, gbc);

        // RIGHT TABLE PANEL
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Name", "Price", "Category"});

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,40),1));
        background.add(scroll, BorderLayout.CENTER);

        loadData();

        addButton.addActionListener(e -> addItem());
        updateButton.addActionListener(e -> updateItem());
        deleteButton.addActionListener(e -> deleteItem());

        setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private void styleField(JTextField field) {
        field.setBackground(new Color(35, 40, 60));
        field.setForeground(Color.WHITE);
        field.setCaretColor(new Color(255,170,90));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        field.setPreferredSize(new Dimension(160, 35));
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255,140,60),
                        0, getHeight(), new Color(180,90,40));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));

        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(new Color(30, 35, 55));
        table.setForeground(Color.WHITE);
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setGridColor(new Color(60, 65, 85));
        table.getTableHeader().setBackground(new Color(255,140,60));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    public void loadData() {
        model.setRowCount(0);
        try {
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);
            String query = "SELECT * FROM items";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price"),
                        rs.getString("category")
                });
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem() {
        try {
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);
            String query = "INSERT INTO items (item_name, price, category) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setString(3, categoryField.getText());

            ps.executeUpdate();
            con.close();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);
            String query = "UPDATE items SET item_name=?, price=?, category=? WHERE item_id=?";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.parseDouble(priceField.getText()));
            ps.setString(3, categoryField.getText());
            ps.setInt(4, id);

            ps.executeUpdate();
            con.close();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
            Connection con = DriverManager.getConnection(url, dbUser, dbPass);
            String query = "DELETE FROM items WHERE item_id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            ps.executeUpdate();
            con.close();
            loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ManageMenu();
    }
}
