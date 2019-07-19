package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import javax.swing.JPanel;

/**
 * Razred koji definira ponašanje komponente za uređivanje
 * podataka o geometrijskom objektu.
 * 
 * @author Ante Miličević
 *
 */
public abstract class GeometricalObjectEditor extends JPanel {
	private static final long serialVersionUID = 1L;

	/**
	 * Metoda za provjeru validnosti podataka kod uređivanja
	 * 
	 * @throws RuntimeException ako podaci nisu ispravni
	 */
	public abstract void checkEditing() throws RuntimeException;
	
	/**
	 * Metoda za upisivanje podataka iz formulara u geometrijski objekt
	 */
	public abstract void acceptEditing();
}
