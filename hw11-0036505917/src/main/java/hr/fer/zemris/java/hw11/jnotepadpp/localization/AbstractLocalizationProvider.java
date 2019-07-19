package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Apstraktni model lokalizatora koji implementira podršku za promatrače i
 * njihovo obavještavanje.
 * 
 * @author Ante Miličević
 *
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	/**
	 * Skup promatrača
	 */
	private List<ILocalizationListener> listeners;

	/**
	 * Konstruktor
	 */
	public AbstractLocalizationProvider() {
		listeners = new CopyOnWriteArrayList<>();
	}

	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if (listeners.contains(listener) || listener == null) {
			return;
		}
		listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Metoda za obavještavanje promatrača o promjeni
	 */
	public void fire() {
		listeners.forEach((listener) -> listener.localizationChanged());
	}
}
