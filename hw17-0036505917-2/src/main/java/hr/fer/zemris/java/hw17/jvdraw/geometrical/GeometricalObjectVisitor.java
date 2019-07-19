package hr.fer.zemris.java.hw17.jvdraw.geometrical;

import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Circle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.FilledCircle;
import hr.fer.zemris.java.hw17.jvdraw.geometrical.impls.Line;

/**
 * Sučelje koje definira visitora za geometrijske objekte.
 * 
 * @author Ante Miličević
 *
 */
public interface GeometricalObjectVisitor {
	/**
	 * Metoda koja se poziva za obradu dužine
	 * 
	 * @param line dužina
	 */
	public abstract void visit(Line line);
	/**
	 * Metoda koja se poziva za obradu kružnice
	 * 
	 * @param circle kružnica
	 */
	public abstract void visit(Circle circle);
	/**
	 * Metoda koja se poziva za obradu kruga
	 * 
	 * @param filledCircle krug
	 */
	public abstract void visit(FilledCircle filledCircle);
}
