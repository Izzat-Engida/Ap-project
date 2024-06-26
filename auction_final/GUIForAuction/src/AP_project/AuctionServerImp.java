package AP_project;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.mysql.cj.xdevapi.SqlUpdateResult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AuctionServerImp extends UnicastRemoteObject implements AuctionServer {
    private Connection connection;

    public AuctionServerImp() throws RemoteException {
        super(); String pass = "St_Gabriel_19";
        try {
            // Replace with your database connection details
            String url = "jdbc:mysql://localhost:3310/auction_db";
            String username = "root";///add yours
            String password = pass;//add your
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean register(Register user) throws RemoteException {
        try {
            String query = "INSERT INTO Users (firstName, lastName, Email, Password, Address, birthDate, RegistrationDate ) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPassword());
            java.util.Date birthDate=  user.getBirthDate();
            Date sqlDate=new Date(birthDate.getTime());
            statement.setDate(6,sqlDate);
            statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

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
    public boolean removeAccount(int userId, String password) throws RemoteException {
        try{
            String query="DELETE from Users Where UserID = ? and password= ? ";
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,userId);
            statement.setString(2,password);
            int row = statement.executeUpdate();
            return row > 0;
        }
        catch (SQLException e){
            System.out.println("----------- Can't delete in removeAccount -----------");
            e.printStackTrace();
        }
        return false;

    }
    @Override
    public ArrayList<Product> getAllProducts(int userId) throws RemoteException {
        String query = "SELECT * FROM products WHERE OwnerID = ?";
        ArrayList<Product> listOfProds = new ArrayList<>();
        try {
            PreparedStatement pStm = connection.prepareStatement(query);
            pStm.setInt(1, userId);
            ResultSet resultSet = pStm.executeQuery();
            int ownerId, prodId;
            String name, description, category, condition;

            while(resultSet.next()){
                ownerId = resultSet.getInt("OwnerID");
                prodId = resultSet.getInt("ProductID" );
                name = resultSet.getString("Name" );
                description = resultSet.getString("Description" );
                category = resultSet.getString("Category" );
                condition = resultSet.getString("Conditionp" );
                listOfProds.add(new Product(prodId, ownerId, name, description, category, condition));

            }
            return listOfProds;
        }catch (SQLException e){
            System.out.println("------------ Exception has occurred in getAllProduct with userId as a Parameter it ----------------");
            e.printStackTrace();
            return listOfProds;
        }
    }

    @Override
    public ArrayList<Product> getAllProducts() throws RemoteException {
        ArrayList<Product> prod=new ArrayList<>();
        try{
            String query="Select * from products INNER JOIN AuctionDetails ON products.ProductId=AuctionDetails.ProductId";
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
    public AuctionDetails getProductDetails(int productId) throws RemoteException {
        AuctionDetails product = null;
        try {
            String query = "SELECT * FROM AuctionDetails  WHERE ProductId=?";
            PreparedStatement s = connection.prepareStatement(query);
            s.setInt(1, productId);
            ResultSet res = s.executeQuery();

            if (res.next()) {
                product = new AuctionDetails();
                product.setProductID(res.getInt("ProductID"));
                product.setAuctionId(res.getInt("AuctionID"));
                product.setSellerID(res.getInt("SellerID"));
                product.setStartPrice(res.getDouble("StartingPrice"));
                product.setCurrentPrice(res.getDouble("CurrentPrice"));
                Timestamp startTime = res.getTimestamp("StartTime");
                Timestamp endTime = res.getTimestamp("EndTime");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                product.setStartTime(startTime.toLocalDateTime());
                product.setEndTime(endTime.toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public ArrayList<Product> getProductDetailsFromBidByBidder(int userId) throws RemoteException {
        ArrayList<Product> prod=new ArrayList<>();
        String query= "SELECT products.ProductId, Name, Description, Category, Conditionp " +
                "FROM products " +
                "INNER JOIN auctiondetails ON products.ProductID = auctiondetails.ProductID " +
                "INNER JOIN biddingactivity ON auctiondetails.AuctionID = biddingactivity.AuctionID " +
                "WHERE biddingactivity.BidderID = ? ";
        try{
            PreparedStatement stm =connection.prepareStatement(query);
            stm.setInt(1, userId);
            ResultSet res = stm.executeQuery();
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
    public void updateCurrentBid(int auctionId, double currentPrice) throws RemoteException {
        try{String query="Update AuctionDetails SET CurrentPrice=? where AuctionID=? ";
        PreparedStatement statement=connection.prepareStatement(query);
        statement.setDouble(1,currentPrice);
        statement.setInt(2,auctionId);
        statement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void placeBid(int auctionId, int userId, double bidAmount) throws RemoteException {
        String query="INSERT INTO BiddingActivity(AuctionId,BidderId,BidAmount,BidTime) Values(?,?,?,?)";
        try {
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,auctionId);
            statement.setInt(2,userId);
            statement.setDouble(3,bidAmount);
            statement.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();
        }catch(Exception e){
          e.printStackTrace();
            System.out.println("Can't save the Bid in the BiddingActivity");
        }

    }

    @Override
    public ArrayList<Transaction> getTransactions_buyer(int userId) throws RemoteException {
        ArrayList<Transaction> transArrayList = new ArrayList<>();
        String query = "SELECT * FROM TransactionActivity WHERE BuyerID = ?";
        try{
            PreparedStatement pStmn = connection.prepareStatement(query);
            pStmn.setInt(1,userId);
            ResultSet resultSet = pStmn.executeQuery();
            int transId, prodId, sellerId;
            double price;
            Timestamp timestamp;
            while(resultSet.next()){
                transId = resultSet.getInt("TransactionID");
                prodId = resultSet.getInt("ProductID");
                sellerId = resultSet.getInt("SellerID");
                timestamp = resultSet.getTimestamp("Datestamp");
                price = resultSet.getDouble("Price");
                transArrayList.add(new Transaction(transId, userId, sellerId, prodId, price, timestamp));
            }

        }
        catch (SQLException e){
            System.out.println("------- Exception in getTransaction_buyer --------");
            e.printStackTrace();
        }
        return transArrayList;
    }


    @Override
    public ArrayList<Transaction> getTransactions_Seller(int userId) throws RemoteException {
        ArrayList<Transaction> transArrayList = new ArrayList<>();
        String query = "SELECT * FROM TransactionActivity WHERE SellerID = ?";
        try{
            PreparedStatement pStmn = connection.prepareStatement(query);
            pStmn.setInt(1,userId);
            ResultSet resultSet = pStmn.executeQuery();
            int transId, prodId, buyerId;
            double price;
            Timestamp timestamp;
            while(resultSet.next()){
                transId = resultSet.getInt("TransactionID");
                prodId = resultSet.getInt("ProductID");
                buyerId = resultSet.getInt("BuyerID");
                timestamp = resultSet.getTimestamp("Datestamp");
                price = resultSet.getDouble("Price");
                transArrayList.add(new Transaction(transId, buyerId, userId, prodId, price, timestamp));
            }

        }
        catch (SQLException e){
            System.out.println("------- Exception in getTransaction_buyer --------");
            e.printStackTrace();
        }
        return transArrayList;
    }


    @Override
    public boolean insertNewProduct(int userId, String name, String description, String catagory, String condition) throws RemoteException {
        String query = "INSERT INTO Products(OwnerID, Name, Description, Category, Conditionp) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement pStm = connection.prepareStatement(query);
            pStm.setInt(1, userId);
            pStm.setString(2, name);
            pStm.setString(3, description);
            pStm.setString(4, catagory);
            pStm.setString(5, condition);

            int rowInserted = pStm.executeUpdate();
            return rowInserted > 0;
        }
        catch (SQLException e){
            System.out.println("----------- Exception in insertNewProduct --------------");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int userId(String email){
        String query = "SELECT UserID  FROM Users WHERE Email = ? LIMIT 1";
        try {
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setString(1, email);
            ResultSet res = stm.executeQuery();
            while(res.next()){
                return res.getInt(1);
            }
            System.out.println("Returning empty string because of some faults");

        }
        catch (SQLException e){
            System.out.println("Error in userId Method Implementation of AucitonServerImp\n");
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public ArrayList<Bid> getBids(int userId) throws RemoteException {
        String query = "SELECT * FROM BiddingActivity WHERE BidderID = ?";
        ArrayList<Bid> bidArrayList = new ArrayList<>();
        try {
            PreparedStatement pStm = connection.prepareStatement(query);
            pStm.setInt(1, userId);
            ResultSet resultSet = pStm.executeQuery();
            while(resultSet.next()){
                int bidId = resultSet.getInt("BidID") ;
                int aucId = resultSet.getInt("AuctionID");
                int bidderId = resultSet.getInt("BidderID");
                double bidAmount = resultSet.getDouble("BidAmount");
                Timestamp bidTime = resultSet.getTimestamp("BidTime");
                Bid newRow = new Bid(
                        bidId, aucId, bidderId,
                        bidAmount, bidTime
                );
                bidArrayList.add(newRow);
            }
        }
        catch (SQLException e){
            System.out.println("----------------Exception in getBids method after AuctionServerImp----------------\n");
            e.printStackTrace();

        }
        return bidArrayList;
    }

    @Override
    public void removeAuction(int auctionID) throws RemoteException {
        String query="DELETE from AuctionDetails WHERE AuctionID=?";
        try{
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,auctionID);
            statement.executeUpdate();
        }
        catch(Exception e){
            System.out.println("-------------Error: Auction element can't be Removed properly-------------");
            e.printStackTrace();
        }
    }

    @Override
    public boolean modifyAuction(int auctionId, double currPrice) throws RemoteException {
        String query = "UPDATE auctiondetails SET CurrentPrice = ? WHERE AuctionID = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1,currPrice);
            preparedStatement.setInt(2, auctionId);

            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error In AuctionServerImp");
            return false;
        }

    }

    @Override
    public boolean deleteBidActivity(int auctionId) throws RemoteException {
        String query2="DELETE From BiddingActivity Where AuctionID=?";
        PreparedStatement statement2= null;
        try {
            statement2 = connection.prepareStatement(query2);
            statement2.setInt(1,auctionId);
            return statement2.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error Happened while Deleting Bid Activity in AuctionImp");
        }


        return false;
    }

    @Override
    public int insertAuction(AuctionDetails auctionDetails) throws RemoteException {
        String query="INSERT INTO AuctionDetails(ProductID,SellerID,StartingPrice,CurrentPrice,StartTime,EndTime) VALUES(?,?,?,?,?,?) ";
        int auctionId=-1;
        try{
            PreparedStatement statement=connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1,auctionDetails.getId());
            statement.setInt(2,auctionDetails.getUserId());
            statement.setDouble(3,auctionDetails.getStartPrice());
            statement.setDouble(4,auctionDetails.getCurrentPrice());
            statement.setTimestamp(5,auctionDetails.getStartTime());
            statement.setTimestamp(6,auctionDetails.getEndTIme());
            statement.executeUpdate();
            ResultSet rs=statement.getGeneratedKeys();
            if(rs.next()){
                auctionId=rs.getInt(1);
            }
        }catch (SQLException e){
        e.printStackTrace();
        }
        return auctionId;
    }
 @Override
     public void updateProduct(int newOwnerId,int productID) throws RemoteException{
            String query="UPDATE Products SET OwnerID=? Where ProductId=?";
         try{
             PreparedStatement statement=connection.prepareStatement(query);
             statement.setInt(1,newOwnerId);
             statement.setInt(2,productID);
             statement.executeUpdate();
         }catch (Exception e){

             e.printStackTrace();
         }
     }


    @Override
    public Bid getWinner(int auctionID) throws RemoteException {
        String query="Select * from BiddingActivity where AuctionID=? ORDER BY BidAmount DESC LIMIT 1";
        try{
            PreparedStatement statement1=connection.prepareStatement(query);

            statement1.setInt(1,auctionID);

            ResultSet resultSet=statement1.executeQuery();
            if(resultSet.next()) {
                int bidId = resultSet.getInt("BidID");
                int aucId = resultSet.getInt("AuctionID");
                int bidderId = resultSet.getInt("BidderID");
                double bidAmount = resultSet.getDouble("BidAmount");
                Timestamp bidTime = resultSet.getTimestamp("BidTime");
                Bid newRow = new Bid(
                        bidId, aucId, bidderId,
                        bidAmount, bidTime
                );


                return newRow;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void addTransactions(Transaction transaction) throws RemoteException {
        String query="INSERT INTO TransactionActivity(ProductID,BuyerId,SellerID,DateStamp,Price) VALUES(?,?,?,?,?)";
        try{
            PreparedStatement statement=connection.prepareStatement(query);
            statement.setInt(1,transaction.getProductId());
            statement.setInt(2,transaction.getBuyerId());
            statement.setInt(3,transaction.getSellerId());
            statement.setTimestamp(4,transaction.getDateStamp());
            statement.setDouble(5,transaction.getPrice());
            statement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkTheOwner(int userId, int productId) throws RemoteException {
        String query = "SELECT * FROM Products WHERE ProductID = ? AND OwnerID = ?";
        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, productId);
            stm.setInt(2, userId);
            ResultSet resultSet = stm.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteBiddingInfo(int userId, int productId) throws RemoteException {
        String query = "DELETE FROM BiddingActivity WHERE BidderId = ? AND AuctionID = ?";
        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, userId);
            stm.setInt(2, productId);
            return stm.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error thrown While Deleting the Information of a Product and it's Bidder");
        }
        return false;
    }

    @Override
    public Register getUserInfo(int userId) throws RemoteException {
        String query = "SELECT * FROM Users WHERE UserId = ?";

        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1,userId);
            ResultSet resultSet = stm.executeQuery();
            ResultSetMetaData meta = resultSet.getMetaData();
            if(resultSet.next()) {
                Register aPerson = new Register(
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("Email"),
                        resultSet.getString("Password"),
                        resultSet.getDate("birthDate"),
                        resultSet.getString("Address")
                );
                aPerson.setUserId(userId);

                return aPerson;
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error Happened while getting UserInfo");
        }


        return null;
    }

    @Override
    public Product getAProduct(int productId) throws RemoteException {
        String query = "SELECT * FROM products WHERE ProductID = ? LIMIT 1";
        Product aProd = null;
        try{
            PreparedStatement stm = connection.prepareStatement(query);
            stm.setInt(1, productId);
            ResultSet resultSet = stm.executeQuery();
            if(resultSet.next()) {
                aProd = new Product(
                        resultSet.getInt("ProductID"),
                        resultSet.getInt("OwnerID"),
                        resultSet.getString("Name"),
                        resultSet.getString("Description"),
                        resultSet.getString("Category"),
                        resultSet.getString("Conditionp")

                );
                return aProd;
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("while Fetching Product info from Product table in AuctionServerImp");
        }


        return null;
    }

}
