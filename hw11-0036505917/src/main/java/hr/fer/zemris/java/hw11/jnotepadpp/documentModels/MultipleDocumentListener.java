package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

/**
 * Sučelje promatrača na {@link MultipleDocumentModel}.
 * 
 * @author Ante Miličević
 *
 */
public interface MultipleDocumentListener {
	/**
	 * Metoda koja se poziva prilikom promjena trenutnog dokumenta u fokusu
	 * 
	 * @param previousModel prethodni dokument
	 * @param currentModel trenutni dokument
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	/**
	 * Metoda koja se poziva prilikom dodavanja novog dokumenta
	 * 
	 * @param model dodani dokument
	 */
	void documentAdded(SingleDocumentModel model);
	/**
	 * Metoda koja se poziva prilikom uklanjanja dokumenta
	 * 
	 * @param model uklonjeni dokument
	 */
	void documentRemoved(SingleDocumentModel model);
}
