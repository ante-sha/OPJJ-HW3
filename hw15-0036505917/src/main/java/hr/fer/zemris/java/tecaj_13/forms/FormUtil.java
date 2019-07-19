package hr.fer.zemris.java.tecaj_13.forms;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Razred koji implementira zajedničke metode koje koriste formulari.
 * 
 * @author Ante Miličević
 *
 */
public class FormUtil {

	/**
	 * Metoda za čitanje teksta za kojeg se zanemaruju početni i završni razmaci.
	 * Ako je parameter null vraća se prazan string.
	 * 
	 * @param parameter tekst
	 * @return parameter bez uvodnih i završnih razmaka ili prazan string ako je
	 *         parameter null
	 */
	public static String readTrimmedString(String parameter) {
		if (parameter == null) {
			return "";
		}
		return parameter.trim();
	}

	/**
	 * Metoda za čitanje teksta gdje ako je parameter null vraća se prazan string.
	 * 
	 * @param parameter tekst
	 * @return parameter ili prazan string ako je parameter null
	 */
	public static String readRawText(String parameter) {
		if (parameter == null) {
			return "";
		}
		return parameter;
	}
	

	/**
	 * Metoda za hashiranje lozinke.
	 * 
	 * @param pass lozinka
	 * @return hashirana lozinka
	 * 
	 * @throws RuntimeException ako hashiranje nije uspilo
	 */
	public static String hashPassword(String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] messageDigest = md.digest(pass.getBytes());
			// create integer number with concatenation of unsigned bytes
			BigInteger no = new BigInteger(1, messageDigest);
			String hashPass = no.toString(16);

			while (hashPass.length() < 32) {
				hashPass = "0" + hashPass;
			}

			return hashPass;
		}

		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Password hashing failed", e);
		}
	}
}
