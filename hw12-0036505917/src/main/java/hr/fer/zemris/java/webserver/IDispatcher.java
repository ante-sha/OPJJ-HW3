package hr.fer.zemris.java.webserver;

/**
 * Sučelje koje definira objekte koji izvode dokument koji se nalazi na traženoj
 * stazi. Ako je dokument slika ili tekst isti se samo kopiraju u izlazni tok, a
 * ako se na odredištu nalazi skripta ona se izvodi i njen rezultat izvođenja se
 * zapisuje u izlazni tok.
 * 
 * @author Ante Miličević
 *
 */
public interface IDispatcher {
	/**
	 * Metoda koja izvodi zahtjev koji se odnosi na stazu urlPath
	 * 
	 * @param urlPath staza zahtjeva
	 * @throws Exception ako se zahtjev ne provede uspješno
	 */
	void dispatchRequest(String urlPath) throws Exception;
}