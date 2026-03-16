import java.util.*;

public class Problem1 {

    // username -> userId
    static HashMap<String, Integer> users = new HashMap<>();

    // username -> attempt count
    static HashMap<String, Integer> attempts = new HashMap<>();

    // Check username availability
    public static boolean checkAvailability(String username) {

        // Track attempts
        attempts.put(username, attempts.getOrDefault(username, 0) + 1);

        return !users.containsKey(username);
    }

    // Suggest alternative usernames
    public static List<String> suggestAlternatives(String username) {

        List<String> suggestions = new ArrayList<>();

        suggestions.add(username + "1");
        suggestions.add(username + "2");
        suggestions.add(username.replace("_", "."));

        return suggestions;
    }

    // Get most attempted username
    public static String getMostAttempted() {

        String maxUser = "";
        int max = 0;

        for (String user : attempts.keySet()) {
            if (attempts.get(user) > max) {
                max = attempts.get(user);
                maxUser = user;
            }
        }

        return maxUser + " (" + max + " attempts)";
    }

    public static void main(String[] args) {

        // Existing users
        users.put("john_doe", 1);
        users.put("admin", 2);
        users.put("alex", 3);

        System.out.println("checkAvailability(\"john_doe\") → " + checkAvailability("john_doe"));
        System.out.println("checkAvailability(\"jane_smith\") → " + checkAvailability("jane_smith"));

        System.out.println("suggestAlternatives(\"john_doe\") → " + suggestAlternatives("john_doe"));

        // simulate attempts
        checkAvailability("admin");
        checkAvailability("admin");
        checkAvailability("admin");

        System.out.println("getMostAttempted() → " + getMostAttempted());
    }
}