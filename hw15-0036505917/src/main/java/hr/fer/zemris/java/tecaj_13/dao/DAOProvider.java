package hr.fer.zemris.java.tecaj_13.dao;

import hr.fer.zemris.java.tecaj_13.dao.jpa.JPADAOImpl;

/**
 * Razred koji implementira oblikovni obrazac Singleton. Služi za dohvaćanje
 * implementacije sučelja {@link DAO}.
 * 
 * @author Ante Miličević
 *
 */
public class DAOProvider {
	/**
	 * Implementacija sučelja DAO
	 */
	private static DAO dao = new JPADAOImpl();
	
	/**
	 * Metoda za dohvat implementacije
	 * @return
	 */
	public static DAO getDAO() {
		return dao;
	}
	
}