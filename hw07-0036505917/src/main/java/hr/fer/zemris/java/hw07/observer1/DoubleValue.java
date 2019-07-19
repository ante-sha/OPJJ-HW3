package hr.fer.zemris.java.hw07.observer1;

/**
 * Razred koji definira observer koji pri promjeni "nadgledanog" objekta
 * ispisuje vrijednost koja je duplo veća on njegove nove vrijednosti.
 * Taj proces se ponavlja n puta (n zadan u konstruktoru) te se nakon
 * toga objekt uklanja iz liste promatrača na IntegerStorage.
 * 
 * @author Ante Miličević
 *
 */
public class DoubleValue implements IntegerStorageObserver {
	/**
	 * Brojač ponavljanja
	 */
	private int counter;

	/**
	 * Konstruktor koji postavlja brojač
	 * 
	 * @param n broj koliko puta objekt prati promjenu prije deregistracije
	 * 
	 * @throws IllegalArgumentException ako je n < 1
	 */
	public DoubleValue(int n) {
		if (n < 1) {
			throw new IllegalArgumentException("Counter can not be set on negative value and 0");
		}
		this.counter = n;
	}

	@Override
	public void valueChanged(IntegerStorage istorage) {
		System.out.format("Double value: %d%n", istorage.getValue() * 2);
		if (--counter <= 0) {
			istorage.removeObserver(this);
		}
	}

}
