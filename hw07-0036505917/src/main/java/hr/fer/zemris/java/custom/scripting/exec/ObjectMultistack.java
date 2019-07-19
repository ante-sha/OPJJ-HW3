package hr.fer.zemris.java.custom.scripting.exec;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Razred koji implementira adapter za korištenje mape tako da je preoblikuje u
 * više-razinski stog gdje je zapravo svaka razina po jedan stog, a stog je u
 * mapi povezan preko svoje string reprezentacije.
 * 
 * @author Ante Miličević
 *
 */
public class ObjectMultistack {
	/**
	 * Mapa u koja predstavlja stogove stogove
	 */
	private Map<String, MultistackEntry> stacks = new HashMap<>();

	/**
	 * Metoda koja na vrh stoga s imenom keyName stavlja valueWrapper, ako stog ne
	 * postoji stvara se.
	 * 
	 * @param keyName      ime stoga
	 * @param valueWrapper vrijednost koja se stavlja na stog
	 * 
	 * @throws NullPointerException ako je keyName ili valueWrapper null
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		Objects.requireNonNull(keyName);
		Objects.requireNonNull(valueWrapper);

		stacks.merge(keyName, new MultistackEntry(valueWrapper, null), (old, init) -> {
			return new MultistackEntry(valueWrapper, old);
		});
	}

	/**
	 * Metoda koja vadi zadnji element sa stoga s imenom keyName
	 * 
	 * @param keyName ime stoga
	 * 
	 * @return element s vrha stoga
	 * 
	 * @throws EmptyMultistackException ako je stog prazan
	 * @throws NullPointerException     ako je keyName null
	 */
	public ValueWrapper pop(String keyName) {
		Objects.requireNonNull(keyName);
		MultistackEntry entry = getEntryFromStack(keyName);

		stacks.compute(keyName, (key, value) -> value.next);

		return entry.getValue();
	}

	/**
	 * Metoda koja čita zadnji element sa stoga s imenom keyName
	 * 
	 * @param keyName ime stoga
	 * @return element s vrha stoga
	 * 
	 * @throws NullPointerException     ako je keyName null
	 * @throws EmptyMultistackException ako je stog prazan
	 */
	public ValueWrapper peek(String keyName) {
		Objects.requireNonNull(keyName);

		return getEntryFromStack(keyName).getValue();
	}

	/**
	 * Metoda koja dohvaća prvi element sa stoga s imenom keyName
	 * 
	 * @param keyName ime stoga
	 * @return element stoga
	 * 
	 * @throws EmptyMultistackException ako je stog prazan
	 */
	private MultistackEntry getEntryFromStack(String keyName) {
		MultistackEntry entry = stacks.get(keyName);
		if (entry == null) {
			throw new EmptyMultistackException("Stack " + keyName + " is empty");
		}
		return entry;
	}

	/**
	 * Metoda koja ispituje je li stog s imenom keyName prazan
	 * 
	 * @param keyName ime stoga
	 * @return true ako je prazan, false inače
	 * 
	 * @throws NullPointerException ako je keyName null
	 */
	public boolean isEmpty(String keyName) {
		Objects.requireNonNull(keyName);

		return stacks.get(keyName) == null;
	}

	/**
	 * Privatna struktura podataka kojom se modelira jedan čvor u povezanoj listi
	 * koja modelira stog unutar mape.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class MultistackEntry {
		/**
		 * Idući unos u listi
		 */
		private MultistackEntry next;
		/**
		 * Vrijednost koju čuva čvor
		 */
		private ValueWrapper value;

		/**
		 * Konstruktor s inicijalizacijom svih varijabli
		 * 
		 * @param value vrijednost koju čvor čuva
		 * @param next  referenca na idući čvor
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}

		/**
		 * Getter za vrijednost
		 * 
		 * @return value
		 */
		public ValueWrapper getValue() {
			return value;
		}

	}
}
