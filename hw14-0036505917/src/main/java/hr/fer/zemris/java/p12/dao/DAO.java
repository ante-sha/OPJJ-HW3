package hr.fer.zemris.java.p12.dao;

import java.util.List;

import hr.fer.zemris.java.p12.model.PollOption;
import hr.fer.zemris.java.p12.model.Poll;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author Ante Miličević
 *
 */
public interface DAO {
	/**
	 * Metoda za dohvat opcija ankete s id-jem poolID.
	 * 
	 * @param pollID id ankete
	 * 
	 * @return lista opcija
	 * 
	 * @throws DAOException ako zahtjev ne uspije
	 */
	public List<PollOption> retrieveOptions(long pollID) throws DAOException;

	/**
	 * Metoda za dohvat opcije s id-jem id.
	 * 
	 * @param id id opcije
	 * 
	 * @return opcija ankete
	 * 
	 * @throws DAOException ako opcija s id-jem id ne postoji
	 */
	public PollOption retrieveOption(long id) throws DAOException;

	/**
	 * Metoda za dohvat svih anketa
	 * 
	 * @return lista svih anketa
	 * 
	 * @throws DAOException ako zahtjev ne uspije
	 */
	public List<Poll> retrievePolls() throws DAOException;

	/**
	 * Metoda za dohvat ankete s id-jem id
	 * 
	 * @param id id ankete
	 * 
	 * @return informacije o anketi
	 * 
	 * @throws DAOException ako ankete s id-jem id ne postoji
	 */
	public Poll retrievePoll(long id) throws DAOException;

	/**
	 * Metoda za povećanje broja glasova za 1 za opciju s id-jem
	 * id.
	 * 
	 * @param id id opcije
	 * 
	 * @throws DAOException ako metoda ne uspije
	 */
	public void voteUpForOption(long id) throws DAOException;
}