import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static int QUEUE_SIZE = 10;
    public static int STOP_AFTER_MESSAGES_CONSUMED = 100;
    public static AtomicInteger MESSAGE_COUNTER = new AtomicInteger(0);
    // consumerWaitTime = ?
    // producerWaitTime = ?

    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        Producer producer = new Producer("P1", messageQueue, 100);
        Producer producer2 = new Producer("P2", messageQueue, 60);

        Consumer consumer = new Consumer("C1", messageQueue);
        Consumer consumer2 = new Consumer("C2", messageQueue);


        producer.start();
        producer2.start();
        consumer.start();
        consumer2.start();

        producer.join(); // waiting producer to end task
        producer2.join();
        consumer.join(); // waiting consumer to end task
        consumer2.join();

        System.out.println("\n\n-----------------------");
        System.out.println("Job done - processed " + STOP_AFTER_MESSAGES_CONSUMED + " messages.");
        System.out.println("-----------------------");
    }
}


