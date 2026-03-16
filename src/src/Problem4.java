
import java.util.*;

public class Problem4 {

    // n-gram → set of document IDs
    private HashMap<String, Set<String>> index = new HashMap<>();

    // document → list of n-grams
    private HashMap<String, List<String>> documentNgrams = new HashMap<>();

    private int N = 5; // size of n-gram

    // Extract n-grams from document text
    public List<String> extractNgrams(String text) {

        List<String> ngrams = new ArrayList<>();

        String[] words = text.toLowerCase().split("\\s+");

        for (int i = 0; i <= words.length - N; i++) {

            StringBuilder sb = new StringBuilder();

            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }

            ngrams.add(sb.toString().trim());
        }

        return ngrams;
    }

    // Add document to database
    public void addDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);

        documentNgrams.put(docId, ngrams);

        for (String gram : ngrams) {

            index.putIfAbsent(gram, new HashSet<>());

            index.get(gram).add(docId);
        }

        System.out.println(docId + " indexed with " + ngrams.size() + " n-grams.");
    }

    // Analyze document for plagiarism
    public void analyzeDocument(String docId, String text) {

        List<String> ngrams = extractNgrams(text);

        System.out.println("\nAnalyzing " + docId);
        System.out.println("Extracted " + ngrams.size() + " n-grams");

        HashMap<String, Integer> matchCount = new HashMap<>();

        for (String gram : ngrams) {

            if (index.containsKey(gram)) {

                for (String existingDoc : index.get(gram)) {

                    matchCount.put(existingDoc,
                            matchCount.getOrDefault(existingDoc, 0) + 1);
                }
            }
        }

        for (Map.Entry<String, Integer> entry : matchCount.entrySet()) {

            String doc = entry.getKey();
            int matches = entry.getValue();

            double similarity = (matches * 100.0) / ngrams.size();

            System.out.println("Found " + matches +
                    " matching n-grams with " + doc);

            System.out.printf("Similarity: %.2f%% ", similarity);

            if (similarity > 50) {
                System.out.println("(PLAGIARISM DETECTED)");
            } else if (similarity > 10) {
                System.out.println("(Suspicious)");
            } else {
                System.out.println("(Low similarity)");
            }
        }
    }

    public static void main(String[] args) {

        Problem4 detector = new Problem4();

        String essay1 = "Artificial intelligence is transforming the world with advanced machine learning techniques";
        String essay2 = "Machine learning techniques are transforming the world of artificial intelligence";
        String essay3 = "Football is a popular sport played by millions of people worldwide";

        detector.addDocument("essay_089.txt", essay1);
        detector.addDocument("essay_092.txt", essay2);

        detector.analyzeDocument("essay_123.txt", essay1 + " with advanced algorithms");
    }
}

