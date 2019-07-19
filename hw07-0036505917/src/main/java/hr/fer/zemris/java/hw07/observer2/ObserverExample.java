package hr.fer.zemris.java.hw07.observer2;

/**
 * Demonstracijski program za drugu implementaciju oblikovnog obrasa Observer
 * 
 * @author Ante Miličević
 *
 */
public class ObserverExample {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		
		IntegerStorage istorage = new IntegerStorage(20);
		IntegerStorageObserver observer = new SquareValue();
		istorage.addObserver(observer);
		istorage.setValue(5);
		istorage.setValue(2);
		istorage.setValue(25);
		istorage.removeObserver(observer);
		istorage.addObserver(new ChangeCounter());
		istorage.addObserver(new DoubleValue(5));
		istorage.setValue(13);
		istorage.setValue(22);
		istorage.setValue(15);
		
		
		System.out.println("-------------------------");
		
		IntegerStorage istorage2 = new IntegerStorage(20);
		IntegerStorageObserver observer2 = new SquareValue();
		
		istorage2.addObserver(observer2);
		istorage2.addObserver(new ChangeCounter());
		istorage2.addObserver(new DoubleValue(1));
		istorage2.addObserver(new DoubleValue(2));
		istorage2.addObserver(new DoubleValue(2));
		istorage2.setValue(5);
		istorage2.setValue(2);
		istorage2.setValue(25);
		
		istorage2.setValue(13);
		istorage2.setValue(22);
		istorage2.setValue(15);
	}
}
