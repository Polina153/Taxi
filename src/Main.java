import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final Object syncMonitor = new Object();
    static ArrayList<TaxiImpl> taxiPark = new ArrayList<>();
    static ArrayList<TaxiImpl> availableTaxis = new ArrayList<>();
    static DispatcherImp dispatcher = new DispatcherImp();


    public static void main(String[] args) {

        TaxiImpl taxi = null;
        for (TaxiImpl taxiTemp : availableTaxis) {
            if (taxiTemp != null) {
                taxi = taxiTemp;
                break;
            }
        }
        availableTaxis.remove(taxi);
/*
        //Создать парк из 10 такси в цикле
        //availableTaxis = taxiPark
        //Получить executedOrders
        while (executedOrders.size < 20) {
            //Взять из availableTaxis

            //если taxi != null -  // availableTaxis.remove(taxi)
            //открываем поток
            //Открыть новый поток
               //Create new Order
               //Place order to Taxi
               //Taxi.run
               //Такси выполнило заказ
               //Taxi notify Dispatcher its available again
               //availableTaxis.add(taxi)

        }*/
        /*Order[] orders = new Order[10];

        }*/
        Order order = new Order();
        for (int i = 0; i <= 10; i++) {
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
                    synchronized (syncMonitor) {
                        try {
                            syncMonitor.wait();
                            taxi.notify();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void placeOrder(Taxi taxi, Order order) {
                    taxi.getExecutedOrders();
                }

                @Override
                public void run() {
                    System.out.println("Dispatcher is ready to work!");
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