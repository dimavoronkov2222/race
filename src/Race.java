import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
public class Race {
    static AtomicLong startRaceTime = new AtomicLong();
    public static void startRace(List<Thread> cars) {
        new Thread(() -> {
            try {
                for (int i = 3; i > 0; i--) {
                    System.out.println(i + "...");
                    Thread.sleep(500);
                }
                System.out.println("GO!!!");
                startRaceTime.set(System.currentTimeMillis());
                for (Thread car : cars) {
                    car.start();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
    public static String convertToTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss.SSS");
        return sdf.format(time);
    }
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the track length (in meters): ");
        int trackLength = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the number of cars: ");
        int numberOfCars = scanner.nextInt();
        scanner.nextLine();
        List<RaceCar> raceCars = new ArrayList<>();
        for (int i = 1; i <= numberOfCars; i++) {
            System.out.print("Enter the name of car #" + i + ": ");
            String carName = scanner.nextLine();
            System.out.print("Enter the maximum speed for " + carName + ": ");
            int maxSpeed = scanner.nextInt();
            scanner.nextLine();
            raceCars.add(new RaceCar(carName, maxSpeed));
        }
        CountDownLatch latch = new CountDownLatch(numberOfCars);
        List<RaceCarRunnable> carRunnables = new ArrayList<>();
        for (RaceCar car : raceCars) {
            carRunnables.add(new RaceCarRunnable(car, trackLength, latch));
        }
        List<Thread> threads = new ArrayList<>();
        for (RaceCarRunnable carRunnable : carRunnables) {
            threads.add(new Thread(carRunnable));
        }
        startRace(threads);
        latch.await();
        raceCars.sort((c1, c2) -> Long.compare(c1.getFinishTime(), c2.getFinishTime()));
        System.out.println("\nRace Results:");
        for (RaceCar car : raceCars) {
            System.out.printf("%s => Time: %s\n", car.getName(), convertToTime(car.getFinishTime()));
        }
        System.out.printf("\nWinner: %s with time: %s\n", raceCars.get(0).getName(), convertToTime(raceCars.get(0).getFinishTime()));
    }
}