import java.time.LocalDateTime;
import java.util.Date;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

class Producer extends Thread {
    private LinkedBlockingQueue<Message> messageQueue;
    private long delayMs;
    private String name;

    public Producer(String name, LinkedBlockingQueue<Message> messageQueue, long delayMs) {
        this.name = name;
        this.messageQueue = messageQueue;
        this.delayMs = delayMs;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (Main.MESSAGE_COUNTER.get() >= Main.STOP_AFTER_MESSAGES_CONSUMED) {
                    break;
                }
                produce();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *
     * This met
     */
    private synchronized void produce() throws InterruptedException {
        Message msg = new Message(UUID.randomUUID().toString(),
                "Data: " + LocalDateTime.now().toString());
        msg.setProcessingTime(delayMs);
        while (messageQueue.size() == Main.QUEUE_SIZE) {
            System.out.println("Queue is full. Producer " + name + " is waiting for queue...");
            Thread.sleep(100);
            //System.out.println("Producer " + name + " was notified by consumer.");
        }
        messageQueue.put(msg);
        System.out.println("Producer " + name + " produced " + msg.getMessageId());
    }
}