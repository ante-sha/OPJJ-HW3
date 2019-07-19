package hr.fer.zemris.java.tecaj_13.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Razred koji implementira objekt ORM-a i predstavlja unos bloga.
 * 
 * @author Ante Miličević
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogEntry.upit1",query="select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
})
@Entity
@Table(name="blog_entries")
@Cacheable(true)
public class BlogEntry {
	/**
	 * Identifikator
	 */
	private Long id;
	/**
	 * Lista komentara
	 */
	private List<BlogComment> comments = new ArrayList<>();
	/**
	 * Trenutak stvaranja unosa
	 */
	private Date createdAt;
	/**
	 * Trenutak zadnje izmjene
	 */
	private Date lastModifiedAt;
	/**
	 * Naslov bloga
	 */
	private String title;
	/**
	 * Tekst unosa bloga
	 */
	private String text;
	/**
	 * Autor
	 */
	private BlogUser creator;
	
	/**
	 * Getter za autora
	 * 
	 * @return creator
	 */
	@ManyToOne
	@JoinColumn(nullable=false)
	public BlogUser getCreator() {
		return creator;
	}

	/**
	 * Setter za autora
	 * 
	 * @param creator
	 */
	public void setCreator(BlogUser creator) {
		this.creator = creator;
	}

	/**
	 * Getter za identifikator
	 * 
	 * @return id
	 */
	@Id @GeneratedValue
	public Long getId() {
		return id;
	}
	
	/**
	 * Setter za identifikator
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Getter za listu komentara
	 * 
	 * @return comments
	 */
	@OneToMany(mappedBy="blogEntry",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("postedOn")
	public List<BlogComment> getComments() {
		return comments;
	}
	
	/**
	 * Setter za komentare
	 * 
	 * @param comments
	 */
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	/**
	 * Getter za trenutak stvaranja
	 * 
	 * @return createdAt
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Setter za trenutak stvaranja
	 * 
	 * @param createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Getter za trenutak zadnje izmjene
	 * 
	 * @return lastModifiedAt
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getLastModifiedAt() {
		return lastModifiedAt;
	}

	/**
	 * Setter za trenutak zadnje izmjene
	 * 
	 * @param lastModifiedAt
	 */
	public void setLastModifiedAt(Date lastModifiedAt) {
		this.lastModifiedAt = lastModifiedAt;
	}

	/**
	 * Getter za naslov
	 * 
	 * @return title
	 */
	@Column(length=200,nullable=false)
	public String getTitle() {
		return title;
	}

	/**
	 * Setter za naslov
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter za tekst
	 * 
	 * @return text
	 */
	@Column(length=4096,nullable=false)
	public String getText() {
		return text;
	}

	/**
	 * Setter za text
	 * 
	 * @param text 
	 */
	public void setText(String text) {
		this.text = text;
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
		BlogEntry other = (BlogEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}