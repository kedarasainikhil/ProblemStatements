import java.util.*;

class TrieNode {
    HashMap<Character, TrieNode> children = new HashMap<>();
    boolean isWord = false;
}

public class Problem7 {

    private TrieNode root = new TrieNode();

    // query → frequency
    private HashMap<String, Integer> frequencyMap = new HashMap<>();


    // Insert query into Trie
    public void insertQuery(String query) {

        TrieNode node = root;

        for (char c : query.toCharArray()) {

            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }

        node.isWord = true;

        frequencyMap.put(query, frequencyMap.getOrDefault(query, 0) + 1);
    }


    // Get node for prefix
    private TrieNode getPrefixNode(String prefix) {

        TrieNode node = root;

        for (char c : prefix.toCharArray()) {

            if (!node.children.containsKey(c)) {
                return null;
            }

            node = node.children.get(c);
        }

        return node;
    }


    // DFS to collect queries
    private void collectQueries(TrieNode node, String prefix, List<String> results) {

        if (node.isWord) {
            results.add(prefix);
        }

        for (Map.Entry<Character, TrieNode> entry : node.children.entrySet()) {

            collectQueries(entry.getValue(),
                    prefix + entry.getKey(),
                    results);
        }
    }


    // Search autocomplete suggestions
    public void search(String prefix) {

        TrieNode node = getPrefixNode(prefix);

        if (node == null) {
            System.out.println("No suggestions found");
            return;
        }

        List<String> results = new ArrayList<>();

        collectQueries(node, prefix, results);

        // Min heap for top 10
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequencyMap.get(a) - frequencyMap.get(b)
        );

        for (String query : results) {

            pq.offer(query);

            if (pq.size() > 10) {
                pq.poll();
            }
        }

        List<String> suggestions = new ArrayList<>();

        while (!pq.isEmpty()) {
            suggestions.add(pq.poll());
        }

        Collections.reverse(suggestions);

        System.out.println("\nSuggestions for \"" + prefix + "\":");

        int rank = 1;

        for (String s : suggestions) {

            System.out.println(rank + ". " + s +
                    " (" + frequencyMap.get(s) + " searches)");

            rank++;
        }
    }


    // Update frequency after new search
    public void updateFrequency(String query) {

        frequencyMap.put(query,
                frequencyMap.getOrDefault(query, 0) + 1);

        System.out.println(query + " → Frequency: " + frequencyMap.get(query));
    }


    public static void main(String[] args) {

        Problem7 system = new Problem7();

        // Insert queries
        system.insertQuery("java tutorial");
        system.insertQuery("javascript");
        system.insertQuery("java download");
        system.insertQuery("java interview questions");
        system.insertQuery("java tutorial");
        system.insertQuery("java tutorial");
        system.insertQuery("java stream api");
        system.insertQuery("java spring boot");
        system.insertQuery("java tutorial for beginners");

        system.search("jav");

        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}