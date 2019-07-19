package hr.fer.zemris.java.tecaj_13.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Razred koji čuva objekt za stvaranje veze s bazom podataka.
 * 
 * @author Ante Miličević
 *
 */
public class JPAEMFProvider {
	/**
	 * Objekt za stvaranje veze s bazom podataka
	 */
	public static EntityManagerFactory emf;
	
	/**
	 * Metoda za dohvaćanje objekta za stvaranje veze s bazom podataka.
	 * 
	 * @return objekt za stvaranje veze s bazom podataka
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Metoda za postavljanje objekta za stvaranje veze s bazom podataka
	 * 
	 * @param emf objekt za stvaranje veze s bazom podataka
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}