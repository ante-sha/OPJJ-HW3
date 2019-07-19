package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Razred koji modelira naredbu koja rotira jedinični vektor orijentacije
 * trenutnog stanja u kontekstu za kut koji je predan u konstruktoru. Kut se
 * tumači u radijanima.
 * 
 * @author Ante Miličević
 *
 */
public class RotateCommand implements Command {
	/**
	 * Kut rotacije
	 */
	private final double angle;

	/**
	 * Konstruktor koji definira kut rotacije
	 * 
	 * @param angle kut rotacije
	 */
	public RotateCommand(double angle) {
		this.angle = angle;
	}

	@Override
	public void execute(Context context, Painter painter) {
		TurtleState state = context.getCurrentState();
		state.getAngle().rotate(angle);
	}

}
