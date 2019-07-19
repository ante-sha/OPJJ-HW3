package hr.fer.zemris.java.hw07.observer2;

/**
 * Razred koji definira observer koji ispisuje kvadratnu vrijednost objekta
 * kojeg "nadgleda" nakon njegove promjene.
 * 
 * @author Ante Miličević
 *
 */
public class SquareValue implements IntegerStorageObserver {

	@Override
	public void valueChanged(IntegerStorageChange change) {
		int newValue = change.getNewValue();
		System.out.format("Provided new value: %d, square is %d%n",newValue,newValue*newValue);
	}

}
