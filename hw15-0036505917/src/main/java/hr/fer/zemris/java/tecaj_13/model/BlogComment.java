package hr.fer.zemris.java.tecaj_13.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred koji implementira objekt ORM-a i predstavlja komentar unosa blog.
 * 
 * @author Ante Miličević
 *
 */
@Entity
@Table(name = "blog_comments")
public class BlogComment {
	/**
	 * Identifikator
	 */
	private Long id;
	/**
	 * Blog unos kojemu pripada komentar
	 */
	private BlogEntry blogEntry;
	/**
	 * Kontakt email
	 */
	private String usersEMail;
	/**
	 * Poruka komentara
	 */
	private String message;
	/**
	 * Trenutak objave
	 */
	private Date postedOn;

	/**
	 * Getter za identifikator
	 * @return id
	 */
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	/**
	 * Setter za identifikator
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter za blog unos
	 * 
	 * @return blogEntry
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	public BlogEntry getBlogEntry() {
		return blogEntry;
	}

	/**
	 * Setter za blog unos
	 * 
	 * @param blogEntry
	 */
	public void setBlogEntry(BlogEntry blogEntry) {
		this.blogEntry = blogEntry;
	}

	/**
	 * Getter za kontakt email
	 * 
	 * @return usersEMail
	 */
	@Column(length = 100, nullable = false)
	public String getUsersEMail() {
		return usersEMail;
	}

	/**
	 * Setter za kontakt email
	 * 
	 * @param usersEMail
	 */
	public void setUsersEMail(String usersEMail) {
		this.usersEMail = usersEMail;
	}

	/**
	 * Getter za poruku komentara
	 * 
	 * @return message
	 */
	@Column(length = 4096, nullable = false)
	public String getMessage() {
		return message;
	}

	/**
	 * Setter za poruku komentara
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Getter za trenutak objave
	 * 
	 * @return postedOn
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public Date getPostedOn() {
		return postedOn;
	}

	/**
	 * Setter za trenutak objave
	 * 
	 * @param postedOn
	 */
	public void setPostedOn(Date postedOn) {
		this.postedOn = postedOn;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogComment other = (BlogComment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}