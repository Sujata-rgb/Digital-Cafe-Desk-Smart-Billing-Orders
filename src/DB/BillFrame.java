package DB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.*;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class BillFrame extends JFrame {

    private JTextArea billArea;

    public BillFrame(DefaultTableModel cartModel, double total){

        setTitle("Digital Cafe Desk - Bill");
        setSize(600,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(new Color(35, 35, 45));
        setContentPane(background);

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(450,600));
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(255,140,0),2,true),
                new EmptyBorder(25,25,25,25)
        ));

        // ===== Header =====
        JLabel title = new JLabel("DIGITAL CAFE DESK",SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));
        title.setForeground(new Color(255,140,0));

        String date = new SimpleDateFormat("dd-MM-yyyy  HH:mm").format(new Date());
        int billNo = new Random().nextInt(9000)+1000;

        JLabel dateLabel = new JLabel("Bill No: "+billNo+"     Date: "+date,SwingConstants.CENTER);
        dateLabel.setFont(new Font("Segoe UI",Font.PLAIN,13));
        dateLabel.setForeground(Color.GRAY);

        JPanel top = new JPanel(new GridLayout(2,1));
        top.setOpaque(false);
        top.add(title);
        top.add(dateLabel);

        card.add(top,BorderLayout.NORTH);

        // ===== Bill Content =====
        billArea = new JTextArea();
        billArea.setEditable(false);
        billArea.setFont(new Font("Monospaced",Font.PLAIN,14));
        billArea.setBackground(Color.WHITE);

        billArea.append("---------------------------------------------\n");
        billArea.append(String.format("%-15s %-5s %-8s %-8s\n",
                "Item","Qty","Price","Total"));
        billArea.append("---------------------------------------------\n");

        for(int i=0;i<cartModel.getRowCount();i++){

            String name = cartModel.getValueAt(i,1).toString();
            String price = cartModel.getValueAt(i,2).toString();
            String qty = cartModel.getValueAt(i,3).toString();
            String subtotal = cartModel.getValueAt(i,4).toString();

            billArea.append(String.format("%-15s %-5s %-8s %-8s\n",
                    name,qty,price,subtotal));
        }

        billArea.append("---------------------------------------------\n");

        DecimalFormat df = new DecimalFormat("0.00");
        billArea.append("\nGrand Total : ₹ "+df.format(total)+"\n");
        billArea.append("\nThank You! Visit Again ☕\n");

        card.add(new JScrollPane(billArea),BorderLayout.CENTER);

        // ===== Buttons =====
        JButton printBtn = new JButton("Print");
        JButton downloadBtn = new JButton("Download");
        JButton closeBtn = new JButton("Close");

        JButton[] buttons = {printBtn, downloadBtn, closeBtn};

        for(JButton btn : buttons){
            btn.setFont(new Font("Segoe UI",Font.BOLD,14));
            btn.setBackground(new Color(255,140,0));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        // PRINT FUNCTION
        printBtn.addActionListener(e -> {
            try {
                billArea.print();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,"Print Failed!");
            }
        });

        // DOWNLOAD FUNCTION
        downloadBtn.addActionListener(e -> {
            try{
                FileWriter fw = new FileWriter("Cafe_Bill_"+billNo+".txt");
                fw.write(billArea.getText());
                fw.close();
                JOptionPane.showMessageDialog(this,"Bill Downloaded Successfully!");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Download Failed!");
            }
        });

        closeBtn.addActionListener(e -> dispose());

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.add(printBtn);
        bottom.add(downloadBtn);
        bottom.add(closeBtn);

        card.add(bottom,BorderLayout.SOUTH);

        background.add(card);
        setVisible(true);
    }
}
