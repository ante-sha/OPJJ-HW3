package hr.fer.zemris.java.tecaj_13.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;

/**
 * Formular za ispunu komentara. Nakon inicijalizacije komponenti vrši se
 * validacija i sve greške spremaju se u mapu grešaka gdje je ključ naziv
 * atributa.
 * 
 * @author Ante Miličević
 *
 */
public class CommentForm {

	/**
	 * Poruka
	 */
	private String message;
	/**
	 * Kontakt email
	 */
	private String email;
	/**
	 * Greške
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Dohvaćanje greške za atribut naziva name
	 * 
	 * @param name naziv atributa
	 * @return greška ili null ako greška ne postoji
	 */
	public String getError(String name) {
		return errors.get(name);
	}

	/**
	 * Provjera postojanja grešaka.
	 * 
	 * @return true ako postoje greške, false inače
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Provjera postojanja greške za atribut naziva name.
	 * 
	 * @param name naziv atributa
	 * @return true ako greška postoji, false inače
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Metoda popunjava formular iz zahtjeva i nakon toga ga validira.
	 * 
	 * @param req http zahtjev
	 */
	public void fillFromRequest(HttpServletRequest req) {
		message = FormUtil.readTrimmedString(req.getParameter("message"));
		email = FormUtil.readTrimmedString(req.getParameter("email"));
		validate();
	}

	/**
	 * Metoda koja validira unos komentara.
	 * <ul>
	 * Zahtjevi su:
	 * <li>Poruka mora postojati</li>
	 * <li>EMail mora postojati</li>
	 * </ul>
	 */
	private void validate() {
		if (message.isEmpty()) {
			errors.put("message", "Message is missing");
		}
		if (email.isEmpty()) {
			errors.put("email", "Email is missing");
		}
	}

	/**
	 * Metoda za popunjavanje objekta koji predstavlja komentar.
	 * 
	 * @param comment komentar
	 */
	public void fillComment(BlogComment comment) {
		comment.setMessage(message);
		comment.setUsersEMail(email);
	}

	/**
	 * Getter za greške.
	 * 
	 * @return errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Metoda za vanjsko postavljanje grešaka.
	 * 
	 * @param name  naziv atributa
	 * @param value poruka greške
	 */
	public void setError(String name, String value) {
		errors.put(name, value);
	}

	/**
	 * Getter za poruku komentara.
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za poruku komentaru.
	 * 
	 * @param message poruka komentara
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter za kontakt EMail
	 * 
	 * @return kontakt EMail
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter za kontakt EMail
	 * 
	 * @param email kontakt EMail
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
