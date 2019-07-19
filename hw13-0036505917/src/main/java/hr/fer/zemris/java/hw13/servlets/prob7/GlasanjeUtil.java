package hr.fer.zemris.java.hw13.servlets.prob7;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

/**
 * Pomoćni razred za sve operacije učitavanja i pisanje pri glasanju.
 * 
 * @author Ante Miličević
 *
 */
public class GlasanjeUtil {

	/**
	 * Monitor za rad s datotekom rezultata ankete
	 */
	public static Object resultsMonitor = new Object();

	/**
	 * Metoda za čitanje glasova. Ako datoteka ne postoji metoda vraća null.
	 * 
	 * @param req zahtjev
	 * @return mapa idBenda:brojGlasova
	 * 
	 * @throws IOException ako učitavanje ne uspije
	 */
	public static Map<Long, Long> readVotesFromFile(HttpServletRequest req) throws IOException {
			Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));
			
			if(!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				return generateVotes(req);
			}
			
			return Files.readAllLines(path, StandardCharsets.UTF_8).stream().collect(Collectors.toMap(
					(line) -> Long.parseLong(line.split("\t")[0]), (line) -> Long.parseLong(line.split("\t")[1])));
	}

	/**
	 * Metoda za generiranje mape idBenda:brojGlasova gdje je broj glasova 0
	 * 
	 * @param req zahtjev
	 * @return mapa idBenda:0
	 * 
	 * @throws IOException ako čitanje iz definicije bendova ne uspije
	 */
	public static Map<Long, Long> generateVotes(HttpServletRequest req) throws IOException {
		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt"));
		return Files.readAllLines(path, StandardCharsets.UTF_8).stream()
				.collect(Collectors.toMap((line) -> Long.parseLong(line.split("\t")[0]), (line) -> 0L));
	}

	/**
	 * Metoda za generiranje mape idBenda:brojGlasova gdje je broj glasova 0
	 * 
	 * @param bands bendovi
	 * @return mapa idBenda:0
	 * 
	 * @throws IOException ako čitanje iz definicije bendova ne uspije
	 */
	public static Map<Long, Long> generateVotes(List<Band> bands) throws IOException {
		return bands.stream().collect(Collectors.toMap((band) -> band.getID(), (band) -> 0L));
	}

	/**
	 * Metoda za čitanje informacija o bendovima
	 * 
	 * @param req zahtjev
	 * @return lista bendova
	 * 
	 * @throws IOException ako čitanje ne uspije
	 */
	public static List<Band> readBands(HttpServletRequest req) throws IOException {
		String fileName = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
		return Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8).stream().map((line) -> {
			String[] data = line.split("\t");
			return new Band(Long.parseLong(data[0]), data[1], data[2]);
		}).collect(Collectors.toList());
	}

	/**
	 * Metoda za upisivanje glasova u datoteku
	 * 
	 * @param req zahtjev
	 * @param data podaci za upis
	 * 
	 * @throws IOException ako upisivanje ne uspije
	 */
	public static void writeVotesInFile(HttpServletRequest req, Map<Long, Long> data) throws IOException {
		Path path = Paths.get(req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt"));
			Files.write(path, data.entrySet().stream().map((entry) -> entry.getKey() + "\t" + entry.getValue())
					.collect(Collectors.toList()));
	}
}
