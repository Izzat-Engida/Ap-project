import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface AuctionServer extends Remote {
    boolean register(Register user) throws RemoteException;
    boolean login(String email,String password) throws  RemoteException;
    void removeAccount(String email,String password) throws  RemoteException;
    ArrayList<Product> getAllProducts() throws RemoteException;
    Product getProductDetails(int productId) throws RemoteException;
    void updateCurrentBid(int auctionId,double currentPrice) throws RemoteException;
    void placeBid(int auctionId,int userId,double bidAmount) throws RemoteException;

    ArrayList<Product> getAllProducts(int userId) throws RemoteException;
    ArrayList<Transaction> getTransactions_buyer(int userId) throws RemoteException;
    ArrayList<Transaction> getTransactions_Seller(int userId) throws RemoteException;
    boolean insertNewProduct(int userId, String name, String description, String catagory, String condition) throws RemoteException;
    int userId(String email) throws RemoteException;
    ArrayList<Bid> getBids(int userId) throws RemoteException;
}
