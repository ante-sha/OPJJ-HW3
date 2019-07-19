package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje definira razrede koji imaju jednu metodu za testiranje objekta.
 * 
 * @author Ante Miličević
 * @param <E> tip elemenata koje metoda {@link #test(E)} može testirati
 *
 */
@FunctionalInterface
public interface Tester<E> {
	/**
	 * Metoda koja testira objekt.
	 * 
	 * @param obj primljeni objekt
	 * @return true -ako prihvaća objekt <br>
	 *         false -ako ne prihvaća objekt
	 */
	boolean test(E obj);
}
