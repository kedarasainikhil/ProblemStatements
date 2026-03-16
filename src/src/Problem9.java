import java.util.*;

public class Problem9 {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    // Classic Two-Sum
    public void findTwoSum(int target) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                System.out.println(
                        "TwoSum Pair → (" + other.id + ", " + t.id + ")"
                );
            }

            map.put(t.amount, t);
        }
    }

    // Two-Sum within time window
    public void findTwoSumTimeWindow(int target, long windowMillis) {

        HashMap<Integer, Transaction> map = new HashMap<>();

        for (Transaction t : transactions) {

            int complement = target - t.amount;

            if (map.containsKey(complement)) {

                Transaction other = map.get(complement);

                if (Math.abs(t.time - other.time) <= windowMillis) {

                    System.out.println(
                            "TimeWindow Pair → (" + other.id + ", " + t.id + ")"
                    );
                }
            }

            map.put(t.amount, t);
        }
    }

    // K-Sum using recursion
    public void findKSum(int k, int target) {

        List<Integer> nums = new ArrayList<>();

        for (Transaction t : transactions) {
            nums.add(t.amount);
        }

        List<List<Integer>> result = kSum(nums, target, k, 0);

        for (List<Integer> r : result) {
            System.out.println("KSum Combination → " + r);
        }
    }

    private List<List<Integer>> kSum(List<Integer> nums, int target, int k, int start) {

        List<List<Integer>> result = new ArrayList<>();

        if (k == 2) {

            HashMap<Integer, Integer> map = new HashMap<>();

            for (int i = start; i < nums.size(); i++) {

                int num = nums.get(i);
                int complement = target - num;

                if (map.containsKey(complement)) {

                    result.add(Arrays.asList(num, complement));
                }

                map.put(num, i);
            }

            return result;
        }

        for (int i = start; i < nums.size(); i++) {

            List<List<Integer>> subsets =
                    kSum(nums, target - nums.get(i), k - 1, i + 1);

            for (List<Integer> subset : subsets) {

                List<Integer> combination = new ArrayList<>();
                combination.add(nums.get(i));
                combination.addAll(subset);

                result.add(combination);
            }
        }

        return result;
    }

    // Duplicate detection
    public void detectDuplicates() {

        HashMap<String, List<Transaction>> map = new HashMap<>();

        for (Transaction t : transactions) {

            String key = t.amount + "-" + t.merchant;

            map.putIfAbsent(key, new ArrayList<>());
            map.get(key).add(t);
        }

        for (Map.Entry<String, List<Transaction>> entry : map.entrySet()) {

            if (entry.getValue().size() > 1) {

                System.out.println("Duplicate Transaction Pattern:");

                for (Transaction t : entry.getValue()) {

                    System.out.println(
                            "ID:" + t.id +
                                    " Account:" + t.account +
                                    " Amount:" + t.amount +
                                    " Merchant:" + t.merchant
                    );
                }
            }
        }
    }

    public static void main(String[] args) {

        Problem9 system = new Problem9();

        system.addTransaction(new Transaction(1, 500, "StoreA", "acc1", 1000));
        system.addTransaction(new Transaction(2, 300, "StoreB", "acc2", 1100));
        system.addTransaction(new Transaction(3, 200, "StoreC", "acc3", 1200));
        system.addTransaction(new Transaction(4, 500, "StoreA", "acc4", 1300));

        System.out.println("Two Sum (target=500)");
        system.findTwoSum(500);

        System.out.println("\nTwo Sum within 1 hour");
        system.findTwoSumTimeWindow(500, 3600000);

        System.out.println("\nK Sum (k=3 target=1000)");
        system.findKSum(3, 1000);

        System.out.println("\nDuplicate Detection");
        system.detectDuplicates();
    }
}