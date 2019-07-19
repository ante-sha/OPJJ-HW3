package hr.fer.zemris.hw16.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Opisnik slike koji sadrži naziv slike, kratak opis i oznake povezane sa
 * slikom.
 * 
 * @author Ante Miličević
 *
 */
public class ImageItem {
	/**
	 * Skup oznaka slike
	 */
	private Set<String> tags;
	/**
	 * Naziv slike
	 */
	private String name;
	/**
	 * Kratak opis slike
	 */
	private String description;

	/**
	 * Getter za oznake slike
	 * 
	 * @return tags
	 */
	public Set<String> getTags() {
		return tags;
	}

	/**
	 * Setter za skup oznaka slike
	 * 
	 * @param tags oznake slike
	 */
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}

	/**
	 * Setter za skup oznaka slike
	 * 
	 * @param tags oznake slike
	 */
	public void setTags(List<String> tags) {
		this.tags = new HashSet<>(tags);
	}

	/**
	 * Getter za naziv slike
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter za naziv slike
	 * 
	 * @param name naziv slike
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter za kratki opis slike
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter za kratki opis slike
	 * 
	 * @param description kratki opis slike
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
