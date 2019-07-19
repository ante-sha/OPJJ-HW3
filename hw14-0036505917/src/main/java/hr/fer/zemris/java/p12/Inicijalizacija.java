package hr.fer.zemris.java.p12;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * Razred koji služi za inicijalizaciju baze podataka prilikom pokretanja
 * aplikacije. Inicijalizacija se vrši tako da se prvo uspostavi konekcija s
 * bazom te se nakon toga tablice u bazi inicijaliziraju po datoteci
 * pollsInit.txt. <br>
 * Razred nakon inicijalizacije sprema objekt za pružanje konekcije u atribute
 * servleta pod imenom "hr.fer.zemris.dbpool".
 * 
 * @author Ante Miličević
 *
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String connectionURL = buildURL(sce);

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
			cpds.setJdbcUrl(connectionURL);

		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		try (Connection con = cpds.getConnection()) {

			checkForTables(con);

			initPolls(con, sce);

		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Razred za inicijalizaciju anketi. Ako anketa postoji provjerava se da li
	 * postoje sve njene opcije do kojih se dolazi preko datoteke čija je staza
	 * navedena uz njihovu definiciju u pollsInit.txt.
	 * 
	 * @param con konekcija s bazom
	 * @param sce kontekst servleta
	 * 
	 * @throws IOException ako čitanje iz datoteke ne uspije
	 */
	private void initPolls(Connection con, ServletContextEvent sce) throws IOException {
		Path path = Paths.get(sce.getServletContext().getRealPath("/WEB-INF/polls/pollsInit.txt"));

		Files.readAllLines(path, StandardCharsets.UTF_8).stream().forEach((line) -> {
			String[] data = line.split("\t");

			Long pollID = pollID(data[0], con);
			if (pollID == null) {
				createPollAndItsOptions(data, con, sce);
			} else {
				setPollOptions(data[2], con, sce, pollID);
			}
		});
	}

	/**
	 * Metoda koja provjerava da li opcije ankete postoje i ako ne postoje stvara ih
	 * se.
	 * 
	 * @param filePath staza do datoteke s podacima o opcijama ankete
	 * @param con      konekcija s bazom
	 * @param sce      kontekst servleta
	 * @param pollID   id ankete
	 * 
	 * @throws RuntimeException ako inicijalizacija ne uspije
	 */
	private void setPollOptions(String filePath, Connection con, ServletContextEvent sce, long pollID) {
		Path path = Paths.get(sce.getServletContext().getRealPath(filePath));

		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO POLLOPTIONS (optionTitle, pollID, optionLink, votesCount) VALUES (?,?,?,0)");
			PreparedStatement checkIfOptionExist = con
					.prepareStatement("SELECT * FROM POLLOPTIONS WHERE pollID = ? AND optionTitle = ?");

			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.toList());

			for (String line : lines) {
				String[] data = line.split("\t");

				// check if option already exists
				checkIfOptionExist.setLong(1, pollID);
				checkIfOptionExist.setString(2, data[0]);

				ResultSet check = checkIfOptionExist.executeQuery();

				if (check != null && check.next()) {
					continue;
				}

				// create if not
				pst.setString(1, data[0]);
				pst.setLong(2, pollID);
				pst.setString(3, data[1]);

				int p = pst.executeUpdate();
				if (p < 1) {
					throw new RuntimeException("Initialization of polloptions has failed");
				}
			}

		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metoda za stvaranje ankete i njenih opcija u bazi.
	 * 
	 * @param data podaci o anketi
	 * @param con  konekcija s bazom
	 * @param sce  kontekst servleta
	 * 
	 * @throws RuntimeException ako inicijalizacija ne uspije
	 */
	private void createPollAndItsOptions(String[] data, Connection con, ServletContextEvent sce) {
		try {
			PreparedStatement pst = con.prepareStatement("INSERT INTO POLLS (title, message) VALUES (?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, data[0]);
			pst.setString(2, data[1]);

			pst.execute();

			long pollID = 0L;
			try (ResultSet res = pst.getGeneratedKeys()) {
				if (res != null && res.next()) {
					pollID = res.getLong(1);
				} else {
					throw new RuntimeException("Poll " + data[0] + " initialization has failed");
				}
			}

			createPollOptions(data[2], pollID, con, sce);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metoda za stvaranje opcija ankete
	 * 
	 * @param filePath staza do opcija ankete
	 * @param pollID   id ankete u bazi
	 * @param con      konekcija s bazom
	 * @param sce      kontekst servleta
	 */
	private void createPollOptions(String filePath, long pollID, Connection con, ServletContextEvent sce) {
		Path path = Paths.get(sce.getServletContext().getRealPath(filePath));

		try {
			PreparedStatement pst = con.prepareStatement(
					"INSERT INTO POLLOPTIONS (optionTitle, pollID, optionLink, votesCount) VALUES (?,?,?,0)");

			List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.toList());

			for (String line : lines) {
				String[] data = line.split("\t");

				pst.setString(1, data[0]);
				pst.setLong(2, pollID);
				pst.setString(3, data[1]);

				int p = pst.executeUpdate();
				if (p < 1) {
					throw new RuntimeException("Initialization of polloptions has failed");
				}
			}

		} catch (SQLException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metoda za dohvat id-ja ankete.
	 * 
	 * @param string naslov ankete
	 * @param con    konekcija s bazom
	 * @return id ankete ako postoji ili null ako id ne postoji
	 */
	private Long pollID(String string, Connection con) {
		try {
			PreparedStatement pst = con.prepareStatement("SELECT * FROM POLLS WHERE title = ?");
			pst.setString(1, string);
			try (ResultSet res = pst.executeQuery()) {
				if (res != null && res.next()) {
					return res.getLong(1);
				}
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Provjera da li tablice postoje
	 * 
	 * @param con konekcija s bazom
	 */
	private void checkForTables(Connection con) {
		try (ResultSet res = con.getMetaData().getTables(null, null, "POLLS", null);) {
			if (res != null && res.next()) {
				return;
			}

			createPollsTable(con);
			createPollOptionsTable(con);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metoda za stvaranje tablice za opcije ankete
	 * 
	 * @param con konekcija
	 * 
	 * @throws SQLException ako stvaranje ne uspije
	 */
	private void createPollOptionsTable(Connection con) throws SQLException {
		PreparedStatement pst = con.prepareStatement("CREATE TABLE PollOptions\n"
				+ " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n" + " optionTitle VARCHAR(100) NOT NULL,\n"
				+ " optionLink VARCHAR(150) NOT NULL,\n" + " pollID BIGINT,\n" + " votesCount BIGINT,\n"
				+ " FOREIGN KEY (pollID) REFERENCES Polls(id)\n" + ")");

		pst.execute();
	}

	/**
	 * Metoda za stvaranje tablice za ankete
	 * 
	 * @param con konekcija s bazom
	 * 
	 * @throws SQLException ako stvaranje ne uspije
	 */
	private void createPollsTable(Connection con) throws SQLException {
		PreparedStatement pst = con
				.prepareStatement("CREATE TABLE Polls\n" + " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n"
						+ " title VARCHAR(150) NOT NULL,\n" + " message CLOB(2048) NOT NULL\n" + ")");

		pst.execute();
	}

	/**
	 * Metoda za generiranje url-a preko kojeg se aplikacija spaja s bazom
	 * 
	 * @param sce kontekst servleta
	 * 
	 * @return adresa na kojoj se nalazi baza s kojom se spajamo
	 */
	private String buildURL(ServletContextEvent sce) {
		Path path = Paths.get(sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties"));
		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			throw new RuntimeException("Database configuration file is missing");
		}

		Properties prop = new Properties();
		try {
			prop.load(Files.newInputStream(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return "jdbc:derby://" + prop.getProperty("host") + ":" + prop.getProperty("port") + "/"
				+ prop.getProperty("name") + ";user=" + prop.getProperty("user") + ";password="
				+ prop.getProperty("password");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource) sce.getServletContext()
				.getAttribute("hr.fer.zemris.dbpool");
		if (cpds != null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}