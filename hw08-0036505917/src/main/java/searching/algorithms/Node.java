package searching.algorithms;

/**
 * Razred modelira čvor stabla pretraživanja
 * 
 * @author Ante Miličević
 *
 * @param <S> tip podatka rješenja
 */
public class Node<S> {
	/**
	 * Stanje u kojem se čvor nalazi
	 */
	private S state;
	/**
	 * Cijena prelaska iz roditelja u ovaj čvor
	 */
	private int cost;
	/**
	 * Referenca na roditelja
	 */
	private Node<S> parent;

	/**
	 * Konstruktor koji inicijalizira sve parametre
	 * 
	 * @param state
	 * @param cost
	 * @param parent
	 */
	public Node(S state, int cost, Node<S> parent) {
		super();
		this.state = state;
		this.cost = cost;
		this.parent = parent;
	}

	/**
	 * Getter za stanje
	 * 
	 * @return
	 */
	public S getState() {
		return state;
	}

	/**
	 * Getter za cijenu prelaska
	 * 
	 * @return cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Getter za referencu na roditelja
	 * 
	 * @return parent
	 */
	public Node<S> getParent() {
		return parent;
	}

}
