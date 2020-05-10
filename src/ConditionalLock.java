import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.Random;

class Data1 {
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

class DataCheker implements Runnable {
	Lock lock;
	Condition condionalLock;
	Data1 data;

	public DataCheker(Lock lock, Data1 data) {
		super();
		this.lock = lock;
		this.data = data;
		condionalLock = lock.newCondition();
	}

	@Override
	public void run() {
		if (lock.tryLock()) {
			try {
				while (data.getData() <= 0) {					
					System.out.println("Data Checker going to sleep as Data is " + data.getData());
					condionalLock.await();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				lock.unlock();
			}
		}
		System.out.println("Data Checker Awake from sleep state Data Changed " + data.getData());
	}

}

class DataConsumer implements Runnable {
	Lock lock;
	Condition condionalLock;
	Data1 data;

	public DataConsumer(Lock lock, Data1 data) {
		super();
		this.lock = lock;
		this.data = data;
		condionalLock = lock.newCondition();
	}

	@Override
	public void run() {
		if (lock.tryLock()) {
			try {
				data.setData(new Random().nextInt(100));
				System.out.println(" Data Changed by consumer " + data.getData());
				condionalLock.signal();
			}finally {
				lock.unlock();
			}
		}
	}

}

public class ConditionalLock {
	public static void main(String args[]) throws InterruptedException {
		Lock lock = new ReentrantLock();
		Data1 data = new Data1();
		data.setData(0);
		
		Thread t1 = new Thread(new DataCheker(lock, data));
		Thread t2 = new Thread(new DataConsumer(lock, data));
		
		t1.start();
		t2.start();

	}
}
