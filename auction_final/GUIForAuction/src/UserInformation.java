import AP_project.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class UserInformation extends GeneralWindow implements MouseListener{
    private JLabel fullName, nameL, emailL, email, addressL, address, tableL, userL;
    private JTable userOwnTable;
    private ArrayList<Product> list = null;
    private JButton listForAuction;
    private JScrollPane scrollPane;
    private int selectedRow = -1;
    private Register aUser;
    private JPanel priceDurationAcceptingPane, userInformationPane;  // should start from setBounds(600, 5, 295, 70)
    private JTextField price, duration;
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
   private MouseListener holdList, holdP, holdD;
    private DefaultTableModel listModel;
    public UserInformation(int userID){
        this.userId = userID;
        this.setSize(900, 500);
        rightBtn.setText("Reload");
        leftBtn.setText("Back");

        priceDurationAcceptingPane = new JPanel();
        priceDurationAcceptingPane.setLayout(null);
        priceDurationAcceptingPane.setBounds(600, 15, 280, 70);
        JLabel label = new JLabel("Starting Price:");
        price = new JTextField(10);
        JLabel label1 = new JLabel("Duration In Min: ");
        duration = new JTextField(10);

        label.setBounds(5,5,100,20);
        price.setBounds(120,5, 100, 30);
        label1.setBounds(5, 40, 100,20);
        duration.setBounds(120, 40, 100, 30);

        price.setEnabled(false);
        duration.setEnabled(false);


        priceDurationAcceptingPane.add(label);
        priceDurationAcceptingPane.add(price);
        priceDurationAcceptingPane.add(label1);
        priceDurationAcceptingPane.add(duration);

        this.add(priceDurationAcceptingPane);

        userInformationPane = new JPanel();
        userInformationPane.setBounds(5, 100, 875,145);
        userInformationPane.setLayout(null);
        userL = new JLabel("User Information");
        nameL = new JLabel("Full Name: ");
        emailL = new JLabel("Email: ");
        addressL = new JLabel("Shipping Address: ");
        tableL = new JLabel("Item Owned");
        try {
            aUser = toServer.getUserInfo(userId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        fullName = new JLabel(aUser.getFirstName() + " "+ aUser.getLastName());
        email = new JLabel(aUser.getEmail());
        address = new JLabel(aUser.getAddress());
        listForAuction = new JButton("List This Item");
        listForAuction.setEnabled(false);

        String [] clmnName = {"Product Id", "Product Name", "Product Category", "Product Condition","Product Description"};

        listModel = new DefaultTableModel(clmnName,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userOwnTable = new JTable(listModel);
        scrollPane = new JScrollPane(userOwnTable);

        viewUserProduct();

        userInformationPane.addMouseListener(this);
        userOwnTable.addMouseListener(this);
        scrollPane.addMouseListener(this);
        headerPanel.addMouseListener(this);
        listForAuction.addActionListener(this);
        this.setVisible(true);
    }

    public void viewUserProduct(){
        headLabel_CurrPage.setText("Inventory");
        listModel.getDataVector().removeAllElements();
        try {
            list = toServer.getAllProducts(userId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if(!list.isEmpty()){
            for(Product aProd: list){
                Object[] aRow = {aProd.getId(), aProd.getName(),  aProd.getCategory(), aProd.getCondition(), aProd.getDescription()};
                listModel.addRow(aRow);
            }
        }else {
            System.out.println("list is Empty");
        }

        nameL.setFont(new Font("Courier", Font.PLAIN, 20));
        emailL.setFont(new Font("Courier", Font.PLAIN, 20));
        addressL.setFont(new Font("Courier", Font.PLAIN, 20));
        fullName.setFont(new Font("Courier", Font.PLAIN, 20));
        email.setFont(new Font("Courier", Font.PLAIN, 20));
        address.setFont(new Font("Courier", Font.PLAIN, 20));

        userL.setBounds(5, 5, 100,20);
        nameL.setBounds(5,35,200,30);
        fullName.setBounds(210,35,200,30);
        emailL.setBounds(5,60,200,30);
        email.setBounds(210,60,200,30);
        addressL.setBounds(5,85,200,30);
        address.setBounds(210,85,200,30);
        tableL.setBounds(5, 120, 200, 30);


        userInformationPane.add(userL);
        userInformationPane.add(nameL);
        userInformationPane.add(fullName);
        userInformationPane.add(emailL);
        userInformationPane.add(email);
        userInformationPane.add(addressL);
        userInformationPane.add(address);
        userInformationPane.add(tableL);



        scrollPane.setBounds(5,250,875,200);

        listForAuction.setBounds(470, 15, 120,70);

        resizeRow(userOwnTable);
        resizeColumn(userOwnTable);

        this.add(userInformationPane);
        this.add(scrollPane);
        this.add(listForAuction);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object hear = e.getSource();
        if( hear == leftBtn){
            System.out.println("back button");
            new UserOptionPage(userId).setVisible(true);
            dispose();
        }
        if( hear == rightBtn){
            headLabel_CurrPage.setText("Reloading......");
            System.out.println("reload button");
            try {
                System.out.println("sleep for 1 sec");
                Thread.sleep(1000);
                System.out.println("wake Up");
                headLabel_CurrPage.setText("");
                viewUserProduct();
            } catch (InterruptedException ex) {

            }
        }
        if(hear == listForAuction){
            int answer = JOptionPane.showConfirmDialog(this,"Do you want to List this Product in the Auction?","Auction Listing",JOptionPane.YES_NO_OPTION);
            if (answer == 0){
                  // List the Auction in the Auction Listing
                  try {
                      double price_ = Double.parseDouble(price.getText());
                      int duration_ = Integer.parseInt(duration.getText());

                      AuctionDetails auctionDetails = new AuctionDetails();
                      LocalDateTime startTime=LocalDateTime.now();
                      LocalDateTime endTime =startTime.plusMinutes(duration_);
                      auctionDetails.setProductID(list.get(selectedRow).getId());
                      auctionDetails.setSellerID(userId);
                      auctionDetails.setStartTime(startTime);
                      auctionDetails.setEndTime(endTime);
                      auctionDetails.setCurrentPrice(price_);
                      auctionDetails.setStartPrice(price_);

                      try {
                          if( toServer.getProductDetails(list.get(selectedRow).getId()) != null){// meaning there exists a transaction and no duplication is needed
                              System.out.println("The Transaction Exist So you can not Make the Auction 2 times");
                              headLabel_CurrPage.setText("Can't List an Item Twice");
                          }
                          else{
                              createAuction(auctionDetails);
                              int newAuctionId = toServer.getProductDetails(list.get(selectedRow).getId()).getAuctionID();
                              toServer.placeBid(newAuctionId, userId, price_);
                              headLabel_CurrPage.setText("The Product is Listed");
                          }
                      } catch (RemoteException | MalformedURLException | NotBoundException ex) {
                          System.out.println("Error Happened while Creating an auction");
                          ex.printStackTrace();
                      }

                  } catch (NumberFormatException  ex) {
                      JOptionPane.showMessageDialog(this,"Please enter a Valid Price and Duration.");
                  }


            }
        }

    }
    public void createAuction(AuctionDetails auctionDetails) throws RemoteException, MalformedURLException, NotBoundException{
        // Insert the auction into the database
        AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
        int auctionId=access.insertAuction(auctionDetails);
        auctionDetails.setAuctionId(auctionId);

        // Calculate the delay until the auction's end time
        long delay = ChronoUnit.MILLIS.between(LocalDateTime.now(), auctionDetails.getEndTime());

        // Schedule a task to run at the auction's end time
        executorService.schedule(() -> {
            try {
                endAuction(auctionDetails);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error Happened while Ending An auction");
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    private void endAuction(AuctionDetails auctionDetails) throws RemoteException, MalformedURLException, NotBoundException {
        System.out.println("\nclosing the auction");
        AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");

        int auctionId=auctionDetails.getAuctionID();
        int userId=auctionDetails.getUserId();
        int productId = auctionDetails.getId();
        access.removeAuction(auctionDetails.getAuctionID());
        try {
            Bid temp = access.getWinner(auctionDetails.getAuctionID());
            Transaction transaction = new Transaction(temp.getBidderId(), userId, productId, temp.getBidAmount(), temp.getBidTime());
            access.updateProduct(temp.getBidderId(), productId);
            access.addTransactions(transaction);
        }
        catch (NullPointerException e){
            System.out.println("\nSorry no person hasn't been bidding on this product (ProductNo: " + productId + ")");
        }

    }

    private void removeMouse_ActionListner(){
        duration.removeMouseListener(holdD);
        price.removeMouseListener(holdP);
        listForAuction.removeActionListener(holdL);
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Object hear = e.getSource();
        selectedRow = userOwnTable.getSelectedRow();
        System.out.println("selectedRow: " + selectedRow);
        if (selectedRow != -1){
            listForAuction.setEnabled(true);
            price.setEnabled(true);
            duration.setEnabled(true);
        }
        if( hear == userInformationPane || hear == headerPanel || (hear == scrollPane && hear != userOwnTable) ){
            listForAuction.setEnabled(false);
            price.setEnabled(false);
            duration.setEnabled(false);
            selectedRow = -1;
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) { }
}
