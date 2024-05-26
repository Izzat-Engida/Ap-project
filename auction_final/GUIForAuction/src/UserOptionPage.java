import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;


public class UserOptionPage extends GeneralWindow{
    // delete after the test
    public static void main(String[] args) {
        new UserOptionPage(2).setVisible(true);
    }
    private ArrayList<JButton> optionBtn = new ArrayList<>();
    private JPanel homePanel;
    public UserOptionPage(int userId) {
        headLabel_CurrPage.setText("Main Menu");
        this.userId = userId;
        headLabel_CurrPage = new JLabel();
        homePanel = new JPanel();
        mainMenu();
        add(homePanel);
    }
    private void mainMenu(){

        rightBtn.setEnabled(false);
        rightBtn.setText("Menu");
        leftBtn.setText("Logout");
        homePanel.setBounds(10, 100, 460, 450);
        homePanel.setLayout(new GridLayout(7,1));
        String [] options = {"1. Live Items in Auction", "2. My Inventory", "3. My Bidding", "4. Transaction History",  "5. Save Product in Inventory", "6. Delete My Account / sign out "};
        for(int i = 0; i < 6; i++){
            optionBtn.add(new JButton(options[i]));
            decorateBtn(optionBtn.get(i));
            optionBtn.get(i).setHorizontalTextPosition(SwingConstants.LEFT);// it is not aligning the text to the left, just center 😔
            optionBtn.get(i).setFont( new Font(Font.SANS_SERIF, Font.ITALIC, 15));
            optionBtn.get(i).addActionListener(this);
            homePanel.add(optionBtn.get(i));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object clicked = e.getSource();
        if (clicked == leftBtn){
            System.out.println("Logging out?");
            int option = JOptionPane.showConfirmDialog(this,"Do you want to Log out?", "LogOut", JOptionPane.YES_NO_OPTION);
            if(option == 0){
                new Window().setVisible(true);
                dispose();
            }
        }
       else if (clicked == optionBtn.get(0)){
           System.out.println("option 1");
           new AuctionList(userId).setVisible(true);
           dispose();
       }
       else if(clicked == optionBtn.get(1)){
           System.out.println("option 2");
            new UserInformation(userId).setVisible(true);
            dispose();
       }
       else if(clicked == optionBtn.get(2)){
           System.out.println("option 3");
            new MyCurrBid(userId).setVisible(true);
            dispose();
       }
       else if(clicked == optionBtn.get(3)){
           System.out.println("option 4");
            new TransactionHistory(userId).setVisible(true);
            dispose();
       }
       else if(clicked == optionBtn.get(4)){
           System.out.println("option 5");
            new ProductSaving(userId).setVisible(true);
            dispose();
       }
       else if(clicked == optionBtn.get(5)){
           System.out.println("option 6");
            new UserDeletion(userId).setVisible(true);
            dispose();
       }

    }
}
