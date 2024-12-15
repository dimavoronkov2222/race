import java.util.concurrent.CountDownLatch;
public class RaceCarRunnable implements Runnable {
    private final RaceCar car;
    private final int distance;
    private final CountDownLatch latch;
    private int passed = 0;
    private boolean isFinished = false;
    public RaceCarRunnable(RaceCar car, int distance, CountDownLatch latch) {
        this.car = car;
        this.distance = distance;
        this.latch = latch;
    }
    private int getRandomSpeed() {
        return (int) (car.getMaxSpeed() / 2 + Math.random() * (car.getMaxSpeed() / 2));
    }
    @Override
    public void run() {
        while (!isFinished) {
            int speed = getRandomSpeed();
            passed += speed;
            System.out.printf("%s => speed: %d; progress: %d/%d\n", car.getName(), speed, passed, distance);
            if (passed >= distance) {
                isFinished = true;
                car.setFinishTime(System.currentTimeMillis() - Race.startRaceTime.get());
                System.out.printf("%s FINISHED! Time: %s\n", car.getName(), Race.convertToTime(car.getFinishTime()));
                latch.countDown();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}