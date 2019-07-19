package hr.fer.zemris.java.hw17.prob1.shell.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw17.prob1.data.Document;
import hr.fer.zemris.java.hw17.prob1.data.Query;
import hr.fer.zemris.java.hw17.prob1.shell.Shell;
import hr.fer.zemris.java.hw17.prob1.shell.ShellCommand;
import hr.fer.zemris.java.hw17.prob1.shell.ShellException;

/**
 * Razred koji implementira metode kojima se koriste naredbe {@link ShellCommand}.
 * @author Ante Miličević
 *
 */
public class CommandUtil {
	
	/**
	 * Metoda za ispis rezultata upita
	 * 
	 * @param query upit
	 * @param printer objekt za ispis
	 */
	public static void printResults(Query query, PrintStream printer) {
		if(Shell.lastQuery == null) {
			throw new ShellException("Query is not set");
		}
		int n = 0;
		List<Double> similarity = new ArrayList<>();
		
		for(Document doc : Shell.documents) {
			double cos = calculateCos(query.getVector(), doc.getVector());
			if(n >= 10 || cos  < 1E-6) {
				break;
			}
			similarity.add(cos);
			n++;
		}
		
		if(n == 0) {
			printer.println("Nema rezultata");
			return;
		}
		printer.printf("Najboljih %d rezultata%n", n);
		
		for(int i = 0; i < n ; i++) {
			printer.printf("[%d] (%.4f) %s%n", i, similarity.get(i), Shell.documents.get(i).getPathToFile().normalize().toAbsolutePath());
		}
	}
	
	/**
	 * Metoda za izračun kosinusa između dva vektora
	 * 
	 * @param vector1 prvi vektor
	 * @param vector2 drugi vektor
	 * @return kosinus kuta između vektora
	 */
	public static double calculateCos(double[] vector1, double[] vector2) {
		if(vector1.length != vector2.length) {
			throw new ShellException("Vectors are corrupted");
		}
		int sum1 = 0;
		int sum2 = 0;
		int numerator = 0;
		for(int i = 0; i < vector1.length; i++) {
			numerator += vector1[i] * vector2[i];
			sum1 += vector1[i] * vector1[i];
			sum2 += vector2[i] * vector2[i];
		}
		
		return numerator / Math.sqrt(sum1 * sum2);
	}
}
