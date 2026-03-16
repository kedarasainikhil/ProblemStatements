import java.util.*;

public class Problem6 {

    // Token bucket class
    static class TokenBucket {

        int maxTokens;
        double tokens;
        double refillRate;
        long lastRefillTime;

        TokenBucket(int maxTokens, double refillRate) {
            this.maxTokens = maxTokens;
            this.tokens = maxTokens;
            this.refillRate = refillRate;
            this.lastRefillTime = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {

            refill();

            if (tokens >= 1) {
                tokens -= 1;
                return true;
            }

            return false;
        }

        void refill() {

            long now = System.currentTimeMillis();

            double tokensToAdd =
                    ((now - lastRefillTime) / 1000.0) * refillRate;

            tokens = Math.min(maxTokens, tokens + tokensToAdd);

            lastRefillTime = now;
        }

        int remainingTokens() {
            return (int) tokens;
        }
    }

    // clientId → TokenBucket
    private HashMap<String, TokenBucket> clients = new HashMap<>();

    private final int LIMIT = 1000;
    private final double REFILL_RATE = 1000.0 / 3600.0;

    // Rate limit check
    public synchronized String checkRateLimit(String clientId) {

        clients.putIfAbsent(clientId,
                new TokenBucket(LIMIT, REFILL_RATE));

        TokenBucket bucket = clients.get(clientId);

        if (bucket.allowRequest()) {

            return "Allowed (" +
                    bucket.remainingTokens() +
                    " requests remaining)";
        }

        long retrySeconds =
                (long) Math.ceil(1 / REFILL_RATE);

        return "Denied (0 requests remaining, retry after "
                + retrySeconds + "s)";
    }

    // Status information
    public String getRateLimitStatus(String clientId) {

        if (!clients.containsKey(clientId)) {
            return "Client not found";
        }

        TokenBucket bucket = clients.get(clientId);

        int used = LIMIT - bucket.remainingTokens();

        long resetTime =
                bucket.lastRefillTime + (3600 * 1000);

        return "{used: " + used +
                ", limit: " + LIMIT +
                ", reset: " + resetTime + "}";
    }

    public static void main(String[] args) {

        Problem6 limiter =
                new Problem6();

        String client = "abc123";

        for (int i = 0; i < 5; i++) {

            System.out.println(
                    limiter.checkRateLimit(client)
            );
        }

        System.out.println(
                limiter.getRateLimitStatus(client)
        );
    }
}