package hr.fer.zemris.java.hw07.observer2;

import java.util.Objects;

/**
 * Razred čiji objekti čuvaju informaciju o promjeni IntegerStorage-a te se
 * koriste u komunikaciji između {@link IntegerStorage}-a i
 * {@link IntegerStorageObserver}-a. Objekti ovog razreda sadržavaju staru i
 * novu vrijednost te referencu na sami IntegerStorage.
 * 
 * @author Ante Miličević
 *
 */
public class IntegerStorageChange {

	/**
	 * Spremnik u kojemu se dogodila promjena
	 */
	private final IntegerStorage storage;
	/**
	 * Stara vrijednost
	 */
	private final int oldValue;
	/**
	 * Nova vrijednost
	 */
	private final int newValue;

	/**
	 * Konstruktor definira sve parametre objekta
	 * 
	 * @param storage spremnik u kojemu se dogodila izmjena
	 * @param oldValue stara vrijednost u spremniku
	 * @param newValue nova vrijednost u spremniku
	 * 
	 * @throws NullPointerException ako je storage null
	 */
	public IntegerStorageChange(IntegerStorage storage, int oldValue, int newValue) {
		super();
		this.storage = Objects.requireNonNull(storage);
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Getter za spremnik
	 * 
	 * @return storage
	 */
	public IntegerStorage getStorage() {
		return storage;
	}

	/**
	 * Getter za staru vrijednost
	 * 
	 * @return oldValue
	 */
	public int getOldValue() {
		return oldValue;
	}

	/**
	 * Getter za novu vrijednost
	 * 
	 * @return newValue
	 */
	public int getNewValue() {
		return newValue;
	}

}
