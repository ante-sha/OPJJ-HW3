package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ComplexPolynomialTest {

	@Test
	public void multiplyTest() {
		ComplexPolynomial p1 = new ComplexPolynomial(new Complex(1,3),new Complex(2,-2),new Complex(0,1));
		ComplexPolynomial p2 = new ComplexPolynomial(new Complex(2,6),new Complex(-1,1),new Complex(2,-4));
		
		ComplexPolynomial p3 = p1.multiply(p2);
		
		assertEquals(4,p3.order());
		
		Complex resultForONE = p3.apply(Complex.ONE);
		assertEquals("(3.00+i15.00)",resultForONE.toString());
		
		Complex resultForCompl = p3.apply(new Complex(2,1));
		assertEquals("(51.00+i93.00)",resultForCompl.toString());
	}
}
