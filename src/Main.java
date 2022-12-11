import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final Object syncMonitor = new Object();

    public static void main(String[] args) {
        /*Order[] orders = new Order[10];

        }*/
        Order order = new Order();
        for (int i = 0; i <= 100; i++) {
            order.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            Thread dispatcherThread = new Thread(new Dispatcher() {
                @Override
                public void notifyAvailable(Taxi taxi) {
                    taxi.notify();
                }

                @Override
                public void placeOrder(Taxi taxi, Order order) {
                    taxi.getExecutedOrders();
                }

                @Override
                public void run() {
                    System.out.println("Dispatchar is ready to work!");
                    while (true) {
                        Runnable task = null;
                        task = order.take();
                        Taxi taxi = null;
                        if (task != null) {
                            do {
                                for (int i = 0; i < Taxi.availableTaxis.size(); i++) {
                                    taxi = Taxi.availableTaxis.get(i);
                                }
                            } while (taxi == null);
                            if (taxi != null) {
                                notifyAvailable(taxi);
                                placeOrder(taxi, (Order) task);
                            }
                        }


                    }
                }
            });
            dispatcherThread.start();
            ExecutorService exServ = Executors.newFixedThreadPool(5);
            for (int k = 0; k <= 10; k++) {
                int finalK = k;
                exServ.execute(new Taxi() {
                    @Override
                    public void run() {
                        Thread.currentThread().setName(String.valueOf(finalK));
                        availableTaxis.add(this);
                        System.out.println("Taxi " + Thread.currentThread().getName() + " is ready to work!");
                        synchronized (syncMonitor) {
                            try {
                                syncMonitor.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            placeOrder(order);
                        }

                    }

                    @Override
                    public void placeOrder(Order order) {
                        System.out.println("Taxi " + Thread.currentThread().getName() + " is driving");
                        try {
                            Thread.sleep(order.getRandomTimeToSleep());
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        executedOrders.add(order);

                        synchronized (syncMonitor) {
                            System.out.println("Taxi " + Thread.currentThread().getName() + " is free");
                            try {
                                syncMonitor.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    }

                    @Override
                    public List<Order> getExecutedOrders() {
                        return executedOrders;
                    }
                });
            }
        }
    }
}