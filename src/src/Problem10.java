
import java.util.*;

public class Problem10
{

    static class VideoData {
        String videoId;
        String content;

        VideoData(String id, String content) {
            this.videoId = id;
            this.content = content;
        }
    }

    // L1 Cache (Memory)
    private LinkedHashMap<String, VideoData> L1 =
            new LinkedHashMap<String, VideoData>(10000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 10000;
                }
            };

    // L2 Cache (SSD simulation)
    private LinkedHashMap<String, VideoData> L2 =
            new LinkedHashMap<String, VideoData>(100000, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, VideoData> eldest) {
                    return size() > 100000;
                }
            };

    // L3 Database (all videos)
    private HashMap<String, VideoData> database = new HashMap<>();

    // Access tracking
    private HashMap<String, Integer> accessCount = new HashMap<>();

    // Statistics
    int L1Hits = 0;
    int L2Hits = 0;
    int L3Hits = 0;

    // Simulated database load
    public void loadVideo(String videoId) {
        database.put(videoId, new VideoData(videoId, "VideoContent_" + videoId));
    }

    // Fetch video
    public VideoData getVideo(String videoId) {

        long start = System.nanoTime();

        // L1 lookup
        if (L1.containsKey(videoId)) {

            L1Hits++;
            System.out.println("L1 Cache HIT (0.5ms)");

            return L1.get(videoId);
        }

        System.out.println("L1 Cache MISS");

        // L2 lookup
        if (L2.containsKey(videoId)) {

            L2Hits++;

            System.out.println("L2 Cache HIT (5ms)");

            VideoData data = L2.get(videoId);

            promoteToL1(videoId, data);

            return data;
        }

        System.out.println("L2 Cache MISS");

        // L3 database lookup
        if (database.containsKey(videoId)) {

            L3Hits++;

            System.out.println("L3 Database HIT (150ms)");

            VideoData data = database.get(videoId);

            addToL2(videoId, data);

            return data;
        }

        System.out.println("Video not found");

        return null;
    }

    // Promote video to L1
    private void promoteToL1(String videoId, VideoData data) {

        L1.put(videoId, data);

        int count = accessCount.getOrDefault(videoId, 0) + 1;
        accessCount.put(videoId, count);
    }

    // Add to L2
    private void addToL2(String videoId, VideoData data) {

        L2.put(videoId, data);

        accessCount.put(videoId, 1);
    }

    // Cache invalidation
    public void invalidate(String videoId) {

        L1.remove(videoId);
        L2.remove(videoId);

        System.out.println("Cache invalidated for " + videoId);
    }

    // Statistics
    public void getStatistics() {

        int total = L1Hits + L2Hits + L3Hits;

        double L1Rate = total == 0 ? 0 : (L1Hits * 100.0 / total);
        double L2Rate = total == 0 ? 0 : (L2Hits * 100.0 / total);
        double L3Rate = total == 0 ? 0 : (L3Hits * 100.0 / total);

        System.out.println("\nCache Statistics");

        System.out.println("L1 Hit Rate: " + String.format("%.2f", L1Rate) + "%");
        System.out.println("L2 Hit Rate: " + String.format("%.2f", L2Rate) + "%");
        System.out.println("L3 Hit Rate: " + String.format("%.2f", L3Rate) + "%");

        System.out.println("Overall Hit Rate: " +
                String.format("%.2f", (L1Rate + L2Rate)) + "%");
    }

    public static void main(String[] args) {

        Problem10 cache = new Problem10();

        // load videos into database
        cache.loadVideo("video_123");
        cache.loadVideo("video_999");

        System.out.println("\nRequest 1");
        cache.getVideo("video_123");

        System.out.println("\nRequest 2");
        cache.getVideo("video_123");

        System.out.println("\nRequest 3");
        cache.getVideo("video_999");

        cache.getStatistics();
    }
}
