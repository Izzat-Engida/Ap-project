import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;

public class UserDeletion extends GeneralWindow {
    private JLabel passL;
    private JPasswordField confirmPass;
    public UserDeletion(int userID){
        headLabel_CurrPage.setText("User Deletion " + "🥺");
        this.userId = userID;
        rightBtn.setText("Delete");
        rightBtn.setEnabled(false);
        passL = new JLabel("Enter Your Password");
        confirmPass = new JPasswordField();
        passL.setBounds(10, 100, 200, 20);
        confirmPass.setBounds(10, 125, 400, 40);

        confirmPass.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if(confirmPass.getPassword().length < 3){
                    rightBtn.setEnabled(false);
                }
                else{
                    rightBtn.setEnabled(true);
                }
            }
            @Override
            public void keyPressed(KeyEvent e) { }
            @Override
            public void keyReleased(KeyEvent e) { }
        });
        add(passL);
        add(confirmPass);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      if(e.getSource() == leftBtn){
            System.out.println("Back button in UserDeletion");
            new UserOptionPage(userId).setVisible(true);
            dispose();
        }
        else if (e.getSource() == rightBtn){
            System.out.println("Try to Delete the Account Button");
            String thePass = String.valueOf(confirmPass.getPassword());
            boolean deleted = false;
          try {
              deleted = toServer.removeAccount(userId, thePass);
          } catch (RemoteException ex) {
              System.out.println("Error Happened in Deleting a User Information");
          }
          if(deleted){
              JOptionPane.showMessageDialog(this,"User Deletion Successful \n\n Sorry to See you Leave");
              new Window().setVisible(true);
              dispose();
          }
          else{
              JOptionPane.showMessageDialog(this,"Incorrect Password");
          }
        }
    }
}
