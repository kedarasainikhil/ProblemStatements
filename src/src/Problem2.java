import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class Problem2 {

    // productId -> stockCount
    private HashMap<String, Integer> inventory = new HashMap<>();

    // productId -> waiting list (FIFO)
    private HashMap<String, LinkedHashMap<Integer, Integer>> waitingList = new HashMap<>();

    // Initialize product stock
    public void addProduct(String productId, int stock) {
        inventory.put(productId, stock);
        waitingList.put(productId, new LinkedHashMap<>());
    }

    // Check stock availability
    public synchronized int checkStock(String productId) {

        if (!inventory.containsKey(productId)) {
            return -1;
        }

        return inventory.get(productId);
    }

    // Purchase item
    public synchronized String purchaseItem(String productId, int userId) {

        if (!inventory.containsKey(productId)) {
            return "Product not found";
        }

        int stock = inventory.get(productId);

        if (stock > 0) {

            stock--;
            inventory.put(productId, stock);

            return "Success, " + stock + " units remaining";

        } else {

            LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

            int position = queue.size() + 1;

            queue.put(userId, position);

            return "Added to waiting list, position #" + position;
        }
    }

    // Get waiting list
    public void showWaitingList(String productId) {

        LinkedHashMap<Integer, Integer> queue = waitingList.get(productId);

        for (Map.Entry<Integer, Integer> entry : queue.entrySet()) {
            System.out.println("User " + entry.getKey() + " -> position " + entry.getValue());
        }
    }

    // Main method to simulate flash sale
    public static void main(String[] args) {

        Problem2 system = new Problem2();

        system.addProduct("IPHONE15_256GB", 100);

        System.out.println("Stock: " + system.checkStock("IPHONE15_256GB") + " units available");

        // Simulate purchases
        for (int i = 1; i <= 105; i++) {

            String result = system.purchaseItem("IPHONE15_256GB", 10000 + i);

            System.out.println("User " + (10000 + i) + " -> " + result);
        }

        System.out.println("\nWaiting List:");
        system.showWaitingList("IPHONE15_256GB");
    }
}