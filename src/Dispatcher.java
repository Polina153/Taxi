public interface Dispatcher extends Runnable {

    void notifyAvailable(Taxi taxi);

    void placeOrder(Taxi taxi, Order order);

    void run();
}
