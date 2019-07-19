package hr.fer.zemris.java.p12.model;

import java.beans.JavaBean;

/**
 * Razred koji služi za spremanje podataka o opciji ankete.
 * 
 * @author Ante Miličević
 *
 */
@JavaBean
public class PollOption {
	/**
	 * Naziv opcije
	 */
	private String name;
	/**
	 * Url linka na opis opcije
	 */
	private String url;
	/**
	 * id ankete u kojoj se nalazi ova opcija
	 */
	private long pollId;
	/**
	 * id opcije
	 */
	private long optionId;
	/**
	 * Broj glasova za opciju
	 */
	private long votesCount;

	/**
	 * Konstruktor
	 */
	public PollOption(String name, long pollId, String url) {
		super();
		this.name = name;
		this.url = url;
		this.pollId = pollId;
	}

	/**
	 * Konstruktor
	 */
	public PollOption() {
	}

	/**
	 * Getter za optionId
	 * 
	 * @return optionId
	 */
	public long getOptionId() {
		return optionId;
	}

	/**
	 * Setter za optionId
	 * 
	 * @param optionId
	 */
	public void setOptionId(long optionId) {
		this.optionId = optionId;
	}

	/**
	 * Getter za pollId
	 * 
	 * @return
	 */
	public long getPollId() {
		return pollId;
	}

	/**
	 * Setter za pollId
	 * 
	 * @param pollId
	 */
	public void setPollId(long pollId) {
		this.pollId = pollId;
	}

	/**
	 * Getter za name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter za name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter za url
	 * 
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

	/**
	 * Getter za votesCount
	 * 
	 * @return votesCount
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Setter za votesCount
	 * 
	 * @param votesCount
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}

}