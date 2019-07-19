package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Sučelje koje definira adaptera za JTextArea.
 * 
 * @author Ante Miličević
 *
 */
public interface SingleDocumentModel {
	/**
	 * Dohvat komponente u kojoj se nalazi tijelo dokumenta
	 * 
	 * @return textComponent
	 */
	JTextArea getTextComponent();

	/**
	 * Metoda za dohvat staze na kojoj se nalazi dokument
	 * 
	 * @return
	 */
	Path getFilePath();

	/**
	 * Metoda za postavljanje staze dokumenta
	 * 
	 * @param path staza
	 */
	void setFilePath(Path path);

	/**
	 * Metoda koja dohvaća zastavicu modificiranosti dokumenta
	 * 
	 * @return modified
	 */
	boolean isModified();

	/**
	 * Metoda koja postavlja zastavicu modificiranosti
	 * 
	 * @param modified
	 */
	void setModified(boolean modified);

	/**
	 * Metoda za dodavanje promatrača
	 * 
	 * @param l promatrač
	 */
	void addSingleDocumentListener(SingleDocumentListener l);

	/**
	 * Metoda za uklanjanje promatrača
	 * 
	 * @param l promatrač
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
}
