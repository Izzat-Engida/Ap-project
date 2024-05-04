import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class AuctionServerImp extends UnicastRemoteObject implements AuctionServer {
    private Connection connection;

    public AuctionServerImp() throws RemoteException {
        super();
        try {
            // Replace with your database connection details
            String url = "jdbc:mysql://localhost:3306/auction_db";
            String username = "root";///add yours
            String password = "123456";//add your
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean register(Register user) throws RemoteException {
        try {
            String query = "INSERT INTO Users (firstName, lastName, Email, Password, Address, birthDate) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            java.util.Date birthDate=  user.getBirthDate();
            java.sql.Date sqlDate=new java.sql.Date(birthDate.getTime());
            statement.setDate(6,sqlDate);

            statement.setString(5, user.getAddress());
            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean login(String email, String password) throws RemoteException {
        try {
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
