package hr.fer.zemris.java.tecaj_13.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Formular za prijavu.
 * <ul>
 * Zahtjevi su:
 * <li>Nick mora imati barem 6 znakova</li>
 * <li>Lozinka mora imati barem 6 znakova</li>
 * </ul>
 * 
 * @author Ante Miličević
 *
 */
public class LoginForm {
	/**
	 * Nick korisnika
	 */
	private String nick;
	/**
	 * Lozinka
	 */
	private String pass;
	/**
	 * Mapa {'naziv atributa','greška'}
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Metoda za dohvat greške atributa naziva name
	 * 
	 * @param name naziv atributa
	 * @return poruka greške ili null ako greška ne postoji
	 */
	public String getError(String name) {
		return errors.get(name);
	}

	/**
	 * Provjera da li postoji greška
	 * 
	 * @return true ako postoji, false inače
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Metoda koja ispituje da li postoji greška za atribut naziva name
	 * 
	 * @param name naziv atributa
	 * @return true ako postoji, false inače
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Metoda za ispunu formulara iz http zahtjeva. Nakon ispune vrši se validacija.
	 * 
	 * @param req http zahtjev
	 */
	public void fillFromRequest(HttpServletRequest req) {
		errors.clear();
		nick = FormUtil.readTrimmedString(req.getParameter("nick"));
		pass = FormUtil.readRawText(req.getParameter("pass"));
		validate();
	}

	/**
	 * Metoda za validiranje
	 */
	private void validate() {
		if (nick.isEmpty()) {
			errors.put("nick", "Nick is missing");
		} else if (nick.length() < 6) {
			errors.put("nick", "Nick must be at least 6 characters long");
		}
		if (pass.isEmpty()) {
			errors.put("pass", "Password is missing");
		} else if (pass.length() < 6) {
			errors.put("pass", "Password must be at least 6 characters long");
		}
	}

	/**
	 * Metoda za ispunjavanje podataka korisnika iz formulara
	 * 
	 * @param user korisnik
	 */
	public void fillUser(BlogUser user) {
		user.setNick(nick);
		user.setPasswordHash(FormUtil.hashPassword(pass));
	}

	/**
	 * Getter za nick.
	 * 
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Setter za nick.
	 * 
	 * @param nick
	 */
	public void setNick(String nick) {
		this.nick = nick;
	}

	/**
	 * Getter za lozinku
	 * 
	 * @return pass
	 */
	public String getPass() {
		return pass;
	}

	/**
	 * Setter za lozinku
	 * 
	 * @param pass
	 */
	public void setPass(String pass) {
		this.pass = pass;
	}

	/**
	 * Getter za mapu s greškama
	 * 
	 * @return errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Setter za grešku atributa naziva name
	 * 
	 * @param name  naziv atributa
	 * @param value poruka greške
	 */
	public void setError(String name, String value) {
		errors.put(name, value);
	}
}
