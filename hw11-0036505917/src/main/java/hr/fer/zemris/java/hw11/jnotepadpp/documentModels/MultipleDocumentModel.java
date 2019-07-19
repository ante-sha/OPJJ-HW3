package hr.fer.zemris.java.hw11.jnotepadpp.documentModels;

import java.nio.file.Path;

/**
 * Sučelje koje definira ponašanje razreda s više modela dokumenta
 * {@link SingleDocumentModel} koji omogućava sve manipulacije dokumentom.
 * 
 * @author Ante Miličević
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	/**
	 * Metoda za stvaranje novog dokumenta
	 * 
	 * @return stvoreni dokument
	 */
	SingleDocumentModel createNewDocument();

	/**
	 * Metoda za dohvat trenutno fokusiranog dokumenta
	 * 
	 * @return trenutni dokument
	 */
	SingleDocumentModel getCurrentDocument();

	/**
	 * Metoda za učitavanje dokumenta koji se nalazi na stazi path.
	 * 
	 * @param path staza do dokumenta
	 * @return učitani dokument
	 */
	SingleDocumentModel loadDocument(Path path);

	/**
	 * Metoda za spremanje dokumenta model na stazu newPath.
	 * 
	 * @param model   dokument koji se sprema
	 * @param newPath staza na koju se dokument pokušava spremiti
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);

	/**
	 * Metoda za zatvaranje dokumenta model
	 * 
	 * @param model dokument koji se zatvara
	 */
	void closeDocument(SingleDocumentModel model);

	/**
	 * Metoda za dodavanje promatrača
	 * 
	 * @param l promatrač
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Metoda za uklanjanje promatrača
	 * 
	 * @param l promatrač
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);

	/**
	 * Metoda za dohvat trenutnog broja dokumenata
	 * 
	 * @return broj dokumenata
	 */
	int getNumberOfDocuments();

	/**
	 * Metoda za dohvat dokumenta koji se nalazi na indeksu index
	 * 
	 * @param index indeks dokumenta
	 * @return dokument
	 */
	SingleDocumentModel getDocument(int index);
}
