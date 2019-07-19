package hr.fer.zemris.java.hw17.prob1.data;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Razred koji modelira jedan dokument
 * 
 * @author Ante Miličević
 *
 */
public class Document {
	/**
	 * Mapa broja pojavljivanje riječi
	 */
	private Map<String, Integer> wordCount = new HashMap<>();
	/**
	 * Vektor koji predstavlja dokument
	 */
	private double[] vector;
	/**
	 * Staza do dokumenta
	 */
	private Path pathToFile;
	
	/**
	 * Konstruktor
	 * 
	 * @param pathToFile staza do dokumenta
	 */
	public Document(Path pathToFile) {
		this.pathToFile = Objects.requireNonNull(pathToFile);
	}
	
	/**
	 * Metoda ažuriranje podataka globalne mape temeljem podataka
	 * ovog dokumenta
	 * 
	 * @param dData grupni podaci dokumenata
	 */
	public void updateDocumentsData(DocumentsData dData) {
		Map<String, Integer> paramMap = dData.getGlobalParamMap();
		wordCount.keySet().forEach((word)-> {
			paramMap.compute(word, (key, value) -> {
				if (value == null) {
					throw new RuntimeException("Error word " + word + " is not vocabulary");
				}
				return value + 1;
			});
		});
	}
	
	/**
	 * Metoda za izgradnju vektora
	 * 
	 * @param dData grupni podaci dokumenta
	 */
	public void buildVector(DocumentsData dData) {
		Map<String, Integer> paramMap = dData.getGlobalParamMap();
		double[] idfComponents = dData.getIdfComponents();
		int i = 0;
		vector = new double[paramMap.size()];
		for(Map.Entry<String, Integer> entry : paramMap.entrySet()) {
			
			Integer n = wordCount.get(entry.getKey());
			n = n == null ? 0 : n;
			vector[i] = n * idfComponents[i];
			i++;
		}
	}
	
	/**
	 * Metoda za prebrajanje riječi
	 * 
	 * @throws IOException ako čitanje datoteke ne uspije
	 */
	public void countWords() throws IOException {
		WordReader.countWordsInFile(wordCount, pathToFile);
	}

	/**
	 * Getter za broj riječi
	 * 
	 * @return wordCount
	 */
	public Map<String, Integer> getWordCount() {
		return wordCount;
	}

	/**
	 * Getter za vektor
	 * 
	 * @return vector
	 */
	public double[] getVector() {
		return vector;
	}

	/**
	 * Getter za stazu do dokumenta
	 * 
	 * @return pathToFile
	 */
	public Path getPathToFile() {
		return pathToFile;
	}
}
