package hr.fer.zemris.lsystems.impl.commands;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.TurtleState;

/**
 * Razred koji modelira naredbu koja mijenja boju trenutnog stanja kornjače u
 * onu zadanu u konstruktoru.
 * 
 * @author Ante Miličević
 *
 */
public class ColorCommand implements Command {
	/**
	 * Boja u koju se mijenja boja trenutnog stanja
	 */
	private final Color color;

	/**
	 * Konstruktor koji definira boju u koje će se mijenjati boja iz trenutnog
	 * stanja u kontekstu.
	 * 
	 * @param color boja
	 * 
	 * @throws NullPointerException ako je {@code color} null
	 */
	public ColorCommand(Color color) {
		this.color = Objects.requireNonNull(color);
	}

	@Override
	public void execute(Context context, Painter painter) {
		TurtleState state = context.getCurrentState();

		state.setColor(color);
	}

}
