import javax.swing.*;
import java.awt.*;

public class ICurrInfo extends JButton{ // delete JPanel extension
    private String descvalue, namevalue, catagoryvalue;
    private JLabel nameL, descL, catagoryL, name, desc, catagory ;
    private int productId;

//    public JFrame frame = new JFrame();
    public ICurrInfo( int productId, String ItemDesc, String ItemName, String ItemCatagory) {

            this.descvalue = ItemDesc;
            this.namevalue = ItemName;
            this.catagoryvalue = ItemCatagory;

        this.setBorder(BorderFactory.createDashedBorder(Color.BLACK));
        this.setLayout(null);
        name = new JLabel("Name: ");
        desc = new JLabel("Description: ");
        catagory = new JLabel("Category:");

        nameL = new JLabel(ItemName);
        descL = new JLabel(ItemDesc);
        catagoryL = new JLabel(ItemCatagory);
        this.setBounds(10,10,460,145);

        name.setBounds(5, 5,100, 20);
        nameL.setBounds(5, 30,300,40);

        desc.setBounds(315,5,100,20);
        descL.setBounds(315,30,140,40);

        catagory.setBounds(215,75,100,20);
        catagoryL.setBounds(215,100,240,40);


        nameL.setBorder(BorderFactory.createEtchedBorder());
        descL.setBorder(BorderFactory.createEtchedBorder());
        catagoryL.setBorder(BorderFactory.createEtchedBorder());


       this.setBorder(BorderFactory.createEtchedBorder());
       this.add(nameL);
       this.add(descL);
       this.add(catagoryL);
       this.add(name);
       this.add(catagory);
       this.add(desc);

//       frame.setLayout(null);
//       frame.setSize(500,200);
//       frame.setResizable(false);
//       frame.add(this);
//       frame.setVisible(true);
    }
    public int getProductID() {  return productId;  }

}
