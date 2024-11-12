import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;

public class Client {
    private static final String KEY_FILE = "../keys/testKey.aes";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Client n");
            return;
        }

        int n = Integer.parseInt(args[0]);
        try {
            String name = "Auction";
            Registry registry = LocateRegistry.getRegistry("localhost");
            Auction server = (Auction) registry.lookup(name);

            SealedObject sealedObject = server.getSpec(n);
            AuctionItem auctionItem = (AuctionItem) decrypt(sealedObject);

            if (auctionItem != null) {
                System.out.println("\nAuction item ID is: " + auctionItem.itemID);
                System.out.println("Auction item name is: " + auctionItem.name);
                System.out.println("Auction item description is: " + auctionItem.description);
                System.out.println("Auction item highest bid is: " + auctionItem.highestBid);
            } else {
                System.out.println("Auction item not found.");
            }
        } catch (Exception e) {
            System.err.println("Exception:");
            e.printStackTrace();
        }
    }

    private static AuctionItem decrypt(SealedObject sealedObject) throws Exception {
        // Decrypts a SealedObject and returns it as an AuctionItem

        SecretKey secretKey = loadKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return (AuctionItem) sealedObject.getObject(cipher);
    }

    private static SecretKey loadKey() throws Exception {
        // Returns the key in its original format
        byte[] key;
        try (FileInputStream fis = new FileInputStream(KEY_FILE)) {
            key = fis.readAllBytes();
        }
        return new SecretKeySpec(key, "AES");
    }
}
