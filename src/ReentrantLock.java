import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class PriceContainer{
	
	private ReentrantReadWriteLock l = new java.util.concurrent.locks.ReentrantReadWriteLock();
	
	
	private Lock lock = new java.util.concurrent.locks.ReentrantLock();
	private int tataSharePrice;
	private int cognizantSharePrice;
	private int hclSharePrice;
	public PriceContainer(int tataSharePrice, int cognizantSharePrice, int hclSharePrice) {
		super();
		this.tataSharePrice = tataSharePrice;
		this.cognizantSharePrice = cognizantSharePrice;
		this.hclSharePrice = hclSharePrice;
	}
	public PriceContainer() {
		super();
	}
	public Lock getLock() {
		return lock;
	}
	public double getTataSharePrice() {
		return tataSharePrice;
	}
	public void setTataSharePrice(int tataSharePrice) {
		this.tataSharePrice = tataSharePrice;
	}
	public double getCognizantSharePrice() {
		return cognizantSharePrice;
	}
	public void setCognizantSharePrice(int cognizantSharePrice) {
		this.cognizantSharePrice = cognizantSharePrice;
	}
	public double getHclSharePrice() {
		return hclSharePrice;
	}
	public void setHclSharePrice(int hclSharePrice) {
		this.hclSharePrice = hclSharePrice;
	}
}
//price updated thread
class SharePriceUpdater implements Runnable{
	PriceContainer priceContainer;
	Random r = new Random();
	public SharePriceUpdater(PriceContainer priceContainer) {
		super();
		this.priceContainer = priceContainer;
	}
	@Override
	public void run() {
		while(true) {
			priceContainer.getLock().lock();			
			try {
				priceContainer.setTataSharePrice(r.nextInt(2000));
				priceContainer.setHclSharePrice(r.nextInt(2000));
				priceContainer.setCognizantSharePrice(r.nextInt(2000));
			}finally {
				priceContainer.getLock().unlock();
			}
		}
		
	}
	
}
//reader threads
class ShareProceMobileReader implements
Runnable{
	PriceContainer priceContainer;
	public ShareProceMobileReader(PriceContainer priceContainer) {
		super();
		this.priceContainer = priceContainer;
	}
	@Override
	public void run() {
		while(true) {
			if(priceContainer.getLock().tryLock()) {
				try {
					System.out.println(" Tata share Prices in Mobile App " + priceContainer.getTataSharePrice());
					System.out.println(" HCL share Prices in Mobile App " + priceContainer.getHclSharePrice());
					System.out.println(" Cognizant share Prices in Mobile App " + priceContainer.getCognizantSharePrice());
				}finally {
					priceContainer.getLock().unlock();
				}
			}
		}		
	}
	
}

class SharePriceAppReader implements Runnable{
	PriceContainer priceContainer;
	
	public SharePriceAppReader(PriceContainer priceContainer) {
		super();
		this.priceContainer = priceContainer;
	}

	@Override
	public void run() {
		while(true) {
			if(priceContainer.getLock().tryLock()) {
				try {
					System.out.println(" Tata share Prices in Desktop App " + priceContainer.getTataSharePrice());
					System.out.println(" HCL share Prices in Desktop App " + priceContainer.getHclSharePrice());
					System.out.println(" Cognizant share Prices in Desktop App " + priceContainer.getCognizantSharePrice());
				}finally {
					priceContainer.getLock().unlock();
				}
			}
		}
		
	}	

}




public class ReentrantLock {
	public static void main(String args[]) {
		PriceContainer priceContainer = new PriceContainer();
		Thread writer = new Thread(new SharePriceUpdater(priceContainer));
		Thread appReader = new Thread(new SharePriceAppReader(priceContainer));
		Thread mobReader = new Thread(new ShareProceMobileReader(priceContainer));
		
		writer.start();
		appReader.start();
		mobReader.start();
	}
}
