import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class SearchServer {
    public static void main(String[]args) throws RemoteException, MalformedURLException, AlreadyBoundException {
        AuctionServer a=new AuctionServerImp();
        LocateRegistry.createRegistry(8000);
        Naming.bind("//localhost:8000/Auction",a);
    }
}
