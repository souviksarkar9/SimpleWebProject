import java.util.*;
import java.util.concurrent.locks.*;

class DollarRate {
	private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
	private final Lock readLock = rwLock.readLock();
	private final Lock writeLock = rwLock.writeLock();
	private int rate;

	public DollarRate() {
		super();
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		if (writeLock.tryLock()) {
			try {
				this.rate = rate;
				System.out.println("Rate updated by " + Thread.currentThread().getName() + " on " + new Date() + " rate " + rate);
			} finally {
				writeLock.unlock();
			}
		}
	}

	public Lock getReadLock() {
		return readLock;
	}

	public Lock getWriteLock() {
		return writeLock;
	}
}

class DollarRateUpdater implements Runnable {
	private Random r = new Random();
	private DollarRate rate;
	public DollarRateUpdater(DollarRate rate) {
		super();
		this.rate = rate;
	}
	@Override
	public void run() {		
		rate.setRate(r.nextInt(100));
	}

}

class DollarRateReader implements Runnable {
	private DollarRate rate;

	public DollarRateReader(DollarRate rate) {
		super();
		this.rate = rate;
	}

	@Override
	public void run() {
		rate.getReadLock().lock();
		try {
			System.out.println(new Date() + " Rate - " + rate.getRate());
		} finally {
			rate.getReadLock().unlock();
		}
	}
}

public class ReentrantLockRwWr {
	public static void main(String args[]) {
		// Reader Thread
		DollarRate rate = new DollarRate();
		for (int i = 0; i < 1000; i++) {
			Thread t1 = new Thread(new DollarRateReader(rate));
			Thread t2 = new Thread(new DollarRateUpdater(rate));
			t1.start();
			t2.start();			
		}
	}
}
