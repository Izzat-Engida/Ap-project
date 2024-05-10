import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private  int transactionId, buyerId, sellerId, productId;
    private  double price;
    private  Timestamp timestamp;
    public Transaction(int transactionId, int buyerId, int sellerId, int productId, double price, Timestamp timestamp){
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.transactionId = transactionId;
        this.price = price;
        this.timestamp = timestamp;
        this.productId = productId;
    }
    public Transaction( int buyerId, int sellerId, int productId, double price, Timestamp timestamp){
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.price = price;
        this.timestamp = timestamp;
        this.productId = productId;
    }
    public int getProductId(){  return productId;  }
    public void setProductId(int productId){this.productId=productId;}
    public double getPrice() {   return price;  }
    public void setPrice(int price){this.price=price;}
    public int getBuyerId() {   return buyerId;  }
    public void setBuyerId(int buyerId){this.buyerId=buyerId;}
    public int getSellerId() { return sellerId;  }
    public void setSellerId(int sellerId){this.sellerId=sellerId;}
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId){this.transactionId=transactionId;}
    public Timestamp getDateStamp() {  return timestamp; }
    public void setTimestamp(Timestamp timestamp){
        this.timestamp=timestamp;
    }

}
