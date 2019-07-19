package hr.fer.zemris.hw06.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Razred koji pomoću javinih razreda {@link Cipher} i {@link MessageDigest}
 * obavlja enkripciju/dekripciju i hashiranje datoteka. Tip enkripcije i
 * dekripcije je SHA-256. Za enkripciju dekripciju potrebno je upisati lozinku i
 * inicijalizacijski vektor. Za provjeru hash-code-a potrebno je samo upisati
 * očekivani hashcode.
 * 
 * @author Ante Miličević
 *
 */
public class Crypto {
	/**
	 * Defaultni tip enkripcije
	 */
	private static final String encryptionType = "SHA-256";
	/**
	 * Objekt za šifriranje
	 */
	private static Cipher cipher;

	/**
	 * Ulazna točka programa. Program prima 2 argumenta ako se radi o provjeri hasha
	 * ili 3 argumenta ako se radi enkripcij. <br>
	 *
	 * 
	 * @param args Prvi argument je uvijek željena operacija
	 *             [checksha/encrypt/decrypt]
	 *             <p>
	 *             kod [checksha] drugi argument je staza do datoteke
	 *             </p>
	 *             <p>
	 *             kod [encrypt/decrypt] drugi argument je staza do datoteke nad
	 *             kojom se vrši kriptiranje, a treći argument je staza odredišta
	 *             kriptirane datoteke
	 *             </p>
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("Command and arguments are missing");
			System.exit(1);
		}
		try (Scanner sc = new Scanner(System.in)) {
			if (args[0].equals("checksha")) {
				checkSha(args, sc);
			} else if (args[0].equals("encrypt")) {
				setCryption(args, sc, true);
			} else if (args[0].equals("decrypt")) {
				setCryption(args, sc, false);
			} else {
				System.err.println("Command is not supported!");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.print(e.getMessage());
			System.exit(1);
		}

	}

	/**
	 * Inicijalizacija objekta za šifriranje
	 * 
	 * @param keyText lozinka
	 * @param ivText  inicijalizacijski vektor
	 * @param encrypt true ako se radi o enkripciji, false ako se radi o dekripciji
	 * 
	 * @throws RuntimeException ako inicijalizacija nije uspjela
	 */
	private static void initCipher(String keyText, String ivText, boolean encrypt) {
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Metoda koja vraća hashcode datoteke koja se nalazi na stazi {@code file}
	 * 
	 * @param file provjeravana datoteka
	 * @return heksadski zapis binarnog hash-a datoteke u string formatu
	 * 
	 * @throws NullPointerException ako je file null
	 * @throws RuntimeException     ako inicijalizacija ne uspije ili staza nije
	 *                              validna
	 */
	public static String digest(Path file) {
		Objects.requireNonNull(file);
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(encryptionType);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Digest can not be instanciated");
		}

		fillMessageDigest(messageDigest, file);

		byte[] result = messageDigest.digest();

		return Util.bytetohex(result);
	}

	/**
	 * Metoda koja čita podatke iz datoteke {@code file} i na temelju nje kroz
	 * {@link MessageDigest} generira njen hash code te ga vraća kao povratnu
	 * vrijednost metode.
	 * 
	 * @param messageDigest objekt koji generira hash vrijednost datoteke
	 * @param file          datoteka koju se hashira
	 * 
	 * @throws RuntimeException ako se dogodi greška pri čitanju
	 */
	private static void fillMessageDigest(MessageDigest messageDigest, Path file) {
		try (BufferedInputStream buffInput = new BufferedInputStream(
				Files.newInputStream(file, StandardOpenOption.READ))) {
			byte[] buff = new byte[4096];
			while (true) {
				int r = buffInput.read(buff);
				if (r <= 0)
					break;
				messageDigest.update(buff, 0, r);
			}
		} catch (IOException e) {
			throw new RuntimeException("Given file can not be read", e);
		}
	}

