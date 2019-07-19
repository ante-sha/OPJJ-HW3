package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Razred koji modelira naredbu koja briše trenutno stanje iz konteksta te
 * indirektno postavlja novo stanje koje se tada nalazi na vrhu stoga.
 * 
 * @author Ante Miličević
 *
 */
public class PopCommand implements Command {

	/**
	 * @throws EmptyStackException ako je {@code context} prazan
	 */
	@Override
	public void execute(Context context, Painter painter) {
		context.popState();
	}

}
