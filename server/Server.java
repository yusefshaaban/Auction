import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Auction {

    AuctionItem [] auctionItems = new AuctionItem[5];

    public Server() {
        super();
        AuctionItem auctionItem1 = new AuctionItem(1,"Car","vehicle",4000);
        AuctionItem auctionItem2 = new AuctionItem(2,"Phone","device",500);
        AuctionItem auctionItem3 = new AuctionItem(3,"Hat","clothing",10);
        AuctionItem auctionItem4 = new AuctionItem(4,"TV","Electrics",500);
        AuctionItem auctionItem5 = new AuctionItem(5,"Charger","Electrics",5);
        auctionItems[0] = auctionItem1;
        auctionItems[1] = auctionItem2;
        auctionItems[2] = auctionItem3;
        auctionItems[3] = auctionItem4;
        auctionItems[4] = auctionItem5;
    
     }

  
    public static void main(String[] args) {
        try {
            Server s = new Server();
            String name = "myserver";
            Auction stub = (Auction) UnicastRemoteObject.exportObject(s, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server ready");
            } catch (Exception e) {
                System.err.println("Exception:");
                e.printStackTrace();
            }
        }

    @Override
    public AuctionItem getSpec(int itemID) throws RemoteException {
        for (int i = 0; i<auctionItems.length;i++){
            if (auctionItems[i].itemID == itemID){
                return auctionItems[i];
            }
        }
        return null;
    }
    }