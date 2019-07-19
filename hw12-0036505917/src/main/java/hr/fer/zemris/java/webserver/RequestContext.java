package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Razred koji nam služi kao posrednik za ispis između servera, radnika, skripti
 * i korisnika koji dobiva ispis. Pri prvom pozivu ispisa ispisuje se header
 * http 1.1 protokola. Jednom kada je ispis krenuo mijenjanje parametara
 * zaglavlja je onemogućeno.
 * 
 * @author Ante Miličević
 *
 */
public class RequestContext {
	/**
	 * Izlazni tok prema korisniku
	 */
	private OutputStream outputStream;
	/**
	 * Kodna stranica za ispis
	 */
	private Charset charset;
	/**
	 * Naziv kodne stranice
	 */
	private String encoding = "UTF-8";
	/**
	 * Status kod odgovora korisniku
	 */
	private int statusCode = 200;
	/**
	 * Tekst statusa
	 */
	private String statusText = "OK";
	/**
	 * Tip podatka u toku
	 */
	private String mimeType = "text/html";
	/**
	 * Duljina dokumenta
	 */
	private Long contentLength = null;
	/**
	 * Parametri zahtjeva korisnika
	 */
	private Map<String, String> parameters;
	/**
	 * Privremeni parametri odgovora
	 */
	private Map<String, String> temporaryParameters;
	/**
	 * Korisnikovi parametri
	 */
	private Map<String, String> persistantParameters;
	/**
	 * Lista kolačića koji se šalju korisniku
	 */
	private List<RCCookie> outputCookies;
	/**
	 * Zastavica koja označava da li je zaglavlje već ispisano
	 */
	private boolean headerGenerated = false;
	/**
	 * Objekt koji iz tražene staze generira odgovor koji ponovo prosljeđuje nazad
	 * RequestContext-u na ispis
	 */
	private IDispatcher dispatcher;

	/**
	 * Konstruktor bez privremenih parametara i otpremitelja
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistantParameters, List<RCCookie> outputCookies) {
		this(outputStream, parameters, persistantParameters, null, outputCookies, null);
	}

	/**
	 * Konstruktor
	 */
	public RequestContext(OutputStream outputStream, Map<String, String> parameters,
			Map<String, String> persistantParameters, Map<String, String> temporaryParameters,
			List<RCCookie> outputCookies, IDispatcher dispatcher) {
		super();
		this.outputStream = Objects.requireNonNull(outputStream);
		this.parameters = parameters == null ? Collections.emptyMap() : parameters;
		this.persistantParameters = persistantParameters == null ? new HashMap<>() : persistantParameters;
		this.temporaryParameters = temporaryParameters == null ? new HashMap<>() : temporaryParameters;
		this.outputCookies = outputCookies == null ? new LinkedList<>() : outputCookies;
		this.dispatcher = dispatcher;
	}

	/**
	 * Getter za parametra naziva name iz zahtjeva korisnika
	 * 
	 * @param name naziv parametra
	 * @return parametar
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}

	/**
	 * Metoda za dohvat imena svih parametara iz korisnikovog zahtjeva
	 * 
	 * @return skup svih imena parametara
	 */
	public Set<String> getParameterNames() {
		return Collections.unmodifiableSet(parameters.keySet());
	}

	/**
	 * Metoda za dohvat parametara korisnika
	 * 
	 * @param name naziv parametra
	 * @return vrijednost parametra
	 */
	public String getPersistantParameter(String name) {
		return persistantParameters.get(name);
	}

	/**
	 * Metoda za dohvat skupa naziva korisnikovih parametara
	 * 
	 * @return skup naziva korisnikovih parametara
	 */
	public Set<String> getPersistantParameterNames() {
		return Collections.unmodifiableSet(persistantParameters.keySet());
	}

	/**
	 * Metoda za postavljanje korisnikovog parametra
	 * 
	 * @param name  naziv pod kojim se vrijednost sprema
	 * @param value vrijednost koja se sprema
	 */
	public void setPersistantParameter(String name, String value) {
		persistantParameters.put(name, value);
	}

	/**
	 * Metoda za uklanjanje korisnikovog parametra pod nazivom name
	 * 
	 * @param name naziv parametra
	 */
	public void removePersistentParameter(String name) {
		persistantParameters.remove(name);
	}

	/**
	 * Metoda za dohvat privremenog parametra pod nazivom name
	 * 
	 * @param name naziv parametra
	 * @return vrijednost parametra
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}

	/**
	 * Metoda za dohvat skupa naziva privremenih parametara
	 * 
	 * @return skup naziva privremenih parametara
	 */
	public Set<String> getTemporaryParameterNames() {
		return Collections.unmodifiableSet(temporaryParameters.keySet());
	}

