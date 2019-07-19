package hr.fer.zemris.java.hw11.jnotepadpp.localization;

/**
 * Sučelje razreda koje pruža lokalizaciju uz mogućnost dodavanja promatrača na
 * promjenu jezika.
 * 
 * @author Ante Miličević
 *
 */
public interface ILocalizationProvider {

	/**
	 * Metoda dodaje promatrača u skup promatrača.
	 * 
	 * @param listener promatrač
	 */
	void addLocalizationListener(ILocalizationListener listener);

	/**
	 * Metoda uklanja promatrača iz skupa promatrača.
	 * 
	 * @param listener promatrač
	 */
	void removeLocalizationListener(ILocalizationListener listener);

	/**
	 * Metoda koja prevodi vrijednost zadanog ključa u njegovu
	 * reprezentaciju u trenutnom jeziku.
	 * 
	 * @param key ključ čija se vrijednost traži
	 * @return vrijednost koja je povežana s ključem
	 */
	String getString(String key);

	/**
	 * Metoda za dohvat oznake trenutnog jezika
	 * 
	 * @return oznaka trenutnog jezika
	 */
	String getLanguage();
}
