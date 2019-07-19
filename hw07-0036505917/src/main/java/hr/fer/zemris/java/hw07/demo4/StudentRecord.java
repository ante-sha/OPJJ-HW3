package hr.fer.zemris.java.hw07.demo4;

import java.util.Objects;

/**
 * Razred predstavlja jedan unos podataka o studentu. Spremljeni podaci postaju
 * read-only varijable razreda. <br>
 * Dostupne informacije su:<br>
 * JMBAG, ime studenta, prezime studenta, broj bodova na međuispitu, broj bodova
 * na završnom ispitu, broj bodova na laboratorijskim vježbama te završna
 * ocjena.
 * 
 * @author Ante Miličević
 *
 */
public class StudentRecord {
	/**
	 * Jmbag studenta
	 */
	private String jmbag;
	/**
	 * Prezime
	 */
	private String prezime;
	/**
	 * Ime
	 */
	private String ime;
	/**
	 * Broj bodova na međuispitu
	 */
	private double meduispit;
	/**
	 * Broj bodova na završnom
	 */
	private double zavrsni;
	/**
	 * Broj bodova na laboratorijskim vježbama
	 */
	private double labos;
	/**
	 * Završna ocjena
	 */
	private int ocjena;

	/**
	 * Konstruktor koji inicijalizira sve varijable objekta
	 * 
	 * @param jmbag
	 * @param prezime
	 * @param ime
	 * @param meduispit
	 * @param zavrsni
	 * @param labos
	 * @param ocjena
	 * 
	 * @throws NullPointerException ako je jmbag, prezime ili ime null
	 */
	public StudentRecord(String jmbag, String prezime, String ime, double meduispit, double zavrsni, double labos,
			int ocjena) {
		super();
		this.jmbag = Objects.requireNonNull(jmbag);
		this.prezime = Objects.requireNonNull(prezime);
		this.ime = Objects.requireNonNull(ime);
		this.meduispit = meduispit;
		this.zavrsni = zavrsni;
		this.labos = labos;
		this.ocjena = ocjena;
	}

	/**
	 * Getter za jmbag
	 * 
	 * @return jmbag
	 */
	public String getJmbag() {
		return jmbag;
	}

	/**
	 * Getter za prezime
	 * 
	 * @return prezime
	 */
	public String getPrezime() {
		return prezime;
	}

	/**
	 * Getter za ime
	 * @return ime
	 */
	public String getIme() {
		return ime;
	}

	/**
	 * Getter za broj bodova na međuispitu
	 * 
	 * @return meduispit
	 */
	public double getMeduispit() {
		return meduispit;
	}

	/**
	 * Getter za broj bodova na završnom ispitu
	 * 
	 * @return zavrsni
	 */
	public double getZavrsni() {
		return zavrsni;
	}

	/**
	 * Getter za broj bodova na laboratorijskim vježbama
	 * 
	 * @return labos
	 */
	public double getLabos() {
		return labos;
	}

	/**
	 * Getter za završnu ocjenu
	 * 
	 * @return ocjena
	 */
	public int getOcjena() {
		return ocjena;
	}

	@Override
	public String toString() {
		return "StudentRecord [jmbag=" + jmbag + ", prezime=" + prezime + ", ime=" + ime + ", meduispit=" + meduispit
				+ ", zavrsni=" + zavrsni + ", labos=" + labos + ", ocjena=" + ocjena + "]";
	}

}
