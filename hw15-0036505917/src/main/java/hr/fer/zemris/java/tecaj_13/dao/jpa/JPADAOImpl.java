package hr.fer.zemris.java.tecaj_13.dao.jpa;

import java.util.List;

import hr.fer.zemris.java.tecaj_13.dao.DAO;
import hr.fer.zemris.java.tecaj_13.dao.DAOException;
import hr.fer.zemris.java.tecaj_13.model.BlogComment;
import hr.fer.zemris.java.tecaj_13.model.BlogEntry;
import hr.fer.zemris.java.tecaj_13.model.BlogUser;

/**
 * Implementacija sloja za perzistenciju podataka preko JPA i ORM-modela SQL baze.
 * 
 * @author Ante Miličević
 *
 */
public class JPADAOImpl implements DAO {

	@Override
	public BlogEntry getBlogEntry(Long id) throws DAOException {
		BlogEntry blogEntry = JPAEMProvider.getEntityManager().find(BlogEntry.class, id);
		return blogEntry;
	}

	@Override
	public BlogUser getBlogUserByNick(String nick) throws DAOException {
		List<BlogUser> list = JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getByNick", BlogUser.class)
				.setParameter("nk", nick).getResultList();
		
		if(list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public void createUser(BlogUser user) throws DAOException {
		JPAEMProvider.getEntityManager().persist(user);
	}

	@Override
	public List<BlogUser> getBlogUsers() throws DAOException {
		return JPAEMProvider.getEntityManager().createNamedQuery("BlogUser.getUsers", BlogUser.class)
				.getResultList();
	}

	@Override
	public BlogUser getBlogUserById(Long id) throws DAOException {
		return JPAEMProvider.getEntityManager().find(BlogUser.class, id);
	}
	
	@Override
	public void createEntry(BlogEntry entry) throws DAOException {
		JPAEMProvider.getEntityManager().persist(entry);
	}

	@Override
	public void createComment(BlogComment comment) throws DAOException {
		JPAEMProvider.getEntityManager().persist(comment);
	}
}