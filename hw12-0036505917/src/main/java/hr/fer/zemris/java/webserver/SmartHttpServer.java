package hr.fer.zemris.java.webserver;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;

/**
 * Razred koji implementira server čiji se atributi definiraju u dokumentu
 * unutar config datoteke, ime dokumenta se predaje u konstruktoru. Komunikacija
 * servera i korisnika se odvija TCP protokolom.
 * 
 * @author Ante Miličević
 *
 */
public class SmartHttpServer {
	@SuppressWarnings("unused")
	/**
	 * Adresa poslužitelja
	 */
	private String address;
	/**
	 * Ime domene poslužitelja
	 */
	private String domainName;
	/**
	 * Port na kojem sluša poslužitelj
	 */
	private int port;
	/**
	 * Broj radničkih dretvi
	 */
	private int workerThreads;
	/**
	 * Vrijeme zadržavanja neaktivne sjednice
	 */
	private int sessionTimeout;
	/**
	 * Tipovi izlaznih dokumenata
	 */
	private Map<String, String> mimeTypes = new HashMap<String, String>();
	/**
	 * Kolekcija imenovanih generatora izlaznih dokumenata
	 */
	private Map<String, IWebWorker> workersMap = new HashMap<>();
	/**
	 * Dretva koja prihvaća zahtjeve koji dolaze na server
	 */
	private ServerThread serverThread;
	/**
	 * Bazen radničkih dretvi
	 */
	private ExecutorService threadPool;
	/**
	 * Staza do vršnog direktorija u kojem se nalaze podaci koji se preko web-a
	 * pružaju korisniku
	 */
	private Path documentRoot;
	/**
	 * Podaci o sjednicama
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	/**
	 * Objekt koji služi generiranju nasumičnih ID-a sjednica
	 */
	private Random sessionRandom = new Random();

	/**
	 * Konstruktor
	 * 
	 * @throws IOException ako se čitanje konfiguracijske datoteke ne uspije
	 */
	public SmartHttpServer(String configFileName) throws IOException {
		readProperties(configFileName);
	}

	/**
	 * Ulazna točka glavnog programa
	 * 
	 * @param args ne koriste se
	 * 
	 * @throws IOException ako učitavanje konfiguracijske datoteke servera ne uspije
	 */
	public static void main(String[] args) throws IOException {
		new SmartHttpServer("server.properties").start();
	}

	/**
	 * Metoda za čitanje i postavljanja parametara iz konfiguracijske datoteke
	 * 
	 * @param configFileName naziv konfiguracijske datoteke
	 * 
	 * @throws IOException ako čitanje ne uspije
	 */
	private void readProperties(String configFileName) throws IOException {
		Properties properties = new Properties();
		properties.load(Files.newBufferedReader(Paths.get("./config", configFileName)));

		address = properties.getProperty("server.address");
		domainName = properties.getProperty("server.domainName");
		port = Integer.parseInt(properties.getProperty("server.port"));
		workerThreads = Integer.parseInt(properties.getProperty("server.workerThreads"));
		sessionTimeout = Integer.parseInt(properties.getProperty("session.timeout"));
		documentRoot = Paths.get(properties.getProperty("server.documentRoot"));

		readMime(properties.getProperty("server.mimeConfig"));
		readWorkers(properties.getProperty("server.workers"));
	}

