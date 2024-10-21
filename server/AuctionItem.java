public class AuctionItem implements java.io.Serializable {
    public int itemID;
    public String name;
    public String description;
    public int highestBid;

    AuctionItem(int itemID, String name, String description, int highestBid){
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.highestBid = highestBid;
    }
}