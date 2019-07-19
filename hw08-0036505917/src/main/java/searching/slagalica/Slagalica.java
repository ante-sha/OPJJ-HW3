package searching.slagalica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import searching.algorithms.Transition;

/**
 * Razred koji implementira uređenu četvorku potrebnu za rješavanje problema
 * slaganja slagalice definiranom {@link KonfiguracijaSlagalice}. Parametri
 * slagalice su početno stanje i predodređeno ciljno stanje [1,2,3,4,5,6,7,8,0]
 * (gdje '0' označava prazninu).
 * 
 * @author Ante Miličević
 *
 */
public class Slagalica implements Supplier<KonfiguracijaSlagalice>,
		Function<KonfiguracijaSlagalice, List<Transition<KonfiguracijaSlagalice>>>, Predicate<KonfiguracijaSlagalice> {

	/**
	 * Početno stanje
	 */
	private KonfiguracijaSlagalice startState;

	/**
	 * Defaultno ciljno stanje
	 */
	private KonfiguracijaSlagalice finalState = new KonfiguracijaSlagalice(new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 0 });

	/**
	 * Konstruktor koji inicijalizira početno stanje
	 * 
	 * @param startState
	 */
	public Slagalica(KonfiguracijaSlagalice startState) {
		this.startState = startState;
	}

	@Override
	public boolean test(KonfiguracijaSlagalice t) {
		int[] array1 = finalState.getPolje();
		int[] array2 = t.getPolje();

		for (int i = 0, n = array1.length; i < n; i++) {
			if (array1[i] != array2[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Metoda označava napredak u rješavanju slagalice (tj. jedan pomak). Moguća
	 * napredovanja ovise o poziciji praznine. Ako se praznina ne nalazi uz desni
	 * rub tada je moguće iz trenutne konfiguracije preći u konfiguraciju gdje će
	 * praznina i član koji se nalazi lijevo od nje zamjeniti mjesta. Ista stvar se
	 * događa i za preostale rubove slagalice.
	 */
	@Override
	public List<Transition<KonfiguracijaSlagalice>> apply(KonfiguracijaSlagalice t) {
		int index = t.indexOfSpace();
		List<Transition<KonfiguracijaSlagalice>> result = new ArrayList<>();
		int[] polje = t.getPolje();
		// ako nije u prvom retku
		if (index > 2) {
			int[] arrCopy = generateReplacedArray(polje, index, index - 3);
			result.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(arrCopy), 1));
		}
		// ako nije u zadnjem retku
		if (index < 6) {
			int[] arrCopy = generateReplacedArray(polje, index, index + 3);
			result.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(arrCopy), 1));
		}
		// ako nije u prvom stupcu
		if (index % 3 > 0) {
			int[] arrCopy = generateReplacedArray(polje, index, index - 1);
			result.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(arrCopy), 1));
		}
		// ako nije u zadnjem stupcu
		if (index % 3 < 2) {
			int[] arrCopy = generateReplacedArray(polje, index, index + 1);
			result.add(new Transition<KonfiguracijaSlagalice>(new KonfiguracijaSlagalice(arrCopy), 1));
		}
		return result;
	}

	/**
	 * Metoda stvara novo polje brojeva gdje se na mjestu index nalazi broj s mjesta
	 * replacement, a na mjestu replacement se nalazi 0.
	 * 
	 * @param array       polje nad čijom kopijom vršimo operaciju
	 * @param index       mjesto na koje stavljamo broj s mjesta replacement
	 * @param replacement mjesto na koje stavljamo 0
	 * @return izgenerirano polje
	 */
	private int[] generateReplacedArray(int[] array, int index, int replacement) {
		int[] arrCopy = Arrays.copyOf(array, array.length);
		arrCopy[index] = arrCopy[replacement];
		arrCopy[replacement] = 0;
		return arrCopy;
	}

	@Override
	public KonfiguracijaSlagalice get() {
		return startState;
	}

}
