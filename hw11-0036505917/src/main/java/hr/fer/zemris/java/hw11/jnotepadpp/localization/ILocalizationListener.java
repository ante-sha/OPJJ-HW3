package hr.fer.zemris.java.hw11.jnotepadpp.localization;

/**
 * Sučelje koje definira promatrača na {@link ILocalizationProvider}.
 * 
 * @author Ante Miličević
 *
 */
public interface ILocalizationListener {

	/**
	 * Metoda kojom se javlja da je došlo do promjene jezika.
	 */
	void localizationChanged();
}
