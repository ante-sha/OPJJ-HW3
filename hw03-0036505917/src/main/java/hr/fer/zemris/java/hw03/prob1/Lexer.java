/**
 * Paket u kojem se nalaze razredi koji su rezultat rješavanja
 * 2. zadatka 3. domaće zadaće.
 */
package hr.fer.zemris.java.hw03.prob1;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static hr.fer.zemris.java.hw03.prob1.LexerState.*;
import java.util.Objects;
import static hr.fer.zemris.java.hw03.prob1.TokenType.*;

/**
 * Razred odgovoran za leksičku analizu predanog teksta.
 * Njegov zadatak je iz predanog teksta generirati tokene tipa {@link Token}. Lexer
 * se može nalaziti u dva stanja definirana kao enumovi {@link LexerState}. Stanje se može 
 * promijeniti samo izvana.
 * <br>Lexer ima i svoja unutarnja stanja kako bi označavao da li je unos validan ili ne.
 * Njegova stanja su preuzeta iz mogućih vrsta tokena {@link TokenType}.
 * @author Ante Miličević
 *
 */
public class Lexer {
	/**polje svih znakova koje Lexer treba obraditi*/
	private char[] data;
	/**zadnji generirani token*/
	private Token token;
	/**index na kojemu je stalo čitanje polja znakova*/
	private int currentIndex;
	/**stanje u kojem se Lexer nalazi*/
	private LexerState currentState;
	
	/**
	 * Konstruktor koji prima tekst za obradu, pretvara ga u polje znakova i sprema
	 * u varijablu data. Uz to kao defaultno stanje postavlja se stanje LexerState.BASIC.
	 * @param text tekst za obradu
	 * @throws NullPointerException ako je text == null
	 */
	public Lexer(String text) {
		Objects.requireNonNull(text);
		
		currentIndex = 0;
		currentState = BASIC;
		data = text.toCharArray();
	}
	
	/**
	 * Metoda za vanjsko postavljanje stanja Lexera
	 * @param state novo stanje Lexera
	 * @throws NullPointerException ako je {@code state == null}
	 */
	public void setState(LexerState state) {
		Objects.requireNonNull(state);
		
		this.currentState = state;
	}
	

	
	/**
	 * Metoda koja vraća zadnji izgenerirani token.
	 * @return zadnji izgenerirani token
	 * @throws NoSuchElementException ako još nije izgeneriran ni jedan element
	 */
	public Token getToken() {
		if(token == null)
			throw new NoSuchElementException("Ni jedan element nije izgeneriran");
		return token;
	}
	
	/**
	 * Metoda koja generira sljedeći token na način koji ovisi o stanju u kojem
	 * se lexer nalazi. Taj isti sprema u varijablu token.
	 * @return izgeneriran token
	 * @throws LexerException ako se zatraži generiranje tokena nakon što je predan 
	 * zadnji token ili format predanog teksta nije dobar
	 */
	public Token nextToken() {
		if(token != null && token.getType() == EOF)
			throw new LexerException("Nema više tokena!");
		
		if(currentState == BASIC)
			token = generateBASICToken();
		else
			token = generateEXTENDEDToken();
		
		return token;
	}
	
	/**
	 * Privatna metoda za generiranje tokena kada se lexer nalazi u LexerState.EXTENDED stanju.
	 * Mogući tokeni koji se generiraju su tipa TokenType.WORD te TokenType.SYMBOL iskuljučivo kod 
	 * pojavljivanja '#'.
	 * @return izgeneriran token
	 */
	private Token generateEXTENDEDToken() {
		char[] resultString = new char[10];
		int index=0;
		TokenType type = null;
		
		int i = 0;
		for(i = currentIndex;i<data.length;i++) {
			if(data[i] == ' ' || data[i] == '\n' || data[i] == '\r' || data[i] == '\t') {
				if(type!=null)
					break;
				
				continue;
			}
			if(data[i] == '#') {
				if(type != null)
					break;
				
				type = SYMBOL;
				resultString=addCharInArray(resultString,data[i],index++);
				i++;
				break;
			}
			if(type == null)
				type = WORD;
			resultString=addCharInArray(resultString,data[i],index++);
		}
		currentIndex = i;
		
		return zavrsnaObradaTokena(type,resultString,index);
	}
	
