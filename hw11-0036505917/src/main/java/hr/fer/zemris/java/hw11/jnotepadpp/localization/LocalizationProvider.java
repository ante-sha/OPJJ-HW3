package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Razred koji omogućava jedinstvenu lokalizaciju cijelog programa (model OO
 * Singleton). Defaultni jezik je engleski. Jezici se čitaju iz paketa
 * "hr.fer.zemris.java.hw11.jnotepadpp.localization" te moraju biti oblika
 * "translation_[oznaka jezka].properties].
 * 
 * @author Ante Miličević
 *
 */
public class LocalizationProvider extends AbstractLocalizationProvider {

	/**
	 * Oznaka jezika
	 */
	private String language;
	/**
	 * Razred koji parsira i dohvaća prijevode
	 */
	private ResourceBundle bundle;
	/**
	 * Primjerak razreda koji osigurava da razred implementira OO Singleton
	 */
	private static LocalizationProvider instance;
	/**
	 * Defaultni jezik
	 */
	private static final String defaultLanguage = "en";

	/**
	 * Konstruktor
	 */
	private LocalizationProvider() {
		setLanguage(defaultLanguage);
	}

	/**
	 * Metoda koja pri prvom pozivu stvara primjerak razreda, a kasnije ga samo
	 * prosljeđuje
	 * 
	 * @return instance
	 */
	public static LocalizationProvider getInstance() {
		if (instance == null) {
			instance = new LocalizationProvider();
		}
		return instance;
	}

	/**
	 * Metoda koja postavlja trenutni jezik. Ako se jezik mijenja svi
	 * promatrači se obavještavaju.
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		if (this.language != null && this.language.equals(language)) {
			return;
		}

		Locale locale = Locale.forLanguageTag(language);
		bundle = ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.localization.translation", locale);
		this.language = language;

		fire();
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return bundle.getString(key);
	}

}
