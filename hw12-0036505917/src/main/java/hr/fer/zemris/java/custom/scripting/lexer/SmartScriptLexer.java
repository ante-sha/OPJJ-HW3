/**
 * Paket u koji sadrži sve razrede koje koristi {@link SmartScriptLexer}
 */
package hr.fer.zemris.java.custom.scripting.lexer;

import static hr.fer.zemris.java.custom.scripting.lexer.SmartScriptLexerState.*;
import static hr.fer.zemris.java.custom.scripting.lexer.TokenType.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Razred koji modelira lexer koji je kompatibilan za korištenje sa {@link SmartScriptParser}.
 * Lexer se može nalaziti u dva stanja definirana u {@link SmartScriptLexerState}. Njegova vanjska
 * stanja mogu se mijenjati samo izvana.Osim tih "vanjskih" stanja lexer 
 * se ovisno o vanjskom stanju može nalaziti u više unutarnjih stanja specifičnih
 * za to unutarnje stanje.
 * <br>
 * Lexer na zahtjev (metodom noviToken()) čitanjem znakova generira {@link Token}. Tokeni su detaljnije 
 * definirani u enumeraciji Token.
 * @author Ante Miličević
 *
 */
public class SmartScriptLexer {
	/**Polje znakova koje lexer treba pročitati*/
	private char[] znakovi;
	/**Dozvoljeni operatori u SmartScriptLexerState.TAG stanju*/
	private static final char[] operatori = {'+','-','*','/','^'};
	/**Pozicija do koje je lexer pročitao tekst*/
	private int index;
	/**Vanjsko stanje u kojem se lexer nalazi, default stanje je SmartScriptLexerState.TEXT*/
	private SmartScriptLexerState trenutnoStanje = TEXT;
	/**Zadnji predani token*/
	private Token zadnjiToken = null;
	
	private boolean stanjeGreske = false;

	/**
	 * Konstruktor koji inicijalizira lexer predavanjem teksta kojeg treba obraditi
	 * @param tijeloDokumenta tekst za obradu
	 * @throws NullPointerException ako je tijeloDokumenta == null
	 */
	public SmartScriptLexer(String tijeloDokumenta) {
		Objects.requireNonNull(tijeloDokumenta);
		znakovi = tijeloDokumenta.toCharArray();
	}
	
	/**
	 * Metoda koja vraća novi token ovisno o stanju u kojem se nalazi
	 * @return novi token
	 * @throws SmartScriptLexerException ako unešeni tekst nije u dobrom formatu ili ako se
	 * lexer nalazi u stanju greške
	 */
	public Token noviToken() {
		if(zadnjiToken != null && zadnjiToken.getType() == EOF)
			throw new SmartScriptLexerException("Nema više tokena!");
		if(stanjeGreske)
			throw new SmartScriptLexerException("Dogodila se greška pri čitanju");
		
		try {
			if(trenutnoStanje == TEXT)
				zadnjiToken = obradiKaoTekst();
			else if (trenutnoStanje == TAG)
				zadnjiToken = obradiKaoTag();
			return zadnjiToken;
		} catch (SmartScriptLexerException e) {
			stanjeGreske = true;
			throw e;
		}
	}
	
	/**
	 * Metoda koja vraća zadnji predani token
	 * @return zadnji token
	 * @throws NoSuchElementException ako prije poziva metode nije bilo generiranja tokena
	 * @throws SmartScriptLexerException ako se lexer nalazi u stanju greške
	 */
	public Token zadnjiToken() {
		if(stanjeGreske)
			throw new SmartScriptLexerException("Dogodila se greška pri čitanju!");
		if(zadnjiToken == null)
			throw new NoSuchElementException("Još nije bilo ni jednog generiranja tokena!");
		else
			return zadnjiToken;
	}
	
