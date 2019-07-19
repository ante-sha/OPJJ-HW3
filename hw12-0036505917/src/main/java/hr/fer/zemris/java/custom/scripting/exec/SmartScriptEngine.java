package hr.fer.zemris.java.custom.scripting.exec;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Stack;

import hr.fer.zemris.java.custom.scripting.elems.Element;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantDouble;
import hr.fer.zemris.java.custom.scripting.elems.ElementConstantInteger;
import hr.fer.zemris.java.custom.scripting.elems.ElementFunction;
import hr.fer.zemris.java.custom.scripting.elems.ElementOperator;
import hr.fer.zemris.java.custom.scripting.elems.ElementString;
import hr.fer.zemris.java.custom.scripting.elems.ElementVariable;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Razred koji izvršava naredbe SmartScript-a i prosljeđuje rezultat izvođenja
 * skripte na ispis u {@link RequestContext} predan u konstruktoru razreda.
 * 
 * @author Ante Miličević
 *
 */
public class SmartScriptEngine {
	/**
	 * Korijenski čvor skripte
	 */
	private DocumentNode documentNode;
	/**
	 * Objekt kojem se prosljeđuje rezultat izvođenja skripte
	 */
	private RequestContext requestContext;
	/**
	 * Pomoćni objekt za čuvanje podataka operacija skripte
	 */
	private ObjectMultistack multistack = new ObjectMultistack();
	/**
	 * Objekt za obradu skripte
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			ElementVariable variableName = node.getVariable();
			String end = node.getEndExpression().asText();
			String step = node.getStepExpression().asText();

			ValueWrapper variable = new ValueWrapper(node.getStartExpression().asText());
			multistack.push(variableName.asText(), variable);

			while (multistack.peek(variableName.getName()).numCompare(end) <= 0) {

				for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
					node.getChild(i).accept(this);
				}

				variable = multistack.pop(variableName.asText());
				variable.add(step == null ? 1 : step);
				multistack.push(variableName.asText(), variable);
			}

			multistack.pop(variableName.getName());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> stack = new Stack<>();

			for (Element elem : node.getElements()) {
				if (elem instanceof ElementConstantDouble) {
					stack.push(((ElementConstantDouble) elem).getValue());
				} else if (elem instanceof ElementConstantInteger) {
					stack.push(((ElementConstantInteger) elem).getValue());
				} else if (elem instanceof ElementString) {
					stack.push(((ElementString) elem).getValue());
				} else if (elem instanceof ElementVariable) {
					ElementVariable variable = (ElementVariable) elem;

					stack.push(multistack.peek(variable.getName()).getValue());
				} else if (elem instanceof ElementOperator) {
					operate((ElementOperator) elem, stack);
				} else if (elem instanceof ElementFunction) {
					applyFunction((ElementFunction) elem, stack);
				}
			}

			if (!stack.isEmpty()) {
				try {
					ListIterator<Object> it = stack.listIterator();
					while (it.hasNext()) {
						requestContext.write(it.next().toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}

		/**
		 * Metoda koja primjenjuje zadanu funkciju na trenutni kontekst naredbe
		 * 
		 * @param elem  funkcija
		 * @param stack kontekst
		 * 
		 * @throws RuntimeException ako funckija nije podržana
		 */
		private void applyFunction(ElementFunction elem, Stack<Object> stack) {
			if (elem.getName().equals("sin")) {
				calculateSin(stack);
			} else if (elem.getName().equals("decfmt")) {
				decimalFormat(stack);
			} else if (elem.getName().equals("dup")) {
				dup(stack);
			} else if (elem.getName().equals("swap")) {
				swap(stack);
			} else if (elem.getName().equals("setMimeType")) {
				setMimeType(stack);
			} else if (elem.getName().equals("paramGet")) {
				paramGet(stack);
			} else if (elem.getName().equals("pparamGet")) {
				pparamGet(stack);
			} else if (elem.getName().equals("pparamSet")) {
				pparamSet(stack);
			} else if (elem.getName().equals("pparamDel")) {
				pparamDel(stack);
			} else if (elem.getName().equals("tparamGet")) {
				tparamGet(stack);
			} else if (elem.getName().equals("tparamSet")) {
				tparamSet(stack);
			} else if (elem.getName().equals("tparamDel")) {
				tparamDel(stack);
			} else {
				throw new RuntimeException("Unsupported function");
			}
		}

		/**
		 * Funkcija brisanja privremenog parametra čiji se naziv nalazi na vrhu stoga
		 * 
		 * @param stack kontekst
		 */
		private void tparamDel(Stack<Object> stack) {
			Object name = stack.pop();

			requestContext.removeTemporaryParameter(name.toString());
		}

		/**
		 * Funckija postavljanja privremenog parametra čiji su naziv i vrijednost
		 * zapisani na stogu
		 * 
		 * @param stack kontekst
		 */
		private void tparamSet(Stack<Object> stack) {
			Object name = stack.pop();
			Object value = stack.pop();

			requestContext.setTemporaryParameter(name.toString(), Objects.toString(value));
		}

		/**
		 * Funkcija za dohvat privremenog parametra čiji se naziv i generička vrijednost
		 * nalaze na stogu
		 * 
		 * @param stack kontekst
		 */
		private void tparamGet(Stack<Object> stack) {
			Object defaultValue = stack.pop();
			Object name = stack.pop();

			String value = requestContext.getTemporaryParameter(name.toString());

			stack.push(value == null ? defaultValue : value);
		}

