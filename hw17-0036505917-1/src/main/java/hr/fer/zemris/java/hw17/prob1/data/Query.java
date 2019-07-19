package hr.fer.zemris.java.hw17.prob1.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Razred koji modelira podatke o upitu
 * 
 * @author Ante Miličević
 *
 */
public class Query {
	/**
	 * Mapa broja riječi
	 */
	private Map<String, Integer> wordCount;
	/**
	 * Vektor upita
	 */
	private double[] vector;

	/**
	 * Konstruktor
	 * 
	 * @param body tijelo upita
	 * @param vocabulary vokabular
	 */
	public Query(String body, Set<String> vocabulary) {
		countWords(body, vocabulary);
	}

	/**
	 * Prebrojavanje riječi
	 * 
	 * @param body tijelo upita
	 * @param vocabulary vokabular
	 */
	private void countWords(String body, Set<String> vocabulary) {
		wordCount = new LinkedHashMap<>();
		WordReader.countWordsInString(wordCount, body);
		
		wordCount.keySet().removeIf((word)->!vocabulary.contains(word));
	}
	
	/**
	 * Građenje vektora
	 * 
	 * @param dData podaci o svim dokumentima
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
	 * Getter za broj riječi
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
}
