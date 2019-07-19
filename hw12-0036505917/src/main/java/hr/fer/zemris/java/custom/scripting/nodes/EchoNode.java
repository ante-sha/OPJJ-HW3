/**
 * Paket u kojem se nalaze svi mogući čvorovi koje generira {@link SmartScriptParser}
 * grupiranjem elemenata {@link Element}.
 */
package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Arrays;

import hr.fer.zemris.java.custom.scripting.elems.Element;

/**
 * Razred koji modelira čvor koji označava naredbu koja generira dinamički tekstualni ispis.
 * Svi elemente koje sadrži unutar taga sačuvani su u varijabli elements.
 * @author Ante Miličević
 *
 */
public class EchoNode extends Node {
	/**Svi elementi naredbe*/
	private Element[] elements;

	/**
	 * Konstruktor koji prima sve elemente koje sadrži u naredbi
	 * @param elements svi elementi naredbe
	 */
	public EchoNode(Element[] elements) {
		super();
		this.elements = elements;
	}

	/**
	 * Getter za elemente
	 * @return elements
	 */
	public Element[] getElements() {
		return elements;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("{$=");
		for(int i = 0;i<elements.length;i++) {
			result.append(" " + elements[i]);
		}
		result.append("$}");
		return result.toString();
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitEchoNode(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof EchoNode))
			return false;
		EchoNode other = (EchoNode) obj;
		return Arrays.deepEquals(elements, other.elements);
	}
	
}
