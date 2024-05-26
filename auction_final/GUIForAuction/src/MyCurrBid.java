import AP_project.AuctionDetails;
import AP_project.AuctionServerImp;
import AP_project.Bid;
import AP_project.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class MyCurrBid extends AuctionList implements ActionListener {

    private ArrayList<Product> currentUserBid = new ArrayList<>();
    public MyCurrBid(int userId) {
        super(userId);

        headLabel_CurrPage.setText("Your current Auction");
        leftBtn.setText("Back");
        rightBtn.setText("Reload");
        this.userId = userId;
        headLabel_CurrPage = new JLabel();
        list = new JList();
        scrollPane = new JScrollPane(list);
        System.out.println("showing my CurrBid class");
        this.ListAuctions();
    }
    private void ListAuctions(){
        currentUserBid.clear();
        listOfItemsBtn.clear();
        try {
            toServer = new AuctionServerImp();
            currentUserBid = toServer.getProductDetailsFromBidByBidder(userId);

        } catch (RemoteException e) {
            System.out.println("Error while retrieving all Auction Lists");
        }
        if (currentUserBid.isEmpty()){
            JLabel sorry = new JLabel("There is no Auction Listing Now");
            this.add(sorry);
        }
        else{
            Box box= Box.createVerticalBox();
            box.setSize(460,100);
            box.removeAll();
            for(Product aProduct: currentUserBid){
                listOfItemsBtn.add(new ICurrInfo(aProduct.getId(),aProduct.getDescription(),aProduct.getName(),aProduct.getCategory()));
                listOfItemsBtn.getLast().setPreferredSize(new Dimension(460, 150));
                listOfItemsBtn.getLast().addActionListener(this);
                JLabel space = new JLabel();
                space.setPreferredSize(new Dimension(460,5));
                box.add(space);
                box.add(listOfItemsBtn.getLast());
                decorateBtn(listOfItemsBtn.getLast());
                System.out.println(aProduct);
                System.out.println("|||||||||| in the for loop");
            }

            scrollPane = new JScrollPane(box,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBounds(10, 100, 460, 450);

            this.add(scrollPane);
        }


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == leftBtn){
            new UserOptionPage(userId).setVisible(true);
            dispose();
        }
        else if(e.getSource() == rightBtn){
            System.out.println("reloading");
            ListAuctions();
            scrollPane.removeAll();
        }
        else {
            for (int i = 0; i < listOfItemsBtn.size(); i++) {
                if (e.getSource() == listOfItemsBtn.get(i)) {
                    System.out.println("passed to the AuctionDetailPanel " + currentUserBid.get(i).getId() + " with user of: " + userId);
                    new AuctionDetailPanel(currentUserBid.get(i).getId(), userId);
                    close();
                    break;
                }
            }
        }
    }
}
