package searching.slagalica;

import java.util.Arrays;
import java.util.Objects;

/**
 * Razred koji modelira stanje slagalice veličine 3x3.
 * 
 * @author Ante Miličević
 *
 */
public class KonfiguracijaSlagalice {

	/**
	 * Spremnik za konfiguraciju slagalice
	 */
	private int[] polje;

	/**
	 * Fiksna veličina polja
	 */
	private static final int poljeLength = 9;

	/**
	 * Konstruktor koji inicijalizira polje
	 * 
	 * @param polje
	 * 
	 * @throws IllegalArgumentException ako predano polje nema duljinu jednaku
	 *                                  poljeLength
	 */
	public KonfiguracijaSlagalice(int[] polje) {
		if (Objects.requireNonNull(polje).length != poljeLength) {
			throw new IllegalArgumentException();
		}
		this.polje = polje;
	}

	/**
	 * Getter za polje
	 * 
	 * @return polje
	 */
	public int[] getPolje() {
		return Arrays.copyOf(polje, polje.length);
	}

	/**
	 * Metoda koja vraća indeks prvog pojavljivanja znaka '0'
	 * @return
	 */
	public int indexOfSpace() {
		for (int i = 0; i < poljeLength; i++) {
			if (polje[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(polje);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof KonfiguracijaSlagalice))
			return false;
		KonfiguracijaSlagalice other = (KonfiguracijaSlagalice) obj;
		return Arrays.equals(polje, other.polje);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < poljeLength; i++) {
			sb.append(polje[i] == 0 ? "*" : polje[i]);
			sb.append(i % 3 == 2 ? "\n" : " ");
		}
		return sb.toString().trim();
	}
}
