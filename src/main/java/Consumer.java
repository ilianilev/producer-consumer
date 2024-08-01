import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

class Consumer extends Thread {
    //private static final int maxSize = 10;
    private LinkedBlockingQueue<Message> messageQueue;
    private String name;

    public Consumer(String name, LinkedBlockingQueue<Message> messageQueue) {
        this.name = name;
        this.messageQueue = messageQueue;
    }

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

    private synchronized void consume() throws InterruptedException {
        while (messageQueue.size() == 0) {
            System.out.println("Consumer " + name + " is waiting for messages...");
            Thread.sleep(100);
            //System.out.println("Consumer " + name + " was notified by producer.");
        }
        Message taken = messageQueue.take();
        Main.MESSAGE_COUNTER.incrementAndGet();
        Thread.sleep(taken.getProcessingTime());
        System.out.println("Consumer " + name + " consumed message " +
                taken.getMessageId() + " with data: " + taken.getData() +
                ". Operation took " + taken.getProcessingTime() + " milliseconds.");
    }
}