	/**
	 * Metoda koja postavlja lexeru stanje izvana
	 * @param stanje novo stanje lexera
	 * @throws NullPointerException ako je stanje == null
	 */
	public void setState(SmartScriptLexerState stanje) {
		Objects.requireNonNull(stanje);
		
		trenutnoStanje = stanje;
	}

	/**
	 * Privatna metoda odgovorna za generiranje tokena u stanju SmartScriptLexerState.TAG
	 * @return novi token
	 * @throws SmartScriptLexerException ako format teksta nije dobar
	 */
	private Token obradiKaoTag() {
		char[] rezultat = new char[10];
		
		int i=0;
		for(i = index;i<znakovi.length;i++) {
			if(znakovi[i] == ' ' || znakovi[i] == '\n' || znakovi[i] == '\r' || znakovi[i] == '\t') {
					continue;
			}
			//spremi znak u rezultat i smatraj ga pročitanim
			index = i+1;
			rezultat[0] = znakovi[i];
			if(Character.isLetter(znakovi[i])) {
				return citajVarijablu(rezultat);
			} else if(znakovi[i] == '@') {
				if(i+1 >= znakovi.length || !(Character.isLetter(znakovi[i+1])))
					throw new SmartScriptLexerException("Funkcija u krivom formatu");
				return citajFunkciju(rezultat);
			} else if(znakovi[i] == '"') {
				return citajString(rezultat);
			} else if(znakovi[i] == '-') {
				if(i+1<znakovi.length && Character.isDigit(znakovi[i+1]))
					return citajBroj(rezultat);
				else
					return citajOperator(rezultat);
			} else if(znakovi[i] == '$') {
				return citajZatvoreniTag(rezultat);
			} else if(Character.isDigit(znakovi[i])) {
				return citajBroj(rezultat);
			} else if (znakovi[i] == '='){
				return citajJednako(rezultat);
			} else if(dobarZnak(znakovi[i])) {
				return citajOperator(rezultat);
			} else
				throw new SmartScriptLexerException("Nedozvoljeni znak " + znakovi[i]);
		}
		return new Token(EOF,null);
	}
	
	/**
	 * Privatna metoda koja validira pisanje znaka $.
	 * @param rezultat polje koje sadrži pročitani znak '$'
	 * @return token zatvorenog taga
	 * @throws SmartScriptLexerException ako se $ ne koristi za zatvaranje tag-a
	 */
	private Token citajZatvoreniTag(char[] rezultat) {
		if(index<znakovi.length && znakovi[index] == '}') {
			index ++;
			return new Token(TokenType.ZATVOREN_TAG,"$}");
		}
		else
			throw new SmartScriptLexerException("Nedozvoljeno pisanje znaka $");
	}
	
	/**
	 * Privatna metoda koja generira token jednako.
	 * @param rezultat polje koje sadrži znak '='
	 * @return novi token
	 */
	private Token citajJednako(char[] rezultat) {
		return new Token(TokenType.JEDNAKO,rezultat[0]);
	}
	
	/**
	 * Privatna metoda koja generira token operatora
	 * @param rezultat polje koje sadrži pročitani operator
	 * @return novi token
	 */
	private Token citajOperator(char[] rezultat) {
		return new Token(TokenType.OPERATOR,rezultat[0]);
	}
	
