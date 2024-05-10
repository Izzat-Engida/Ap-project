import java.sql.Timestamp;

public class Transaction {
    private final int transactionId, buyerId, sellerId, productId;
    private final double price;
    private final Timestamp timestamp;
    public Transaction(int transactionId, int buyerId, int sellerId, int productId, double price, Timestamp timestamp){
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.transactionId = transactionId;
        this.price = price;
        this.timestamp = timestamp;
        this.productId = productId;
    }
    public int getProductId(){  return productId;  }
    public double getPrice() {   return price;  }
    public int getBuyerId() {   return buyerId;  }
    public int getSellerId() { return sellerId;  }
    public int getTransactionId() { return transactionId; }
    public Timestamp getDateStamp() {  return timestamp; }

}
