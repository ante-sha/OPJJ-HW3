package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Program za iscrtavanje fraktala nastalik Newton-Raphson-ovom iteracijom.
 * 
 * @author Ante Miličević
 *
 */
public class Newton {
	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.\n"
				+ "Please enter at least two roots, one root per line. Enter 'done' when done.");

		List<Complex> roots = null;
		try (Scanner sc = new Scanner(System.in)) {
			roots = readRoots(sc);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		if (roots.size() < 2) {
			System.err.println("You must enter at least 2 roots");
			System.exit(1);
		}

		ComplexRootedPolynomial original = new ComplexRootedPolynomial(new Complex(1, 0),
				roots.toArray(new Complex[roots.size()]));

		FractalViewer.show(new MojProducer(original));
	}

	/**
	 * Razred odgovoran za generiranje konvergencije za svaki piksel. Rezultat
	 * generiranja je polje koje sadrži 0 ako broj ne konvergira ili sadrži index
	 * korijena u kojeg konvergira. Provjera konvergencija se odvija višedretveno.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class MojProducer implements IFractalProducer {

		/**
		 * Manager višedretvenih zahtjeva
		 */
		private ExecutorService executor;
		/**
		 * Maksimalan broj iteracija pri traženju konvergencije
		 */
		public static int maxIter = 16 * 16;
		/**
		 * Tolerancija konvergencije
		 */
		public static double convergenceTreshold = 1e-3;
		/**
		 * Polinom preko kojega se traži konvergencija
		 */
		private ComplexRootedPolynomial original;

		/**
		 * Defaultni konstrukor koji stvara thread pool za izvođenje zadatka generiranja
		 * 
		 * @param original polinom nad kojim se provodi generiranje indeksa
		 *                 konvergencije
		 * 
		 * @throws NullPointerException ako je original null
		 */
		public MojProducer(ComplexRootedPolynomial original) {
			super();
			executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				}
			});
			this.original = Objects.requireNonNull(original);
		}

		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

			System.out.println("Starting...");

			ComplexPolynomial polynomial = original.toComplexPolynom();
			ComplexPolynomial derived = polynomial.derive();

			short[] data = new short[width * height];
			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0, n = 8 * Runtime.getRuntime().availableProcessors(); i < n; i++) {
				int stripesPerWorker = height / n;
				int yMin = i * stripesPerWorker;
				int yMax = (i + 1) * stripesPerWorker - 1;

				if (i + 1 == n) {
					yMax = height - 1;
				}

				results.add(executor.submit(new Posao(reMin, reMax, imMin, imMax, yMin, yMax, width, height, data,
						maxIter, convergenceTreshold, cancel, original, polynomial, derived)));
			}

			for (Future<Void> result : results) {
				while (true) {
					try {
						result.get();
					} catch (InterruptedException | ExecutionException e) {
						continue;
					}
					break;
				}

			}

			System.out.println("Done, rendering image...");
			observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
		}

	}

	/**
	 * Razred koji modelira posao generiranja "index-a" konvergencije
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class Posao implements Callable<Void> {

		/**
		 * Minimalan realni broj prikazan u prozoru
		 */
		double reMin;
		/**
		 * Maksimalan realan broj prikazan u prozoru
		 */
		double reMax;
		/**
		 * Minimalan imaginarni broj prikazan u prozoru
		 */
		double imMin;
		/**
		 * Maksimalan imaginaran broj prikazan u prozoru
		 */
		double imMax;
		/**
		 * Visina od koje ovaj posao generira indekse konvergencije
		 */
		int yMin;
		/**
		 * Visina do koje ovaj posao generira indekse konvergencije
		 */
		int yMax;
		/**
		 * Širina prozora
		 */
		int width;
		/**
		 * Visina prozora
		 */
		int height;
		/**
		 * Polje u kojem se spremaju indeksi konvergencije
		 */
		short[] data;
		/**
		 * Maksimalan broj iteracija
		 */
		int maxIter;
		/**
		 * Tolerancija
		 */
		double convergenceTreshold;
		/**
		 * Zastavica za zastaru rezultata
		 */
		AtomicBoolean cancel;
		/**
		 * Polinom nad kojim se testiraju konvergencije faktoriziran po korijenima
		 */
		private ComplexRootedPolynomial original;
		/**
		 * Polinom nad kojim se testiraju konvergencije faktoriziran po potencijama
		 */
		private ComplexPolynomial polynomial;
		/**
		 * Derivirani polynom
		 */
		private ComplexPolynomial derived;

		/**
		 * Konstruktor koji inicijalizira sve parametre potrebne za izračunavanje
		 * indeksa korijena u koji kompleksan broj kovergira
		 * 
		 * @param reMin
		 * @param reMax
		 * @param imMin
		 * @param imMax
		 * @param yMin
		 * @param yMax
		 * @param width
		 * @param height
		 * @param data
		 * @param maxIter
		 * @param convergenceTreshold
		 * @param cancel
		 * @param original
		 * @param polynomial
		 * @param derived
		 */
		public Posao(double reMin, double reMax, double imMin, double imMax, int yMin, int yMax, int width, int height,
				short[] data, int maxIter, double convergenceTreshold, AtomicBoolean cancel,
				ComplexRootedPolynomial original, ComplexPolynomial polynomial, ComplexPolynomial derived) {
			super();
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.yMin = yMin;
			this.yMax = yMax;
			this.width = width;
			this.height = height;
			this.data = data;
			this.maxIter = maxIter;
			this.convergenceTreshold = convergenceTreshold;
			this.cancel = cancel;
			this.original = original;
			this.polynomial = polynomial;
			this.derived = derived;
		}

		@Override
		public Void call() throws Exception {
			int offset = yMin * width;
			for (int y = yMin; y <= yMax; y++) {
				if (cancel.get()) {
					break;
				}
				for (int x = 0; x < width; x++) {
					Complex zn = mapToComplexPlain(x, y, width, height, reMin, reMax, imMin, imMax);
					Complex zn1 = null;

					int i = 0;
					double module = 0;
					do {
						Complex numerator = polynomial.apply(zn);
						Complex denominator = derived.apply(zn);
						Complex fraction = numerator.divide(denominator);

						zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						zn = zn1;
						i++;
					} while (Math.abs(module) > convergenceTreshold && i < maxIter);
					int index = original.indexOfClosestRootFor(zn1, convergenceTreshold);

					data[offset++] = index == -1 ? 0 : Integer.valueOf(index).shortValue();
				}
			}
			return null;
		}

		/**
		 * Metoda koja mapira x i y koji su zadani u prozoru u njihovu kompleksnu
		 * vrijednost.
		 * 
		 * @param x      relativna širina piksela
		 * @param y      relativna visina piksela
		 * @param width  širina prozora
		 * @param height visina prozora
		 * @param reMin  vrijednost realnog dijela kompleksnog broja pri x = 0
		 * @param reMax  vrijednost realnog dijela kompleksnog broja pri x = width - 1
		 * @param imMin  vrijednost imaginarnog dijela kompleksnog broja pri y = height
		 *               - 1
		 * @param imMax  vrijednost imaginarnog dijela kompleksnog broja pri y = 0
		 * 
		 * @return kompleksna reprezentacija x i y
		 */
		private static Complex mapToComplexPlain(int x, int y, int width, int height, double reMin, double reMax,
				double imMin, double imMax) {
			double re = (reMax - reMin) * x / width + reMin;
			double im = (imMin - imMax) * y / height + imMax;
			return new Complex(re, im);
		}

	}

	/**
	 * Metoda koja kroz sc čita kompleksne brojeve u zapisu "a+ib" i sprema ih u
	 * listu te ih vraća kroz rezultat.
	 * 
	 * @param sc ulazni stream za čitanje
	 * @return lista kompleksnih brojeva
	 */
	private static List<Complex> readRoots(Scanner sc) {
		List<Complex> roots = new ArrayList<>();
		while (true) {
			System.out.printf("Root %d> ", roots.size() + 1);
			String s = sc.nextLine();
			if (s.trim().equals("done")) {
				break;
			}
			try {
				roots.add(convertComplex(s));
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}

		return roots;
	}

	/**
	 * Metoda za pretvaranje kompleksnog broja formata "a+ib" u njegovu
	 * {@link Complex} reprezentaciju.
	 * 
	 * @param string string koji sadrži kompleksni broj
	 * @return kompleksni broj u Complex reprezentaciji
	 * 
	 * @throws IllegalArgumentException ako string ne sadrži korijen u dobrom
	 *                                  formatu
	 */
	private static Complex convertComplex(String string) {
		double re, im;
		// brisanje razmaka koji se ne nalaze direktno između brojeva
		string = string.replaceAll("([0-9])\\s+([^0-9|i])", "$1$2");
		string = string.replaceAll("([^0-9])\\s+([0-9|i])", "$1$2");
		// regex za kompleksne brojeve s obje komponente
		if (string.matches("^[+|-]?[0-9]+\\.?[0-9]*[+|-]i([0-9]+\\.?[0-9]*)?$")) {
			String separateParts = string.replaceAll("^([+|-]?[0-9]+\\.?[0-9]*)([+|-]i([0-9]+\\.?[0-9]*)?)$", "$1 $2");
			String[] numbers = separateParts.split(" ");

			re = Double.parseDouble(numbers[0]);
			im = parseComplex(numbers[1]);

			// regex za kompleksne brojeve koji imaju samo realni dio
		} else if (string.matches("^[+|-]?[0-9]+\\.?[0-9]*$")) {
			re = Double.parseDouble(string);
			im = 0;

			// regex za kompleksne brojeve koji imaju samo imaginarni dio
		} else if (string.matches("^[+|-]?i([0-9]+\\.?[0-9]*)?$")) {
			re = 0;
			im = parseComplex(string.replaceAll("i", ""));
		} else
			throw new IllegalArgumentException(String.format("%s is not in format Re{z}+iIm{z}", string));

		return new Complex(re, im);
	}

	/**
	 * Metoda koja prebacuje string koji stoji uz znak 'i' u njegovu numerički
	 * reprezentaciju
	 * 
	 * @param string string koji stoji uz i
	 * @return numerička reprezentacija
	 */
	private static double parseComplex(String string) {
		double im;
		try {
			im = Double.parseDouble(string);
		} catch (NumberFormatException e) {
			if (string.contains("-"))
				im = -1;
			else
				im = 1;
		}
		return im;
	}
}
