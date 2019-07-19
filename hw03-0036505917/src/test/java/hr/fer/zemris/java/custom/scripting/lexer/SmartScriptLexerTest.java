package hr.fer.zemris.java.custom.scripting.lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

public class SmartScriptLexerTest {

	@Test
	public void praznoTijeloDokumenta() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertEquals(TokenType.EOF,lexer.noviToken().getType(),
				"Prazano tijelo dokumenta se treba vratiti kao jedan EOF token");
	}
	
	@Test
	public void nullTijeloDokumenta() {
		assertThrows(NullPointerException.class,()->new SmartScriptLexer(null),
				"NULL je nedozvoljeno tijelo dokumenta");
	}
	
	@Test
	public void zadnjiElementPrijeGeneriranjaTokena() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		assertThrows(NoSuchElementException.class,()->lexer.zadnjiToken(),
				"Pozivanje zadnjeg tokena prije stvaranja tokena je ne legalno");
	}
	
	@Test
	public void zadnjiTokenIstiKaoTekGenerirajuci() {
		SmartScriptLexer lexer = new SmartScriptLexer("neki tekst");
		
		Token prvi = lexer.noviToken();
		assertEquals(prvi,lexer.zadnjiToken(),"Zadnji token i tek generirajuci bi trebali biti isti");
	}
	
	@Test
	public void radNakonEOF() {
		SmartScriptLexer lexer = new SmartScriptLexer("");
		
		lexer.noviToken();
		
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken(),
				"Svako dohvaćanje poslije tokena EOF je nelegalno");
	}
	
	@Test
	public void tokenPraznogTeksta() {
		SmartScriptLexer lexer = new SmartScriptLexer("  \t \n \r  ");
		
		assertEquals("  \t \n \r  ",lexer.noviToken().getValue(),
				"Prvi token mora biti tekst");
		
		assertEquals(TokenType.EOF,lexer.noviToken().getType(),
				"Drugi token mora biti EOF");
	}
	
	@Test
	public void simboliUTekstu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" .:+*m,/51#$!");
		
		assertEquals(" .:+*m,/51#$!",lexer.noviToken().getValue());
	}
	
	@Test
	public void escapeanjeEscapeaUTekstu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \\\\ ");
		
		assertEquals(" \\ ",lexer.noviToken().getValue(),
				"Rezultat treba biti samo jedan escape znak unutar 2 razmaka");
	}
	
	@Test
	public void escapeanjeViticeUTekstu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \\{ ");
		
		Token token = lexer.noviToken();
		assertEquals(" { ",token.getValue(),
				"Rezultat treba biti samo jedana vitica znak unutar 2 razmaka");
	}
	
	@Test
	public void escapeanjeViticePrijeDolaraUTekstu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \\{$FOR$}");
		
		assertEquals(" {$FOR$}",lexer.noviToken().getValue(),
				"Escape sekvenca onesposobljava tag");
	}
	
	@Test
	public void vracanjeOtvorenogTagaUTekstu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \t{$    FOR sjaodij \n");
		
		assertEquals(TokenType.TEXT_TYPE,lexer.noviToken().getType());
		assertEquals(TokenType.OTVOREN_TAG,lexer.noviToken().getType());
	}
	
	@Test
	public void escapeUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer(" \\p sdhja");
		lexer.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken(),"");
	}
	
	@Test
	public void nedozvoljeniZnakoviUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer(".");
		lexer.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer(":");
		lexer2.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer2.noviToken());
		
		SmartScriptLexer lexer3 = new SmartScriptLexer(";");
		lexer3.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer3.noviToken());
		
		SmartScriptLexer lexer4 = new SmartScriptLexer("&");
		lexer4.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer4.noviToken());
		
		SmartScriptLexer lexer5 = new SmartScriptLexer("_");
		lexer5.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer5.noviToken());
	}
	
	@Test
	public void pozivGeneriranjaTokenaNakonGreske() {
		SmartScriptLexer lexer = new SmartScriptLexer(". abcd");
		lexer.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
	}
	
	@Test
	public void pozivZadnjegTokenaNakonGreške() {
		SmartScriptLexer lexer = new SmartScriptLexer(". abcd");
		lexer.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.zadnjiToken());
	}
	
	@Test
	public void pozivZadnjegTokenaPrijeGeneriranja() {
		SmartScriptLexer lexer = new SmartScriptLexer(" p 2 3 z");
		assertThrows(NoSuchElementException.class,()->lexer.zadnjiToken());
	}
	
	@Test
	public void provjeraRezultataZadnjegTokena() {
		SmartScriptLexer lexer = new SmartScriptLexer(" p 3 2 z \n");
		Token token = lexer.noviToken();
		assertEquals(token.getValue(),lexer.zadnjiToken().getValue());
	}
	
	@Test
	public void testiranjeVarijableUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer("FORko Ok7_ano");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals("FORko",lexer.noviToken().getValue());
		assertEquals("Ok7_ano",lexer.noviToken().getValue());
	}
	
	@Test
	public void testiranjeVarijableSUnderscoreomUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer("FOR_ko _Okano");
		lexer.setState(SmartScriptLexerState.TAG);
		
		assertEquals("FOR_ko",lexer.noviToken().getValue());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
	}
	
	@Test
	public void testiranjeCijelihBrojevaUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer("tekst-1 1776tekst");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.VARIJABLA,"tekst"),lexer.noviToken());
		assertEquals(new Token(TokenType.DECIMALNI,Integer.valueOf(-1)),lexer.noviToken());
		assertEquals(new Token(TokenType.DECIMALNI,Integer.valueOf(1776)),lexer.noviToken());
		assertEquals(new Token(TokenType.VARIJABLA,"tekst"),lexer.noviToken());
	}
	
	@Test
	public void testiranjeDecimalihBrojevaUTagu() {
		SmartScriptLexer lexer = new SmartScriptLexer("tekst-1.23 17.76tekst");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.VARIJABLA,"tekst"),lexer.noviToken());
		assertEquals(new Token(TokenType.DECIMALNI,Double.valueOf(-1.23)),lexer.noviToken());
		assertEquals(new Token(TokenType.DECIMALNI,Double.valueOf(17.76)),lexer.noviToken());
		assertEquals(new Token(TokenType.VARIJABLA,"tekst"),lexer.noviToken());
	}
	
	@Test
	public void testiranjeTočkeIzaDecimalnogBroja() {
		SmartScriptLexer lexer = new SmartScriptLexer("1.23.ekst");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.DECIMALNI,Double.valueOf(1.23)),lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
	}
	
	@Test
	public void testiranjeTočkeIzaCijelogBroja() {
		SmartScriptLexer lexer = new SmartScriptLexer("123.ekst");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.DECIMALNI,Integer.valueOf(123)),lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
	}
	
	@Test
	public void testiranjeDecimalihBrojevaUTaguSNedozvoljenimZnakom() {
		SmartScriptLexer lexer = new SmartScriptLexer("tekst-1.23: 17.76tekst");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.VARIJABLA,"tekst"),lexer.noviToken());
		assertEquals(new Token(TokenType.DECIMALNI,Double.valueOf(-1.23)),lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
	}
	
	@Test
	public void testiranjeNazivaFunkcija() {
		SmartScriptLexer lexer = new SmartScriptLexer("FOR@sin1 @sin_p @sin.s");
		lexer.setState(SmartScriptLexerState.TAG);
		
		assertEquals("FOR",lexer.noviToken().getValue());
		assertEquals(new Token(TokenType.FUNKCIJA,"@sin1"),lexer.noviToken());
		assertEquals(new Token(TokenType.FUNKCIJA,"@sin_p"),lexer.noviToken());
		assertEquals(new Token(TokenType.FUNKCIJA,"@sin"),lexer.noviToken());
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer("@_sin");
		lexer2.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer2.noviToken());
	}
	
	@Test
	public void testiranjeStringovaPriUmetanjuRazmaka() {
		SmartScriptLexer lexer = new SmartScriptLexer("\"ab \t\n\rcd\"");
		lexer.setState(SmartScriptLexerState.TAG);
		assertEquals(new Token(TokenType.STRING,"\"ab \t\n\rcd\""),lexer.noviToken());
	}
	
	@Test
	public void escapeSekvenceUStringovima() {
		SmartScriptLexer lexer = new SmartScriptLexer("\"ab \\\"cd\"");
		lexer.setState(SmartScriptLexerState.TAG);
		
		assertEquals(new Token(TokenType.STRING,"\"ab \"cd\""),lexer.noviToken());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer("\"ab \\\\cd\"");
		lexer2.setState(SmartScriptLexerState.TAG);
		
		assertEquals(new Token(TokenType.STRING,"\"ab \\cd\""),lexer2.noviToken());
		
		SmartScriptLexer lexer3 = new SmartScriptLexer("\"ab \\n \\t \\r  cd\"");
		lexer3.setState(SmartScriptLexerState.TAG);
		
		assertEquals(new Token(TokenType.STRING,"\"ab \n \t \r  cd\""),lexer3.noviToken());
	}
	
	@Test
	public void nedozvoljeneEscapeSekvenceUStringuTaga() {
		SmartScriptLexer lexer = new SmartScriptLexer("\"ab \\a \"");
		lexer.setState(SmartScriptLexerState.TAG);
		assertThrows(SmartScriptLexerException.class,()->lexer.noviToken());
		
		SmartScriptLexer lexer2 = new SmartScriptLexer("\"ab \\1 \"");
		lexer2.setState(SmartScriptLexerState.TAG);
		
		assertThrows(SmartScriptLexerException.class,()->lexer2.noviToken());
		
		SmartScriptLexer lexer3 = new SmartScriptLexer("\"ab \\  \"");
		lexer3.setState(SmartScriptLexerState.TAG);
		
		assertThrows(SmartScriptLexerException.class,()->lexer3.noviToken());
	}
}
