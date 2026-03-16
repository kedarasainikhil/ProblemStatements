
public class Problem8 {

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            licensePlate = null;
            occupied = false;
        }
    }

    private ParkingSpot[] table;
    private int capacity = 500;

    private int totalVehicles = 0;
    private int totalProbes = 0;

    public Problem8() {

        table = new ParkingSpot[capacity];

        for (int i = 0; i < capacity; i++) {
            table[i] = new ParkingSpot();
        }
    }

    // Hash function
    private int hash(String plate) {

        int hash = 0;

        for (char c : plate.toCharArray()) {
            hash = (hash * 31 + c) % capacity;
        }

        return hash;
    }

    // Park vehicle
    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {

            index = (index + 1) % capacity;
            probes++;
        }

        table[index].occupied = true;
        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();

        totalVehicles++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + plate + "\") → Assigned spot #" + index +
                " (" + probes + " probes)");
    }

    // Exit vehicle
    public void exitVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (table[index].occupied) {

            if (table[index].licensePlate.equals(plate)) {

                long exitTime = System.currentTimeMillis();

                long durationMillis = exitTime - table[index].entryTime;

                double hours = durationMillis / 3600000.0;

                double fee = hours * 5.0; // $5 per hour

                table[index].occupied = false;
                table[index].licensePlate = null;

                System.out.println(
                        "exitVehicle(\"" + plate + "\") → Spot #" + index +
                                " freed, Duration: " +
                                String.format("%.2f", hours) +
                                "h, Fee: $" + String.format("%.2f", fee)
                );

                return;
            }

            index = (index + 1) % capacity;
            probes++;
        }

        System.out.println("Vehicle not found");
    }

    // Find nearest available spot
    public void findNearestSpot() {

        for (int i = 0; i < capacity; i++) {

            if (!table[i].occupied) {

                System.out.println("Nearest available spot: #" + i);
                return;
            }
        }

        System.out.println("Parking full");
    }

    // Statistics
    public void getStatistics() {

        int occupied = 0;

        for (ParkingSpot spot : table) {

            if (spot.occupied)
                occupied++;
        }

        double occupancyRate = (occupied * 100.0) / capacity;

        double avgProbes = totalVehicles == 0 ? 0 :
                (double) totalProbes / totalVehicles;

        System.out.println("Occupancy: " +
                String.format("%.2f", occupancyRate) + "%");

        System.out.println("Avg Probes: " +
                String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {

        Problem8 parking = new Problem8();

        parking.parkVehicle("ABC-1234");
        parking.parkVehicle("ABC-1235");
        parking.parkVehicle("XYZ-9999");

        parking.findNearestSpot();

        parking.exitVehicle("ABC-1234");

        parking.getStatistics();
    }
}
