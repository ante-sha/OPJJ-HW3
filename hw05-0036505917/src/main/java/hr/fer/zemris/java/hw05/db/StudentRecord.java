package hr.fer.zemris.java.hw05.db;

import java.util.Objects;

/**
 * Razred koji modelira zapis podataka o studentu gdje svi podaci moraju biti
 * definirani.
 * 
 * @author Ante Miličević
 *
 */
public class StudentRecord {
	/** Jmbag studenta */
	private String jmbag;
	/** Ime studenta */
	private String lastName;
	/** Prezime studenta */
	private String firstName;
	/** Završna ocjena studenta*/
	private int finalGrade;

	/**
	 * Konstuktor koji definira sve članske varijable objekta
	 * 
	 * @param jmbag jmbag studenta
	 * @param lastName ime studenta
	 * @param firstName prezime studenta
	 * @param finalGrade završna ocjena studenta
	 * 
	 * @throws NullPointerException ako je neki od argumenata jednak null
	 * @throws IllegalArgumentException ako je završna ocjena izvan intervala [1,5]
	 */
	public StudentRecord(String jmbag, String lastName, String firstName, int finalGrade) {
		super();
		this.jmbag = Objects.requireNonNull(jmbag);
		this.lastName = Objects.requireNonNull(lastName);
		this.firstName = Objects.requireNonNull(firstName);
		this.finalGrade = validateFinalGrade(finalGrade);
	}

	/**
	 * Metoda koja validira da li je ocjena unutar intervala [1,5]
	 * @param finalGrade ocjena
	 * @return ocjena
	 */
	private int validateFinalGrade(int finalGrade) {
		if(finalGrade < 1 || finalGrade > 5)
			throw new IllegalArgumentException("Grade " + finalGrade + " is not in interval [1,5]");
		return finalGrade;
	}

	/**
	 * Getter za jmbag
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter za prezime
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Getter za ime
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Getter za konačnu ocjenu
	 * @return finalGrade
	 */
	public int getFinalGrade() {
		return finalGrade;
	}

	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", lastName=" + lastName + ", firstName=" + firstName + ", finalGrade="
				+ finalGrade + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(jmbag);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof StudentRecord))
			return false;
		StudentRecord other = (StudentRecord) obj;
		return Objects.equals(jmbag, other.jmbag);
	}
}
