package hr.fer.zemris.java.hw07.observer2;

import java.util.ArrayList;
import java.util.List;

/**
 * Razred koji modelira spremnik primitivnog integera kojeg je moguće
 * "nadgledati" pomoću razreda koji implementiraju
 * {@link IntegerStorageObserver}. Prilikom promjene vrijednosti koju objekt
 * čuva, pozivaju se notifikacijske metode IntegerStorageObserver-a.
 * 
 * @author Ante Miličević
 *
 */
public class IntegerStorage {

	/**
	 * Vrijednost koju čuva objekt
	 */
	private int value;
	/**
	 * Lista promatrača objekta
	 */
	private List<IntegerStorageObserver> observers = new ArrayList<>();

	/**
	 * Konstruktor s inicijalnom vrijednosti
	 * 
	 * @param initialValue inicijalna vrijednost
	 */
	public IntegerStorage(int initialValue) {
		this.value = initialValue;
	}

	/**
	 * Metoda za dodavanje promatrača na objekt. Promatrač je dodan u listu
	 * promatrača ako se tamo već ne nalazi i ako nije null.
	 * 
	 * @param observer promatrač
	 */
	public void addObserver(IntegerStorageObserver observer) {
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	/**
	 * Metoda za uklanjanje promatrača
	 * 
	 * @param observer promatrač
	 */
	public void removeObserver(IntegerStorageObserver observer) {
		observers.remove(observer);
	}

	/**
	 * Metoda za brisanje svih promatrača
	 */
	public void clearObservers() {
		observers.clear();
	}

	/**
	 * Metoda za dohvat trenutne vrijednosti
	 * 
	 * @return value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Metoda za mijenjanje trenutne vrijednosti. Ako dođe do promjene vrijednosti
	 * tada se promjena upiše u {@link IntegerStorageChange} te se pošalje pomoću
	 * notifikacijskih metoda svim promatračima na ovaj objekt.
	 * 
	 * @param value nova vrijednost
	 */
	public void setValue(int value) {
		List<IntegerStorageObserver> observers = new ArrayList<>(this.observers);
		if (this.value != value) {

			IntegerStorageChange change = new IntegerStorageChange(this, this.value, value);
			this.value = value;

			if (observers != null) {
				for (IntegerStorageObserver observer : observers) {
					observer.valueChanged(change);
				}
			}
		}
	}
}
