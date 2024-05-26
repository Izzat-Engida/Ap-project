import AP_project.Product;
import AP_project.Register;
import AP_project.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

public class TransactionHistory extends GeneralWindow{
    private ArrayList<Transaction> boughtItem = null, soldItem = null;
    private int userId;
    private JTable soldTable, boughtTable;
    private JScrollPane soldPane, boughtPane;
    private JLabel soldL, boughtL;
    private DefaultTableModel soldModel, boughtModel;
    public TransactionHistory(int userId){
        this.userId = userId;
        setSize(900, 630);
        headLabel_CurrPage.setText("Transaction History");
        leftBtn.setText("Back");
        rightBtn.setText("Reload");

        String [] clmnName = {"Product Id ", "Product Name ", "Product Condition ","Product Description ", "To "};

        soldModel = new DefaultTableModel(clmnName,0){
            @Override
            public boolean isCellEditable(int row, int column) {   return false;  }
        };

        clmnName[4] = "From";

        boughtModel = new DefaultTableModel(clmnName,0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        soldTable = new JTable(soldModel);
        soldPane = new JScrollPane(soldTable);

        boughtTable = new JTable(boughtModel);
        boughtPane = new JScrollPane(boughtTable);

        soldL = new JLabel("Sold Items");
        soldL.setBounds(5, 100, 100, 20);
        boughtL = new JLabel("Bought Items");
        boughtL.setBounds(5,330,100,20);

        this.add(soldL);
        this.add(boughtL);


        viewTheHistory();
        resizeColumn(soldTable);
        resizeColumn(boughtTable);

        resizeRow(soldTable);
        resizeRow(boughtTable);

        this.setVisible(true);

    }

    private void viewTheHistory() {
        System.out.println("in view History");
        boughtModel.getDataVector().removeAllElements();
        soldModel.getDataVector().removeAllElements();

        try{
            boughtItem = toServer.getTransactions_buyer(userId);
            soldItem = toServer.getTransactions_Seller(userId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("BoughtLength: " + boughtItem.size());
        System.out.println("SoldLength: " + soldItem.size());
        for(Transaction abought : boughtItem){
            Product aProduct = null;
            Register aPerson = null;
            try {
                 aProduct = toServer.getAProduct(abought.getProductId());
                 aPerson = toServer.getUserInfo(abought.getSellerId()); // maybe change it to abought.getSellerId()
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (aProduct != null) {
                System.out.println("aProduct is not null in boughtItem");
                Object[] rowItem = {abought.getProductId(), aProduct.getName(),aProduct.getCondition(),aProduct.getDescription(),aPerson.getFirstName() + " " + aPerson.getLastName()};
                System.out.println(Arrays.toString(rowItem));
                boughtModel.addRow(rowItem);
            }
        }

        for(Transaction aSold : soldItem){
            Product aProduct = null;
            Register aPerson = null;
            try {
                aProduct = toServer.getAProduct(aSold.getProductId());
                aPerson = toServer.getUserInfo(aSold.getBuyerId()); // maybe change it to abought.getBuyerId()
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (aProduct != null) {
                System.out.println("aProduct is not null in soldItem");
                Object[] rowItem = {aSold.getProductId(), aProduct.getName(),aProduct.getCondition(),aProduct.getDescription(),aPerson.getFirstName() + " " + aPerson.getLastName()};
                System.out.println(Arrays.toString(rowItem));
                soldModel.addRow(rowItem);
            }
        }

        soldPane.setBounds(5,125,875,200);
        boughtPane.setBounds(5,365,875,200);

        this.add(soldPane);
        this.add(boughtPane);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object clicked = e.getSource();
        if(clicked == leftBtn){
            new UserOptionPage(userId).setVisible(true);
            dispose();
        }
        else if(clicked == rightBtn){
            viewTheHistory();
        }
    }
}