	@SuppressWarnings("deprecation")
	/**
	 * Metoda za čitanje i spremanje generatora izlaznog dokumenta
	 * 
	 * @param configFileName naziv konfiguracijske datoteke gdje se nalazi staza po
	 *                       kojoj se zahtjev prosljeđuje generatoru i njegov fully
	 *                       qualified name
	 * 
	 * @throws IOException              ako čitanje ne uspije
	 * @throws IllegalArgumentException ako konfiguracijska datoteka nije validna
	 */
	private void readWorkers(String configFileName) throws IOException {
		Properties workProp = new Properties();
		workProp.load(Files.newBufferedReader(Paths.get(configFileName)));

		try {
			for (var entry : workProp.entrySet()) {
				String path = entry.getKey().toString();
				String fqcn = entry.getValue().toString();

				Class<?> referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
				Object newObject = referenceToClass.newInstance();
				IWebWorker iww = (IWebWorker) newObject;
				workersMap.put(path, iww);
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Metoda za čitanje i spremanje tipova izlaznog dokumenta
	 * 
	 * @param configFileName naziv konfiguracijske datoteke u kojoj je svaki radak
	 *                       oblika [tip] = [vrijednost tipa u izlaznom dokumentu]
	 * @throws IOException ako čitanje ne uspije
	 */
	private void readMime(String configFileName) throws IOException {
		Properties mimeProp = new Properties();
		mimeProp.load(Files.newBufferedReader(Paths.get(configFileName)));

		mimeProp.entrySet().forEach((entry) -> mimeTypes.put(entry.getKey().toString(), entry.getValue().toString()));
	}

	/**
	 * Metoda za pokretanje servera
	 */
	protected synchronized void start() {
		if (serverThread != null && serverThread.isAlive()) {
			return;
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);

		serverThread = new ServerThread();
		serverThread.start();
	}

	@SuppressWarnings("deprecation")
	/**
	 * Metoda za zaustavljanje servera
	 */
	protected synchronized void stop() {
		serverThread.stop();
		threadPool.shutdown();
	}

	/**
	 * Razred koji modelira dretvu poslužitelja servera koja samo prima poslove koje
	 * prosljeđuje na izvođenje
	 * 
	 * @author Ante Miličević
	 *
	 */
	protected class ServerThread extends Thread {
		@Override
		public void run() {
			/**
			 * Čistač podataka o zastarjelim sjednicama
			 */
			Thread cleaner = new Thread(() -> {
				while (true) {
					synchronized (sessions) {
						sessions.values().removeIf((entry) -> {
							return entry.validUntil < new Date().getTime();
						});
					}
					try {
						Thread.sleep(300_000);
					} catch (InterruptedException ignore) {
					}
				}
			});
			cleaner.setDaemon(true);
			cleaner.start();

			try (ServerSocket serverSocket = new ServerSocket()) {

				serverSocket.bind(new InetSocketAddress((InetAddress) null, port));

				while (true) {
					Socket client = serverSocket.accept();

					ClientWorker cw = new ClientWorker(client);

					threadPool.submit(cw);
				}

			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * Razred koji modelira posao posluživanja korisnika
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		/**
		 * Socket koji je poveznica poslužitelja i korisnika
		 */
		private Socket csocket;
		/**
		 * Ulazni tok socketa
		 */
		private PushbackInputStream istream;
		/**
		 * Izlazni tok socketa
		 */
		private OutputStream ostream;
		/**
		 * Verzija protokola zahtjeva
		 */
		private String version;
		/**
		 * Pozvana metoda
		 */
		private String method;
		/**
		 * Host
		 */
		private String host;
		/**
		 * Parametri zahtjeva
		 */
		private Map<String, String> params =  new HashMap<>();
		/**
		 * Privremeni parametri
		 */
		private Map<String, String> tempParams = new HashMap<>();
		/**
		 * Korisnikovi parametri
		 */
		private Map<String, String> permParams;
		/**
		 * Izlazni kolačići
		 */
		private List<RequestContext.RCCookie> outputCookies = new ArrayList<>();
		/**
		 * Session ID
		 */
		private String SID;
		/**
		 * Kontekst
		 */
		private RequestContext context = null;

		/**
		 * Konstruktor
		 * @param csocket socket
		 */
		public ClientWorker(Socket csocket) {
			super();
			this.csocket = csocket;

		}

		@Override
		public void run() {
			try {
				istream = new PushbackInputStream(csocket.getInputStream());
				ostream = new BufferedOutputStream(csocket.getOutputStream());

				List<String> request = readRequest();
				if (request == null || request.size() < 1) {
					sendError(400, "Bad request");
					return;
				}

				String[] firstLine = request.get(0).split(" ");
				if (firstLine.length != 3) {
					sendError(400, "Bad request");
					return;
				}

				method = firstLine[0].toUpperCase();
				if (!method.equals("GET")) {
					sendError(405, "Method not Allowed");
					return;
				}

				version = firstLine[2].toUpperCase();
				if (!(version.equals("HTTP/1.0") || version.equals("HTTP/1.1"))) {
					sendError(400, "Bad request");
					return;
				}

				setHost(request);

				String[] data = firstLine[1].split("\\?");
				String path = data[0];
				String paramString = data.length >= 2 ? firstLine[1].substring(data[0].length() + 1) : "";

				String sidCandidate = checkSession(path, request);
				generateActualSid(sidCandidate);

				permParams = sessions.get(SID).map;

				outputCookies.add(new RequestContext.RCCookie("sid", SID, null, host, "/"));

				parseParams(paramString);

				createContext();

				if (ifWorkerWork(path)) {
					return;
				}

				if (ifExternalWork(path)) {
					return;
				}

				Path requestedPath = documentRoot.resolve(path.substring(1));
				if (!Files.exists(requestedPath, LinkOption.NOFOLLOW_LINKS)) {
					sendError(403, "Forbidden");
					return;
				}
				if (!Files.isRegularFile(requestedPath, LinkOption.NOFOLLOW_LINKS)
						|| !Files.isReadable(requestedPath)) {
					sendError(404, "Not found");
					return;
				}
				internalDispatchRequest(path, true);

			} catch (Exception e) {
				e.printStackTrace();
				try {
					sendError(400, "Bad request");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} finally {
				try {
					ostream.close();
					csocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		/**
		 * Metoda koja sprema sidCandidate kao session ID ako je validan ili generira
		 * novi session ID i sprema ga
		 * 
		 * @param sidCandidate vrijednost sid-a u kolačiću zaglavlja zahtjeva
		 */
		private void generateActualSid(String sidCandidate) {
			if (sidCandidate == null) {
				SID = generateRandomSid();
				return;
			}

			synchronized (sessions) {
				SessionMapEntry entry = sessions.get(sidCandidate);
				if (entry == null || !entry.host.equals(host)) {
					SID = generateRandomSid();
					return;
				}
				Date curDate = new Date();
				Date timeout = new Date();
				if (curDate.compareTo(timeout) < 0) {
					SID = generateRandomSid();
					return;
				}
				entry.validUntil += sessionTimeout;
				SID = entry.sid;
			}
		}

		/**
		 * Metoda koja generira random sid koji nije zauzet i vraća ga kao rezultat.
		 * 
		 * @return novi sid
		 */
		private String generateRandomSid() {
			synchronized (sessions) {
				String sid;
				do {
					char[] chars = new char[20];
					for (int i = 0; i < 20; i++) {
						chars[i] = (char) ('A' + sessionRandom.nextInt(26));
					}
					sid = new String(chars);
				} while (sessions.containsKey(sid));

				sessions.put(sid, new SessionMapEntry(sid, host, new Date().getTime() + sessionTimeout));
				return sid;
			}
		}

		/**
		 * Metoda za dohvat session ID-ja iz kolačića
		 * 
		 * @param path    staza na kojoj se koristi kolačić (parametar se trenutno ne
		 *                koristi pa je kolačić validan na cijeloj domeni)
		 * @param request zaglavlje zahtjeva
		 * 
		 * @return vrijednost sid parametra iz kolačića
		 */
		private String checkSession(String path, List<String> request) {
			String sidCandidate = null;
			for (String line : request) {
				if (!line.startsWith("Cookie:")) {
					continue;
				}
				String[] data = line.replaceAll("Cookie:\\s+", "").split(";\\s+");

				String[] cookie = data[0].split("=");
				if (cookie[0].equals("sid")) {
					sidCandidate = cookie[1];
				}
			}
			return sidCandidate == null ? null : sidCandidate.substring(1, sidCandidate.length() - 1);
		}

		@SuppressWarnings("deprecation")
		/**
		 * Metoda za proslijeđivanje zahtjeva vanjskom radniku ako je zahtjev usmjeren
		 * na vanjskog radnika
		 * 
		 * @param path staza zahtjeva
		 * @return true ako je staza za radnika i radnik je obradio zahtjev false ako
		 *         staza nije za radnika
		 * 
		 * @throws Exception ako je obrada započela, a nije uspjela
		 */
		private boolean ifExternalWork(String path) throws Exception {
			if (!path.startsWith("/ext/")) {
				return false;
			}
			path = path.replace("/ext/", "");
			String externalClass = path.substring(0, path.indexOf('?') < 0 ? path.length() : path.indexOf('?'));

			IWebWorker obj = (IWebWorker) Class.forName("hr.fer.zemris.java.webserver.workers." + externalClass)
					.newInstance();

			obj.processRequest(context);

			return true;
		}

		/**
		 * Metoda koja provjerava da li je staza asocirana s radnikom i ako je
		 * prosljeđuje mu zahtjev.
		 * 
		 * @param path staza zahtjeva
		 * @return true ako je zahtjev obavljen, false inače
		 * 
		 * @throws Exception ako je obrada započela, a nije uspjela
		 */
		private boolean ifWorkerWork(String path) throws Exception {
			IWebWorker worker = workersMap.get(path);
			if (worker == null) {
				return false;
			}

			worker.processRequest(context);

			return true;
		}

		/**
		 * Metoda za upisivanje sadržaja datoteke s lokacije urlPath u RCContext
		 * 
		 * @param urlPath staza do datoteke
		 * 
		 * @throws IOException ako ispis ne uspije
		 */
		private void fillRC(String urlPath) throws IOException {
			byte[] bytes = Files.readAllBytes(Paths.get(urlPath));

			context.write(bytes);
			ostream.flush();
		}

		/**
		 * Metoda za parsiranje parametara zahtjeva iz url-a
		 * 
		 * @param paramString argumenti iz url-a
		 */
		private void parseParams(String paramString) {
			if (paramString.isEmpty()) {
				return;
			}
			String[] entries = paramString.split("&");

			for (String entry : entries) {
				if (entry.isEmpty()) {
					continue;
				}
				String[] data = entry.split("=");
				params.put(data[0], data[1]);
			}
		}

		/**
		 * Metoda za određivanje tipa izlaznog dokumenta
		 * 
		 * @param urlPath staza do izlaznog dokumenta
		 * @return tip dokumenta
		 */
		private String determineMimeType(String urlPath) {
			String fileName = Paths.get(urlPath).getFileName().toString();
			if (!fileName.contains(".")) {
				return "application/octet-stream";
			}

			String key = fileName.substring(fileName.lastIndexOf(".") + 1);

			return mimeTypes.get(key) == null ? "application/octet-stream" : mimeTypes.get(key);
		}

		/**
		 * Čitanje host parametra iz zaglavlja, ako isti ne postoji host se postavlja na
		 * ime domene
		 * 
		 * @param request zaglavlje zahtjeva korisnika
		 */
		private void setHost(List<String> request) {
			Iterator<String> it = request.iterator();
			while (it.hasNext()) {
				String param = it.next();
				if (!param.startsWith("Host:")) {
					continue;
				}

				param = param.replace("Host:", "").trim();
				param = param.substring(0, param.indexOf(":"));
				host = param;
				return;
			}

			host = domainName;

		}

		/**
		 * Metoda za slanje poruke greške korisniku
		 * 
		 * @param statusCode status kod
		 * @param message    poruka greške
		 * 
		 * @throws IOException ako ispis ne uspije
		 */
		private void sendError(int statusCode, String message) throws IOException {
			createContext();
			context.setStatusCode(statusCode);
			context.setStatusText(message);
			context.setEncoding("UTF-8");

			context.write("<html><head><title>Error</title></head>" + "<body><h2 align=\"center\">" + statusCode + " "
					+ message + "</h2><hr></body></html>");
		}

		/**
		 * Metoda koja ispisuje dokument ili izvodi skriptu koja se nalazi na stazi urlPath
		 * 
		 * @param urlPath staza
		 * @param directCall zastavica koja označava da li je metoda pozvana direktno ili ne
		 * 
		 * @throws Exception ako zahtjev nije uspješno izveden
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws Exception {
			if ((urlPath.equals("/private") || urlPath.startsWith("/private/")) && directCall) {
				sendError(404, "Not found");
				return;
			}

			if (urlPath.contains(".smscr")) {
				String documentBody = Files.readString(documentRoot.resolve(Paths.get(urlPath.substring(1))));
				new SmartScriptEngine(new SmartScriptParser(documentBody).getDocumentNode(), context).execute();
				ostream.flush();
			} else {
				String mimeType = determineMimeType(urlPath);

				context.setMimeType(mimeType);
				context.setStatusCode(200);

				fillRC(documentRoot.resolve(Paths.get(urlPath.substring(1))).toString());
			}
		}

		/**
		 * Metoda koja stvara kontekst ako isti ne postoji
		 */
		private void createContext() {
			if (context == null) {
				permParams = permParams == null ? new HashMap<>() : permParams;
				context = new RequestContext(ostream, params, permParams, tempParams, outputCookies, this);
			}
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Metoda za čitanje zaglavlja zahtjeva
		 * 
		 * @return lista parametara zahtjeva
		 * 
		 * @throws IOException ako čitanje ne uspije
		 */
		private List<String> readRequest() throws IOException {
			List<String> result = new ArrayList<>();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;

			l: while (true) {
				byte b = (byte) istream.read();
				if (b == -1) {
					return null;
				}
				if (b != 13) {
					bos.write(b);
				}
				switch (state) {
				case 0:
					if (b == 13) {
						state = 1;
					} else if (b == 10)
						state = 4;
					break;
				case 1:
					if (b == 10) {
						state = 2;
					} else
						state = 0;
					break;
				case 2:
					if (b == 13) {
						state = 3;
					} else
						state = 0;
					break;
				case 3:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				case 4:
					if (b == 10) {
						break l;
					} else
						state = 0;
					break;
				}
			}
			for (String line : bos.toString(StandardCharsets.US_ASCII).split("\n")) {
				if (line.isEmpty()) {
					continue;
				}
				result.add(line);
			}
			return result;
		}
	}

	/**
	 * Podaci koji se čuvaju o sjednici korisnika
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class SessionMapEntry {
		/**
		 * ID sjednice
		 */
		private String sid;
		/**
		 * Host adresa
		 */
		private String host;
		/**
		 * Vrijeme do kada traje sjednica
		 */
		private long validUntil;
		/**
		 * Korisnikovi podaci
		 */
		private Map<String, String> map = new ConcurrentHashMap<>();

		/**
		 * Konstruktor
		 */
		public SessionMapEntry(String sid, String host, long validUntil) {
			super();
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
		}
	}
}