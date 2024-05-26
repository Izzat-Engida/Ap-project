package AP_project;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;

public class AuctionDetails implements Serializable {
    private int id,userId,auctionId;
    private double currentPrice,startPrice;
    private LocalDateTime startTime,endTIme;
    public void setProductID(int id) {
        this.id=id;
    }
    public int getId(){
        return id;
    }

    public void setSellerID(int userId) {      this.userId=userId;    }
    public int getUserId(){    return userId;   }
    public void setEndTime(LocalDateTime endtime) {      this.endTIme=endtime;   }
    public Timestamp getEndTIme(){   return Timestamp.valueOf(endTIme); }
    public void setCurrentPrice(double price) {     this.currentPrice=price;   }
    public double getCurrentPrice(){    return currentPrice;   }
    public void setStartPrice(double price) {    this.startPrice=price;   }
    public double getStartPrice(){      return startPrice;      }
    public void setStartTime(LocalDateTime startTime) {       this.startTime=startTime;     }
    public Timestamp getStartTime(){    return Timestamp.valueOf(startTime);    }
    public Temporal getEndTime() {       return endTIme;    }
    public int getAuctionID() {     return auctionId;   }
    public void setAuctionId(int auctionId){   this.auctionId=auctionId;   }
    @Override
    public String toString() {
        return "AuctionDetails [productID=" + id + ", sellerID=" + userId
         + ", startTime=" + startTime
                + ", endTime=" + getEndTIme() + ", currentPrice=" + currentPrice + ", startPrice=" + startPrice + "]";
    }

}
