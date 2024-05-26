import AP_project.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class AuctionDetailPanel extends GeneralWindow{
    private AuctionDetails theDetail = new AuctionDetails();
    private long remainingSeconds = 10;
    private JButton cancel, stake;
    private JSpinner price;
    private JLabel startingPrice, currPrice, timer, startLabel, currLabel, timmerLabel, priceLabel;
    private JTextArea info;
    private int userId, productId;
    private JPanel containerPanel;
    private Timer countDownTimer;
    private boolean canParticipate = true;

    public AuctionDetailPanel(int productId, int userId) {
        this.productId = productId;
        this.userId = userId;
        this.headLabel_CurrPage.setText("Live Auction");
        try {
            theDetail = toServer.getProductDetails(productId);
            if(toServer.getAProduct(productId).getOwnerId() == userId)
                canParticipate = false;
        } catch (RemoteException e) {
            System.out.println("Error happened while fetching product detail from the database");
        }


        Instant auctionEnd = theDetail.getEndTIme().toInstant();
        Instant currTime = Instant.now();

        Duration timeElapsed = Duration.between(currTime, auctionEnd);
        remainingSeconds = timeElapsed.toSeconds();

        Function <Long, String > formatTime = sec ->{
            Long minutes = sec / 60;
            Long secs = sec % 60;
            return String.format("%02d:%02d", minutes, secs);
        };

        countDownTimer = new Timer(1000, e->{
            remainingSeconds --;
            if(remainingSeconds >= 0){
                timer.setText(formatTime.apply(remainingSeconds));
                update();
            }
            else{
                timer.setText("Time's UP!");
                ((Timer) e.getSource()).stop();
                stake.setEnabled(false);
                cancel.setEnabled(false);
                price.setEnabled(false);
                Bid temp = null;
                Register aPerson = null;
                try {
                    temp = toServer.getWinner(theDetail.getAuctionID());
                    aPerson = (temp != null) ? toServer.getUserInfo(temp.getBidderId()) : null ;
                } catch (RemoteException ex) {
                    System.out.println("Error Happened while showing the winner");
                }
                System.out.println("checking...... \n");
                if (aPerson == null || temp == null){
                    info.setText("There is no Winner");
                    System.out.println("no winner");
                }
                else if(!canParticipate){   // meaning the user is the one using this window
                    info.setText("Sorry No Body Bid on Your Product");
                    try {
                        toServer.removeAuction(theDetail.getAuctionID());
                        System.out.println("Auction Removed Because of Time");
                    } catch (RemoteException ex) {
                        System.out.println("Error in Removing the Auction");
                    }
                }
                else if(aPerson.getUserId() == userId){
                    info.setText("Congratulations you are the winner of this Auction");
                    cancel.setText("Cancel It");
                    stake.setText("Buy It");
                    stake.setEnabled(canParticipate);
                    cancel.setEnabled(canParticipate);
                    System.out.println("you are the winner");
                    try {
                        toServer.removeAuction(theDetail.getAuctionID());
                        System.out.println("Auction Removed Because of Time");
                    } catch (RemoteException ex) {
                        System.out.println("Error in Removing the Auction");
                    }
                }
                else {
                    info.setText("The Winner of This Auction is: " + aPerson.getFirstName() + " " + aPerson.getLastName());
                    System.out.println("smn is the winner");
                }
            }
        });

        System.out.println("on the view");
        preview();
        countDownTimer.start();

    }
    private void update(){
        System.out.println("updating every now and then");

        try {
            Bid theMaxBidder = toServer.getWinner(theDetail.getAuctionID());
            currPrice.setText("$ " + Double.toString(theMaxBidder.getBidAmount()));
            System.out.println("The Max Bid is: " + theMaxBidder.getBidAmount());
            if(theMaxBidder.getBidAmount() >= (double)price.getValue() ) {
                SpinnerNumberModel priceModel = new SpinnerNumberModel((theMaxBidder.getBidAmount() + theMaxBidder.getBidAmount() * 0.1), (theMaxBidder.getBidAmount() + theMaxBidder.getBidAmount() * 0.1), Integer.MAX_VALUE, 10);
                price.setModel(priceModel);
            }
            toServer.modifyAuction(theDetail.getAuctionID(), theMaxBidder.getBidAmount());

        } catch (RemoteException ex) {
            System.out.println("Error Happened while Update the AuctionDetail Panel");
        }

    }
    private void preview(){
        leftBtn.setText("Back");
        rightBtn.setText("Menu");
        containerPanel = new JPanel();
        containerPanel.setLayout(null);
        startingPrice = new JLabel();
        setFontToAll(startingPrice);
        currPrice = new JLabel();
        setFontToAll(currPrice);
        stake = new JButton("Stake");
        cancel = new JButton("Cancel All Together");
        startLabel = new JLabel("Starting Price");
        currLabel = new JLabel("Current Price");
        timmerLabel = new JLabel("Available Time");
        priceLabel = new JLabel("Stake Price");
        info = new JTextArea();
        info.setText("""
                    The winner 
                            will Be 
                                listed when the Auction Ends 
                """);

        timer = new JLabel();

        currPrice.setText("$ " + Double.toString(theDetail.getCurrentPrice()));
        startingPrice.setText("$ " + (Double.toString(theDetail.getStartPrice())));
        // Don't Forget about the timmer


        SpinnerNumberModel priceModel = new SpinnerNumberModel((theDetail.getCurrentPrice() + theDetail.getCurrentPrice() * 0.1),(theDetail.getCurrentPrice() + theDetail.getCurrentPrice() * 0.1),Integer.MAX_VALUE,10);
        price = new JSpinner(priceModel); // JSpinner needs max and low and such specifications

        price.setFont(new Font("MV Boli",Font.PLAIN,25));
        // ActionListener Implementation
        leftBtn.addActionListener(this);
        rightBtn.addActionListener(this);
        cancel.addActionListener(this);
        stake.addActionListener(this);

        stake.setEnabled(canParticipate);
        cancel.setEnabled(canParticipate);
        price.setEnabled(canParticipate);

        info.setEditable(false);

        borderComp(price);
        borderComp(stake);
        borderComp(cancel);
        borderComponent(startingPrice);
        borderComponent(currPrice);
        borderComponent(timer);
        borderComponent(startLabel);
        borderComponent(currLabel);
        borderComponent(timmerLabel);
        borderComponent(priceLabel);

        startLabel.setBounds(5,5,100,20);
        startingPrice.setBounds(5, 30, 190,30);
        currLabel.setBounds(255,5,100,20);
        currPrice.setBounds(255,30,190,30);
        info.setBounds(5, 65,450,200);
        cancel.setBounds(5, 350, 190,50);
        stake.setBounds(255,350,190,50);
        priceLabel.setBounds(255,270,100,20);
        price.setBounds(255, 295,190,40);
        timmerLabel.setBounds(5,270,100,20);
        timer.setBounds(5, 295, 190, 40);

       containerPanel.add(startingPrice);
       containerPanel.add(currPrice);
       containerPanel.add(info);
       containerPanel.add(cancel);
       containerPanel.add(stake);
       containerPanel.add(price);
       containerPanel.add(timmerLabel);

       containerPanel.add(timer);
       containerPanel.add(priceLabel);
       containerPanel.add(startLabel);
       containerPanel.add(currLabel);
       containerPanel.setBounds(5,100,460,450);
        this.add(containerPanel);

    }

    private void borderComponent(JLabel a){   a.setBorder(BorderFactory.createEtchedBorder());  }
    private void borderComp(JButton a){  a.setBorder(BorderFactory.createEtchedBorder()); }
    private void borderComp(JSpinner a){ a.setBorder(BorderFactory.createEtchedBorder()); }
    private void setFontToAll(JComponent all){   all.setFont(new Font("MV Boli",Font.PLAIN,15)); }



    @Override
    public void actionPerformed(ActionEvent e) {
        Object hear = e.getSource();
        if(hear == cancel){
            if(Objects.equals(cancel.getText(), "Cancel It")){
                System.out.println("cancel it button clicked after Refusing The winning");
                new AuctionList(userId).setVisible(true);
                dispose();
            }
            else{
                JOptionPane optionPane = new JOptionPane();
                int answer = optionPane.showConfirmDialog(this,"Do you want to Cancel out your Bidding all Together On this Item?", "Cancel Bidding", JOptionPane.YES_NO_OPTION);
                if (answer == 0) {  // which is yes
                    try {
                        boolean deleted = toServer.deleteBiddingInfo(userId, theDetail.getAuctionID());
                        if (deleted)
                            System.out.println("User Auction Data is deleted");
                        else
                            System.out.println("User Data was not Found in the Bidding Table");
                    } catch (RemoteException ex) {
                        System.out.println("Error while calling deleteBiddingInfo");
                    }
                }
            }
        }
        else if(hear == stake){
            if(Objects.equals(stake.getText(), "Buy It")){
                cancel.setEnabled(false);
                try {
                    Transaction aTransaction  = new Transaction(userId, theDetail.getUserId(), theDetail.getId(), toServer.getWinner(theDetail.getAuctionID()).getBidAmount(), toServer.getWinner(theDetail.getAuctionID()).getBidTime());
                    System.out.println(aTransaction);
                    toServer.addTransactions(aTransaction);
                    toServer.updateProduct(userId, aTransaction.getProductId());
                    System.out.println("Item ownership transfered to " + userId + " with ProductId of: " +  aTransaction.getProductId());
                    headLabel_CurrPage.setText("Bought Successful");
                } catch (RemoteException ex) {
                    System.out.println("Error Happened while writing the Transaction in the History");
                }
                stake.setEnabled(false);
            }
            else {
                double price_ = (double) price.getValue();
                try {

                    toServer.placeBid(theDetail.getAuctionID(), userId, price_);
                    toServer.modifyAuction(theDetail.getAuctionID(), price_);
                    System.out.println("Modifying the Auction to the Highest Bid");
                    currPrice.setText(Double.toString(toServer.getWinner(theDetail.getAuctionID()).getBidAmount()));
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        else if (hear == leftBtn){
            System.out.println("left Button clicked");
            new AuctionList(userId).setVisible(true);
            countDownTimer.stop();
            dispose();
        }
        else if(hear == rightBtn){
            new UserOptionPage(userId).setVisible(true);
            dispose();
        }
    }

}
