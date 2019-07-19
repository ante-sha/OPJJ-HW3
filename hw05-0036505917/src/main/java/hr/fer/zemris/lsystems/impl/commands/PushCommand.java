package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji modelira naredbu koja duplicira trenutno stanje kornače iz
 * konteksta i stavlja ga na vrh stoga.
 * 
 * @author Ante Miličević
 *
 */
public class PushCommand implements Command {

	@Override
	public void execute(Context context, Painter painter) {
		context.pushState(context.getCurrentState().copy());
	}

}
