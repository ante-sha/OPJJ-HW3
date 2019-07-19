package hr.fer.zemris.java.tecaj_13.model;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 * Razred koji implementira objekt ORM-a i predstavlja korisnika.
 * 
 * @author Ante Miličević
 *
 */
@NamedQueries({
	@NamedQuery(name="BlogUser.getByNick",query="select b from BlogUser as b where b.nick=:nk"),
	@NamedQuery(name="BlogUser.getUsers", query="select b from BlogUser as b")
})
@Entity
@Table(name="users")
public class BlogUser {

	/**
	 * Identifikator
	 */
	@Id @GeneratedValue
	private Long id;
	/**
	 * Ime
	 */
	@Column(length=100,nullable=false)
	private String firstName;
	/**
	 * Prezime
	 */
	@Column(length=100,nullable=false)
	private String lastName;
	/**
	 * Nick
	 */
	@Column(length=100,nullable=false, unique=true)
	private String nick;
	/**
	 * Kontakt email
	 */
	@Column(length=100,nullable=false)
	private String email;
	/**
	 * Hashirana lozinka
	 */
	@Column(length=100,nullable=false)
	private String passwordHash;
	/**
	 * Kolekcija unosa
	 */
	@OneToMany(mappedBy="creator",fetch=FetchType.LAZY, cascade=CascadeType.PERSIST, orphanRemoval=true)
	@OrderBy("createdAt")
	private Collection<BlogEntry> entries;
	
	/**
	 * Getter za kolekciju unosa
	 * 
	 * @return entries
	 */
	public Collection<BlogEntry> getEntries() {
		return entries;
	}
	
	/**
	 * Setter za kolekciju unosa
	 * @param entries
	 */
	public void setEntries(Collection<BlogEntry> entries) {
		this.entries = entries;
	}
	
	/**
	 * Getter za identifikator
	 * 
	 * @return id
	 */
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
	 * Getter za kontakt email
	 * 
	 * @return email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Setter za kontakt email
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Getter za hashiranu lozinku
	 * 
	 * @return passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * Setter za hashiranu lozinku
	 * 
	 * @param passwordHash
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof BlogUser))
			return false;
		BlogUser other = (BlogUser) obj;
		return Objects.equals(id, other.id);
	}
}
