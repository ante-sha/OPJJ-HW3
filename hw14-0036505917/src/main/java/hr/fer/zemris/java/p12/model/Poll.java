package hr.fer.zemris.java.p12.model;

import java.beans.JavaBean;

/**
 * Razred koji opisuje podatke o anketi.
 * 
 * @author Ante Miličević
 *
 */
@JavaBean
public class Poll {
	/**
	 * ID ankete
	 */
	private long id;
	/**
	 * Naslov ankete
	 */
	private String title;
	/**
	 * Poruka ankete
	 */
	private String message;

	/**
	 * Konstruktor
	 */
	public Poll() {
	}

	/**
	 * Getter za id
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter za id
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter za naslov
	 * 
	 * @return naslov
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za naslov
	 * 
	 * @param title naslov
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za poruku
	 * 
	 * @return poruka
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za poruku
	 * 
	 * @param message poruka
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
