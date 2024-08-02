import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;



public class Main {

    public static int QUEUE_SIZE = 10;
    public static int STOP_AFTER_MESSAGES_CONSUMED = 100;
    public static AtomicInteger MESSAGE_COUNTER = new AtomicInteger(0);
    public static long consumerWaitTimeMs = 100;
    public static long producerWaitTimeMs = 100;
    /**
     * Entry point of the program that demonstrates message processing using producers and consumers.
     */

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        Producer producer = new Producer("P1", messageQueue, 100);
        Producer producer2 = new Producer("P2", messageQueue, 60);

        Consumer consumer = new Consumer("C1", messageQueue);
        Consumer consumer2 = new Consumer("C2", messageQueue);


        producer.start(); //Start thread execution for producer
        producer2.start();
        consumer.start(); //Start thread execution for consumer
        consumer2.start();

        producer.join(); // wait for another producer to free the common resource
        producer2.join();
        consumer.join(); // wait for another consumer to free the common resource
        consumer2.join();

        System.out.println("\n\n-----------------------");
        System.out.println("Job done - processed " + STOP_AFTER_MESSAGES_CONSUMED + " messages.");
        System.out.println("-----------------------");
    }
}


