package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw05.db.ComparisonOperators.*;

import org.junit.jupiter.api.Test;

public class ComparisonOperatorsTest {

	@Test
	public void testEquals() {
		assertTrue(EQUALS.satisfied("", ""));
		assertFalse(EQUALS.satisfied("p", ""));
		assertTrue(EQUALS.satisfied("one", "one"));
		assertFalse(EQUALS.satisfied("one ", "one"));
	}
	
	@Test
	public void testLess() {
		assertFalse(LESS.satisfied("", ""));
		assertTrue(LESS.satisfied("", "a"));
		assertTrue(LESS.satisfied("one", "onf"));
		assertFalse(LESS.satisfied("one", "one"));
		assertTrue(LESS.satisfied("Ana", "Jasna"));
		assertTrue(LESS.satisfied("o e", "one"));
	}
	
	@Test
	public void testGreater() {
		assertFalse(GREATER.satisfied("", ""));
		assertTrue(GREATER.satisfied("a", ""));
		assertFalse(GREATER.satisfied("one", "onf"));
		assertFalse(GREATER.satisfied("one", "one"));
		assertTrue(GREATER.satisfied("Jasna", "Ana"));
		assertFalse(GREATER.satisfied("o e", "one"));
	}
	
	@Test
	public void testLessOrEquals() {
		assertTrue(LESS_OR_EQUALS.satisfied("", ""));
		assertTrue(LESS_OR_EQUALS.satisfied("", "a"));
		assertTrue(LESS_OR_EQUALS.satisfied("one", "onf"));
		assertTrue(LESS_OR_EQUALS.satisfied("one", "one"));
		assertTrue(LESS_OR_EQUALS.satisfied("Ana", "Jasna"));
		assertFalse(LESS_OR_EQUALS.satisfied("one", "o e"));
	}
	
	@Test
	public void testGreaterOrEquals() {
		assertTrue(GREATER_OR_EQUALS.satisfied("", ""));
		assertTrue(GREATER_OR_EQUALS.satisfied("a", ""));
		assertFalse(GREATER_OR_EQUALS.satisfied("one", "onf"));
		assertTrue(GREATER_OR_EQUALS.satisfied("one", "one"));
		assertTrue(GREATER_OR_EQUALS.satisfied("Jasna", "Ana"));
		assertFalse(GREATER_OR_EQUALS.satisfied("o e", "one"));
	}
	
	@Test
	public void testNotEquals() {
		assertFalse(NOT_EQUALS.satisfied("", ""));
		assertTrue(NOT_EQUALS.satisfied("p", ""));
		assertFalse(NOT_EQUALS.satisfied("one", "one"));
		assertTrue(NOT_EQUALS.satisfied("one ", "one"));
	}
	
	@Test
	public void testLike() {
		assertTrue(LIKE.satisfied("", ""));
		assertFalse(LIKE.satisfied("AAA", "AA*AA"));
		assertTrue(LIKE.satisfied("AAAA", "AA*AA"));
		assertFalse(LIKE.satisfied("Zagreb", "Aba*"));
		assertTrue(LIKE.satisfied("Ban", "B*"));
		assertTrue(LIKE.satisfied("Ban", "*an"));
		assertThrows(IllegalArgumentException.class,()->LIKE.satisfied("Ban", "*a*"));
	}
	
	@Test
	public void interpretStringAsOperatorTest() {
		assertEquals(EQUALS,interpretStringAsOperator("="));
		assertEquals(LESS,interpretStringAsOperator("<"));
		assertEquals(GREATER,interpretStringAsOperator(">"));
		assertEquals(NOT_EQUALS,interpretStringAsOperator("!="));
		assertEquals(LESS_OR_EQUALS,interpretStringAsOperator("<="));
		assertEquals(GREATER_OR_EQUALS,interpretStringAsOperator(">="));
		assertEquals(LIKE,interpretStringAsOperator("LIKE"));
		assertThrows(NullPointerException.class,()->interpretStringAsOperator(null));
		assertThrows(IllegalArgumentException.class,()->interpretStringAsOperator("<>"));
		assertThrows(IllegalArgumentException.class,()->interpretStringAsOperator("=>"));
	}
}
