package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.util.Objects;

import javax.swing.JLabel;

/**
 * Razred koji predstavlja lokaliziranu labelu.
 * 
 * @author Ante Miličević
 *
 */
public class LJLabel extends JLabel {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ključ pod kojim se sprema prijevod za labelu
	 */
	protected String key;
	/**
	 * Lokalizator
	 */
	private ILocalizationProvider prov;

	/**
	 * Konstrukor
	 * 
	 * @param key  ključ
	 * @param prov lokalizator
	 */
	public LJLabel(String key, ILocalizationProvider prov) {
		this.key = Objects.requireNonNull(key);
		this.prov = Objects.requireNonNull(prov);

		updateLabel();

		prov.addLocalizationListener(() -> {
			updateLabel();
		});
	}

	/**
	 * Metoda koja se poziva prilikom promjene jezika lokalizatora. Postavlja
	 * vrijednost labele na vrijednost asociranu s ključem u trenutnom jeziku.
	 */
	private void updateLabel() {
		setText(prov.getString(key));
	}
}
