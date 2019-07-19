package hr.fer.zemris.java.hw13.servlets.prob7;

import java.beans.JavaBean;

/**
 * Razred koji služi za spremanje podataka o bendu.
 * 
 * @author Ante Miličević
 *
 */
@JavaBean
public class Band {
	/**
	 * Jedinstveni ID benda
	 */
	private long ID;
	/**
	 * Naziv benda
	 */
	private String name;
	/**
	 * Url linka na neku od poznatih izvedbi benda
	 */
	private String url;

	/**
	 * Konstruktor
	 */
	public Band(long iD, String name, String url) {
		super();
		ID = iD;
		this.name = name;
		this.url = url;
	}

	/**
	 * Getter za id
	 * @return ID
	 */
	public long getID() {
		return ID;
	}

	/**
	 * Setter za id
	 * @param iD
	 */
	public void setID(long iD) {
		ID = iD;
	}

	/**
	 * Getter za name
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter za name
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter za url
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter za url
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
