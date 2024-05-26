import javax.swing.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class ProductSaving extends GeneralWindow{
    private JScrollPane savePane;
    private JPanel container;
    private JTextField nameP, description;
    private JComboBox condition, category;
    private JLabel nameL, descriptionL, conditionL, categoryL;

    public ProductSaving(int userId){
        headLabel_CurrPage.setText("Save Here");
        this.userId = userId;
        container = new JPanel();
        container.setLayout(null);
        container.setBounds(10, 100, 460, 450);
        nameP = new JTextField();
        nameP.setBounds(5,30,450,20);
        nameL = new JLabel("Name of the Product: ");
        nameL.setBounds(5, 5, 200, 30);

        description = new JTextField();
        description.setBounds(5,80,450,60);
        descriptionL = new JLabel("Description: ");
        descriptionL.setBounds(5,55,100,20);

        condition = new JComboBox();
        condition.setBounds(5,170,450,20);
        conditionL = new JLabel("Condition: ");
        conditionL.setBounds(5,145,100,20);
        String[] conditions = { "Brand New", "Used", "2nd Hand"};
        for(String cond : conditions)
            condition.addItem(cond);

        category = new JComboBox();
        category.setBounds(5,220,450,20);
        categoryL = new JLabel("Category:");
        categoryL.setBounds(5,195,100,20);
        String[] categorys = { "Food", "Drinks", "Electronics", "Luxury","Furniture", "Other"};
        for(String cat : categorys)
            category.addItem(cat);

        rightBtn.setText("Save");

        container. add(nameP);
        container. add(nameL);
        container. add(description);
        container. add(descriptionL);
        container. add(condition);
        container. add(conditionL);
        container. add(category);
        container. add(categoryL);
        add(container);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == leftBtn){
            System.out.println("Back button in ProductSaving");
            new UserOptionPage(userId).setVisible(true);
            dispose();

        }
        if (e.getSource() == rightBtn){
            System.out.println("save button in ProductSaving");
            String nameOfProd = nameP.getText();
            String descrip = description.getText();
            String cat = String.valueOf(category.getSelectedItem());
            String cond = String.valueOf(condition.getSelectedItem());
            if(nameOfProd.isEmpty() || descrip.isEmpty()){
                JOptionPane.showMessageDialog(this,"Some or all Values of the Inputs are Empty.");
            }
            else{
                try {
                    toServer.insertNewProduct(userId, nameOfProd, descrip, cat, cond);
                    System.out.println("Saved");
                    nameP.setText("");
                    description.setText("");
                    headLabel_CurrPage.setText("Product saved Successfully");
                } catch (RemoteException ex) {
                    System.out.println("Error Happened while Inserting a new Product in the Database");
                }

            }

        }
    }
}
