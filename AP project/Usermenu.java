import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Usermenu {
    public void menu(int userId) throws MalformedURLException, NotBoundException, RemoteException {
        Scanner scan=new Scanner(System.in);
        int choice;
        while (true){
            // deleting expaired auction if found
            System.out.println("1. Check the items to bid on");
            System.out.println("2. List an item to be bid on");
            System.out.println("3. Check the items you bid on");
            System.out.println("4. See your history");
//            System.out.println("5. manage your money");
            System.out.println("5. Delete your account ");
            System.out.println("6. Save your Product Information in the system");
            System.out.println("7. See your List of product saved in the system");
            System.out.println("8. logout");
            System.out.print("choice: ");
            AuctionServerImp serverImp = new AuctionServerImp();


            if(!scan.hasNextInt()){
                System.out.println("Invalid input. Please enter a number.");
                scan.next(); // discard the non-integer input
                continue;
            }
            choice=scan.nextInt();
            scan.nextLine(); // consume newline left-over
            switch(choice){
                case 1:
                    Item item=new Item();
                    item.itemMenu(userId);
                    break;
                case 2:

                    ListItem items= new ListItem();
                    items.menu(userId);
                    break;
                case 3:
                    // Call the method to check the items the user bid on
                    ArrayList<Bid> bidArrayList = serverImp.getBids(userId);

                    if(bidArrayList.size() < 1){
                        System.out.println("You didn't have any biding");
                    }
                    else{
                        int size = bidArrayList.size();
                        System.out.println("BidderId; " + bidArrayList.get(0).getBidderId());
                        System.out.println(String.format("%5s%5s%5s%20s", "BidId", "AuctionID", "BidAmount", "BidTime"));

                        for(int i = 0; i < size; i++){
                            Bid aRow = bidArrayList.get(i);
                            System.out.printf("%5d %5d %5f %20s", aRow.getBidId(), aRow.getAuctionId(), aRow.getBidAmount(), aRow.getBidTime().toString());
                        }
                        System.out.println();
                    }
                    break;
                case 4:
                    System.out.println("UserId: " + userId);
                    ArrayList<Transaction> boughtList = serverImp.getTransactions_buyer(userId);
                    ArrayList<Transaction> soldList = serverImp.getTransactions_Seller(userId);
                    System.out.println("User Bought: " + boughtList.size() + " Items");
                    System.out.println("User Sold: " + soldList.size() + " Items");
                    if(boughtList.size() > 0) {
                        System.out.println(String.format("%5s %5s %5s %5s %20s","TransactionId", "ProductId", "price", "SellerId", "DateStamp"));
                        for (int i = 0; i < boughtList.size(); i++) {
                            Transaction aRow = boughtList.get(i);
                            System.out.println(String.format("%5d %5d %5f %5d %20s", aRow.getTransactionId(), aRow.getProductId(), aRow.getPrice(), aRow.getSellerId(), aRow.getDateStamp().toString()));
                        }
                        System.out.println();
                        System.out.println();
                    }
                    if(soldList.size() > 0){
                        System.out.println(String.format("%5s %5s %5s %5s %20s","TransactionId", "ProductId", "price", "BuyerId", "DateStamp"));
                        for(int i = 0; i < soldList.size(); i++){
                            Transaction aRow = soldList.get(i);
                            System.out.println(String.format("%5d %5d %5f %5d %20s", aRow.getTransactionId(), aRow.getProductId(), aRow.getPrice(), aRow.getBuyerId(), aRow.getDateStamp().toString()));                        }
                    }
                    System.out.println();
                    System.out.println();
                    break;
//                case 5:
//                    break;
                case 5:
                    System.out.println("Enter your password: ");
                    String password=scan.nextLine();
                    AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
                    if(access.removeAccount(userId, password)){
                        access.removeAccount(userId,password);
                        System.out.println("sorry to see you leave");
                        return;
                    }
                    else{
                        System.out.println("sorry, you can't delete this account\n you have inputted wrong information");
                    }

                    break;


                case 6:
                    Scanner cin = new Scanner(System.in);
                    System.out.print("Enter Name of the Product: ");
                    String name = cin.nextLine();
                    System.out.print("Enter Description: ");
                    String des = cin.nextLine();
                    System.out.print("Enter Category of the Product: ");
                    String category = cin.nextLine();
                    System.out.print("Enter the Condition of the Product: ");
                    String cond = cin.nextLine();
                    boolean success = serverImp.insertNewProduct(userId, name, des,category, cond);
                    if(!success){
                        System.out.println("----- Sorry can't Save your Product in the System -----");
                    }
                    else{
                        System.out.println("\nProduct saved in the System Product List Successfully\n");
                    }
                    break;
                case 7:
                    System.out.println("userId: " + userId);
                    ArrayList<Product> listOfProds = serverImp.getAllProducts(userId);
                    if(listOfProds.size() < 1){
                        System.out.println("No Product hasn't been logged for storage");
                    }
                    else{
                        System.out.println(String.format("%5s%10s%10s \t %-5s", "ProductId", "Name", "Category", "Description"));
                        for(int i = 0; i < listOfProds.size(); i++){
                            Product aRow = listOfProds.get(i);
                            System.out.println(String.format("%5d%10s%10s \t %-5s", aRow.getId(), aRow.getName(), aRow.getCategory(), aRow.getDescription()));
                        }
                    }
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return; // exit the method
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }
    }
}
