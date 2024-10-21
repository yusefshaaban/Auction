import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements ICalc {

    public Server() {
        super();
     }
  
    public int factorial(int n) {
        int total = 1;
        for (int i = n; i > 0; i--) {
            total *= i;
        }
        System.out.println("client request handled");
        return total;
    }
  
    public static void main(String[] args) {
        try {
            Server s = new Server();
            String name = "myserver";
            ICalc stub = (ICalc) UnicastRemoteObject.exportObject(s, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Server ready");
            } catch (Exception e) {
                System.err.println("Exception:");
                e.printStackTrace();
            }
        }
    }