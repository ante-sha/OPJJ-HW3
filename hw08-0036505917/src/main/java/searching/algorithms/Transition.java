package searching.algorithms;

/**
 * Razred koji modelira prijelaz u stanje {@code state} po cijeni {@code cost}
 * 
 * @author Ante Miličević
 *
 * @param <S> tip podatka rješenja
 */
public class Transition<S> {

	/**
	 * Stanje u koje se prelazi prilikom tranzicije
	 */
	private S state;
	/**
	 * Cijena prelaska
	 */
	private int cost;

	/**
	 * Konstruktor koji inicijalizira sve podakte
	 * 
	 * @param state
	 * @param cost
	 */
	public Transition(S state, int cost) {
		super();
		this.state = state;
		this.cost = cost;
	}

	/**
	 * Getter za stanje u koje se prelazi
	 * 
	 * @return state
	 */
	public S getState() {
		return state;
	}

	/**
	 * Getter za cijenu prelaska tranzicije
	 * 
	 * @return cijena prelaska tranzicije
	 */
	public int getCost() {
		return cost;
	}

}
