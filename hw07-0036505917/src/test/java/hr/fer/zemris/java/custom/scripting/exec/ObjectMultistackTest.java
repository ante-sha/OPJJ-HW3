package hr.fer.zemris.java.custom.scripting.exec;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectMultistackTest {
	private ObjectMultistack multistack = new ObjectMultistack();
	
	@BeforeEach
	public void newMultistack() {
		multistack = new ObjectMultistack();
	}

	@Test
	public void pushNullKeyName() {
		assertThrows(NullPointerException.class,()->multistack.push(null, new ValueWrapper(5)));
	}
	
	@Test
	public void pushNullValue() {
		assertThrows(NullPointerException.class,()->multistack.push("key", null));
	}
	
	@Test
	public void isEmptyNullKeyName() {
		assertThrows(NullPointerException.class,()->multistack.isEmpty(null));
	}
	
	@Test
	public void popNullKeyName() {
		assertThrows(NullPointerException.class,()->multistack.pop(null));
	}
	
	@Test
	public void peekNullKeyName() {
		assertThrows(NullPointerException.class,()->multistack.peek(null));
	}
	
	@Test
	public void pushValueWrapper() {
		multistack.push("first",new ValueWrapper(2));
		multistack.push("second",new ValueWrapper(null));
		multistack.push("first", new ValueWrapper(6));
	}
	
	@Test
	public void popEmptyStack() {
		multistack.push("stack", new ValueWrapper("elem"));
		assertEquals("elem",multistack.pop("stack").getValue());
		assertThrows(EmptyMultistackException.class,()->multistack.pop("stack"));
	}
	
	@Test
	public void popUnexistingStack() {
		assertThrows(EmptyMultistackException.class, ()->multistack.pop("not there"));
	}
	
	@Test
	public void peekStack() {
		multistack.push("stack", new ValueWrapper("elem"));
		assertEquals("elem",multistack.peek("stack").getValue());
		
		multistack.push("stack", new ValueWrapper("elem2"));
		assertEquals("elem2",multistack.peek("stack").getValue());
	}
	
	@Test
	public void peekEmptyStack() {
		multistack.push("stack", new ValueWrapper(3));
		multistack.pop("stack");
		
		assertThrows(EmptyMultistackException.class,()->multistack.peek("stack"));
	}
	
	@Test
	public void peekUnexistingStack() {
		assertThrows(EmptyMultistackException.class, ()->multistack.peek("not there"));
	}
	
	@Test
	public void isEmptyUnexistingStack() {
		assertTrue(multistack.isEmpty("not there"));
	}
	
	@Test
	public void isEmptyOnEmptyStack() {
		multistack.push("stack", new ValueWrapper(2));
		multistack.pop("stack");
		assertTrue(multistack.isEmpty("stack"));
	}
	
	@Test
	public void isEmptyOnNonEmptyStack() {
		multistack.push("stack", new ValueWrapper(2));
		multistack.pop("stack");
		multistack.push("stack", new ValueWrapper("text"));
		assertFalse(multistack.isEmpty("stack"));
	}
	
	@Test
	public void multiplePushAndPop() {
		multistack.push("stack", new ValueWrapper(1));
		multistack.push("stack", new ValueWrapper(2));
		assertEquals(Integer.valueOf(2),multistack.pop("stack").getValue());
		
		multistack.push("stack", new ValueWrapper(5));
		assertEquals(Integer.valueOf(5),multistack.pop("stack").getValue());
		assertEquals(Integer.valueOf(1),multistack.pop("stack").getValue());
		
		assertThrows(EmptyMultistackException.class,()->multistack.pop("stack"));
	}
}
