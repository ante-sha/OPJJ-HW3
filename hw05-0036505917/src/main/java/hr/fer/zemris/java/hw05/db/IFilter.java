package hr.fer.zemris.java.hw05.db;

/**
 * Sučelje koje definira filter nad objektima razreda {@link StudentRecord}
 * @author Ante Miličević
 *
 */
@FunctionalInterface
public interface IFilter {
	
	/**
	 * Metoda koja testira {@code record} i vraća rezultat "prihvatljivosti"
	 * @param record zapis koji se testira
	 * @return true ako se {@code record} prihvaća, false inače
	 */
	boolean accepts(StudentRecord record);
}
