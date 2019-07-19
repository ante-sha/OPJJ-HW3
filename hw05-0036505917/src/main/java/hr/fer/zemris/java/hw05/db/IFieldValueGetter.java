package hr.fer.zemris.java.hw05.db;

/**
 * Sučelje koje definira ekstraktor podataka iz razreda {@link StudentRecord}
 * 
 * @author Ante Miličević
 *
 */
@FunctionalInterface
public interface IFieldValueGetter {
	/**
	 * Metoda koja ekstrahira podatak iz argumenta {@code record}
	 * 
	 * @param record objekt nad kojim se vrši ekstrahiranje
	 * @return traženi podatak
	 * 
	 * @throws NullPointerException ako je record == null
	 */
	public String get(StudentRecord record);
}
