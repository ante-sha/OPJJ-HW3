package hr.fer.zemris.java.p12.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import hr.fer.zemris.java.p12.dao.DAO;
import hr.fer.zemris.java.p12.dao.DAOException;
import hr.fer.zemris.java.p12.model.Poll;
import hr.fer.zemris.java.p12.model.PollOption;

/**
 * Implementacija sučelja DAO uporabom tehnologije SQL. Razred se oslanja na
 * podršku {@link SQLConnectionProvider} za uspostavljanje konekcije s bazom.
 * 
 * @author Ante Miličević
 */
public class SQLDAO implements DAO {

	@Override
	public List<PollOption> retrieveOptions(long pollID) {
		Connection con = SQLConnectionProvider.getConnection();

		List<PollOption> result = new LinkedList<>();
		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLOPTIONS WHERE pollID = ? ORDER BY id");
			pst.setLong(1, pollID);

			ResultSet res = pst.executeQuery();

			while (res.next()) {
				PollOption option = new PollOption();

				option.setOptionId(res.getLong(1));
				option.setName(res.getString(2));
				option.setUrl(res.getString(3));
				option.setPollId(res.getLong(4));
				option.setVotesCount(res.getLong(5));

				result.add(option);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return result;
	}

	@Override
	public PollOption retrieveOption(long id) {
		Connection con = SQLConnectionProvider.getConnection();

		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLOPTIONS WHERE id = ?");
			pst.setLong(1, id);

			ResultSet res = pst.executeQuery();

			if (res != null && res.next()) {
				PollOption option = new PollOption();

				option.setOptionId(res.getLong(1));
				option.setName(res.getString(2));
				option.setUrl(res.getString(3));
				option.setPollId(res.getLong(4));
				option.setVotesCount(res.getLong(5));

				return option;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		throw new DAOException("There is no option with id " + id);
	}

	@Override
	public List<Poll> retrievePolls() {
		Connection con = SQLConnectionProvider.getConnection();

		List<Poll> result = new LinkedList<>();
		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLS");

			ResultSet res = pst.executeQuery();

			while (res.next()) {
				Poll poll = new Poll();

				poll.setId(res.getLong(1));
				poll.setTitle(res.getString(2));
				poll.setMessage(res.getString(3));

				result.add(poll);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return result;
	}

	@Override
	public Poll retrievePoll(long id) {
		Connection con = SQLConnectionProvider.getConnection();

		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLS WHERE id = ?");
			pst.setLong(1, id);

			ResultSet res = pst.executeQuery();

			if (res.next()) {
				Poll poll = new Poll();

				poll.setId(res.getLong(1));
				poll.setTitle(res.getString(2));
				poll.setMessage(res.getString(3));

				return poll;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		throw new DAOException("There is no poll with id " + id);
	}

	@Override
	public void voteUpForOption(long id) {
		Connection con = SQLConnectionProvider.getConnection();

		try {
			PreparedStatement pst = con
					.prepareStatement("UPDATE POLLOPTIONS SET votesCount = votesCount + 1 WHERE id = ?");
			pst.setLong(1, id);

			int p = pst.executeUpdate();

			if (p < 1) {
				throw new DAOException("Update on optionID " + id + " has failed");
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

}