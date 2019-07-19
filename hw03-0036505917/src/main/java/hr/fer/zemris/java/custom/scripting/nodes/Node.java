/**
 * Paket u kojem se nalaze svi mogući čvorovi koje generira {@link SmartScriptParser}
 * grupiranjem elemenata {@link Element}.
 */
package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.ArrayIndexedCollection;

/**
 * Razred koji modelira čvor kojeg generira {@link SmartScriptParser} grupiranjem elemenata {@link Element}.
 * Svaki čvor može imati svoje "podčvorove"(tzv. djecu) kao što nalaže strukturiranje parsera.
 * @author Ante Miličević
 *
 */
public class Node {
	/**Kolekcija koja čuva svu djecu čvora*/
	private ArrayIndexedCollection djeca = new ArrayIndexedCollection();
	
	/**
	 * Metoda koja dodaje dijete u kolekciju čvora
	 * @param child novi čvor kojeg se dodaje u kolekciju djeca
	 * @throws NullPointerException ako je child == null
	 */
	public void addChildNode(Node child) {
		Objects.requireNonNull(child);
		
		djeca.add(child);
	}
	
	/**
	 * Metoda koja vraća broj trenutne djece čvora.
	 * @return broj djece
	 */
	public int numberOfChildren() {
		return djeca.size();
	}
	
	/**
	 * Metoda koja vraća dijete čvora kojeg u kolekciji čuva na mjestu index
	 * @param index mjesto djeteta u kolekciji
	 * @return referenca na dijete
	 * @throws IndexOutOfBoundsException ako index nije u dobrom rasponu
	 */
	public Node getChild(int index) {
		if(index < 0 || index >= numberOfChildren())
			throw new IndexOutOfBoundsException(String.format("%d nije u rasponu [0,%d>", index,numberOfChildren()));
		
		return (Node)djeca.get(index);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(djeca);
	}
	
	/**
	 * <p>Metoda koja provjerava da li je čvor jednak objektu obj, ako se radi o referenci na drugi čvor
	 * tada se provjerava da li su im djeca jednaka. Ako se pojavi primjer više razina grananja metoda
	 * pozivanja jednakosti djece će se rekurzivno spustiti u sve grane stabla.
	 * @return true - ako su im jednaka djeca
	 * <br>false - ako obj nije primjerak čvora ili im sva djeca nisu jednaka</p>
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Node))
			return false;
		Node other = (Node) obj;
		
		return Arrays.equals(djeca.toArray(), other.djeca.toArray());
	}
	
	
}
