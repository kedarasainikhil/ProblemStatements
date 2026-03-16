
import java.util.*;

class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class Problem3 {

    private int capacity;

    // LinkedHashMap used for LRU eviction
    private LinkedHashMap<String, DNSEntry> cache;

    private int hits = 0;
    private int misses = 0;

    public Problem3(int capacity) {
        this.capacity = capacity;

        cache = new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > Problem3.this.capacity;
            }
        };
    }

    // Resolve domain
    public synchronized String resolve(String domain) {

        if (cache.containsKey(domain)) {

            DNSEntry entry = cache.get(domain);

            if (!entry.isExpired()) {
                hits++;
                System.out.println(domain + " → Cache HIT → " + entry.ipAddress);
                return entry.ipAddress;
            }

            else {
                cache.remove(domain);
                System.out.println(domain + " → Cache EXPIRED");
            }
        }

        misses++;

        // Simulate upstream DNS lookup
        String ip = queryUpstreamDNS(domain);

        DNSEntry entry = new DNSEntry(domain, ip, 5); // TTL = 5 seconds
        cache.put(domain, entry);

        System.out.println(domain + " → Cache MISS → " + ip);

        return ip;
    }

    // Simulate upstream DNS query
    private String queryUpstreamDNS(String domain) {

        Random r = new Random();

        return "172.217." + r.nextInt(255) + "." + r.nextInt(255);
    }

    // Remove expired entries
    public void cleanExpiredEntries() {

        Iterator<Map.Entry<String, DNSEntry>> iterator = cache.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, DNSEntry> entry = iterator.next();

            if (entry.getValue().isExpired()) {
                iterator.remove();
            }
        }
    }

    // Cache statistics
    public void getCacheStats() {

        int total = hits + misses;

        double hitRate = total == 0 ? 0 : (hits * 100.0) / total;

        System.out.println("\nCache Stats:");
        System.out.println("Hits: " + hits);
        System.out.println("Misses: " + misses);
        System.out.println("Hit Rate: " + String.format("%.2f", hitRate) + "%");
    }

    public static void main(String[] args) throws InterruptedException {

        Problem3 dnsCache = new Problem3(5);

        dnsCache.resolve("google.com");
        dnsCache.resolve("google.com");
        dnsCache.resolve("amazon.com");

        Thread.sleep(6000); // wait for TTL expiration

        dnsCache.resolve("google.com");

        dnsCache.getCacheStats();
    }
}

