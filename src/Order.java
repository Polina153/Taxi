import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Order{
    private final Queue<Runnable> queue = new LinkedList<>();
    Random random = new Random();
    int randomTimeToSleep = random.nextInt(1000, 4000);

    public int getRandomTimeToSleep() {
        return randomTimeToSleep;
    }
    private final Object monitor = new Object();

    public void add(Runnable task) {
        synchronized (monitor) {
            queue.add(task);
            monitor.notify();
        }
    }

    public Runnable take() {
        synchronized (monitor) {
            while(queue.isEmpty()){
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            return queue.poll();
        }
    }
}
