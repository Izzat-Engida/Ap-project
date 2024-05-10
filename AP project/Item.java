import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Item {
void itemMenu(int useriD) throws MalformedURLException, NotBoundException, RemoteException {
    Scanner scan=new Scanner(System.in);
    int choice;
  AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
    ArrayList<Product> products=new ArrayList<>();
     products=access.getAllProducts();
    if(products.size()>0){
    for(Product p:products){
        System.out.println(p);
        System.out.println("-------------------------------");
    }
    System.out.println("Select the product by id for more details: ");
    choice=scan.nextInt();
    scan.nextLine();
    productDetails(choice,useriD);
    }
    else{
        System.out.println("Sorry,no product in the listing");
    }
}
    void productDetails(int id,int userid) throws MalformedURLException, NotBoundException, RemoteException {
        AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
        AuctionDetails p=access.getProductDetails(id);
        System.out.println(p);
        System.out.println();
        System.out.println();
        Scanner scan=new Scanner(System.in);
        double temp=p.getCurrentPrice() + p.getCurrentPrice()*0.1;
        System.out.println("The min bid allowed is: " + temp);
        System.out.println("Do you want to bid on the item (yes/No)");
        String answer=scan.nextLine();
        if(answer.equalsIgnoreCase("yes")){
            System.out.println("Do you want to bid more than the min amount (yes/No) ");
            answer=scan.nextLine();
            if(answer.equalsIgnoreCase("yes")){
                System.out.println("amount ");
                double temp2=scan.nextDouble();
                scan.nextLine();
                while(temp2<temp){
                    System.out.println("the money should be equal or greater than the min amount");
                    System.out.println("amount ");
                    temp2=scan.nextDouble();
                    scan.nextLine();
                }
                temp=temp2;
                access.placeBid(p.getAuctionID(),userid,temp);
                access.updateCurrentBid(p.getAuctionID(),temp);
                System.out.println("you have successfully bid on the item");
            }

        }
        else{
            System.out.println("Maybe next time");
        }
    }

}
