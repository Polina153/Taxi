import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class TaxiApp {

    private final BlockingQueue<Order> queue;

    private static final Order DONE = new Order("Last order");
    private final Order[] orders = {new Order("Order1"), new Order("Order2"), new Order("Order3"), new Order("Order4"), new Order("Order5")};

    public TaxiApp() {
        queue = new ArrayBlockingQueue<>(1, true);
        (new Thread(new Dispatcher())).start();
        (new Thread(new Taxi())).start();
    }

    class Dispatcher implements Runnable {
        public void run() {
            try {
                for (Order order : orders) {
                    queue.put(order);
                    Thread.sleep(500);
                }
                queue.put(DONE);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    class Taxi implements Runnable {
        public void run() {
            try {
                String msg;
                while (!((msg = queue.take().getOrderName()).equals(DONE.getOrderName()))) {
                    System.out.println(msg);
                }
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new TaxiApp();
    }
}