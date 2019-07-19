/**
 * Paket u kojem se nalaze svi mogući čvorovi koje generira {@link SmartScriptParser}
 * grupiranjem elemenata {@link Element}.
 */
package hr.fer.zemris.java.custom.scripting.nodes;

import java.util.Objects;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;

/**
 * Razred koji modelira konstrukciju for petlje može sadržavari 3 ili 4 elementa od kojih
 * 1 mora biti {@link ElementVariable} a ostali mogu biti {@link ElementString},{@link ElementVariable},
 * {@link ElementConstantInteger},{@link ElementConstantDouble}
 * @author Ante Miličević
 *
 */
public class ForLoopNode extends Node {
	/**Varijabla iteracije*/
	private ElementVariable variable;
	/**Početno stanje varijable*/
	private Element startExpression;
	/**Završno stanje varijable*/
	private Element endExpression;
	/**Korak promjene varijable*/
	private Element stepExpression;
	
	/**
	 * Konstruktor koji inicijalizira sve članske varijable
	 * @param variable varijabla iteracije
	 * @param startExpression početno stanje vaijable
	 * @param endExpression završno stanje varijable
	 * @param stepExpression korak promjene varijable
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression,
			Element stepExpression) {
		Objects.requireNonNull(variable);
		Objects.requireNonNull(startExpression);
		Objects.requireNonNull(endExpression);
		
		
		dobarElement(startExpression);
		dobarElement(endExpression);
		if(stepExpression != null)
			dobarElement(stepExpression);
	
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpression = stepExpression;
		
	}
	
	@Override
	public void accept(INodeVisitor visitor) {
		visitor.visitForLoopNode(this);
	}
	
	/**
	 * Privatna metoda koja provjerava da li je element u dobrom "formatu"
	 * @param element element koji se provjerava
	 * @throws IllegalArgumentException ako for petlja nije dobro incijalizirana
	 */
	private void dobarElement(Element element) {
		if (element instanceof ElementVariable  
				|| element instanceof ElementConstantDouble 
				|| element instanceof ElementConstantInteger 
				|| element instanceof ElementString)
			return;
		else
			throw new IllegalArgumentException("For petlja nije dobro inicijalizirana");
			
	}

	/**
	 * Getter za varijablu
	 * @return varijable
	 */
	public ElementVariable getVariable() {
		return variable;
	}

	/**
	 * Getter za startExpression
	 * @return startExoression
	 */
	public Element getStartExpression() {
		return startExpression;
	}

	/**
	 * Getter za endExpression
	 * @return endExpression
	 */
	public Element getEndExpression() {
		return endExpression;
	}

	/**
	 * Getter za stepExpression
	 * @return stepExpression
	 */
	public Element getStepExpression() {
		return stepExpression;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String rezultat = String.format("{$FOR %s %s %s", variable,startExpression,endExpression);
		if(stepExpression != null)
			rezultat += " " + stepExpression;
		
		return rezultat + "$}";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(endExpression, startExpression, stepExpression, variable);
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
		if (!(obj instanceof ForLoopNode))
			return false;
		ForLoopNode other = (ForLoopNode) obj;
		return Objects.equals(endExpression, other.endExpression)
				&& Objects.equals(startExpression, other.startExpression)
				&& Objects.equals(stepExpression, other.stepExpression) && Objects.equals(variable, other.variable);
	}	
	
}
