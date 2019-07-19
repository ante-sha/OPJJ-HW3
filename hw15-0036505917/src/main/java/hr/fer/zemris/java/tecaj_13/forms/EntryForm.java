package hr.fer.zemris.java.tecaj_13.forms;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import hr.fer.zemris.java.tecaj_13.model.BlogEntry;

/**
 * Formular za ispunu podataka o blog unosu.
 * <ul>
 * Zahtjevi su:
 * <li>Naslov ne smije biti prazan</li>
 * <li>Tekst ne smije biti prazan</li>
 * </ul>
 * 
 * @author Ante Miličević
 *
 */
public class EntryForm {

	/**
	 * Naslov unosa.
	 */
	private String title;
	/**
	 * Tekst unosa.
	 */
	private String text;

	/**
	 * Mapa {'naziv atributa','poruka greške'}
	 */
	Map<String, String> errors = new HashMap<>();

	/**
	 * Dohvat poruke greške za atribut naziva name
	 * 
	 * @param name naziv atributa
	 * @return poruka greške ili null ako greške ne postoji
	 */
	public String getError(String name) {
		return errors.get(name);
	}

	/**
	 * Provjera da li postoji greška u ispuni formulara
	 * 
	 * @return true ako postoji, false inače
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Provjera da li postoji greška za atribut naziva name
	 * 
	 * @param name naziv atributa
	 * @return true ako postoji, false inače
	 */
	public boolean hasError(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Metoda za popunu formulara iz http zahtjeva. Nakon popunjavanja
	 * nad formularom se vrši validacija.
	 * 
	 * @param req http zahtjev
	 */
	public void fillFromRequest(HttpServletRequest req) {
		errors.clear();
		title = FormUtil.readTrimmedString(req.getParameter("title"));
		text = FormUtil.readRawText(req.getParameter("text"));
		validate();
	}

	/**
	 * Metoda za validaciju.
	 */
	private void validate() {
		if (text.length() < 1) {
			errors.put("text", "Text is required");
		}
		if (title.length() < 1) {
			errors.put("title", "Title is required");
		}
	}

	/**
	 * Metoda za popunu blog unosa iz formulara
	 * 
	 * @param entry blog unos
	 */
	public void fillEntry(BlogEntry entry) {
		entry.setText(text);
		entry.setTitle(title);
	}

	/**
	 * Metoda za ispunjavanje formulara iz blog unosa. Nakon
	 * popunjavanja vrši se validacija.
	 * 
	 * @param entry blog unos
	 */
	public void fillFormFromEntry(BlogEntry entry) {
		errors.clear();
		text = entry.getText();
		title = entry.getTitle();
		validate();
	}

	/**
	 * Getter za naslov blog unosa
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za naslov blog unosa
	 * 
	 * @param title naslov
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za text blog unosa.
	 * 
	 * @return text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setter za tekst blog unosa
	 * 
	 * @param text tekst blog unosa
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public int hashCode() {
		return Objects.hash(text, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof EntryForm))
			return false;
		EntryForm other = (EntryForm) obj;
		return Objects.equals(text, other.text) && Objects.equals(title, other.title);
	}

}
