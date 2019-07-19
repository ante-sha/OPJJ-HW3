package hr.fer.zemris.java.hw17.prob1.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

/**
 * Razred koji modelira grupne podatke o dokumentima
 * 
 * @author Ante Miličević
 *
 */
public class DocumentsData {
	/**
	 * Vokabular
	 */
	private Set<String> vocabulary = new HashSet<>();
	/**
	 * Mapa (riječ, broj dokumenata koji sadrže tu riječ)
	 */
	private Map<String, Integer> globalParamMap = new TreeMap<>();
	/**
	 * Ukupni broj dokumenata
	 */
	private int numOfDocuments;
	/**
	 * IDF komponente vektora
	 */
	private double[] idfComponents;

	/**
	 * Konstruktor
	 * 
	 * @param dirOfFiles      staza do direktorija u kojem se nalaze dokumenti
	 * @param pathToStopWords staza do stop riječi
	 * @throws IOException ako čitanje datoteka ne uspije
	 */
	public DocumentsData(Path dirOfFiles, Path pathToStopWords) throws IOException {
		setVocabulary(dirOfFiles);
		removeStopWordsFromVocabulary(pathToStopWords);
		initKeysInGlobalParamMap();
	}

	/**
	 * Metoda za računanje idf komponente
	 */
	public void calculateIdfComponents() {
		int i = 0;
		idfComponents = new double[globalParamMap.size()];
		for (Integer p : globalParamMap.values()) {
			idfComponents[i++] = Math.log(1.0 * numOfDocuments / p);
		}
	}

	/**
	 * Inicijalizacija ključeva u mapi pojavljivanja riječi
	 */
	private void initKeysInGlobalParamMap() {
		vocabulary.forEach((word) -> {
			globalParamMap.put(word, 0);
		});
	}

	/**
	 * Metoda za uklanjanje stop riječi iz vokabulara
	 * 
	 * @param pathToStopWords staza do stop riječi
	 * @throws IOException ako čitanje ne uspije
	 */
	private void removeStopWordsFromVocabulary(Path pathToStopWords) throws IOException {
		vocabulary.removeAll(WordReader.readWordsFromFile(pathToStopWords));
	}

	/**
	 * Metoda za postavljanje vokabulara
	 * 
	 * @param dirOfFiles staza do direktorija s dokumentima
	 * @throws IOException ako čitanje dokumenata ne uspije
	 */
	private void setVocabulary(Path dirOfFiles) throws IOException {
		numOfDocuments = 0;

		try (Stream<Path> fileStream = Files.list(dirOfFiles)) {
			Iterator<Path> it = fileStream.iterator();
			while (it.hasNext()) {
				Path path = it.next();
				if (!Files.isReadable(path)) {
					continue;
				}
				vocabulary.addAll(WordReader.readWordsFromFile(path));
				numOfDocuments++;
			}
		}
	}

	/**
	 * Getter za vokabular
	 * 
	 * @return vocabulary
	 */
	public Set<String> getVocabulary() {
		return vocabulary;
	}

	/**
	 * Getter za mapu pojavljivanja riječi
	 * 
	 * @return globalParamMap
	 */
	public Map<String, Integer> getGlobalParamMap() {
		return globalParamMap;
	}

	/**
	 * Getter za ukupni broj dokumenata
	 * 
	 * @return numOfDocuments
	 */
	public int getNumOfDocuments() {
		return numOfDocuments;
	}

	/**
	 * Getter za idf komponente
	 * 
	 * @return idfComponents
	 */
	public double[] getIdfComponents() {
		return idfComponents;
	}
}
