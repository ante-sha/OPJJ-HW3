package hr.fer.zemris.java.custom.collections;

/**
 * Sučelje koji nam služi za generičku obradu razreda {@link Collection}.
 * 
 * @author Ante Miličević
 * @param <E> tip elemenat koje metoda {@link #process(E)} može obraditi
 */
@FunctionalInterface
public interface Processor<E> {
	/**
	 * Metoda koja obrađuje predani element
	 * 
	 * @param value predani element
	 */
	void process(E value);
}
