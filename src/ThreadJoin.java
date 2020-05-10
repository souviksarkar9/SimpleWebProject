
import java.math.BigInteger;
import java.util.*;

class ComplexCalculation {
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) {
        BigInteger result = BigInteger.ZERO;
        List<PowerCalculatingThread> res = new ArrayList<>();
        res.add(new PowerCalculatingThread(base1,power1));
        res.add(new PowerCalculatingThread(base2,power2));
        
        for(PowerCalculatingThread pt : res){
            Thread t = new Thread(pt);
            t.start();
            try{
                t.join();
            }catch (InterruptedException e) {
				System.out.println(" " + e);
			}
        }
       for(PowerCalculatingThread pt : res){
            result = result.add(pt.getResult());
        }
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;
    
        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }
    
        @Override
        public void run() {
           for(BigInteger r=BigInteger.ONE; r.compareTo(power) <= 0; r=r.add(BigInteger.ONE)){
               result = result.multiply(base);
           }
        }
    
        public BigInteger getResult() { return result; }
    }
}


public class ThreadJoin {
	
	public static void main(String[] args) {
	    BigInteger b1 = new BigInteger("652");
	    BigInteger p1 = new BigInteger("25");
	    BigInteger b2 = new BigInteger("625");
	    BigInteger p2 = new BigInteger("52");
		BigInteger result = new ComplexCalculation().calculateResult(b1,p1,b2,p2);
		System.out.println(result);
	}

}