	/**
	 * Privatna metoda koja generira token stringa u tag stanju. Dozvoljene su escape sekvence "\"", "\\", "\r", "\t", "\n". 
	 * @param rezultat polje koje sadrži pročitani znak '"'
	 * @return novi token
	 * @throws SmartScriptLexerException ako je generirana nedozvoljena escape sekvenca
	 */
	private Token citajString(char[] rezultat) {
		int i = 0;
		int duljina = 1;
		boolean escape = false;
		for(i = index;i<znakovi.length;i++) {
			if(znakovi[i] == '\\') {
				if(!escape) {
					escape = true;
					continue;
				}
				escape = false;
			} else if(znakovi[i] == '"') {
				if(!escape) {
					rezultat = dodajZnakUPolje(rezultat,znakovi[i++],duljina++);
					break;
				}
				escape = false;
			} else if(escape) {
				if(znakovi[i] == 'n')
					znakovi[i] = '\n';
				else if (znakovi[i] == 't')
					znakovi[i] = '\t';
				else if(znakovi[i] == 'r')
					znakovi[i] = '\r';
				else
					throw new SmartScriptLexerException("Nedozvoljena escape sekvenca unutar stringa u tagu");
				escape = false;
			}
			
			rezultat = dodajZnakUPolje(rezultat,znakovi[i],duljina++);
		}
		index = i;
		rezultat = Arrays.copyOfRange(rezultat, 0, duljina);
		
		return new Token(TokenType.STRING,new String(rezultat));
	}
	
	/**
	 * Privatna metoda koja interpretira nastavak znakova za čitanje kao naziv varijable.
	 * Regex varijable je {@code "[a-zA-Z][a-zA-Z|_|0-9]*"}.
	 * @param rezultat polje u kojem se nalazi prvi znak varijable
	 * @return novi token
	 */
	private Token citajVarijablu(char[] rezultat) {
		int duljina = 1;
		int i = 0;
		for(i = index; i < znakovi.length;i++) {
			if(!(Character.isLetter(znakovi[i]) || znakovi[i] == '_' || Character.isDigit(znakovi[i])))
				break;
			rezultat = dodajZnakUPolje(rezultat,znakovi[i],duljina++);
		}
		index = i;
		rezultat = Arrays.copyOfRange(rezultat, 0, duljina);
		return new Token(TokenType.VARIJABLA,new String(rezultat));
	}
	
	/**
	 * Privatna metoda koja sljedeći niz znakova interpretira kao broj. U metodi koja poziva ovu
	 * utvrđeno je da se radi o broju koji je ili "-[0-9][.|0-9]*" ili istom takvom bez minusa.
	 * @param rezultat polje u kojem se nalazi minus ili prvi broj
	 * @return novi token
	 * @throws NumberFormatException ako je broj cijeli i ne može se zapisati kao Integer ili ako
	 * je broj decimalan i ne može se zapisati kao double
	 */
	private Token citajBroj(char[] rezultat) {
		int duljina = 1;
		int i = 0;
		boolean tocka = false;
		for(i = index;i < znakovi.length;i++) {
			if(!(Character.isDigit(znakovi[i]) || znakovi[i] == '.'))
				break;
			if(znakovi[i] == '.') {
				if(tocka)
					break;
				if(i+1<znakovi.length && Character.isDigit(znakovi[i+1]))
					tocka = true;
				else
					break;
			}
			rezultat = dodajZnakUPolje(rezultat,znakovi[i],duljina++);
		}
		index = i;
		rezultat = Arrays.copyOfRange(rezultat, 0, duljina);
		String broj = new String(rezultat);
		try {
			if(!tocka)
				return new Token(TokenType.DECIMALNI,Integer.parseInt(broj));
			else
				return new Token(TokenType.DECIMALNI, Double.parseDouble(broj));
		} catch(NumberFormatException e) {
			throw new SmartScriptLexerException("Broj nije napisan u dobrom formatu!");
		}
	}
	
	/**
	 * Privatna metoda koja prevodi nastavak niza i interpretira ga kao naziv funkcije. Format naziva funkcije treba biti u obliku
	 * {@code "@[a-zA-Z][a-zA-Z|_|0-9]*"}. U metodi koja ju poziva provjerena su prva dva znaka naziva.
	 * @param rezultat polje u kojemu se nalazi '@'
	 * @return novi token
	 */
	private Token citajFunkciju(char[] rezultat) {
		int i = 0;
		int duljina = 1;
		for(i = index;i<znakovi.length;i++) {
			if(!(Character.isLetter(znakovi[i]) || znakovi[i] == '_' || Character.isDigit(znakovi[i])))
				break;
			rezultat = dodajZnakUPolje(rezultat,znakovi[i],duljina++);
		}
		index = i;
		rezultat = Arrays.copyOfRange(rezultat, 0, duljina);
		return new Token(TokenType.FUNKCIJA,new String(rezultat));
	}

