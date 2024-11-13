import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Auction {
    private int numberOfUsers = 0;
    private int totalItems = 0;
    Map<Integer, String> id_to_email = new HashMap<Integer, String>();
    Map<Integer, ArrayList> userIdToUserAuctionsMap = new HashMap<Integer, ArrayList>();
    Map<Integer, ArrayList> userIdToAllAuctionsMap = new HashMap<Integer, ArrayList>();
    AuctionItem aItem = new AuctionItem();
    ArrayList<AuctionItem> userAuctions = new ArrayList<AuctionItem>();
    ArrayList<AuctionItem> allAuctions = new ArrayList<AuctionItem>();
    
    private static final String KEY_FILE = "../keys/testKey.aes";
    
    public Server() {
        super();
        
        // Initializes AuctionItems
        aItem.itemID = 1;
        aItem.name = "Car";
        aItem.description = "Vehicle";
        aItem.highestBid = 2000;
        userAuctions.add(aItem);

        aItem.itemID = 2;
        aItem.name = "Phone";
        aItem.description = "Electronics";
        aItem.highestBid = 300;
        userAuctions.add(aItem);        
    }

    

    public static void main(String[] args) {
        try {
            Server server = new Server();
            String name = "Auction";
            Auction stub = (Auction) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server ready");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
        
    
    @Override
    public int register(String email) throws RemoteException {
        int userId = -1;
        if (id_to_email.containsValue(email)) {
            System.out.println("Already registered. Logging in to registered account");
            for (Map.Entry<Integer, String> entry : id_to_email.entrySet()) {
                if (entry.getValue().equals(email)) {
                    userId = entry.getKey();
                }
            }
        }
        else {
            numberOfUsers++;
            userId = numberOfUsers;
            id_to_email.put(userId, email);
            userAuctions = new ArrayList<AuctionItem>();
            userIdToUserAuctionsMap.put(userId, userAuctions);
        }

        return userId;
    }



    @Override
    public AuctionItem getSpec(int itemID) throws RemoteException {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpec'");
    }

    
    
    @Override
    public int newAuction(int userID, AuctionSaleItem item) throws RemoteException {
        totalItems++;
        
        aItem.itemID = totalItems;
        aItem.name = item.name;
        aItem.description = item.description;
        aItem.highestBid = item.reservePrice;
        userAuctions = userIdToUserAuctionsMap.get(userID);
        userAuctions.add(aItem);
        allAuctions.add(aItem);

        return totalItems;
    }



    @Override
    public AuctionItem[] listItems() throws RemoteException {
        AuctionItem[] auctionItemsArray = new AuctionItem[allAuctions.size()];
        
        return auctionItemsArray;
    }



    @Override
    public AuctionResult closeAuction(int userID, int itemID) throws RemoteException {
        AuctionResult auctionResult = new AuctionResult();
        auctionResult.winningEmail = id_to_email.get(userID);
        auctionResult.winningPrice = allAuctions.get(itemID).highestBid;
        allAuctions.remove(itemID);
        userAuctions = userIdToUserAuctionsMap.get(userID);
        userAuctions.remove(itemID);
        
        return auctionResult;
    }



    @Override
    public boolean bid(int userID, int itemID, int price) throws RemoteException {
        if (price > allAuctions.get(itemID).highestBid) {
            allAuctions.get(itemID).highestBid = price;
            userAuctions = userIdToUserAuctionsMap.get(userID);
            userAuctions.get(itemID).highestBid = price;
            
            return true;
        }
        else {
            return false;
        }
    }
}
