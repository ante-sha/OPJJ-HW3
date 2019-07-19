package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji modelira naredbu koja iscrtava liniju duljine
 * {@code step}*{@link TurtleState#getUnitLength()} i orijentacije
 * {@link TurtleState#getAngle()} bojom koja je definirana u trenutnom stanju.
 * 
 * @author Ante Miličević
 *
 */
public class DrawCommand implements Command {
	/**
	 * Postotak jedinične duljine za iscrtavanje
	 */
	private final double step;

	/**
	 * Konstruktor koji inicijalizira postotak jediničnu duljinu za iscrtavanje
	 * 
	 * @param step postotak jedinične duljine za iscrtavanje
	 */
	public DrawCommand(double step) {
		this.step = step;
	}

	@Override
	public void execute(Context context, Painter painter) {
		TurtleState state = context.getCurrentState();
		double shift = state.getUnitLength() * step;

		Vector2D shiftVector = state.getAngle().scaled(shift);
		Vector2D startVector = state.getPosition().copy();

		state.getPosition().translate(shiftVector);

		painter.drawLine(startVector.getX(), startVector.getY(), state.getPosition().getX(), state.getPosition().getY(),
				state.getColor(), 1f);

	}

}
