import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ListItem {
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

    public void menu(int userId) throws MalformedURLException, NotBoundException, RemoteException, RemoteException {
        System.out.println("Welcome user here you can Auction the items you added before\nLets begin");
        System.out.println("userId: " + userId);
        AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
        ArrayList<Product> listOfProds = access.getAllProducts(userId);
        if(listOfProds.size() < 1){
            System.out.println("No Product hasn't been logged in\n add items to Auction them ");
        }
        else{
            System.out.println(String.format("%5s%10s%10s \t %-5s", "ProductId", "Name", "Category", "Description"));
            for(int i = 0; i < listOfProds.size(); i++){
                Product aRow = listOfProds.get(i);
                System.out.println(String.format("%5d%10s%10s \t %-5s", aRow.getId(), aRow.getName(), aRow.getCategory(), aRow.getDescription()));
            }
            System.out.println("Select which product needs to added to auction by inputing the auction id: ");
            System.out.print("choice: ");
            Scanner scan=new Scanner(System.in);
            int id=scan.nextInt();
            scan.nextLine();


            AuctionDetails auctionDetails = new AuctionDetails();
            System.out.println("Enter the starting price of the product: ");
            double price=scan.nextDouble();
            System.out.println();
            scan.nextLine();
            System.out.println("Enter the duration of the auction in minutes: ");
            int duration=scan.nextInt();
            System.out.println();
            scan.nextLine();
            LocalDateTime startTime=LocalDateTime.now();
            LocalDateTime endTime =startTime.plusMinutes(duration);
            auctionDetails.setProductID(id);
            auctionDetails.setSellerID(userId);
            auctionDetails.setStartTime(startTime);
            auctionDetails.setEndTime(endTime);
            auctionDetails.setCurrentPrice(price);
            auctionDetails.setStartPrice(price);

            createAuction(auctionDetails);
        }
    }

    public void createAuction(AuctionDetails auctionDetails) throws RemoteException, MalformedURLException, NotBoundException {
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
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

    private void endAuction(AuctionDetails auctionDetails) throws RemoteException, MalformedURLException, NotBoundException {
        System.out.println("closing the auction");
        AuctionServer access=(AuctionServer) Naming.lookup("//localhost:8000/Auction");
        Bid temp=access.getWinner(auctionDetails.getAuctionID());
        Transaction transaction=new Transaction(temp.getBidderId(),auctionDetails.getUserId(),auctionDetails.getId(), temp.getBidAmount(), temp.getBidTime());
        access.updateProduct(temp.getBidderId(),auctionDetails.getId());
        access.removeAuction(auctionDetails.getAuctionID());
        access.addTransactions(transaction);

    }
}
