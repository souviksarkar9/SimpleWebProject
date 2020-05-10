import java.util.concurrent.atomic.AtomicInteger;

public class AtomicThreadOperations extends Thread {
	
	AtomicInteger balance;
	
	public AtomicThreadOperations() {
		balance = new AtomicInteger(100);
	}
	
	private int incBalance() {
		return balance.incrementAndGet();
	}
	
	private int decBalance() {
		return balance.decrementAndGet();
	}
		
	@Override
	public void run() {
		System.out.println(" Balance After Increment " + incBalance());
		System.out.println(" Balance After Decrement " + decBalance());
	}
	
	public static void main(String args[]) {
		for(int i=1; i<=1000; i++)
			new Thread(new AtomicThreadOperations()).start();
			
	}

}