	/**
	 * Privatna metoda za generiranje tokena kada se lexer nalazi u LexerState.BASIC stanju. Princip
	 * rada lexera u ovoj metodi je isti kao i kod determinističkog konačnog automata gdje je početno stanje {@code null}.
	 * '\' znak označava escape tj. ako se iza njega nađe još jedan '\' ili broj tada lexer to tumači
	 * kao znak, sve ostale escape sekvence su zabranjene.
	 * <br>Svi tipovi tokena su mogući ({@link TokenType})
	 * @return izgeneriran token
	 * @throws LexerStateException ako dođe do nedozvoljene escape sekvence
	 * <br>ako broj koji je upisan ne stane u varijablu tipa long
	 */
	private Token generateBASICToken() {
		char[] resultString = new char[10];
		int index=0;
		TokenType type = null;
		boolean escapeFlag = false;
		
		int i = 0;
		for(i = currentIndex;i<data.length;i++) {
			if(data[i] == ' ' || data[i] == '\n' || data[i] == '\r' || data[i] == '\t') {
				if(type!=null)
					break;
				
				continue;
			}
			if(Character.isLetter(data[i])) {
				if(type == null && escapeFlag)
					throw new LexerException(data[i] + " ne može biti escape-an!");
				if(type == NUMBER)
					break;
				
				type = WORD;
				
			} else if(Character.isDigit(data[i])) {
				
				if(type == WORD && !escapeFlag)
					break;
				
				if(type != NUMBER && escapeFlag) {
					type = WORD;
					escapeFlag = false;
				}
				else if(type == null)
					type = NUMBER;
			} else if(data[i] == '\\') {
				
				if(type == NUMBER)
					break;
				if(!escapeFlag) {
					escapeFlag = true;
					continue;
				} else {
					escapeFlag = false;
					type = WORD;
				}
			} else {
				if(escapeFlag)
					throw new LexerException(data[i] + " ne moze biti escapean!");
				
				if(type != null)
					break;
				
				type = SYMBOL;
				resultString=addCharInArray(resultString,data[i],index++);
				i++;
				break;
			}
			resultString=addCharInArray(resultString,data[i],index++);
		}
		currentIndex = i;
		
		if(escapeFlag == true)
			throw new LexerException("Neiskorišten escape!");
		
		return zavrsnaObradaTokena(type,resultString,index);
	}
	
	/**
	 * Prevođenje unutarnjeg stanja lexera u token
	 * @param type unutarnje stanje lexera
	 * @param resultString polje trenutno pročitanih znakova
	 * @param duljina količina elemenata u polju relevantnih za stvaranje tokena
	 * @return token
	 * @throws LexerException ako broj ne stane u long varijablu
	 */
	private Token zavrsnaObradaTokena(TokenType type, char[] resultString,int duljina) {
		if(type == null)
			return new Token(EOF,null);
		if(type == SYMBOL) {
			return new Token(type,resultString[0]);
		}
		
		resultString = Arrays.copyOf(resultString, duljina);
		String result = new String(resultString);
		
		if(type == NUMBER) {
			try {
				long broj = Long.parseLong(result);
				return new Token(type,broj);
			} catch(NumberFormatException e) {
				throw new LexerException("Ulaz ne valja!");
			}
		} else if(type == WORD)
			return new Token(type,new String(resultString));
		else
			throw new LexerException("Neočekivana greška");
		
	}

	/**
	 * Privatna metoda za dodavanje znaka u polje znakova, gdje se pri
	 * popunjenom kapacitetu realocira polje duple veličine
	 * @param array polje u koje se upisuje znak
	 * @param c znak koji se upisuje
	 * @param index mjesto na koje se upisuje znak
	 * @return referencu na polje nakon upisivanje znaka
	 */
	private char[] addCharInArray(char[] array, char c,int index) {
		if(index >= array.length)
			array=Arrays.copyOf(array, array.length*2);
		
		array[index] = c;
		return array;
	}
}
