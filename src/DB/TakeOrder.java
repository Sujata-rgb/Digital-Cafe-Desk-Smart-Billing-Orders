package DB;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;

public class TakeOrder extends JFrame {

    private JTable menuTable, cartTable;
    private DefaultTableModel menuModel, cartModel;
    private JLabel totalLabel;
    private JButton placeOrderBtn;

    private final DecimalFormat df = new DecimalFormat("0.00");

    public TakeOrder() {

        setTitle("Cafe Management - Take Order");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== MAIN BACKGROUND PANEL =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(30, 30, 30));
        add(mainPanel);

        // ===== HEADER =====
        JLabel title = new JLabel("☕ Cafe Management - Take Order", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(255, 170, 0));
        title.setBorder(new EmptyBorder(20, 10, 20, 10));
        mainPanel.add(title, BorderLayout.NORTH);

        // ===== TABLE PANEL =====
        JPanel centerPanel = new JPanel(new GridLayout(1,2,20,0));
        centerPanel.setBorder(new EmptyBorder(10,20,10,20));
        centerPanel.setBackground(new Color(30,30,30));

        // ===== MENU TABLE =====
        menuModel = new DefaultTableModel(new String[]{"ID", "Item Name", "Price"}, 0);
        menuTable = new JTable(menuModel);
        styleTable(menuTable);

        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setBorder(BorderFactory.createTitledBorder("Menu"));
        centerPanel.add(menuScroll);

        // ===== CART TABLE =====
        cartModel = new DefaultTableModel(
                new String[]{"Item ID", "Item Name", "Price", "Qty", "Subtotal"}, 0);
        cartTable = new JTable(cartModel);
        styleTable(cartTable);

        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setBorder(BorderFactory.createTitledBorder("Cart"));
        centerPanel.add(cartScroll);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,20,15));
        bottomPanel.setBackground(new Color(40,40,40));

        totalLabel = new JLabel("Total: ₹ 0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalLabel.setForeground(Color.WHITE);

        placeOrderBtn = new JButton("Place Order");
        placeOrderBtn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        placeOrderBtn.setBackground(new Color(255,140,0));
        placeOrderBtn.setForeground(Color.BLACK);
        placeOrderBtn.setFocusPainted(false);
        placeOrderBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        placeOrderBtn.addActionListener(e -> placeOrder());

        bottomPanel.add(totalLabel);
        bottomPanel.add(placeOrderBtn);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        loadData();

        // ===== CLICK EVENT =====
        menuTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = menuTable.getSelectedRow();
                if(row==-1) return;

                int id = (int) menuModel.getValueAt(row,0);
                String name = (String) menuModel.getValueAt(row,1);
                double price = (double) menuModel.getValueAt(row,2);

                String qtyStr = JOptionPane.showInputDialog("Enter Quantity:");

                try{
                    int qty = Integer.parseInt(qtyStr);
                    if(qty<=0) throw new Exception();

                    double subtotal = qty*price;
                    cartModel.addRow(new Object[]{id,name,price,qty,subtotal});
                    updateTotal();

                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Invalid Quantity!");
                }
            }
        });

        setVisible(true);
    }

    // ===== TABLE STYLE =====
    private void styleTable(JTable table){
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));
        table.getTableHeader().setBackground(new Color(255,140,0));
        table.getTableHeader().setForeground(Color.BLACK);
        table.setGridColor(Color.LIGHT_GRAY);
    }

    // ===== UPDATE TOTAL =====
    private void updateTotal(){
        double total = 0;
        for(int i=0;i<cartModel.getRowCount();i++){
            total += (double)cartModel.getValueAt(i,4);
        }
        totalLabel.setText("Total: ₹ "+df.format(total));
    }

    // ===== DB CONNECTION =====
    private Connection getConnection() throws Exception{
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/cafe_db",
                "root",
                "1234"
        );
    }

    // ===== LOAD MENU =====
    private void loadData(){
        try(Connection con = getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM items")){

            while(rs.next()){
                menuModel.addRow(new Object[]{
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("price")
                });
            }

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Database Error!");
        }
    }

    // ===== PLACE ORDER =====
//    private void placeOrder(){
//        if(cartModel.getRowCount()==0){
//            JOptionPane.showMessageDialog(this,"Cart is Empty!");
//            return;
//        }
//
//        try(Connection con = getConnection();
//            PreparedStatement ps = con.prepareStatement(
//                    "INSERT INTO orders (item_id,item_name,price,qty,subtotal) VALUES (?,?,?,?,?)")){
//
//            for(int i=0;i<cartModel.getRowCount();i++){
//                ps.setInt(1,(int)cartModel.getValueAt(i,0));
//                ps.setString(2,(String)cartModel.getValueAt(i,1));
//                ps.setDouble(3,(double)cartModel.getValueAt(i,2));
//                ps.setInt(4,(int)cartModel.getValueAt(i,3));
//                ps.setDouble(5,(double)cartModel.getValueAt(i,4));
//                ps.executeUpdate();
//            }
//
//            JOptionPane.showMessageDialog(this,"Order Placed Successfully!");
//            cartModel.setRowCount(0);
//            updateTotal();
//
//        }catch(Exception e){
//            JOptionPane.showMessageDialog(this,"Order Failed!");
//        }
//    }
    
    
    private void placeOrder(){

        if(cartModel.getRowCount()==0){
            JOptionPane.showMessageDialog(this,"Cart is Empty!");
            return;
        }

        double finalTotal = 0;

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO orders (item_id,item_name,price,qty,subtotal) VALUES (?,?,?,?,?)")){

            for(int i=0;i<cartModel.getRowCount();i++){

                int id = (int)cartModel.getValueAt(i,0);
                String name = (String)cartModel.getValueAt(i,1);
                double price = (double)cartModel.getValueAt(i,2);
                int qty = (int)cartModel.getValueAt(i,3);
                double subtotal = (double)cartModel.getValueAt(i,4);

                ps.setInt(1,id);
                ps.setString(2,name);
                ps.setDouble(3,price);
                ps.setInt(4,qty);
                ps.setDouble(5,subtotal);
                ps.executeUpdate();

                finalTotal += subtotal;
            }

            // 🧾 BILL WINDOW OPEN
            new BillFrame(cartModel, finalTotal);

            cartModel.setRowCount(0);
            updateTotal();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"Order Failed!");
        }
    }

}
