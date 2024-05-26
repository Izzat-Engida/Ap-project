import AP_project.AuctionServer;
import AP_project.AuctionServerImp;
import AP_project.Register;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Calendar;      // just to access the current year so that the user will not provide wrong birthYear
import java.util.Date;


public class Window extends GeneralWindow{
    public static void main(String[] args) {     new Window().setVisible(true);   }
    private String fName, lName, emailStr, pass, confPass, addressStr;
    private JButton login, register, submitBtn, theLogin;
    private JPanel homePanel, addressPanel, submitBtn_panel, emailPanel, confirmPanel, passPanel, l_namePanel, f_namePanel;
    private JComboBox<String> monthCombo;
    private JTextField emailField, addressField, fname, lname, emailInput;
    private int year, day, currentPage = 0;
    private JPasswordField passwordField, confirmPass;
    private JLabel f_name, email, address, confirmPassword, passLabel, dateLable, l_name;
    private JSpinner daySpinner, yearSpinner ;
    private SpinnerNumberModel dayModel, yearModel;
    private AuctionServer access = null;

    public Window() {
        homePanel = new JPanel();

        monthCombo = new JComboBox<>();

        emailInput = new JTextField("Enter your Email");
        passwordField = new JPasswordField();
        f_name = new JLabel("First Name: ");
        fname = new JTextField(15);
        f_namePanel = new JPanel();
        l_name = new JLabel("Last Name: ");
        lname = new JTextField(20);
        l_namePanel = new JPanel();

        fname.addActionListener(this);
        lname.addActionListener(this);

        login = new JButton("Login");
        login.addActionListener(this);
        theLogin = new JButton("Login");
        theLogin.addActionListener(this);
        register = new JButton("Register");
        register.addActionListener(this);
        dateLable = new JLabel("Birth Date: ");


        dayModel = new SpinnerNumberModel(1, 1, 31, 1);
        daySpinner = new JSpinner(dayModel);

        int maxPossYear = Calendar.getInstance().get(Calendar.YEAR);
        yearModel = new SpinnerNumberModel(2000, 1924,maxPossYear,1);
        yearSpinner = new JSpinner(yearModel);
        passLabel = new JLabel("Password:");
        passPanel = new JPanel();

        confirmPassword = new JLabel("Confirm Password:");
        confirmPass = new JPasswordField(20);
        confirmPanel = new JPanel();

        email = new JLabel("Email Address:");
        emailField = new JTextField(20);
        emailPanel = new JPanel();

        address = new JLabel("Address:");
        addressField = new JTextField(20);
        addressPanel = new JPanel();

        submitBtn_panel = new JPanel(null);
        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);

        rightBtn.setText("Menu");
        rightBtn.setEnabled(false);


        homePage();

