package hr.fer.zemris.java.webserver;

/**
 * Sučelje koje definira radnika koji generira generira izlazni dokument te ga
 * zapisuje u izlazni tok konteksta.
 * 
 * @author Ante Miličević
 *
 */
public interface IWebWorker {
	/**
	 * Metoda koja izvodi zahtjev i rezultat zapisuje u context
	 * 
	 * @param context objekt koji je posrednik između servera i korisnika pri ispisu
	 * 
	 * @throws Exception ako se zahtjev je uspije izvesti
	 */
	public void processRequest(RequestContext context) throws Exception;
}