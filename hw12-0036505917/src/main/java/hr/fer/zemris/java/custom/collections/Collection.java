/**
 * Paket koji sadrži razrede koji su
 * rezultat rješavanja prvi zadatak 3. domaće zadaće.
 */
package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * Sučelje koji služi za generičku pohranu objekata.
 * @author Ante Miličević
 *
 */
public interface Collection {

	/**
	 * Metoda koja provjerava je li kolekcija prazna.
	 * @return true ako je kolekcija prazna
	 */
	default boolean isEmpty() {
		return size()==0;
	}

	/**
	 * Metoda koja vraća broj trenutnih objekata u kolekciji.
	 * 
	 * @return broj trenutnih objekata u kolekciji
	 */
	int size();
	
	/**
	 * Metoda koja dodaje objekt u kolekciju.
	 * @param value objekt koji se dodaje
	 */
	void add(Object value);
	
	/**
	 * Metoda koja provjerava sadrži li kolekcija objekt.
	 * @param value objekt kojeg se traži u kolekciji
	 * @return true ako se objekt nalazi u kolekciji
	 */
	boolean contains(Object value);
	
	/**
	 * Metoda koja uklanja objekt iz kolekcije ako se on nalazi u njoj.
	 * 
	 * @param value objekt kojeg se uklanja
	 * @return <p>true - ako se objekt nalazio u kolekciji i sada je uklonjen</p>
	 * 		   <p>false - ako se objekt nije nalazio u kolekciji i ništa nije uklonjeno</p>
	 */
	boolean remove(Object value);
	
	/**
	 * Metoda koja zauzima polje objekata iste veličine kao broj elemenata u kolekciji te
	 * ga puni objektima i vraća to polje.
	 * @return polje svih elemenata kolekcije
	 */
	Object[] toArray();
	
	/**
	 * Metoda kojom se obrađuje svaki element kolekcije metodom 
	 * processor.process(element) i pri tome ostavlja kolekciju ne promijenjenu. 
	 * Redoslijed slanja u ovu metodu je ne definiran.
	 * @param processor objekt pomoću kojega se obrađuju elementi
	 */
	default void forEach(Processor processor) {
		Objects.requireNonNull(processor);
		
		ElementsGetter getter = createElementsGetter();
		
		while(getter.hasNextElement()) {
			processor.process(getter.getNextElement());
		}
	}
	
	/**
	 * Metoda koja dodaje sve elemente kolekcije other u kolekciju i pri tome ostavlja kolekciju
	 * other ne promijenjenu
	 * @param other
	 * @throws NullPointerException ako je other == null
	 */
	default void addAll(Collection other) {
		Objects.requireNonNull(other);
		
		Processor dodavac = (object)->this.add(object);
		
		other.forEach(dodavac);
	}
	
	/**
	 * Metoda koja briše sve elemente kolekcije.
	 */
	void clear();
	
	/**
	 * Metoda koja vraća {@link ElementsGetter}. Pomoću njega omogućen je prolazak
	 * svim elementima redom u složenosti O(1). 
	 * @return ElementsGetter kolekcije
	 */
	ElementsGetter createElementsGetter();
	
	/**
	 * Metoda koja dodaje sve elemente u kolekciju iz kolekcije {@code col} koji zadovoljavaju uvjete
	 * {@code tester}-ove metode tester.test().
	 * @param col kolekcija ciji se elementi dodaju
	 * @param tester objekt koji metodom test() odlučuje prihvaća li se element kolekcije
	 */
	default void addAllSatisfying(Collection col, Tester tester) {
		Objects.requireNonNull(col);
		Objects.requireNonNull(tester);
		ElementsGetter getter = col.createElementsGetter();
		
		while(getter.hasNextElement()) {
			Object element = getter.getNextElement();
			if(tester.test(element))
				add(element);
		}
	}
}
