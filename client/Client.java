import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client{
     public static void main(String[] args) {
       if (args.length < 1) {
       System.out.println("Usage: java Client n");
       return;
       }

         int n = Integer.parseInt(args[0]);
         try {
               String name = "myserver";
               Registry registry = LocateRegistry.getRegistry("localhost");
               Auction server = (Auction) registry.lookup(name);
  
                AuctionItem auctionItem = server.getSpec(n);
                System.out.println("Auction item ID is: " + auctionItem.itemID);
                System.out.println("Auction item name is: " + auctionItem.name);
                System.out.println("Auction item desctiption is: " + auctionItem.description);
                System.out.println("Auction item highest bid is: " + auctionItem.highestBid);

              }
              catch (Exception e) {
               System.err.println("Exception:");
               e.printStackTrace();
               }
      }
}
