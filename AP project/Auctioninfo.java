import java.sql.Time;
import java.sql.Timestamp;
public class Auctioninfo {
    private int auctionId, productId, sellerId;
    private double startingPrice, currentPrice;
    private Timestamp startTime, endTime;

    public Auctioninfo(int auctionId, int productId, int sellerId, double startingPrice, double currentPrice, Timestamp startTime, Timestamp endTime) {
        this.auctionId = auctionId;
        this.productId = productId;
        this.sellerId = sellerId;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAuctionId() {   return auctionId;  }
    public int getProductId() {   return productId;  }
    public int getSellerId() {    return sellerId;   }
    public double getStartingPrice() {   return startingPrice;  }
    public double getCurrentPrice() {     return currentPrice;   }
    public Timestamp getStartTime() {     return startTime;   }
    public Timestamp getEndTime() {    return endTime;  }

}
