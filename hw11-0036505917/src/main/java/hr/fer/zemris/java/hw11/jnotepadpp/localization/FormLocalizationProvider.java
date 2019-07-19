package hr.fer.zemris.java.hw11.jnotepadpp.localization;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

/**
 * Razred koji služi izgradnji posrednika na implementaciju
 * {@link ILocalizationProvider} koji se prilikom zatvaranja prozora sam odspaja
 * od lokalizatora da bi postao materijal za garbage collector.
 * 
 * @author Ante Miličević
 *
 */
public class FormLocalizationProvider extends LocalizationProviderBridge implements WindowListener {

	/**
	 * Konstruktor
	 * 
	 * @param parent implementacija lokalizatora
	 * @param window prozor koji se služi ovim objektom
	 */
	public FormLocalizationProvider(LocalizationProvider parent, JFrame window) {
		super(parent);

		window.addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
		connect();
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		disconnect();
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

}
