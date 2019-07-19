package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.Painter;

/**
 * Sučelje koje opisuje naredbu koja mijenja {@link Context#getCurrentState()} i
 * ako je potrebno preko {@link Painter}-a iscrtava naredbu. Svi parametri
 * naredbe se primaju u konstruktoru.
 * 
 * @author Ante Miličević
 *
 */
public interface Command {

	/**
	 * Metoda koja izvršava naredbu nad {@link ctx}-om i/ili {@code painter}-om
	 * 
	 * @param ctx     kontekst stanja
	 * @param painter objekt koji omogućava iscrtavanje
	 */
	void execute(Context ctx, Painter painter);
}
