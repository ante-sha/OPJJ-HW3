package hr.fer.zemris.java.hw17.prob1.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Razred odgovoran za sve vrste čitanja dokumenta
 * 
 * @author Ante Miličević
 *
 */
public class WordReader {
	/**
	 * Metoda za čitanje skupa riječi
	 * 
	 * @param path staza do dokumenta
	 * @return skup riječi
	 * @throws IOException ako čitanje dokumenta ne uspije
	 */
	public static Set<String> readWordsFromFile(Path path) throws IOException {
		BufferedReader reader = Files.newBufferedReader(path);
		Set<String> result = new HashSet<>();
		
		while(true) {
			String line = reader.readLine();
			if(line == null) {
				break;
			}
			
			WordParser.parseString(line.toLowerCase()).forEach((word)->{
				if(word.isEmpty()) {
					return;
				}
				result.add(word);
			});
		}
		
		return result;
	}
	
	/**
	 * Metoda za prebrojavanje riječi u dokumentu
	 * 
	 * @param wordCount mapa (riječ, broj ponavljanja)
	 * @param pathToFile staza do dokumenta
	 * @throws IOException ako čitanje ne uspije
	 */
	public static void countWordsInFile(Map<String, Integer> wordCount, Path pathToFile) throws IOException {
		BufferedReader reader = Files.newBufferedReader(pathToFile);
		
		while(true) {
			String line = reader.readLine();
			if(line == null) {
				break;
			}
			
			WordParser.parseString(line.toLowerCase()).forEach((word)->{
				if(word.isEmpty()) {
					return;
				}
				wordCount.compute(word, (key, value) ->{
					if(value == null) {
						value = 0;
					}
					return value + 1;
				});
			});
		}
	}
	
	/**
	 * Metoda za prebrojavanje riječi u tekstu
	 * 
	 * @param wordCount mapa (riječ, broj ponavljanja)
	 * @param string tekst
	 */
	public static void countWordsInString(Map<String, Integer> wordCount, String string) {
		WordParser.parseString(string.toLowerCase()).forEach((word)->{
			if(word.isEmpty()) {
				return;
			}
			wordCount.compute(word, (key, value) ->{
				if(value == null) {
					value = 0;
				}
				return value + 1;
			});
		});
	}
}
