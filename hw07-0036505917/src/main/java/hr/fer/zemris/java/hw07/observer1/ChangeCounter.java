package hr.fer.zemris.java.hw07.observer1;

/**
 * Razred koji definira observer koji broji promjene objekta kojeg "nadgleda".
 * 
 * @author Ante Miličević
 *
 */
public class ChangeCounter implements IntegerStorageObserver {
	/**
	 * Brojač promjena
	 */
	int counter = 0;

	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.format("Number of value changes since tracking: %d%n", ++counter);
	}
}
