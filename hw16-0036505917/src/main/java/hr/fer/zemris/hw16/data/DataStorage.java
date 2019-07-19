package hr.fer.zemris.hw16.data;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

/**
 * Razred odgovoran za dohvaćanje svih podataka o slikama (oznake, slike i
 * njihove minijature). Prije prvog dohvata potrebno je izvršiti inicijalizaciju
 * staza do relevantih datoteka.
 * 
 * @author Ante Miličević
 *
 */
public class DataStorage {
	/**
	 * Skup oznaka slika
	 */
	private static Set<String> tags;
	/**
	 * Staza do opisnika
	 */
	private static Path pathToDescriptor;
	/**
	 * Informacije o slikama spremljene pod nazivom slike
	 */
	private static Map<String, ImageItem> imageInfo;
	/**
	 * Staza do direktorija sa slikama
	 */
	private static Path pathToImages;
	/**
	 * Staza do direktorija sa minijaturama slika
	 */
	private static Path pathToThumbs;

	/**
	 * Metoda za inicijalizaciju statičkih varijabli
	 * 
	 * @param pathToDescriptor staza do opisnika
	 * @param pathToImages     staza do direktorija sa slikama
	 * @param pathToThumbs     staza do direktorija sa minijaturama slika
	 * @throws IOException ako čitanje opisnika ne uspije
	 */
	public static void initializePaths(Path pathToDescriptor, Path pathToImages, Path pathToThumbs) throws IOException {
		DataStorage.pathToDescriptor = pathToDescriptor;
		DataStorage.pathToImages = pathToImages;
		DataStorage.pathToThumbs = pathToThumbs;
		firstRead();
	}

	/**
	 * Metoda koja obavlja čitanje opisnika i puni statičke varijable podacima iz
	 * opisnika
	 * 
	 * @throws IOException ako čitanje ne uspije
	 */
	private static void firstRead() throws IOException {
		imageInfo = new HashMap<>();
		tags = new HashSet<>();
		try (BufferedReader reader = Files.newBufferedReader(pathToDescriptor)) {
			while (true) {
				String imageName = reader.readLine();
				if (imageName == null) {
					break;
				}
				ImageItem item = new ImageItem();
				item.setName(imageName);

				String description = reader.readLine();
				item.setDescription(description);

				List<String> tags = Arrays.asList(reader.readLine().split(","));
				tags.replaceAll((string) -> string.trim());

				DataStorage.tags.addAll(tags);
				item.setTags(tags);

				imageInfo.put(imageName, item);
			}
		}
	}

	/**
	 * Metoda za dohvat skupa svih oznaka slika
	 * 
	 * @return tags
	 */
	public static Set<String> getTags() {
		return tags;
	}

	/**
	 * Metoda za provjeru postojanja slike
	 * 
	 * @param name ime slike
	 * @return true ako slika postoji, false inače
	 */
	public static boolean imageExists(String name) {
		return imageInfo.containsKey(name);
	}

	/**
	 * Skup slika koje su povezane s oznakom tag
	 * 
	 * @param tag oznaka
	 * @return skup imena slika
	 */
	public static Set<String> getImageNamesThatContainsTag(String tag) {
		Set<String> result = new HashSet<>();
		for (ImageItem item : imageInfo.values()) {
			if (item.getTags().contains(tag)) {
				result.add(item.getName());
			}
		}

		return result;
	}

	/**
	 * Dohvat informacija o slici s imenom name.
	 * 
	 * @param name naziv slike
	 * @return opisnik slike ili null ako slika ne postoji
	 */
	public static ImageItem getImageItem(String name) {
		return imageInfo.get(name);
	}

	/**
	 * Metoda za čitanje slike naziva name
	 * 
	 * @param name naziv slike
	 * @return pročitana slika
	 * 
	 * @throws IOException ako čitanje ne uspije
	 */
	public static BufferedImage getFullImageForName(String name) throws IOException {
		BufferedImage img = null;

		img = ImageIO.read(pathToImages.resolve(name).toFile());

		return img;
	}

	/**
	 * Metoda koja učitava minijaturu slike. Ako takva ne postoji, stvara se i
	 * sprema se u odgovarajuću datoteku.
	 * 
	 * @param name naziv slike
	 * @return minijaturu slike naziva name
	 * 
	 * @throws IOException ako čitanje ne uspije
	 */
	public static BufferedImage getThumbnailForImageName(String name) throws IOException {
		Path path = pathToThumbs.resolve(name);

		if (!Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
			Image img = getFullImageForName(name).getScaledInstance(150, 150, Image.SCALE_FAST);

			BufferedImage thumb = new BufferedImage(150, 150, BufferedImage.TYPE_INT_RGB);
			Graphics2D graph = thumb.createGraphics();
			graph.drawImage(img, 0, 0, null);
			graph.dispose();

			Files.createFile(path);
			ImageIO.write(thumb, "jpg", path.toFile());

			return thumb;
		} else {
			return ImageIO.read(path.toFile());
		}
	}
}
