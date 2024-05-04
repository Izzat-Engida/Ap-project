
import com.mysql.cj.xdevapi.UpdateStatement;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void removeAccount(String email, String password) throws RemoteException {
        try{
            String query="DELETE from Users Where email= ? and password= ? ";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setString(1,email);
            statement.setString(2,password);
            statement.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> getAllProducts() throws RemoteException {
        ArrayList<Product> prod=new ArrayList<>();
        try{
            String query="Select * from products";
            Statement s=connection.createStatement();
            ResultSet res = s.executeQuery(query);
            while(res.next()){
                Product p=new Product();
                p.setId(res.getInt("ProductID"));
                p.setName(res.getString("Name"));
                p.setDescription(res.getString("Description"));
                p.setCategory(res.getString("Category"));
                p.setCondition(res.getString("Conditionp"));
                prod.add(p);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return prod;
    }

    @Override
    public Product getProductDetails(int productId) throws RemoteException {
        Product product = null;
        try {
            String query = "SELECT * FROM Products p JOIN AuctionDetails a ON p.ProductID = a.ProductID WHERE p.ProductID = ?";
            PreparedStatement s = connection.prepareStatement(query);
            s.setInt(1, productId);
            ResultSet res = s.executeQuery();

            if (res.next()) {
                product = new Product();

                product.setAuctionId(res.getInt("AuctionID"));
                product.setSellerId(res.getInt("SellerID"));
                product.setStartingPrice(res.getDouble("StartingPrice"));
                product.setCurrentPrice(res.getDouble("CurrentPrice"));
                Timestamp startTime = res.getTimestamp("StartTime");
                Timestamp endTime = res.getTimestamp("EndTime");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                product.setStartTime(dateFormat.format(startTime));
                product.setEndTime(dateFormat.format(endTime));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void updateCurrentBid(int auctionId, double currentPrice) throws RemoteException {
        try{String query="Update AuctionDetails SET CurrentPrice=? where AuctionID=? ";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setDouble(1,currentPrice);
        statement.setInt(2,auctionId);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void placeBid(int auctionId, int userId, double bidAmount) throws RemoteException {
        try{String query="Insert Into BiddingActivity(AuctionId,BidderId,BidAmount,BidTime) values(?,?,?,?)";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setInt(1,auctionId);
        statement.setInt(2,userId);
        statement.setDouble(3,bidAmount);
        statement.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
        statement.executeUpdate();
        }
        catch(Exception e){
          e.printStackTrace();
        }

    }
}
