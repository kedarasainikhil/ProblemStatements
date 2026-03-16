
import java.util.*;

class Event {
    String url;
    String userId;
    String source;

    public Event(String url, String userId, String source) {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}

public class Problem5 {

    // pageUrl → visit count
    private HashMap<String, Integer> pageViews = new HashMap<>();

    // pageUrl → unique visitors
    private HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();

    // traffic source → count
    private HashMap<String, Integer> trafficSources = new HashMap<>();


    // Process incoming event
    public void processEvent(Event e) {

        // Count page views
        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);

        // Track unique visitors
        uniqueVisitors.putIfAbsent(e.url, new HashSet<>());
        uniqueVisitors.get(e.url).add(e.userId);

        // Count traffic source
        trafficSources.put(e.source,
                trafficSources.getOrDefault(e.source, 0) + 1);
    }


    // Display dashboard
    public void getDashboard() {

        System.out.println("\nTop Pages:");

        // Sort pages by visit count
        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(pageViews.entrySet());

        list.sort((a, b) -> b.getValue() - a.getValue());

        int rank = 1;

        for (Map.Entry<String, Integer> entry : list) {

            if (rank > 10) break;

            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.get(url).size();

            System.out.println(rank + ". " + url +
                    " - " + views + " views (" +
                    unique + " unique)");

            rank++;
        }


        // Traffic sources
        System.out.println("\nTraffic Sources:");

        int total = 0;
        for (int count : trafficSources.values()) {
            total += count;
        }

        for (Map.Entry<String, Integer> entry : trafficSources.entrySet()) {

            double percent = (entry.getValue() * 100.0) / total;

            System.out.printf("%s: %.2f%%\n",
                    entry.getKey(), percent);
        }
    }


    public static void main(String[] args) {

        Problem5 analytics = new Problem5();

        // Simulated events
        analytics.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        analytics.processEvent(new Event("/sports/championship", "user_777", "google"));
        analytics.processEvent(new Event("/sports/championship", "user_888", "direct"));
        analytics.processEvent(new Event("/sports/championship", "user_999", "google"));
        analytics.processEvent(new Event("/article/breaking-news", "user_123", "google"));

        analytics.getDashboard();
    }
}
