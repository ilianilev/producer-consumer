import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Represents a producer thread that generates messages and adds them to a shared message queue.
 */
class Producer extends Thread {
    private final LinkedBlockingQueue<Message> messageQueue;
    private final long delayMs;
    private final String name;
    private boolean isSuccessful = false;

    /**
     * Constructs a new producer with the given name, message queue, and delay.
     *
     * @param name         The name of the producer.
     * @param messageQueue The shared message queue.
     * @param delayMs      The processing delay for each produced message.
     */
    public Producer(String name, LinkedBlockingQueue<Message> messageQueue, long delayMs) {
        this.name = name;
        this.messageQueue = messageQueue;
        this.delayMs = delayMs;
    }

    /**
     * Continuously produces messages and adds them to the queue until the termination condition is met.
     */
    @Override
    public void run() {
        try {
            while (Main.MESSAGE_COUNTER.get() < Main.STOP_AFTER_MESSAGES_CONSUMED) {
                produce();
            }
            isSuccessful = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Producer " + name + " was interrupted.");
            isSuccessful = false;
        } catch (Exception e) {
            System.err.println("Producer " + name + " encountered an exception: " + e.getMessage());
            isSuccessful = false;
        }
    }

    /**
     * Generates a new message, sets its processing time, and adds it to the queue.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for the queue.
     */
    private void produce() throws InterruptedException {
        Message msg = new Message(UUID.randomUUID().toString(),
                "Data: " + LocalDateTime.now());
        msg.setProcessingTime(delayMs);

        // Wait until there is free space in the queue
        while (messageQueue.size() == Main.QUEUE_SIZE) {
            System.out.println("Queue is full. Producer " + name + " is waiting for queue...");
            Thread.sleep(Main.producerWaitTimeMs);
        }

        messageQueue.put(msg);
        System.out.println("Producer " + name + " produced " + msg.getMessageId());
    }

    /**
     * @return true if the producers successful ended
     */
    public boolean isSuccessful() {
        return isSuccessful;
    }
}
