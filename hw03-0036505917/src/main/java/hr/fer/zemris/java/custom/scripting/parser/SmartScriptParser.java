/**
 * Paket u kojem se nalazi parser i iznimka koja se baca ako dođe do greške pri parsiranju.
 */
package hr.fer.zemris.java.custom.scripting.parser;

import java.util.Arrays;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.EmptyStackException;
import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.elems.*;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexer;
import hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState;
import hr.fer.zemris.java.custom.scripting.lexer.Token;
import hr.fer.zemris.java.custom.scripting.lexer.TokenType;
import hr.fer.zemris.java.custom.scripting.nodes.*;

/**
 * Razred koji modelira parser koji u komunikaciji sa {@link SmartScriptLexer}-om parsira dokument
 * jezika VLANG. Njegovu reprezentaciju sprema u čvorove {@link Node}. Vršni čvor je 
 * {@link DocumentNode}, dok ostali čvorovi predstavljaju moguće funkcije u jeziku i plain text
 * ({@link TextNode}). <br>Pri stvaranju parsera odmah se pokreće parsiranje.
 * @author Ante Miličević
 *
 */
public class SmartScriptParser {
	/**Lexer koji generira tokene za predani tekst*/
	private SmartScriptLexer lexer;
	/**Pomoćna varijabla za strukturiranje čvorova*/
	private ObjectStack stog=new ObjectStack();
	/**Vršni čvor dokumenta*/
	private DocumentNode glava = new DocumentNode();
	/**Nazivi podržanih naredbi*/
	private static final String FOR = "FOR";
	private static final String END = "END";
	private static final String ECHO = "=";

	/**
	 * Konstruktor koji inicijalizira lexer sa prosljeđenim tijelom dokumenta
	 * @param tijeloDokumenta
	 * @throws SmartScriptParserException ako parsiranje ne uspije, s metodom
	 * getCause() dobiva se uzrok iznimke
	 */
	public SmartScriptParser(String tijeloDokumenta) {
		try {
			this.lexer = new SmartScriptLexer(tijeloDokumenta);
			parsiraj();
		} catch(Exception e) {
			throw new SmartScriptParserException("Parsiranje nije uspjelo",e);
		}
	}
	
	/**
	 * Metoda koja vraća vršni čvor dokumenta
	 * @return vršni čvor dokumenta
	 */
	public DocumentNode getDocumentNode() {
		return glava;
	}
	
	/**
	 * Rekurzivna statička metoda za stvaranje teksta koji je isti, ali ne i ekvivalentni 
	 * izvornom tekst (čuva se semantika teksta) iz vršnog čvora
	 * dokumenta. 
	 * @param glava vršni čvor dokumenta
	 * @return izvorni tekst dokumenta
	 */
	public static String createOriginalDocumentBody(Node glava) {
		Objects.requireNonNull(glava);

		StringBuilder skupljac = new StringBuilder(1000);
		
		if(!(glava instanceof DocumentNode))
			skupljac.append(glava.toString());
		for(int i = 0;i<glava.numberOfChildren();i++)
			skupljac.append(createOriginalDocumentBody(glava.getChild(i)));
		if(glava instanceof ForLoopNode)
			skupljac.append("{$END$}");
		
		return skupljac.toString();
	}

	/**
	 * Metoda kojom započinje parsiranje teksta
	 * @throws SmartScriptParserException ako tekst nema dobru gramatiku
	 * @throws SmartScriptLexerException ako tekst nije u dobrom formatu
	 */
	private void parsiraj() {
		stog.push(glava);
		
		String tekst = new String("");
		ParserStanje zadnjeStanje = null;
		
		for(Token token = lexer.noviToken();token.getType()!=TokenType.EOF;token = lexer.noviToken()) {
			if(token.getType() == TokenType.TEXT_TYPE) {
				if(zadnjeStanje == ParserStanje.TAG || zadnjeStanje == null) {
					tekst = new String();
					zadnjeStanje = ParserStanje.TEXT;
				}
				
				tekst += token.getValue();
			} else {
				if(zadnjeStanje == ParserStanje.TEXT) {// || zadnjeStanje == null
					if(!tekst.equals(""))
						((Node)stog.peek()).addChildNode(new TextNode(tekst));
				}
				if(token.getType() == TokenType.OTVOREN_TAG) {
					
					lexer.setState(SmartScriptLexerState.TAG);
					
					
					token = lexer.noviToken();
					if(token.getType() == TokenType.VARIJABLA) {
						if(token.getValue().toString().toUpperCase().equals(END)) {
							provjeriEnd();
						}
						else if(token.getValue().toString().toUpperCase().equals(FOR)) {
							vratiFor();
						} else 
							throw new SmartScriptParserException("Tag naziva " + token.getValue() + " nije definiran!");
					} else if(token.getType() == TokenType.JEDNAKO) {
						vratiEcho();
					} else
						throw new SmartScriptParserException("Tag naziva " + token.getValue() + " nije definiran!");
					
					lexer.setState(SmartScriptLexerState.TEXT);
					zadnjeStanje = ParserStanje.TAG;
					
				} else {
					throw new SmartScriptParserException("Tag naziva " + token.getValue() + " nije definiran!");
				}
				
			}
		}
		if(zadnjeStanje == ParserStanje.TEXT) {
			if(!tekst.equals(""))
				((Node)stog.peek()).addChildNode(new TextNode(tekst));
		}
		
		if(stog.size()>1)
			throw new SmartScriptParserException("Premalo {$END$} tagova!");
	}



