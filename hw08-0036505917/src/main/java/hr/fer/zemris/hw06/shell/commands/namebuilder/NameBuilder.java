package hr.fer.zemris.hw06.shell.commands.namebuilder;

import hr.fer.zemris.hw06.shell.commands.MassrenameShellCommand.FilterResult;

/**
 * Sučelje koje definira ponašanje generatora naziva.
 * 
 * @author Ante Miličević
 *
 */
public interface NameBuilder {

	/**
	 * Metoda koja na temelju filtriranog podatka dodaje tekst u StringBuilder
	 * {@code sb}.
	 * 
	 * @param result rezultat filtriranja
	 * @param sb     StringBuilder unutar kojega se gradi naziv
	 */
	void execute(FilterResult result, StringBuilder sb);

	/**
	 * Metoda tvornica za NameBuilder koji samo nadodaje zadani tekst u
	 * StringBuilder
	 * 
	 * @param text tekst kojeg se nadodaje u StringBuilder
	 * @return generirani NameBuilder
	 */
	static NameBuilder text(String text) {
		return (filterResult, sb) -> sb.append(text);
	}

	/**
	 * Metoda tvornica za NameBuilder koji nadodaje grupu s indeksom index iz
	 * filtriranog rezultata u StringBuilder
	 * 
	 * @param index indeks grupe koji se dodaje
	 * @return generirani NameBuilder
	 */
	static NameBuilder group(int index) {
		return (filterResult, sb) -> sb.append(filterResult.group(index));
	}

	/**
	 * Metoda tvornica za NameBuilder koji nadodaje grupu s indeksom index iz
	 * filtriranog rezultata u StringBuilder. Minimalna širina teksta grupe je
	 * zadana s parametrom minWidth. Ako je širina teksta grupe manja od minWidth
	 * tekst se nadopunjuje s znakovima padding do širine minWidth.
	 * 
	 * @param index    indeks grupe koji se dodaje
	 * @param padding  znak kojim se nadopunjuje tekst ako je širine ispod minWidth
	 * @param minWidth minimalna širina teksta
	 * @return generirani NameBuilder
	 */
	static NameBuilder group(int index, char padding, int minWidth) {
		return (filterResult, sb) -> {
			String text = filterResult.group(index);

			int count = text.length() >= minWidth ? 0 : minWidth - text.length();
			text = Character.toString(padding).repeat(count) + text;

			sb.append(text);
		};
	}

	/**
	 * Metoda tvornica za kompozitni NameBuilder
	 * 
	 * @param builder NameBuilder koji izvršava svoju narebu nakon NameBuilder-a nad
	 *                kojim je then naredba pozvana
	 * @return generirani kompozitni NameBuilder
	 */
	default NameBuilder then(NameBuilder builder) {
		return (filterResult, sb) -> {
			this.execute(filterResult, sb);
			builder.execute(filterResult, sb);
		};
	}
}
