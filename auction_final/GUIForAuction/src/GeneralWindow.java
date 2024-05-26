import AP_project.AuctionServerImp;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class GeneralWindow extends JFrame implements ActionListener {
    protected int userId;
    protected JButton leftBtn, rightBtn;
    protected JLabel headLabel_CurrPage;
    protected JPanel headerPanel;
    protected AuctionServerImp toServer;
    protected ActionListener holdR, holdL;
    public GeneralWindow() {
        try{
            toServer = new AuctionServerImp();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        headLabel_CurrPage = new JLabel();
        leftBtn = new JButton();
        rightBtn = new JButton();
        headerPanel = new JPanel();
        headLabel_CurrPage.setLayout(null);
        headLabel_CurrPage.setVerticalTextPosition(JLabel.TOP);
        headLabel_CurrPage.setHorizontalTextPosition(JLabel.CENTER);

        leftBtn.addActionListener(this);
        rightBtn.addActionListener(this);       // this might cause a problem in the extended class because they may Implement the ActionListner in their View method

        header();

        ImageIcon icon = new ImageIcon("src/images_/brown_hammer.png");
        this.setIconImage(icon.getImage());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500, 600);
        this.setResizable(false);
        this.setTitle("META Auction");
        this.add(headerPanel);
    }

    private void header() {
        headLabel_CurrPage.setFont(new Font("Monospaced", Font.PLAIN, 15));

        headLabel_CurrPage.setBounds(85,5,290,70);
        headLabel_CurrPage.setVerticalTextPosition(JLabel.TOP);
        headLabel_CurrPage.setHorizontalTextPosition(JLabel.CENTER);

        headLabel_CurrPage.setOpaque(true);
        headLabel_CurrPage.setBorder(BorderFactory.createEtchedBorder());

        leftBtn.setText("Back");
        leftBtn.setBounds(5, 5, 70, 70);
        decorateBtn(leftBtn);


        rightBtn.setText("Right");
        rightBtn.setBounds(380, 5, 70, 70);
        decorateBtn(rightBtn);

        headerPanel.setBounds(10,10, 460,80);
        headerPanel.setLayout(null);

        headerPanel.add(leftBtn);
        headerPanel.add(rightBtn);
        headerPanel.add(headLabel_CurrPage);
    }
    
    protected static void decorateBtn(JButton btn){
        btn.setFocusable(false);
        btn.setBackground(Color.lightGray);
        btn.setBorder(BorderFactory.createEtchedBorder());
    }
    protected void resizeRow(JTable userOwnTable){
        for (int row = 0; row < userOwnTable.getRowCount(); row++) {
            int maxHeight = 0;
            for (int column = 0; column < userOwnTable.getColumnCount(); column++) {
                TableCellRenderer renderer = userOwnTable.getCellRenderer(row, column);
                Component comp = userOwnTable.prepareRenderer(renderer, row, column);
                maxHeight = Math.max(maxHeight, comp.getPreferredSize().height);
            }
            userOwnTable.setRowHeight(row, maxHeight);
        }
    }
    protected void resizeColumn(JTable userOwnTable){
        for (int column = 0; column < userOwnTable.getColumnCount(); column++) {
            int maxWidth = 0;
            for (int row = 0; row < userOwnTable.getRowCount(); row++) {
                TableCellRenderer renderer = userOwnTable.getCellRenderer(row, column);
                Component comp = userOwnTable.prepareRenderer(renderer, row, column);
                maxWidth = Math.max(maxWidth, comp.getPreferredSize().width);
            }
            userOwnTable.getColumnModel().getColumn(column).setPreferredWidth(maxWidth);
        }
    }
    protected void waitFor(int mill){
        try {
            Thread.sleep(mill);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void close(){
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent. WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == leftBtn){
            System.out.println("left button");
        }
        if (e.getSource() == rightBtn){
            System.out.println("right button");
        }
    }

}
