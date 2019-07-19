package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Objects;

/**
 * Razred čija je svrha spriječiti memory leak prilikom ostvarivanja
 * lokalizacije. Razred služi kao posrednik između implementacije
 * {@link ILocalizationProvider} i promatrača.
 * 
 * @author Ante Miličević
 *
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {

	/**
	 * Prava implementacija ILocalizationProvidera
	 */
	private ILocalizationProvider parent;
	/**
	 * Model promatrača koji je u funkciji dok je posrednik spojen
	 */
	private ILocalizationListener listener = () -> {
		language = parent.getLanguage();
		fire();
	};
	/**
	 * Zastavica koja označava da li je promatrač spojen na parent-a
	 */
	private boolean connected;
	/**
	 * Oznaka jezika pri zadnjem ažuriranju jezika
	 */
	private String language;

	/**
	 * Konstruktor
	 * 
	 * @param parent prava implementacija lokalizatora
	 */
	public LocalizationProviderBridge(LocalizationProvider parent) {
		this.parent = Objects.requireNonNull(parent);
		connect();
	}

	/**
	 * Metoda za odspajanje listenera od parenta
	 */
	public void disconnect() {
		if (!connected) {
			return;
		}
		parent.removeLocalizationListener(listener);
		connected = false;
	}

	/**
	 * Metoda za spajanje listenera na parenta i provjera
	 * promjene jezika od zadnjeg perioda spojenosti
	 */
	public void connect() {
		if (connected) {
			return;
		}
		parent.addLocalizationListener(listener);
		connected = true;
		language = parent.getLanguage();
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public String getString(String key) {
		return parent.getString(key);
	}

}
