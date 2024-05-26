import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import AP_project.*;
import AP_project.AuctionServerImp;

public class AuctionList extends GeneralWindow implements ActionListener {
    protected int userId;
    protected ArrayList<ICurrInfo> listOfItemsBtn = new ArrayList<>();
    protected JScrollPane scrollPane;
    protected JList list;
    ArrayList<Product> listOfItemsInAuction = new ArrayList<>();
    public AuctionList(int userId) {
        headLabel_CurrPage.setText("Current Auction");
        leftBtn.setText("Back");
        rightBtn.setText("Reload");
        this.userId = userId;
        headLabel_CurrPage = new JLabel();
        list = new JList();
        scrollPane = new JScrollPane(list);

        ListAuctions();
    }

    private void ListAuctions(){
        listOfItemsInAuction.clear();
        listOfItemsBtn.clear();
        try {
            toServer = new AuctionServerImp();
            listOfItemsInAuction = toServer.getAllProducts();
        } catch (RemoteException e) {
            System.out.println("Error while retrieving all Auction Lists");
        }
        if (listOfItemsInAuction.isEmpty()){
            JLabel sorry = new JLabel("There is no Auction Listing Now");
            this.add(sorry);
        }
        else{
            Box box= Box.createVerticalBox();
            box.setSize(460,100);
            box.removeAll();
            for(Product aProduct: listOfItemsInAuction){
                listOfItemsBtn.add(new ICurrInfo(aProduct.getId(),aProduct.getDescription(),aProduct.getName(),aProduct.getCategory()));
                listOfItemsBtn.getLast().setPreferredSize(new Dimension(460, 150));
                listOfItemsBtn.getLast().addActionListener(this);
                JLabel space = new JLabel();
                space.setPreferredSize(new Dimension(460,5));
                box.add(space);
                box.add(listOfItemsBtn.getLast());
                decorateBtn(listOfItemsBtn.getLast());
                System.out.println(aProduct);
            }

            scrollPane = new JScrollPane(box,
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setBounds(10, 100, 460, 450);

            this.add(scrollPane);
        }
    }
    // runner


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
                    System.out.println("passed to the AuctionDetailPanel " + listOfItemsInAuction.get(i).getId() + " with user of: " + userId);
                    new AuctionDetailPanel(listOfItemsInAuction.get(i).getId(), userId).setVisible(true);
                    break;
                }
            }
            dispose();
        }
    }
}
