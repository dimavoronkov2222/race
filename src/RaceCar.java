public class RaceCar {
    private String name;
    private int maxSpeed;
    private long finishTime;
    public RaceCar(String name, int maxSpeed) {
        this.name = name;
        this.maxSpeed = maxSpeed;
    }
    public String getName() {
        return name;
    }
    public int getMaxSpeed() {
        return maxSpeed;
    }
    public long getFinishTime() {
        return finishTime;
    }
    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }
}