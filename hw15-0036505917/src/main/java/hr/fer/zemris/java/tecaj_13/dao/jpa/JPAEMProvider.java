package hr.fer.zemris.java.tecaj_13.dao.jpa;

import hr.fer.zemris.java.tecaj_13.dao.DAOException;

import javax.persistence.EntityManager;

/**
 * Razred koji čuva veze s bazom podataka. Veze se vežu uz oznaku dretve
 * koja izvodi dohvat veze.
 * 
 * @author Ante Miličević
 *
 */
public class JPAEMProvider {
	/**
	 * Mapa koja čuva aktivne veze s bazom podataka
	 */
	private static ThreadLocal<EntityManager> locals = new ThreadLocal<>();

	/**
	 * Metoda koja dohvaća aktivnu vezu s bazom podataka. Ako ista već ne postoji
	 * stvara se.
	 * 
	 * @return aktivna veza s bazom podataka
	 */
	public static EntityManager getEntityManager() {
		EntityManager em = locals.get();
		if(em==null) {
			em = JPAEMFProvider.getEmf().createEntityManager();
			em.getTransaction().begin();
			locals.set(em);
		}
		return em;
	}

	/**
	 * Metoda koja zatvara vezu s bazom podataka i uklanja vezu iz mape
	 * aktivnih veza.
	 * 
	 * @throws DAOException ako zatvaranje veze ne uspije
	 */
	public static void close() throws DAOException {
		EntityManager em = locals.get();
		if(em==null) {
			return;
		}
		DAOException dex = null;
		try {
			em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
}