package hr.fer.zemris.java.tecaj_13.dao;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Sučelje koje definira način komunikacije između servleta i sloja za
 * perzistenciju podataka.
 * 
 * @author Ante Miličević
 *
 */
public interface DAO {

	/**
	 * Dohvaća entry sa zadanim <code>id</code>-em. Ako takav entry ne postoji,
	 * vraća <code>null</code>.
	 * 
	 * @param id ključ zapisa
	 * @return entry ili <code>null</code> ako entry ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu podataka
	 */
	public BlogEntry getBlogEntry(Long id) throws DAOException;

	/**
	 * Dohvaća usera sa zadanim nickom. Ako takav ne postoji vraća null.
	 * 
	 * @param nick userov nick
	 * @return user ili null ako takav user ne postoji
	 * @throws DAOException ako dođe do pogreške pri dohvatu
	 */
	public BlogUser getBlogUserByNick(String nick) throws DAOException;

	/**
	 * Stvara novog usera.
	 * 
	 * @param user podaci o user-u
	 * 
	 * @throws DAOException ako dođe do pogreške pri stvaranju korisnika
	 */
	public void createUser(BlogUser user) throws DAOException;

	/**
	 * Dohvaća sve korisnike.
	 * 
	 * @return lista svih korisnika
	 * @throws DAOException ako dođe do pogreške pri dohvaćanju korisnika
	 */
	public List<BlogUser> getBlogUsers() throws DAOException;

	/**
	 * Dohvaća korisnika s id-jem id. Ako takav ne postoji vraća se null.
	 * 
	 * @param id identifikator korisnika
	 * 
	 * @return user ako takav postoji, null ako ne
	 * 
	 * @throws DAOException ako dođe do pogreške pri dohvaćanju korisnika
	 */
	public BlogUser getBlogUserById(Long id) throws DAOException;

	/**
	 * Stvara unos bloga.
	 * 
	 * @param entry unos
	 * @throws DAOException ako stvaranje unosa ne uspije
	 */
	public void createEntry(BlogEntry entry) throws DAOException;

	/**
	 * Stvara komentar.
	 * 
	 * @param comment komentar
	 * @throws DAOException ako stvaranje komentara ne uspije
	 */
	public void createComment(BlogComment comment) throws DAOException;
}