		/**
		 * Funkcija za brisanje trajnog parametra čiji se naziv nalazi na vrhu stoga
		 * 
		 * @param stack kontekst
		 */
		private void pparamDel(Stack<Object> stack) {
			Object name = stack.pop();

			requestContext.removePersistentParameter(name.toString());
		}

		/**
		 * Funkcija za postavljanje trajnog parametra čiji se naziv i vrijednost nalaze
		 * na stogu
		 * 
		 * @param stack kontekst
		 */
		private void pparamSet(Stack<Object> stack) {
			Object name = stack.pop();
			Object value = stack.pop();

			requestContext.setPersistantParameter(name.toString(), value.toString());
		}

		/**
		 * Funkcija za dohvat trajnog parametra čiji se naziv i generička vrijednost
		 * nalaze na stogu
		 * 
		 * @param stack kontekst
		 */
		private void pparamGet(Stack<Object> stack) {
			Object defaultValue = stack.pop();
			Object name = stack.pop();

			String value = requestContext.getPersistantParameter(name.toString());

			stack.push(value == null ? defaultValue : value);
		}

		/**
		 * Funkcija za dohvat parametra iz zaglavlja korisnikovog zahtjeva čiji se naziv
		 * i defaultna vrijednost nalaze na vrhu stoga
		 * 
		 * @param stack kontekst
		 */
		private void paramGet(Stack<Object> stack) {
			Object defaultValue = stack.pop();
			Object name = stack.pop();

			String value = requestContext.getParameter(name.toString());

			stack.push(value == null ? defaultValue : value);
		}

		/**
		 * Funkcija za postavljanje tipa dokumenta koji će se predati kao odgovor
		 * korisniku, tip se nalazi na vrhu stoga
		 * 
		 * @param stack kontekst
		 */
		private void setMimeType(Stack<Object> stack) {
			Object o = stack.pop();

			requestContext.setMimeType((String) o);
		}

		/**
		 * Funkcija zamjene parametara s vrha stoga
		 * 
		 * @param stack kontekst
		 */
		private void swap(Stack<Object> stack) {
			Object a = stack.pop();
			Object b = stack.pop();

			stack.push(a);
			stack.push(b);
		}

		/**
		 * Funkcija za dupliciranje vrijednosti s vrha stoga
		 * 
		 * @param stack kontekst
		 */
		private void dup(Stack<Object> stack) {
			Object o = stack.pop();

			stack.push(o);
			stack.push(o);
		}

		/**
		 * Funckija za formatiranje decimalnog broja gdje se format i decimalni broj
		 * nalaze na vrhu stoga
		 * 
		 * @param stack kontekst
		 */
		private void decimalFormat(Stack<Object> stack) {
			Object format = stack.pop();

			DecimalFormat f = new DecimalFormat((String) format);
			Number x = parseObjectIntoNumber(stack.pop());

			stack.push(f.format(x.doubleValue()));
		}

		/**
		 * Metoda za parsiranje objekta u broj
		 * 
		 * @param o objekt
		 * @return objekt u formatu Number
		 * 
		 * @throws RuntimeException ako se objekt ne može isparsirati u broj
		 */
		private Number parseObjectIntoNumber(Object o) {
			if (o instanceof Integer || o instanceof Double) {
				return (Number) o;
			} else if (o instanceof String) {
				try {
					return Integer.parseInt((String) o);
				} catch (NumberFormatException e) {
				}
				try {
					return Double.parseDouble((String) o);
				} catch (NumberFormatException e) {
				}
			}

			throw new RuntimeException("Unsupported type of object " + o.getClass());
		}

		/**
		 * Funckija koja računa sinus broja s vrha stoga i vrijednost zapisuje nazad na
		 * stog
		 * 
		 * @param stack kontekst
		 * 
		 * @throws RuntimeException ako se objekt s vrha stoga ne može isparsirati u
		 *                          broj
		 */
		private void calculateSin(Stack<Object> stack) {
			Number x = parseObjectIntoNumber(stack.pop());

			Object result = Math.sin(x.doubleValue());

			stack.push(result);
		}

		/**
		 * Metoda koja izvršava binarni operator zapisan u elem i rezultat
		 * operacije zapisuje na stog
		 * 
		 * @param elem binarni operator
		 * @param stack kontekst
		 */
		private void operate(ElementOperator elem, Stack<Object> stack) {
			Number operand2 = parseObjectIntoNumber(stack.pop());
			ValueWrapper operand1 = new ValueWrapper(stack.pop());

			if (elem.asText().equals("+")) {
				operand1.add(operand2);
			} else if (elem.asText().equals("-")) {
				operand1.subtract(operand2);
			} else if (elem.asText().equals("*")) {
				operand1.multiply(operand2);
			} else if (elem.asText().equals("/")) {
				operand1.divide(operand2);
			} else {
				throw new RuntimeException("Binary operator is not supported " + elem.asText());
			}

			stack.push(operand1.getValue());
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			for (int i = 0, n = node.numberOfChildren(); i < n; i++) {
				node.getChild(i).accept(this);
			}
		}

	};

	/**
	 * Konstruktor
	 * 
	 * @throws NullPointerException ako je documentNode ili requestContext null
	 */
	public SmartScriptEngine(DocumentNode documentNode, RequestContext requestContext) {
		this.documentNode = Objects.requireNonNull(documentNode);
		this.requestContext = Objects.requireNonNull(requestContext);
	}

	/**
	 * Metoda koja pokreće izvođenje skripte
	 */
	public void execute() {
		documentNode.accept(visitor);
	}
}
