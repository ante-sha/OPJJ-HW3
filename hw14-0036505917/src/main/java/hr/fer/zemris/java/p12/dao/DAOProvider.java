package hr.fer.zemris.java.p12.dao;

import hr.fer.zemris.java.p12.dao.sql.SQLDAO;

/**
 * Singleton razred koji od korisnika sakriva detalje o
 * stvarnom načinu dohvata podataka.
 * 
 * @author Ante Miličević
 *
 */
public class DAOProvider {

	/**
	 * Objekt preko kojeg se dohvaćaju podaci
	 */
	private static DAO dao = new SQLDAO();
	
	/**
	 * Dohvat primjerka.
	 * 
	 * @return objekt za dohvaćanje podataka
	 */
	public static DAO getDao() {
		return dao;
	}
	
}