import java.util.concurrent.LinkedBlockingQueue;


/**
 * Represents a consumer thread that processes messages from a shared message queue.
 */
class Consumer extends Thread {
    private final LinkedBlockingQueue<Message> messageQueue;
    private final String name;

    /**
     * Constructs a new consumer with the given name and message queue.
     *
     * @param name          The name of the consumer.
     * @param messageQueue  The shared message queue.
     */
    public Consumer(String name, LinkedBlockingQueue<Message> messageQueue) {
        this.name = name;
        this.messageQueue = messageQueue;
    }

    /**
     * Continuously consumes messages from the queue until the termination condition is met.
     */
    @Override
    public void run() {
        while (true) {
            try {
                consume();
                if (Main.MESSAGE_COUNTER.get() >= Main.STOP_AFTER_MESSAGES_CONSUMED) {
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Waits for messages in the queue, processes them, and prints relevant information.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for messages.
     */
    private void consume() throws InterruptedException {
        // Wait until there are messages in the queue
        while (messageQueue.isEmpty()) {
            System.out.println("Consumer " + name + " is waiting for messages...");
            Thread.sleep(Main.consumerWaitTimeMs);
        }

        //Get a message from the queue
        Message taken = messageQueue.take();
        Main.MESSAGE_COUNTER.incrementAndGet();

        //Simulate processing time
        Thread.sleep(taken.getProcessingTime());


        System.out.println("Consumer " + name + " consumed message " +
                taken.getMessageId() + " with data: " + taken.getData() +
                ". Operation took " + taken.getProcessingTime() + " milliseconds.");
    }
}

