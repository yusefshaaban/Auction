import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class Client {
    static Scanner scanner = new Scanner(System.in);
    static Auction server;
    static int userID;
    
    public static void main(String[] args) {
        try {
            String name = "Auction";
            Registry registry = LocateRegistry.getRegistry("localhost");
            Auction server = (Auction) registry.lookup(name);

            // AuctionItem auctionItem = server.getSpec(1);

            // if (auctionItem != null) {
            //     System.out.println("Item ID: " + auctionItem.itemID);
            // }
            // else {
            //     System.out.println("Auction item not found.");
            // }

            System.out.println("Welcome to the auction! Please enter your email address to continue:");
            String email = scanner.nextLine();
            userID = server.register(email);
            
            AuctionSaleItem item = new AuctionSaleItem();
            item.name = "Ipod";
            item.description = "Apple Ipod";
            item.reservePrice = 100;
            int auctionID = server.newAuction(userID, item);
            displayMenu();
            
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }

    static void displayMenu() throws RemoteException {
        System.out.println("\nMenu\nPlease choose one of the following options:");
        System.out.println("1.List items      2.Bid      3.Start a new auction      4.Close an auction      5.logout      6.exit");

        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice == 1) {
            listItems();
        }
        else if (choice == 2) {
           bid();
        }
        else if (choice == 3) {
            startNewAuction();
        }
        else if (choice == 4) {
            closeAuction();
        }
        else if (choice == 5) {
            
        }
        else if (choice == 6) {
            System.exit(0);
        }
        else {
            System.out.println("Invalid choice. Please try again.");
            displayMenu();
        }
        
    }

    static void listItems() throws RemoteException {
        // List items
        System.out.println("userID: " + userID);
        System.out.println("server: " + server);
        System.out.println("Items available for auction:");
        AuctionItem[] items = server.listItems();
        if (items == null || items.length == 0) {
            System.out.println("No items available.");
        } else {
            for (AuctionItem item : items) {
                if (item != null) {
                    System.out.println("Item ID: " + item.itemID);
                    System.out.println("Name: " + item.name);
                    System.out.println("Description: " + item.description);
                    System.out.println("Highest bid: " + item.highestBid);
                } else {
                    System.out.println("Warning: Encountered a null item in the list.");
                }
            }
        }
        displayMenu();
    }

    static void bid() throws RemoteException {
        // Bid
        System.out.println("Please enter the item ID  of the item that you want to bid on:");
        int itemID = scanner.nextInt();
        System.out.println("Please enter the amount you want to bid:");
        int price = scanner.nextInt();
        boolean result = server.bid(userID, itemID, price);
        if (result) {
            System.out.println("Bid successful.");
        }
        else {
            System.out.println("Bid unsuccessful.");
        }
        displayMenu();
    }

    static void startNewAuction() throws RemoteException {
        // Start a new auction
        AuctionSaleItem item = new AuctionSaleItem();
        System.out.println(item);
        System.out.println(item.name);
        System.out.println("\nPlease enter the name of the item you want to list:");
        item.name = scanner.nextLine();
        System.out.println("Please enter the description of the item of the item you want to list:");
        item.description = scanner.nextLine();
        System.out.println("Please enter the reserve price of the item of the description you want to list:");
        item.reservePrice = scanner.nextInt();

        int auctionID = server.newAuction(userID, item);
        System.out.println("Auction created");
        displayMenu();
    }

    static void closeAuction() throws RemoteException {
        // Close an auction
        System.out.println("Please enter the item ID of the item that you want to close:");
        int itemID = scanner.nextInt();
        AuctionResult result = server.closeAuction(userID, itemID);
        System.out.println("The winning email is: " + result.winningEmail);
        System.out.println("The winning price is: " + result.winningPrice);
        displayMenu();
    }
}
