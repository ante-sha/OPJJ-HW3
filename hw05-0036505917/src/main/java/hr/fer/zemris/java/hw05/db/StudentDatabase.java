package hr.fer.zemris.java.hw05.db;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Razred koji modelira bazu podataka modeliranih {@link StudentRecord}. Baza se
 * inicijalizira u konstuktoru koji prima listu stringova koji reprezentiraju
 * studente. Jmbag studenta mora biti jedinstven. Omogućen je dohvat studenta po
 * jmbagu te dohvaćanje filtriranih zapisa.
 * 
 * @author Ante Miličević
 *
 */
public class StudentDatabase {
	/**
	 * Mapa koja čuva zapis studenta pod ključem njegovog jmbaga
	 */
	private Map<String, StudentRecord> database = new LinkedHashMap<>();

	/**
	 * Konstruktor koji inicijalizira bazu podataka preko liste zapisa koji
	 * predstavljaju tabom odvojene vrijednosti atributa studenta format izgleda kao
	 * (*jmbag*\t*lastName*\t*firstName*\t*finalGrade*)
	 * 
	 * @param students lista zapisa studenata u zadanom string formatu
	 * 
	 * @throws NullPointerException     ako je lista prazna
	 * @throws IllegalArgumentException ako dolazi do dupliciranja jmbaga ili ocjena
	 *                                  nije u dozvoljenim granicama ([1-5]) ili ako
	 *                                  se broj atributa ne podudara
	 */
	public StudentDatabase(List<String> students) {
		convertStringsToRecords(Objects.requireNonNull(students));
	}

	/**
	 * Metoda koja pretvara svaki zapis iz liste u {@link StudentRecord} te ga
	 * sprema u {@link #database} pod njegov jmbag
	 * 
	 * @param students lista zapisa
	 * 
	 * @throws IllegalArgumentException ako dolazi do dupliciranja jmbaga ili ocjena
	 *                                  nije u dozvoljenim granicama ([1-5]) ili ako
	 *                                  se broj atributa ne podudara
	 */
	private void convertStringsToRecords(List<String> students) {
		for (String student : students) {
			student = student.trim();
			String[] studentData = student.split("[\t]");

			if (studentData.length != 4) {
				throw new IllegalArgumentException(
						"Number of data in record " + student + " must be 4 but is " + studentData.length);
			}

			if (database.containsKey(studentData[0])) {
				throw new IllegalArgumentException("JMBAG " + studentData[0] + " is already in use");
			}
			studentData[1] = studentData[1].trim();
			studentData[2] = studentData[2].trim();
			int grade = readGrade(studentData[3]);
			validateJmbag(studentData[0]);
			checkIsEmpty(studentData[1], "Last name");
			checkIsEmpty(studentData[2], "First name");
			database.put(studentData[0], new StudentRecord(studentData[0], studentData[1], studentData[2], grade));

		}

	}

	/**
	 * Metoda koja provjerava da li je predani string prazan
	 * 
	 * @param string   string koji se provjerava
	 * @param atribute atribut kojeg string predstavlja
	 * 
	 * @throws IllegalArgumentException ako je string prazan
	 */
	private void checkIsEmpty(String string, String atribute) {
		if (string.length() == 0) {
			throw new IllegalArgumentException(atribute + " must be defined!");
		}
	}

	/**
	 * Metoda koja provjerava da li je jmbag u dobrom formatu
	 * 
	 * @param jmbag jmbag koji se provjerava
	 * 
	 * @throws IllegalArgumentException ako jmbag nije u dobrom formatu
	 */
	private void validateJmbag(String jmbag) {
		if (!jmbag.matches("[0-9]{10}")) {
			throw new IllegalArgumentException("JMBAG " + jmbag + " is in wrong format");
		}
	}

	/**
	 * Metoda koja parsira i validira ocjenu predanu u argumentu {@code string}
	 * 
	 * @param string zapis ocjene u string formatu
	 * @return broj između 1 i 5 koji je bio zapisan u stringu
	 * 
	 * @throws IllegalArgumentException ako ocjena nije u dobrom formatu
	 */
	private int readGrade(String string) {
		int grade;
		try {
			grade = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Grade " + string + " can not be parsed into integer");
		}
		if (grade < 1 || grade > 5)
			throw new IllegalArgumentException("Grade " + grade + " is not in defined boundaries [1,5]");
		return grade;
	}

	/**
	 * Metoda koja dohvaća {@link StudentRecord} iz baze preko jmbaga
	 * 
	 * @param jmbag jmbag traženog zapisa
	 * @return zapis s vrijedosti jmbaga {@code jmbag}
	 */
	public StudentRecord forJMBAG(String jmbag) {
		return database.get(jmbag);
	}

	/**
	 * Metoda koja filtrira sve zapise iz baze po pravilima zadanim u
	 * {@code filter}-u. Kroz rezultat se predaje lista zapisa koje je propustilo
	 * filtriranje.
	 * 
	 * @param filter implementacija pravila filtriranja
	 * @return lista zapisa koji zadovoljavaju pravila filtriranja
	 */
	public List<StudentRecord> filter(IFilter filter) {
		return database.values().stream().filter(record -> filter.accepts(record)).collect(Collectors.toList());
	}
}
