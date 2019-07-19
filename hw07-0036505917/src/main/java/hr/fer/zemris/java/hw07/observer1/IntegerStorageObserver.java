package hr.fer.zemris.java.hw07.observer1;

/**
 * Sučelje koje definira objekt koji implementira oblikovni obrazac Observer nad
 * razredom {@link IntegerStorage}.
 * 
 * @author Ante Miličević
 *
 */
public interface IntegerStorageObserver {
	/**
	 * Metoda koju poziva objekt tipa {@link IntegerStorage} nad svim svojim
	 * Observer-ima (IntegerStorageObserver-ima) kada dođe do promjene vrijednosti
	 * koju IntegerStorage čuva.
	 * 
	 * @param istorage objekt koji poziva metodu
	 */
	public void valueChanged(IntegerStorage istorage);
}
