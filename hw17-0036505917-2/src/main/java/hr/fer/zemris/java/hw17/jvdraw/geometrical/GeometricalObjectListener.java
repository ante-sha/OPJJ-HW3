package hr.fer.zemris.java.hw17.jvdraw.geometrical;

/**
 * Sučelje koje definira metode promatrača geometrijskog objekta
 * {@link GeometricalObject}.
 * 
 * @author Ante Miličević
 *
 */
public interface GeometricalObjectListener {
	/**
	 * Metoda koja se poziva prilikom promjene geometrijskog objekta
	 * 
	 * @param o geometrijski objekt
	 */
	public void geometricalObjectChanged(GeometricalObject o);
}
