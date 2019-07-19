package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji modelira naredbu koja pomiče kornjaču bez da ona ostavlja trag za
 * sobom. Kroz konstruktor prima duljinu tog skoka u odnosu na jediničnu duljinu
 * koraka.
 * 
 * @author Ante Miličević
 *
 */
public class SkipCommand implements Command {
	/**
	 * Koeficijent duljine koraka
	 */
	private final double skip;

	/**
	 * Konstruktor koji inicijalizira koeficijent duljine koraka
	 * 
	 * @param skip koeficijent duljine koraka
	 */
	public SkipCommand(double skip) {
		this.skip = skip;
	}

	@Override
	public void execute(Context context, Painter painter) {
		TurtleState state = context.getCurrentState();
		Vector2D shiftVector = state.getAngle().scaled(skip);

		state.getPosition().translate(shiftVector);
	}

}
