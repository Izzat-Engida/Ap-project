import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuctionServer extends Remote {
    boolean register(Register user) throws RemoteException;
    boolean login(String email,String password) throws  RemoteException;
}
