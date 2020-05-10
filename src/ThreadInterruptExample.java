import java.math.BigInteger;

class MathPowBigInt  implements Runnable{
	BigInteger base;
	BigInteger pow;
	BigInteger result;
	
	MathPowBigInt(BigInteger b , BigInteger p){
		this.base=b;
		this.pow=p;
		this.result = BigInteger.ONE;
	}	
	
	public BigInteger getresult() {
		for(BigInteger i=BigInteger.ZERO; i.compareTo(pow)!=0; i=i.add(BigInteger.ONE))
			if(Thread.currentThread().isInterrupted()) {
				System.out.println("Thread Interuppted Externally ");
				return BigInteger.ZERO;
			}
			result = result.multiply(base);
		return result;
	}
	
	@Override
	public void run() {		
		System.out.println(Thread.currentThread().getState().toString());
		System.out.println(getresult());
	}
}

public class ThreadInterruptExample {
	public static void main(String args[]) throws InterruptedException {	
		BigInteger base = new BigInteger("20");
		BigInteger pow = new BigInteger("10");		
		
		Thread t = new Thread(new MathPowBigInt(base,pow));
		t.start();
		
		//System.out.println(t.getState());
		//Thread.currentThread().sleep(5000);
		
		if(t.isAlive()) {
			t.interrupt();
			//if(t.currentThread().getState().toString().compareTo(""))
		}
		
		
		
		
		
	}



}
