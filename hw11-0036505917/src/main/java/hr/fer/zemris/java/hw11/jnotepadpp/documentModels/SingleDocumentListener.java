package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

/**
 * Sučelje promatrača na {@link SingleDocumentModel}
 * 
 * @author Ante Miličević
 *
 */
public interface SingleDocumentListener {
	/**
	 * Metoda koja se poziva prilikom promjene statusa modificiranosti
	 * 
	 * @param model dokument koji je modificiran
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	/**
	 * Metoda koja se poziva kada se staza dokumenta mijenja
	 * 
	 * @param model dokument
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
