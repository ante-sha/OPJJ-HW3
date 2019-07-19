package hr.fer.zemris.java.hw07.observer2;

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
	 * @param change objekt koji čuva informacije o promjeni vrijednosti
	 *               IntegerStorage-a
	 */
	public void valueChanged(IntegerStorageChange change);
}
