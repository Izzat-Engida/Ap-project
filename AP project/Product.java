import java.io.Serializable;

public class Product implements Serializable {
    private int productId;
    private int ownerId;
    private String name;
    private String description;
    private String category;
    private String condition;
    public Product() {

    }
    public Product(int productId, int ownerId, String name, String description, String category, String condition) {
        this.productId = productId;
        this.ownerId = ownerId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.condition = condition;
    }
    public int getId() {
        return productId;
    }

    public void setId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String toString() {
        return "Product ID: " + productId + "\n" +
                "Name: " + name + "\n" +
                "Description: " + description + "\n" +
                "Category: " + category + "\n" +
                "Condition: " + condition + "\n" ;
    }

private int auctionId,sellerId;
    private double startingPrice,currentPrice;
    public void setAuctionId(int auctionID) {
        this.auctionId=auctionID;
    }
    public int getAuctionId(){
        return auctionId;
    }

    public void setSellerId(int sellerID) {
        this.sellerId=sellerID;
    }
    public int getSellerId(){
        return sellerId;
    }

    public void setStartingPrice(double startingPrice) {
        this.startingPrice=startingPrice;
    }
public double getStartingPrice(){
        return startingPrice;
}
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice=currentPrice;
    }
    public double getCurrentPrice(){
        return currentPrice;
    }
    private String startTime;
    private String endTime;



    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
