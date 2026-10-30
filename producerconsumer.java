import java.util.LinkedList;

class Buffer {
    private LinkedList<Integer> list = new LinkedList<>();
    private int capacity = 5;

    public void produce(int value) throws InterruptedException {
        synchronized (this) {
            while (list.size() == capacity) {
                System.out.println("Buffer is full, Producer waiting...");
                wait();
            }
            list.add(value);
            System.out.println("Producer produced-" + value);
            notify();
            Thread.sleep(500);
        }
    }

    public void consume() throws InterruptedException {
        synchronized (this) {
            while (list.isEmpty()) {
                System.out.println("Buffer is empty, Consumer waiting...");
                wait();
            }
            int val = list.removeFirst();
            System.out.println("Consumer consumed-" + val);
            notify();
            Thread.sleep(500);
        }
    }
}

class Producer extends Thread {
    Buffer buffer;
    Producer(Buffer b) { this.buffer = b; }
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                buffer.produce(i);
            }
        } catch (InterruptedException e) {}
    }
}

class Consumer extends Thread {
    Buffer buffer;
    Consumer(Buffer b) { this.buffer = b; }
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                buffer.consume();
            }
        } catch (InterruptedException e) {}
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Producer p = new Producer(buffer);
        Consumer c = new Consumer(buffer);
        p.start();
        c.start();
    }
}
