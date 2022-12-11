import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Taxi extends Runnable {
    List<Taxi> availableTaxis = Collections.synchronizedList(new ArrayList<>());
    List<Order> executedOrders = Collections.synchronizedList(new ArrayList<>());
    void run();
    void placeOrder(Order order);
    List<Order> getExecutedOrders();
}