        this.add(headerPanel);
        this.add(homePanel);

//        this.setVisible(true);
    }
    private void homePage(){
        currentPage = 0;
        headLabel_CurrPage.setText("Home");
        removeAllComps(homePanel);
        leftBtn.setText("Exit");
        login.setBounds(0,0,460,100);
        login.setFont(new Font("MV Boli",Font.PLAIN,25));
        login.setMnemonic('l');
        decorateBtn(login);

        register.setBounds(0,105,460,100);
        register.setFont(new Font("MV Boli",Font.PLAIN,25));
        register.setMnemonic('r');
        decorateBtn(register);

        homePanel.setBounds(10, 100, 460, 450);


        homePanel.add(login);
        homePanel.add(register);
    }
    private void gotoLogin(){
        currentPage = 1;
        headLabel_CurrPage.setText("Login");
        removeAllComps(homePanel);
        setGridToEachLabel_Field(emailPanel,emailField ,email);
        setGridToEachLabel_passField(passPanel,passwordField, passLabel);
        theLogin.setBounds(400, 400, 150, 50);
        decorateBtn(theLogin);
        JLabel __ = new JLabel();
        __.setSize(100, 100);
        homePanel.add(emailPanel);
        homePanel.add(passPanel);
        homePanel.add(__);
        homePanel.add(theLogin);
        homePanel.setLayout(new GridLayout(8,1,0,5));

    }
    private void gotoRegister(){
        currentPage = 2;
        headLabel_CurrPage.setText("Registeration");
        removeAllComps(homePanel);
        homePanel.setLayout(new GridLayout(8, 1));

        setGridToEachLabel_Field(f_namePanel, fname,f_name);

        setGridToEachLabel_Field(l_namePanel, lname, l_name);

        monthCombo.setSize(100,20);

        String[] months = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        for(String month : months)
            monthCombo.addItem(month);

        daySpinner.setSize(100,20);



        yearSpinner.setSize(100,30);
        ChangeListener holdYear, changeInYear = e->{
            year = (int)yearSpinner.getValue();
        };
        holdYear = changeInYear;

        JPanel datePanel = new JPanel(new BorderLayout());
        datePanel.add(dateLable,BorderLayout.NORTH);

        datePanel.add(daySpinner,BorderLayout.WEST);

        datePanel.add(monthCombo,BorderLayout.CENTER);

        datePanel.add(yearSpinner,BorderLayout.EAST);
        setGridToEachLabel_passField(passPanel,passwordField, passLabel);
        setGridToEachLabel_passField(confirmPanel, confirmPass, confirmPassword);
        setGridToEachLabel_Field(emailPanel,emailField ,email);
        setGridToEachLabel_Field(addressPanel,addressField,address);

        submitBtn.setMnemonic('s');
        decorateBtn(submitBtn);
        submitBtn.setBounds(5,5,100,30);
        submitBtn_panel.add(submitBtn, BorderLayout.EAST);

        homePanel.add(f_namePanel);
        homePanel.add(l_namePanel);
        homePanel.add(emailPanel);
        homePanel.add(datePanel);
        homePanel.add(passPanel);
        homePanel.add(confirmPanel);
        homePanel.add(addressPanel);
        homePanel.add(submitBtn_panel);

    }
    private void setGridToEachLabel_Field(JPanel panel, JTextField textField, JLabel headLabel_CurrPage){
        panel.setLayout(new GridLayout(2,0,0, 0));
        panel.add(headLabel_CurrPage);
        panel.add(textField);
        panel.setSize(200,40);
    }

    private void setGridToEachLabel_passField(JPanel panel, JPasswordField pass, JLabel headLabel_CurrPage){
        panel.setLayout(new GridLayout(2,0,0, 5));
        panel.add(headLabel_CurrPage);
        panel.add(pass);
        panel.setSize(200,40);
    }
    private void removeAllComps(JPanel sm){
        sm.removeAll();
        sm.revalidate();
        sm.repaint();
        sm.setLayout(null);
    }

    public void actionPerformed(ActionEvent e) {
        Object clicked = e.getSource();
        if( clicked == login){
            gotoLogin();
            leftBtn.setText("Back");
        }
        else if(clicked == register){
            gotoRegister();
            leftBtn.setText("Back");

        }
        else if(clicked == leftBtn){
            System.out.println("currPage :" + currentPage);
            if(currentPage == 0){
                int answer = JOptionPane.showConfirmDialog(null,"Do you want to Exit ?","Exit",JOptionPane.YES_NO_OPTION);
                if(answer == 0){
                    System.exit(0);
                }
            }
            else{
                fname.setText("");
                lname.setText("");
                passwordField.setText("");
                confirmPass.setText("");

                homePage();
            }
        }
        else if (clicked == theLogin){
            System.out.println("Login is clicked");
            emailStr = emailField.getText();
            pass = String.valueOf(passwordField.getPassword());
            if(emailStr.isEmpty() || pass.isEmpty()){
                System.out.println("either or both is Empty");
                headLabel_CurrPage.setText("Email or Passowrd field is/are empty");
            }
            else{
                int userId = 0;
                boolean success = false;
                try {
                    access = new AuctionServerImp();
                    success = access.login(emailStr, pass);
                    userId = access.userId(emailStr);

                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                if(success && userId != -1){
                    headLabel_CurrPage.setText("login in success");
                    new UserOptionPage(userId).setVisible(true);
                    dispose();
                }
                else{
                    headLabel_CurrPage.setText("login failed");
                }
            }

        }
        else if(clicked == submitBtn){
            fName = fname.getText();
            lName = lname.getText();
            addressStr = addressField.getText();
            emailStr = emailField.getText();
            int month = -1;
            month = monthCombo.getSelectedIndex();
            pass = String.valueOf(passwordField.getPassword());
            confPass = String.valueOf(confirmPass.getPassword());
            day = (int)daySpinner.getValue();
            year = (int) yearSpinner.getValue();

            if(!fName.isEmpty() && !lName.isEmpty() && !addressStr.isEmpty() && !emailStr.isEmpty() && month != -1 && !pass.isEmpty()){
                if(pass.equals(confPass)){
                    Calendar c = Calendar.getInstance();
                    c.set(year, month, day);
                    Date birthDate = c.getTime();
                    Register register = new Register(fName, lName, emailStr, pass, birthDate, addressStr);

                    boolean success = false;
                    try {
                        access = new AuctionServerImp();
                        access = (AuctionServer) Naming.lookup("//localhost:8000/Auction");
                        success = access.register(register);
                    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                    if (success)
                        headLabel_CurrPage.setText("Registered Successfully");
                    else
                        headLabel_CurrPage.setText("Registration failed");
                }else{
                    headLabel_CurrPage.setText("your Password doesn't match");
                }
            }
            else{
                headLabel_CurrPage.setText("Some Fields are Empty");
            }
        }
    }
}
