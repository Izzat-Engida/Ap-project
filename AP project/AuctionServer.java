import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface AuctionServer extends Remote {

    boolean register(Register user) throws RemoteException;
    boolean login(String email,String password) throws  RemoteException;
    boolean removeAccount(int userId,String password) throws  RemoteException;
    ArrayList<Product> getAllProducts() throws RemoteException;
    AuctionDetails getProductDetails(int productId) throws RemoteException;
    void updateCurrentBid(int auctionId,double currentPrice) throws RemoteException;
    void placeBid(int auctionId,int userId,double bidAmount) throws RemoteException;

    ArrayList<Product> getAllProducts(int userId) throws RemoteException;
    ArrayList<Transaction> getTransactions_buyer(int userId) throws RemoteException;
    ArrayList<Transaction> getTransactions_Seller(int userId) throws RemoteException;
    boolean insertNewProduct(int userId, String name, String description, String catagory, String condition) throws RemoteException;
    int userId(String email) throws RemoteException;
    ArrayList<Bid> getBids(int userId) throws RemoteException;

    void removeAuction(int auctionID) throws RemoteException;

    int insertAuction(AuctionDetails auctionDetails) throws RemoteException;
    void updateProduct(int userid,int productID) throws RemoteException;
    Bid getWinner(int auctionID)throws RemoteException;
    void addTransactions(Transaction transaction) throws RemoteException;
    boolean checkTheOwner(int userId, int productId) throws RemoteException;
}
