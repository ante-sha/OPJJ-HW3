/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koje definira razrede koji imaju jednu metodu za testiranje objekta.
 * @author Ante Miličević
 *
 */
public interface Tester {
	/**
	 * Metoda koja testira objekt.
	 * @param obj primljeni objekt
	 * @return true -ako prihvaća objekt
	 * <br>false -ako ne prihvaća objekt
	 */
	boolean test(Object obj);
}