	/**
	 * Metoda za dohvat sessionID-a koji se nalazi u izlaznim kolačićima
	 * 
	 * @return sessionID
	 */
	public String getSessionID() {
		for (RCCookie cookie : outputCookies) {
			if (cookie.getName().equals("sid")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * Metoda za postavljanje privremenog parametra naziva name i vrijednosti value
	 * 
	 * @param name  naziv parametra
	 * @param value vrijednost parametra
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}

	/**
	 * Metoda za uklanjanje privremenog parametra naziva name
	 * 
	 * @param name naziv parametra
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}

	/**
	 * Metoda za upisivanje bajtova u izlazni tok outputStream
	 * 
	 * @param data podaci
	 * @return this
	 * 
	 * @throws IOException ako upis nije bio uspješan
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}

	/**
	 * Metoda za generiranje i ispis zaglavlja odgovora korisniku
	 * 
	 * @throws IOException ako ispis ne uspije
	 */
	private void generateHeader() throws IOException {
		charset = Charset.forName(encoding);

		StringBuilder sb = new StringBuilder();

		sb.append(String.format("HTTP/1.1 %s %s\r\n", statusCode, statusText));

		sb.append(String.format("Content-Type: %s", mimeType));
		if (mimeType.startsWith("text/")) {
			sb.append(String.format("; charset=%s", encoding));
		}
		sb.append("\r\n");

		if (contentLength != null) {
			sb.append(String.format("Content-Length:%d\r\n", contentLength));
		}

		outputCookies.forEach((cookie) -> {
			sb.append("Set-Cookie: ");
			sb.append(String.format("%s=\"%s\"", cookie.name, cookie.value));
			if (cookie.domain != null) {
				sb.append(String.format("; Domain=%s", cookie.domain));
			}
			if (cookie.path != null) {
				sb.append(String.format("; Path=%s", cookie.path));
			}
			if (cookie.maxAge != null) {
				sb.append(String.format("; Max-Age=%s", cookie.maxAge));
			}
			sb.append("; HttpOnly");
			sb.append("\r\n");
		});
		sb.append("\r\n");

		outputStream.write(sb.toString().getBytes(StandardCharsets.US_ASCII));

		headerGenerated = true;
	}

	/**
	 * Metoda za ispis bajtova s određene pozicije i duljine u polju
	 * 
	 * @param data   bajtovi podataka
	 * @param offset pomak
	 * @param len    duljina
	 * @return this
	 * 
	 * @throws IOException ako ispis ne uspije
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}

		outputStream.write(data, offset, len);

		return this;
	}

	/**
	 * Metoda za ispis teksta u izlazni tok
	 * 
	 * @param text tekst za ispis
	 * @return this
	 * 
	 * @throws IOException ako ispis ne uspije
	 */
	public RequestContext write(String text) throws IOException {
		if (!headerGenerated) {
			generateHeader();
		}

		outputStream.write(text.getBytes(charset));

		return this;
	}

	/**
	 * Metoda za postavljanje kodne stranice po nazivu
	 * 
	 * @param encoding naziv kodne stranice
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void setEncoding(String encoding) {
		checkHeaderAndThrow();
		this.encoding = encoding;
	}

	/**
	 * Metoda koja provjerava da li je izmjena parametra zaglavlja validna
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	private void checkHeaderAndThrow() {
		if (headerGenerated) {
			throw new RuntimeException("Header already generated");
		}
	}

	/**
	 * Metoda koja postavlja kod statusa odgovora
	 * 
	 * @param statusCode
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void setStatusCode(int statusCode) {
		checkHeaderAndThrow();
		this.statusCode = statusCode;
	}

	/**
	 * Metoda koja postavlja tekst statusa odgovora
	 * 
	 * @param statusText
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void setStatusText(String statusText) {
		checkHeaderAndThrow();
		this.statusText = statusText;
	}

	/**
	 * Metoda za postavljanje tipa dokumenta koji se ispisuje korisniku
	 * 
	 * @param mimeType
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void setMimeType(String mimeType) {
		checkHeaderAndThrow();
		this.mimeType = mimeType;
	}

	/**
	 * Metoda za postavljanje duljine dokumenta
	 * 
	 * @param contentLength
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void setContentLength(Long contentLength) {
		checkHeaderAndThrow();
		this.contentLength = contentLength;
	}

	/**
	 * Metoda za dodavanje kolačića
	 * 
	 * @param cookie
	 * 
	 * @throws RuntimeException ako je zaglavlje već generirano
	 */
	public void addRCCookie(RCCookie cookie) {
		Objects.requireNonNull(cookie);
		checkHeaderAndThrow();

		outputCookies.add(cookie);
	}

	/**
	 * Metoda za dohvat posrednika pri čitanju web resursa
	 * 
	 * @return dispatcher
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Razred koji modelira kolačić koji se koristi za očuvanje
	 * korisnikovih podataka pri komunikaciji
	 * 
	 * @author Ante Miličević
	 *
	 */
	public static class RCCookie {
		/**
		 * Naziv podatka
		 */
		private String name;
		/**
		 * Vrijednost podatka
		 */
		private String value;
		/**
		 * Domena s koje se kolačić šalje
		 */
		private String domain;
		/**
		 * Staza na kojoj se kolačić treba poslati nazad
		 */
		private String path;
		/**
		 * Maksimalna starost
		 */
		private Integer maxAge;

		/**
		 * Konstruktor
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path) {
			super();
			this.name = name;
			this.value = value;
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
		}

		/**
		 * Getter za ime
		 * 
		 * @return name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter za vrijednost
		 * 
		 * @return value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Getter za domenu
		 * 
		 * @return domain
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Getter za stazu
		 * 
		 * @return path
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Getter za maksimalnu starost
		 * 
		 * @return maxAge
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
	}
}
