
public class CreatingThreadsJava8 {
	public static void main(String args[]) {		
		new Thread(() -> {System.out.println("Hello Souvik");}).start();
	}
}
