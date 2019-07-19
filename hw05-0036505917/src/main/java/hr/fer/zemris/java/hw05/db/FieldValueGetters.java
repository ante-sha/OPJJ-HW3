package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji nudi implementacije {@link IFieldValueGetter}-a za sve atribute
 * {@link StudentRecord}-a osim za atribut finalGrade.
 * 
 * @author Ante Miličević
 *
 */
public class FieldValueGetters {

	/**
	 * Ekstraktor atributa firstName iz {@link StudentRecord}-a
	 */
	public static final IFieldValueGetter FIRST_NAME = record -> record.getFirstName();
	/**
	 * Ekstraktor atributa lastName iz {@link StudentRecord}-a
	 */
	public static final IFieldValueGetter LAST_NAME = record -> record.getLastName();
	/**
	 * Ekstraktor atributa jmbag iz {@link StudentRecord}-a
	 */
	public static final IFieldValueGetter JMBAG = record -> record.getJmbag();

	/**
	 * Naziv atributa kojeg ekstrahira {@link #JMBAG}
	 */
	private static final String jmbag = "jmbag";
	/**
	 * Naziv atributa kojeg ekstrahira {@link #FIRST_NAME}
	 */
	private static final String firstName = "firstName";
	/**
	 * Naziv atributa kojeg ekstrahira {@link #LAST_NAME}
	 */
	private static final String lastName = "lastName";

	/**
	 * Metoda koja predani string interpretira kao naziv atributa te vraća njegov
	 * ekstraktor {@link IFieldValueGetter}
	 * 
	 * @param string ime atributa
	 * @return {@link IFieldValueGetter} za specificirani atribut
	 * 
	 * @throws NullPointerException ako je {@code string == null}
	 */
	public static IFieldValueGetter interpretStringAsFieldGetter(String string) {
		Objects.requireNonNull(string);
		if (string.equals(jmbag)) {
			return FieldValueGetters.JMBAG;
		} else if (string.equals(firstName)) {
			return FieldValueGetters.FIRST_NAME;
		} else if (string.equals(lastName)) {
			return FieldValueGetters.LAST_NAME;
		} else {
			throw new IllegalArgumentException(string + " can not be interpreted as fieldGetter");
		}
	}

}