	/**
	 * Privatna metoda koja provjerava da li je predani znak operator
	 * @param c provjeravani znak
	 * @return true - ako je, false inače
	 */
	private boolean dobarZnak(char c) {
		for(int i = 0; i< operatori.length;i++)
			if(operatori[i] == c)
				return true;
		return false;
	}

	/**
	 * Privatna metoda koja obrađuje nastavak teksta ako je lexer u stanju SmartScriptLexerState.TEXT
	 * @return novi token
	 * @throws SmartScriptLexerException ako format teksta nije dobar
	 */
	private Token obradiKaoTekst() {
		char[] rezultat = new char[10];
		int duljina = 0;
		TextType tip = null;
		
		int i=0;
		for(i = index;i<znakovi.length;i++) {
			if(tip == null) {
				if(znakovi[i] == '\\') {
					tip = TextType.ESCAPE;
					continue;
				} else if(znakovi[i] == '{') {
					tip = TextType.VITICE;
					continue;
				} else
					tip = TextType.TEKST;
			} else if(tip == TextType.TEKST) {
				if(znakovi[i] == '\\') {
					tip = TextType.ESCAPE;
					continue;
				} else if(znakovi[i] == '{') {
					if(i+1<znakovi.length && znakovi[i+1] == '$')
						break;
				}
			} else if(tip == TextType.VITICE) {
				if(znakovi[i] == '$') {
					tip = TextType.OTVORENI_TAG;
					rezultat[0] = '{';
					rezultat[1] = '$';
					duljina = 2;
					i++;
					break;
				}
			} else if(tip == TextType.ESCAPE) {
				if(znakovi[i] == '\\' || znakovi[i] == '{') {
					tip = TextType.TEKST;
				}
				else
					throw new SmartScriptLexerException("Nedozvoljena escape sekvenca");
			}
			
			rezultat = dodajZnakUPolje(rezultat,znakovi[i],duljina++);
		}
		
		index = i;
		if(tip == TextType.ESCAPE)
			throw new SmartScriptLexerException("Escape nije iskorišten!");
		if(tip == TextType.VITICE) {
			rezultat = dodajZnakUPolje(rezultat,'{',duljina++);
			tip = TextType.TEKST;
		}
		
		if(tip == TextType.TEKST) {
			rezultat = Arrays.copyOf(rezultat, duljina);
			return new Token(TEXT_TYPE,new String(rezultat));
		} else if(tip == TextType.OTVORENI_TAG) {
			return new Token(OTVOREN_TAG,"{$");
		} else return new Token(EOF,null);
	}
	
	/**
	 * Privatna metoda za dodavanje znaka u polje gdje ako se pokuša u puno polje dodati element
	 * polje se realocira na dvostruku veličini te se kopiraju svi elementi u dodaje se novi znak
	 * u polje. Vraćena referenca je referenca na polje
	 * @param polje polje u koje se dodaje znak
	 * @param c znak koji se dodaje
	 * @param index mjesto na koje se znak dodaje
	 * @return referenca na polje
	 */
	private char[] dodajZnakUPolje(char[] polje, char c,int index) {
		if(index >= polje.length)
			polje=Arrays.copyOf(polje, polje.length*2);
		
		polje[index] = c;
		return polje;
	}
	
	/**
	 * Privatna enumeracija za unutarnja stanja lexera kada se nalazi u stanju
	 * SmartScriptLexerState.TEXT
	 * @author Ante Miličević
	 *
	 */
	private enum TextType{
		TEKST,
		ESCAPE,
		VITICE,
		OTVORENI_TAG;
	}

}
