package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Razred koji modelira naredbu koja uvećava jediničnu duljinu koraka kornjače
 * za faktor predan u konstruktoru.
 * 
 * @author Ante Miličević
 *
 */
public class ScaleCommand implements Command {
	/**
	 * Faktor uvećanja jedinične duljine koraka
	 */
	private final double factor;

	/**
	 * Konstruktor koji inicijalizira faktor uvećanja jedinične duljine koraka
	 * 
	 * @param factor faktor uvećanja
	 */
	public ScaleCommand(double factor) {
		this.factor = factor;
	}

	@Override
	public void execute(Context context, Painter painter) {
		TurtleState stanje = context.getCurrentState();
		stanje.setUnitLength(stanje.getUnitLength() * factor);
	}

}
