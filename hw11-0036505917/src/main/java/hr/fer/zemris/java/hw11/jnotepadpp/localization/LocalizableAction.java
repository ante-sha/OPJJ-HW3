package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Razred koji predstavlja apstraktni model lokalizirane akcije.
 * 
 * @author Ante Miličević
 *
 */
public abstract class LocalizableAction extends AbstractAction {

	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Lokalizator
	 */
	private ILocalizationProvider prov;
	/**
	 * Ključ pod kojim je spremljeno ime akcije
	 */
	private String keyName;
	/**
	 * Ključ pod kojim je spremljen opis akcije
	 */
	private String keyDesc;

	/**
	 * Konstruktor
	 * 
	 * @param keyName ključ naziva akcije
	 * @param prov    lokalizator
	 * @param keyDesc ključ opisa akcije ili null ako se ne koristi
	 */
	public LocalizableAction(String keyName, String keyDesc, ILocalizationProvider prov) {
		this.keyName = Objects.requireNonNull(keyName);
		this.prov = Objects.requireNonNull(prov);
		this.keyDesc = keyDesc;

		putValue(Action.NAME, prov.getString(keyName));
		if (keyDesc != null) {
			putValue(Action.SHORT_DESCRIPTION, prov.getString(keyDesc));
		}

		prov.addLocalizationListener(() -> {
			putValue(Action.NAME, LocalizableAction.this.prov.getString(LocalizableAction.this.keyName));
			if (LocalizableAction.this.keyDesc != null) {
				putValue(Action.SHORT_DESCRIPTION,  LocalizableAction.this.prov.getString(LocalizableAction.this.keyDesc));
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	}

}
