import java.io.Serializable;
import java.lang.foreign.SegmentAllocator;
import java.sql.Timestamp;

public class Bid implements Serializable {
    private final int bidId, auctionId, bidderId;
    private final double bidAmount;
    private final Timestamp bidTime;
    public Bid(int bidId, int auctionId, int bidderId, double bidAmount, Timestamp bidTime){
        this.auctionId= auctionId;
        this.bidAmount = bidAmount;
        this.bidderId = bidderId;
        this.bidId = bidId;
        this.bidTime = bidTime;
    }

    public double getBidAmount() {        return bidAmount;    }
    public Timestamp getBidTime() {        return bidTime;    }
    public int getAuctionId() {        return auctionId;    }
    public int getBidderId() {        return bidderId;    }
    public int getBidId() {        return bidId;    }
}

