import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TaxiImpl implements Taxi {

    private static List<Order> executedOrders = Collections.synchronizedList(new ArrayList<>());
    private Order order;

    @Override
    public void run() {
        try {
            Thread.sleep(order.getRandomTimeToSleep());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executedOrders.add(order);

    }

    @Override
    public void placeOrder(Order order) {
        this.order = order;

        //добавить
    }

    @Override
    public List<Order> getExecutedOrders() {
        return executedOrders;
    }
}
