import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;

public class Server implements Auction {
    AuctionItem[] auctionItems = new AuctionItem[20];
    private static final String KEY_FILE = "../keys/testKey.aes";
    
    public Server() {
        super();
        
        // Initializes AuctionItems
        auctionItems[0] = new AuctionItem();
        auctionItems[0].itemID = 1;
        auctionItems[0].name = "Car";
        auctionItems[0].description = "Vehicle";
        auctionItems[0].highestBid = 4000;
        
        auctionItems[1] = new AuctionItem();
        auctionItems[1].itemID = 2;
        auctionItems[1].name = "Phone";
        auctionItems[1].description = "Device";
        auctionItems[1].highestBid = 500;
        
        auctionItems[2] = new AuctionItem();
        auctionItems[2].itemID = 3;
        auctionItems[2].name = "Hat";
        auctionItems[2].description = "Clothing";
        auctionItems[2].highestBid = 10;
        
        auctionItems[3] = new AuctionItem();
        auctionItems[3].itemID = 4;
        auctionItems[3].name = "TV";
        auctionItems[3].description = "Electronics";
        auctionItems[3].highestBid = 500;
        
        auctionItems[4] = new AuctionItem();
        auctionItems[4].itemID = 5;
        auctionItems[4].name = "Charger";
        auctionItems[4].description = "Electronics";
        auctionItems[4].highestBid = 5;
        
        auctionItems[5] = new AuctionItem();
        auctionItems[5].itemID = 6;
        auctionItems[5].name = "Laptop";
        auctionItems[5].description = "Electronics";
        auctionItems[5].highestBid = 1000;
        
        auctionItems[6] = new AuctionItem();
        auctionItems[6].itemID = 7;
        auctionItems[6].name = "Bicycle";
        auctionItems[6].description = "Vehicle";
        auctionItems[6].highestBid = 250;
        
        auctionItems[7] = new AuctionItem();
        auctionItems[7].itemID = 8;
        auctionItems[7].name = "Watch";
        auctionItems[7].description = "Accessory";
        auctionItems[7].highestBid = 80;
        
        auctionItems[8] = new AuctionItem();
        auctionItems[8].itemID = 9;
        auctionItems[8].name = "Refrigerator";
        auctionItems[8].description = "Appliance";
        auctionItems[8].highestBid = 350;
        
        auctionItems[9] = new AuctionItem();
        auctionItems[9].itemID = 10;
        auctionItems[9].name = "Painting";
        auctionItems[9].description = "Artwork";
        auctionItems[9].highestBid = 1500;
        
        auctionItems[10] = new AuctionItem();
        auctionItems[10].itemID = 11;
        auctionItems[10].name = "Sofa";
        auctionItems[10].description = "Furniture";
        auctionItems[10].highestBid = 2300;
        
        auctionItems[11] = new AuctionItem();
        auctionItems[11].itemID = 12;
        auctionItems[11].name = "Speakers";
        auctionItems[11].description = "Audio Equipment";
        auctionItems[11].highestBid = 200;
        
        auctionItems[12] = new AuctionItem();
        auctionItems[12].itemID = 13;
        auctionItems[12].name = "Bicycle Helmet";
        auctionItems[12].description = "Safety Gear";
        auctionItems[12].highestBid = 25;
        
        auctionItems[13] = new AuctionItem();
        auctionItems[13].itemID = 14;
        auctionItems[13].name = "Table";
        auctionItems[13].description = "Furniture";
        auctionItems[13].highestBid = 600;
        
        auctionItems[14] = new AuctionItem();
        auctionItems[14].itemID = 15;
        auctionItems[14].name = "Glasses";
        auctionItems[14].description = "Wearable Tech";
        auctionItems[14].highestBid = 150;
        
        auctionItems[15] = new AuctionItem();
        auctionItems[15].itemID = 16;
        auctionItems[15].name = "Radio";
        auctionItems[15].description = "Electronics";
        auctionItems[15].highestBid = 100;
        
        auctionItems[16] = new AuctionItem();
        auctionItems[16].itemID = 17;
        auctionItems[16].name = "Camera";
        auctionItems[16].description = "Electronics";
        auctionItems[16].highestBid = 250;
        
        auctionItems[17] = new AuctionItem();
        auctionItems[17].itemID = 18;
        auctionItems[17].name = "Blender";
        auctionItems[17].description = "Appliance";
        auctionItems[17].highestBid = 120;
        
        auctionItems[18] = new AuctionItem();
        auctionItems[18].itemID = 19;
        auctionItems[18].name = "Bag";
        auctionItems[18].description = "Accessory";
        auctionItems[18].highestBid = 25;
        
        auctionItems[19] = new AuctionItem();
        auctionItems[19].itemID = 20;
        auctionItems[19].name = "Drone";
        auctionItems[19].description = "Technology";
        auctionItems[19].highestBid = 900;
        
    }
    

    @Override
    public SealedObject getSpec(int itemID) throws RemoteException {
        // Encryptes an AuctionItem and returns it as a SealedObject

        try {
            // Generates a key and store it
            generateKey();
            SecretKey secretKey = loadKey();

            // Finds the AuctionItem using its ID
            AuctionItem auctionItem = null;
            for (AuctionItem item : auctionItems) {
                if (item.itemID == itemID) {
                    auctionItem = item;
                    break;
                }
            }

            // Encrypts the AuctionItem
            if (auctionItem != null) {
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                SealedObject sealedObject = new SealedObject(auctionItem, cipher);
                return sealedObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void generateKey() throws Exception {
        // Generates a key and stores in binary format
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128, new SecureRandom());
        SecretKey secretKey = keyGen.generateKey();
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)){
            fos.write(secretKey.getEncoded());
        }
    }

    public static SecretKey loadKey() throws Exception {
        // Returns the key in its original format
        byte[] key;
        try (FileInputStream fis = new FileInputStream(KEY_FILE)) {
            key = fis.readAllBytes();
        }
        return new SecretKeySpec(key, "AES");
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
}
