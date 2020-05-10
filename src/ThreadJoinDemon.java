import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * 
 * @author Souvik
 *Demon threads run on background and dies as soon as the main thread finishes computation
 *Example-The auto save feature of email, that saves the email even when we are composing
 */

//Using thread to compute factorial of a list of numbers
class Compute implements Runnable{
	Long no;
	BigInteger result = BigInteger.ONE;
	boolean isFinished = false;
	
	Compute(Long no){
		this.no = no;		
	}
	   
	@Override
	public void run() {
		while(no > 0) {
			if(Thread.currentThread().isInterrupted()) {
				result = BigInteger.ZERO;
				isFinished = false;	
			}
			result = result.multiply(new BigInteger(Long.toString(no--)));
		}
		isFinished = true;		
	}

	public boolean isFinished() {
		return isFinished;
	}

	public BigInteger getResult() {
		return result;
	}	
	
}

public class ThreadJoinDemon {
	public static void main(String args[]) {
		List<Compute> computeList = new ArrayList<>();
		Arrays.asList(582135L , 6L , 2L ).forEach( p -> {
			computeList.add(new Compute(p))	;			
		});	
		
		computeList.forEach(p -> {
			Thread t = new Thread(p);
			//t.setDaemon(true);			
			t.start();
			try {
				/*
				 * t.join here means the main thread will wait for all the working thread to complete and then it will exit,
				 * problem with this if the thread wait for an IO for long time the main will never be executed.
				 * t.join(1000) - means the main thread will wait for 1 sec and invoke t.interrrupt to the running threads
				 * thus the control from the threads returns to main in 1 seconds and main terminates.
				 * 
				 * if t.setDaemon(true) then the main thread will not wait for the threads to be executed. 
				 */
				t.join(1000);
			} catch (InterruptedException e) {
				System.out.println(" " + e);
			}
		});
		
		computeList.forEach(p -> {
			if(p.isFinished())
				System.out.println(p.getResult());
			else
				System.out.println(" Result is in preocess ");
		});
		
		System.out.println("Finished Main Thread");
	}
	
}
