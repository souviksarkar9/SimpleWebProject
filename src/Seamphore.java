import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Random;

class Data {
	Integer data;
	ReentrantReadWriteLock rwlock = new ReentrantReadWriteLock();
	Lock readLock = rwlock.readLock();
	Lock writeLock = rwlock.writeLock();

	public Integer getData() {
		Integer d = 0;
		if (readLock.tryLock()) {
			try {
				d = this.data;
			} finally {
				readLock.unlock();
			}
		}
		return d;
	}

	public void setData(Integer data) {
		if (writeLock.tryLock()) {
			try {
				this.data = data;
			} finally {
				writeLock.unlock();
			}
		}
	}

}

class Producer implements Runnable {
	Semaphore full;
	Semaphore empty;
	Data data;
	Random r = new Random();

	public Producer(Semaphore full, Semaphore empty, Data data) {
		super();
		this.full = full;
		this.empty = empty;
		this.data = data;
	}

	@Override
	public void run() {
		while (true) {
			try {
				//Thread.currentThread().sleep(1000);// delaying the producer thread results in delaying the consumer
				empty.acquire(); // Producer get a lock on empty
				int no = r.nextInt(100);
				data.setData(no);// generated a random integer
				System.out.println("Produced.. " + no);
				full.release(); // notifies the Consumer thread to wake up and consumes line no 83
			} catch (InterruptedException e) {
				full.release();
			}
		}
	}

}

class Consumer implements Runnable {
	Semaphore full;
	Semaphore empty;
	Data data;

	public Consumer(Semaphore full, Semaphore empty, Data data) {
		super();
		this.full = full;
		this.empty = empty;
		this.data = data;
	}

	@Override
	public void run() {
		while (true) {
			try {
				full.acquire();// when consumer tries to accquire a lock it will go to sleep as there are no slots, and wait for other thead to get notify()
				System.out.println("Consumed.. " + this.data.getData());//consumer thread wakes up and consumes, since empty is acquired by producer and released by consumer, the producer can't 
				//further until consumer does an empty.release.
				empty.release();
			} catch (InterruptedException e) {
				empty.release();
			}
		
		}

	}

}

public class Seamphore {
	public static void main(String args[]) {
		Semaphore full = new Semaphore(0); // any thread trying to acquire a lock will be suspended/sleep as there are 0 slots
		Semaphore empty = new Semaphore(1); // any thread trying to acquire a lock can acquire it as 1
		Data data = new Data();
		
		Thread t1 = new Thread(new Producer(full, empty, data));
		Thread t2 = new Thread(new Consumer(full, empty, data));
		
		t1.start();
		t2.start();

	}
}
