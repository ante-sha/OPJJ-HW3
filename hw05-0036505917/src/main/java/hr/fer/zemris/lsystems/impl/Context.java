package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;

/**
 * Razred koji implementira stog koji prihvaća samo {@link TurtleState} te uz
 * metode pushState, popState nudi i getCurrentState metodu koja vraća stanje s
 * vrha stoga.
 * 
 * @author Ante Miličević
 *
 */
public class Context {
	/**
	 * Varijabla u kojoj se čuvaju stanja
	 */
	private ObjectStack<TurtleState> stack = new ObjectStack<>();

	/**
	 * Metoda koja vraća stanje sa vrha stoga (trenutno stanje)
	 * 
	 * @return stanje s vrha stoga
	 * 
	 * @throws EmptyStackException ako je stog prazan
	 */
	public TurtleState getCurrentState() {
		return stack.peek();
	}

	/**
	 * Stavlja {@code state} na stog
	 * 
	 * @param state stanje koje se stavlja na stog
	 * 
	 * @throws NullPointerException ako je {@code state} null
	 */
	public void pushState(TurtleState state) {
		stack.push(state);
	}

	/**
	 * Briše stanje s vrha stoga
	 * 
	 * @throws EmptyStackException ako je stog prazan
	 */
	public void popState() {
		stack.pop();
	}
}
