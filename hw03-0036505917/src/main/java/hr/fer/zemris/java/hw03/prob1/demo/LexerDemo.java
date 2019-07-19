/**
 * Paket za demonstraciju 2.zadatka 3.domaće zadaće
 */
package hr.fer.zemris.java.hw03.prob1.demo;

import java.util.Scanner;

import hr.fer.zemris.java.hw03.prob1.Lexer;
import hr.fer.zemris.java.hw03.prob1.LexerState;
import hr.fer.zemris.java.hw03.prob1.Token;
import hr.fer.zemris.java.hw03.prob1.TokenType;

/**
 * Program koji kroz konzolu prima unos sve dok ne dođe do unosa "kraj" i onda ih provlači kroz
 * lexer i vraća ispis njegovih tokena.
 * @author Ante Miličević
 *
 */
public class LexerDemo {
	/**
	 * Ulazna točka programa
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		
		try(Scanner sc = new Scanner(System.in)){
			String text = sc.nextLine();
			while(!text.equals("kraj")) {
				
				Lexer lex = new Lexer(text);
				Token tk = lex.nextToken();
				LexerState stanje = LexerState.BASIC;
				while(tk.getType()!=TokenType.EOF) {
					if(tk.getType() == TokenType.SYMBOL && tk.getValue().equals("#")) {
						if(stanje == LexerState.EXTENDED)
							stanje = LexerState.BASIC;
						else
							stanje = LexerState.EXTENDED;
						
						lex.setState(stanje);
					}
						
					System.out.println(tk);
					tk=lex.nextToken();
				}
				System.out.println(tk);
				text = sc.nextLine();
			}
		}
	}

}
