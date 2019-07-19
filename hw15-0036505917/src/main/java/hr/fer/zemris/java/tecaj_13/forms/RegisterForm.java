package hr.fer.zemris.java.tecaj_13.forms;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Formular za registraciju korisnika.
 * <ul>
 * Zahtjevi su:
 * <li>Nick mora imati barem 6 znakova</li>
 * <li>Lozinka mora imati barem 6 znakova</li>
 * <li>Ime mora imati između 1 i 50 znakova</li>
 * <li>Prezime mora imati između 1 i 100 znakova</li>
 * <li>EMail mora biti validan</li>
 * </ul>
 * @author Ante Miličević
 *
 */
public class RegisterForm {
	/**
	 * Nick
	 */
	private String nick;
	/**
	 * Lozinka
	 */
	private String pass;
	/**
	 * Ime korisnika
	 */
	private String firstName;
	/**
	 * Prezime korisnika
	 */
	private String lastName;
	/**
	 * Kontakt email korisnika
	 */
	private String email;
	/**
	 * Mapa {'naziv atributa','poruka greške'}
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Dohvat greške atributa naziva name
	 * 
	 * @param name naziv atributa
	 * @return poruka greške ili null ako greška ne postoji
	 */
	public String getError(String name) {
		return errors.get(name);
	}
	
	/**
	 * Provjera da li greška postoji
	 * 
	 * @return true ako postoji, false inače
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	/**
	 * Provjera da li greška postoji za atribut naziva name
	 * 
	 * @param name naziv atributa
	 * @return true ako postoji, false inače
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}
	
	/**
	 * Metoda za popunjavanje formulara iz http zahtjeva. Nakon popunjavanja
	 * vrši se validacija.
	 * 
	 * @param req http zahtjev
	 */
	public void fillFromRequest(HttpServletRequest req) {
		nick = FormUtil.readTrimmedString(req.getParameter("nick"));
		pass = FormUtil.readRawText(req.getParameter("pass"));
		firstName = FormUtil.readTrimmedString(req.getParameter("firstName"));
		lastName = FormUtil.readTrimmedString(req.getParameter("lastName"));
		email = FormUtil.readTrimmedString(req.getParameter("email"));
		validate();
	}

	/**
	 * Metoda za validiranje
	 */
	private void validate() {
		if(nick.isEmpty()) {
			errors.put("nick", "Nick is missing");
		} else if(nick.length() < 6) {
			errors.put("nick", "Nick must be at least 6 characters long");
		}
		if(pass.isEmpty()) {
			errors.put("pass", "Password is missing");
		} else if(pass.length() < 6) {
			errors.put("pass", "Password must be at least 6 characters long");
		}
		if(firstName.isEmpty()) {
			errors.put("firstName","First name is missing");
		} else if(firstName.length() > 50) {
			errors.put("firstName","First name can have from 1 to 50 characters");
		}
		
		if(lastName.isEmpty()) {
			errors.put("lastName", "Last name is missing");
		} else if(lastName.length() > 100) {
			errors.put("lastName", "Last name must have from 1 to 100 characters");
		}
		
		if(email.isEmpty()) {
			errors.put("email", "Email is missing");
		} else if(!email.toUpperCase().matches("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$")) {
			errors.put("email", "Invalid email");
		}
	}
	
	/**
	 * Metoda za popunjavanje podataka o korisniku iz formulara.
	 * 
	 * @param user korisnik
	 */
	public void fillUser(BlogUser user) {
		user.setNick(nick);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setPasswordHash(FormUtil.hashPassword(pass));
	}

	/**
	 * Getter za nick
	 * 
	 * @return nick
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Setter za nick
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
	 * Getter za ime
	 * 
	 * @return firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Setter za ime
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Getter za prezime
	 * 
	 * @return lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter za prezime
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Getter za kontakt email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Setter za kontakt email
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter za greške
	 * 
	 * @return errors
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Setter za grešku atributa naziva name
	 * 
	 * @param name naziv atributa
	 * @param value poruka greške
	 */
	public void setError(String name, String value) {
		errors.put(name, value);
	}
}