	/**
	 * Provjera koja se vrši pri pojavi END taga radi utvrđivanja preostale strukture na stogu.
	 */
	private void provjeriEnd() {
		if(lexer.noviToken().getType() == TokenType.ZATVOREN_TAG) {
			try {
				Node n = (Node)stog.pop();
				if(n instanceof DocumentNode)
					throw new EmptyStackException();
			} catch (EmptyStackException e) {
				throw new SmartScriptParserException("U kodu se nalazi previše {$END$} tagova"); 
			}
		} else 
			throw new SmartScriptParserException("Tag {$END$} nije u dobrom formatu!");
		
	}

	/**
	 * Pomoćna metoda koja sljedeći niz tokena interpretira kao argumente for petlje. For petlja je
	 * definirana s jednom varijablom i dodatna 2 ili 3 argumenta koji su konstante ili stringovi.
	 * @return čvor for petlje
	 * @throws SmartScriptParserException ako argumenti nisu dobri
	 */
	private void vratiFor() {
		
		Token token = lexer.noviToken();
		
		
		//provjera dobrog formata elemenata koji se unose
		Element varijabla = tokenPrevoditelj(token,FOR);
		
		if(!(varijabla instanceof ElementVariable))
			throw new SmartScriptParserException(String.format("%s nije valjano ime varijable", varijabla.asText()));
		
		Element[] varijable = new Element[3];
		
		int i = 0;
		for(token = lexer.noviToken();token.getType()!=TokenType.EOF && token.getType() != TokenType.ZATVOREN_TAG; token = lexer.noviToken()) {

			if(i>=3)
				throw new SmartScriptParserException("For tag sadrži previše argumenata");
			varijable[i++] = tokenPrevoditelj(token,FOR);
		}
		//provjera dobrog formata unosa
		if(token.getType() == TokenType.EOF)
			throw new SmartScriptParserException("For tag nije zatvoren!");
		if(i<2)
			throw new SmartScriptParserException("For tag sadrži premalo argumenata");
			
		ForLoopNode forNode = null;
		try {
			forNode = new ForLoopNode((ElementVariable)varijabla,varijable[0],varijable[1],varijable[2]);
		} catch (IllegalArgumentException e) {
			throw new SmartScriptParserException(
					String.format("Tijelo FOR taga nije dobro inicijalizirano!"));
		}

		((Node)stog.peek()).addChildNode(forNode);
		stog.push(forNode);
	}

	/**
	 * Pomoćna metoda koja prevodi tokene u elemente naredbe
	 * @param token token koji se prevodi
	 * @param ime ime naredbe
	 * @return element naredbe
	 */
	private Element tokenPrevoditelj(Token token, String ime) {
		//EOF i ZATVORENI_TAG nemaju svoje Elemente u koje bi se preveli
		if(token.getType() == TokenType.EOF)
			throw new SmartScriptParserException(ime + " tag nije zatvoren!");
		if(token.getType() == TokenType.ZATVOREN_TAG)
			throw new SmartScriptParserException(ime + " tag ne sadrži sve elemente!");
		
		Element elem = null;
		if(token.getType() == TokenType.OPERATOR) {
			String text = token.getValue().toString();
			elem = new ElementOperator(text.substring(0,1));
		} else if(token.getType() == TokenType.DECIMALNI) {
			if(token.getValue() instanceof Double)
				elem = new ElementConstantDouble(((Double)token.getValue()).doubleValue());
			else if(token.getValue() instanceof Integer)
				elem = new ElementConstantInteger(((Integer)token.getValue()).intValue());
			else
				throw new SmartScriptParserException("Broj unutar {$=$} taga nije u dobrom formatu!");
		} else if(token.getType() == TokenType.FUNKCIJA) {
			elem = new ElementFunction(token.getValue().toString().substring(1));
		} else if(token.getType() == TokenType.STRING) {
			String string = token.getValue().toString();
			elem = new ElementString(string.substring(1,string.length()-1));
		} else if(token.getType() == TokenType.VARIJABLA) {
			elem = new ElementVariable(token.getValue().toString());
		} else if(token.getType() == TokenType.JEDNAKO)
			throw new SmartScriptParserException(ECHO + " nije dozvoljen operator!");
		return elem;
	}

	/**
	 * Pomoćna metoda idući niz tokena interpretira kao elemente ECHO naredbe.
	 * Dozvoljene su konstante, stringovi, funkcije, varijable i operatori.
	 * @return novi echo čvor
	 */
	private void vratiEcho() {
		Element[] polje = new Element[10];
		int index=0;
		Token token = null;
		for(token = lexer.noviToken();token.getType()!=TokenType.EOF && token.getType() != TokenType.ZATVOREN_TAG;token = lexer.noviToken()) {
			Element elem = tokenPrevoditelj(token,ECHO);
			polje = dodajUPolje(polje,elem,index++);
		}
		
		if(token.getType() == TokenType.EOF)
			throw new SmartScriptParserException("Echo naredba nije napisana u dobrom formatu!");
		
		Node echo = new EchoNode(Arrays.copyOf(polje, index));
		((Node)stog.peek()).addChildNode(echo);;
	}
	
	/**
	 * Pomoćna metoda za dodavanje elemenata u polje na mjesto index, ako index bude jednak veličini
	 * polja veličina se poveća dva puta
	 * @param polje polje u koje se element dodaje
	 * @param e element koji se dodaje
	 * @param index mjesto na koje se dodaje
	 * @return referenca na polje
	 */
	private Element[] dodajUPolje(Element[] polje,Element e,int index) {
		if(index >= polje.length) {
			polje = Arrays.copyOf(polje, polje.length*2);
		}
		polje[index] = e;
		return polje;
	}
	
	/**
	 * Enumeracija koja označava unutarnja stanja parsera
	 * @author Ante Miličević
	 *
	 */
	private enum ParserStanje {
		TEXT,
		TAG;
	}
	
}