	/**
	 * Metoda koja kriptira {@code source} datoteku i šalje izlaz u datoteku na
	 * lokaciji {@code destination}. Kriptiranje se odvija pomoću {@link Cipher}.
	 * 
	 * @param source      staza do izvorne datoteke
	 * @param destination staza do odredišne datoteke
	 * 
	 * @throws RuntimeException ako izvorna datoteka ne postoji ili ako odredišna
	 *                          datoteka već postoji
	 */
	private static void cryptData(Path source, Path destination) {
		try (BufferedInputStream input = new BufferedInputStream(Files.newInputStream(source, StandardOpenOption.READ));
				BufferedOutputStream cipherStream = new BufferedOutputStream(
						Files.newOutputStream(destination, StandardOpenOption.CREATE_NEW))) {
			byte[] buff = new byte[4096];
			while (true) {
				int r = input.read(buff);
				if (r <= 0)
					break;
				cipherStream.write(cipher.update(buff, 0, r));
			}
			cipherStream.write(cipher.doFinal());

		} catch (IllegalBlockSizeException | IOException | BadPaddingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * Metoda prima argumente naredbenog retka i na temelju njih inicijalizira
	 * objekt {@link Cipher} pomoću kojeg šifrira datoteku te rezultat zapisuje na
	 * lokaciju predanu na mjestu trećeg argumenta naredbenog retka.
	 * 
	 * @param args    argumenti naredbenog retka
	 * @param sc      {@link Scanner} povezan sa System.in ulazom
	 * @param encrypt true ako se radi o enkripciji, false ako se radi o dekripciji
	 * 
	 * @throws IllegalArgumentException ako broj argumenata nije dobar ili ako
	 *                                  ključevi nisu u dobrom formatu
	 * @throws RuntimeException         ako kriptiranje ne uspije
	 */
	private static void setCryption(String[] args, Scanner sc, boolean encrypt) {
		if (args.length != 3) {
			throw new IllegalArgumentException("Command [" + args[0] + "] requires two arguments!");
		}
		String key = readShaKey(sc, true);
		String initVector = readShaKey(sc, false);

		initCipher(key, initVector, encrypt);

		Path source = Paths.get(args[1]);
		Path destination = Paths.get(args[2]);

		cryptData(source, destination);

		if (encrypt) {
			System.out.printf("Encryption completed. Generated file %s based on file %s.", args[2], args[1]);
		} else {
			System.out.printf("Decryption completed. Generated file %s based on file %s.", args[2], args[1]);
		}
	}

	/**
	 * Metoda koja sa {@link Scanner}-a čita 32-znamenkasti sha heksadekadski ključ
	 * 
	 * @param sc  Scanner preko kojeg se ostvaruje komunikacija s korisnikom
	 * @param key true ako se radi o lozinci, false ako se radi o inicijalizacijskom
	 *            vektoru
	 * 
	 * @return sha ključ
	 * 
	 * @throws IllegalArgumentException ako ključ nije u dobrom formatu
	 */
	private static String readShaKey(Scanner sc, boolean key) {
		if (key) {
			System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n> ");
		} else {
			System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n> ");
		}
		String value = sc.next();
		shaKeyValidation(value, key);
		return value;
	}

	/**
	 * Metoda koja validira 32-znamenkasti heksadekadski sha ključ
	 * 
	 * @param value vrijednost ključa
	 * @param key   true ako se radi o lozinci, false ako se radi o
	 *              inicijalizacijskom vektoru
	 * 
	 * @throws IllegalArgumentException ako ključ nije u dobrom formatu
	 */
	private static void shaKeyValidation(String value, boolean key) {
		if (!value.matches("[0-9|A-Z|a-z]{32}")) {
			throw new IllegalArgumentException((key ? "Key" : "Initialization vector") + " must be 32 hex-digits");
		}
	}

	/**
	 * Metoda koja provjerava ispravnost predanog hashcoda datoteke. Provjera se
	 * vrši usporedbom s novo izračunatim hash-om i onog predanog. Računanje
	 * hashcode-a se obavlja preko {@link MessageDigest}.
	 * 
	 * @param args argumenti naredbenog retka
	 * @param sc   Scanner preko kojeg se odvija komunikacija s korisnikom
	 * 
	 * @throws IllegalArgumentException ako u naredbenom retku nisu predani dobri
	 *                                  argumenti
	 * @throws RuntimeException         ako se dogodi greška pri čitanju datoteke
	 *                                  ili pri inicijalizaciji
	 *                                  {@link MessageDigest}-a
	 */
	private static void checkSha(String[] args, Scanner sc) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Command checksha requires only one argument!");
		}
		System.out.print("Please provide expected sha-256 digest for " + args[1] + ":\n> ");

		String checkedDigest = sc.next();
		String digest = digest(Paths.get(args[1]));

		String finalMessage = null;
		if (digest.equals(checkedDigest)) {
			finalMessage = successDigest(args[1]);
		} else {
			finalMessage = failDigest(args[1], digest);
		}
		System.out.println(finalMessage);
	}

	/**
	 * Metoda koja generira poruku ako nije prošla provjera hashcode-a datoteke
	 * 
	 * @param string staza do datoteke koja je provjerena
	 * @param digest izračunati hash
	 * 
	 * @return poruka
	 */
	private static String failDigest(String string, String digest) {
		return String.format("Digesting completed. Digest of %s does not match expected digest. Digest was: %s", string,
				digest);

	}

	/**
	 * Metoda koja generira poruku ako je prošla provjera hashcode-a datoteke
	 * 
	 * @param string staza do datoteke koja je provjerena
	 * 
	 * @return poruka
	 */
	private static String successDigest(String string) {
		return String.format("Digesting completed. Digest of %s matches expected digest", string);
	}

}
