package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ValueWrapperTest {

	@Test
	public void nullValueWrapper() {
		ValueWrapper wrapper = new ValueWrapper(null);

		assertEquals(null, wrapper.getValue());
	}

	@Test
	public void nullAndNullAdd() {
		ValueWrapper wrapper = new ValueWrapper(null);

		wrapper.add(null);
		assertEquals(0, wrapper.getValue());
	}

	@Test
	public void nullAndNullMul() {
		ValueWrapper wrapper = new ValueWrapper(null);

		wrapper.multiply(null);
		assertEquals(0, wrapper.getValue());
	}

	@Test
	public void nullAndNullSub() {
		ValueWrapper wrapper = new ValueWrapper(null);

		wrapper.subtract(null);
		assertEquals(0, wrapper.getValue());
	}

	@Test
	public void nullAndNullDiv() {
		ValueWrapper wrapper = new ValueWrapper(null);

		assertThrows(IllegalArgumentException.class, () -> wrapper.divide(null));
	}

	@Test
	public void nullAndNullNumCompare() {
		ValueWrapper wrapper = new ValueWrapper(null);

		assertEquals(0, wrapper.numCompare(null));
	}

	@Test
	public void integerAdd() {
		ValueWrapper wrapper = new ValueWrapper(2);
		wrapper.add(Integer.valueOf(5));

		assertEquals(7, wrapper.getValue());
	}

	@Test
	public void stringAdd() {
		ValueWrapper wrapper = new ValueWrapper("2");
		wrapper.add(Integer.valueOf(5));
		assertEquals(7, wrapper.getValue());

		ValueWrapper wrapper2 = new ValueWrapper("1.2e-2");
		wrapper2.add("5.0");

		assertTrue(doubleEquals(5.012, (Double) wrapper2.getValue()));

		ValueWrapper wrapper3 = new ValueWrapper("2E-2");
		wrapper3.add(4.2);

		assertTrue(doubleEquals(4.22, (Double) wrapper3.getValue()));
	}

	@Test
	public void stringSubtract() {
		ValueWrapper wrapper = new ValueWrapper("2");
		wrapper.subtract(Integer.valueOf(5));
		assertEquals(-3, wrapper.getValue());

		ValueWrapper wrapper2 = new ValueWrapper("2.1e-2");
		wrapper2.subtract("5.0");

		assertTrue(doubleEquals(-4.979, (Double) wrapper2.getValue()));

		ValueWrapper wrapper3 = new ValueWrapper("2E-2");
		wrapper3.subtract(4.2);

		assertTrue(doubleEquals(-4.18, (Double) wrapper3.getValue()));
	}

	@Test
	public void stringMultiply() {
		ValueWrapper wrapper = new ValueWrapper("2");
		wrapper.multiply(Integer.valueOf(5));
		assertEquals(10, wrapper.getValue());

		ValueWrapper wrapper2 = new ValueWrapper("5.2e-1");
		wrapper2.multiply("5.0");

		assertTrue(doubleEquals(2.6, (Double) wrapper2.getValue()));

		ValueWrapper wrapper3 = new ValueWrapper("2E-2");
		wrapper3.multiply(4.2);

		assertTrue(doubleEquals(0.084, (Double) wrapper3.getValue()));
	}

	@Test
	public void stringDivide() {
		ValueWrapper wrapper = new ValueWrapper("2");
		wrapper.divide(Integer.valueOf(5));
		assertEquals(0, wrapper.getValue());

		ValueWrapper wrapper2 = new ValueWrapper("1.2e-2");
		wrapper2.divide("5.0");

		assertTrue(doubleEquals(2.4E-3, (Double) wrapper2.getValue()));

		ValueWrapper wrapper3 = new ValueWrapper(4.2);
		wrapper3.divide("2E-1");

		assertTrue(doubleEquals(21, (Double) wrapper3.getValue()));
	}

	@Test
	public void stringNumCompare() {
		ValueWrapper wrapper = new ValueWrapper("2");
		assertTrue(wrapper.numCompare(1) > 0);

		ValueWrapper wrapper2 = new ValueWrapper("2e-2");
		assertTrue(wrapper2.numCompare("5.0") < 0);

		ValueWrapper wrapper3 = new ValueWrapper("2E-2");

		assertTrue(wrapper3.numCompare(2E-2) == 0);
	}

	@Test
	public void validAddOperations() {
		ValueWrapper wrapper = new ValueWrapper(3);
		wrapper.add(null);
		assertEquals(3, wrapper.getValue());

		wrapper.add(-2);
		assertEquals(1, wrapper.getValue());

		wrapper.add("3.2");
		assertTrue(wrapper.getValue() instanceof Double);
		assertTrue(doubleEquals(4.2, (Double) wrapper.getValue()));

		wrapper.add(".24");
		assertTrue(doubleEquals(4.44, (Double) wrapper.getValue()));
	}

	@Test
	public void invalidAddOperations() {
		ValueWrapper wrapper = new ValueWrapper(true);
		assertThrows(RuntimeException.class, () -> wrapper.add(2));

		ValueWrapper wrapper2 = new ValueWrapper(".2.4");
		assertThrows(RuntimeException.class, () -> wrapper2.add(1));
	}

	@Test
	public void validSubtractOperations() {
		ValueWrapper wrapper = new ValueWrapper(3);
		wrapper.subtract(null);
		assertEquals(3, wrapper.getValue());

		wrapper.subtract(-2);
		assertEquals(5, wrapper.getValue());

		wrapper.subtract("3.2");
		assertTrue(wrapper.getValue() instanceof Double);
		assertTrue(doubleEquals(1.8, (Double) wrapper.getValue()));

		wrapper.subtract(".24");
		assertTrue(doubleEquals(1.56, (Double) wrapper.getValue()));
	}

	@Test
	public void invalidSubtractOperations() {
		ValueWrapper wrapper = new ValueWrapper(true);
		assertThrows(RuntimeException.class, () -> wrapper.subtract(2));

		ValueWrapper wrapper2 = new ValueWrapper(".2.4");
		assertThrows(RuntimeException.class, () -> wrapper2.subtract(1));
	}

	@Test
	public void validMultiplyOperations() {
		ValueWrapper wrapper = new ValueWrapper(3);
		wrapper.multiply(null);
		assertEquals(0, wrapper.getValue());

		wrapper.setValue(3);
		wrapper.multiply(-2);
		assertEquals(-6, wrapper.getValue());

		wrapper.multiply("3.2");
		assertTrue(wrapper.getValue() instanceof Double);
		assertTrue(doubleEquals(-19.2, (Double) wrapper.getValue()));

		wrapper.multiply(".24");
		assertTrue(doubleEquals(-4.608, (Double) wrapper.getValue()));
	}

	@Test
	public void invalidMultiplyOperations() {
		ValueWrapper wrapper = new ValueWrapper(true);
		assertThrows(RuntimeException.class, () -> wrapper.multiply(2));

		ValueWrapper wrapper2 = new ValueWrapper(".2.4");
		assertThrows(RuntimeException.class, () -> wrapper2.multiply(1));
	}

	@Test
	public void validDivideOperations() {
		ValueWrapper wrapper = new ValueWrapper(3);
		wrapper.divide(1);
		assertEquals(3, wrapper.getValue());

		wrapper.divide(-2);
		assertEquals(-1, wrapper.getValue());

		wrapper.setValue(-1.5);
		wrapper.divide("3.2");
		assertTrue(doubleEquals(-0.46875, (Double) wrapper.getValue()));

		wrapper.divide(".25");
		assertTrue(doubleEquals(-1.875, (Double) wrapper.getValue()));
	}

	@Test
	public void invalidDivideOperations() {
		ValueWrapper wrapper3 = new ValueWrapper(2);
		assertThrows(IllegalArgumentException.class, () -> wrapper3.divide(0));
		assertThrows(IllegalArgumentException.class, () -> wrapper3.divide(0.0));

		ValueWrapper wrapper = new ValueWrapper(true);
		assertThrows(RuntimeException.class, () -> wrapper.divide(2));

		ValueWrapper wrapper2 = new ValueWrapper(".2.4");
		assertThrows(RuntimeException.class, () -> wrapper2.divide(1));
	}

	@Test
	public void validNumCompareOperations() {
		ValueWrapper wrapper = new ValueWrapper(3);
		assertTrue(wrapper.numCompare(null) > 0);

		assertTrue(wrapper.numCompare(4.1) < 0);

		wrapper.setValue("3.2");
		assertTrue(wrapper.numCompare("3E1") < 0);

		wrapper.setValue(".24");
		assertTrue(wrapper.numCompare(0.24) == 0);
	}

	@Test
	public void invalidNumCompareOperations() {
		ValueWrapper wrapper = new ValueWrapper(true);
		assertThrows(RuntimeException.class, () -> wrapper.numCompare(2));

		ValueWrapper wrapper2 = new ValueWrapper(".2.4");
		assertThrows(RuntimeException.class, () -> wrapper2.numCompare(1));
	}

	private static boolean doubleEquals(double first, double second) {
		return Math.abs(first - second) < 1E-6;
	}
}
