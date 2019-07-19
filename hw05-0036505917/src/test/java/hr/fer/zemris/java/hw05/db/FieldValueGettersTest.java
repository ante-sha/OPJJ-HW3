package hr.fer.zemris.java.hw05.db;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw05.db.FieldValueGetters.*;

import org.junit.jupiter.api.Test;

public class FieldValueGettersTest {
	static StudentRecord[] records = { new StudentRecord("0000000001", "Perić", "Pero", 4),
			new StudentRecord("0000000006", "Anić", "Ana", 2) };
	

	@Test
	public void lastNameTest() {
		assertThrows(NullPointerException.class, () -> LAST_NAME.get(null));
		assertEquals(records[0].getLastName(),LAST_NAME.get(records[0]));
	}
	
	@Test
	public void firstNameTest() {
		assertThrows(NullPointerException.class, () -> FIRST_NAME.get(null));
		assertEquals(records[1].getFirstName(),FIRST_NAME.get(records[1]));
	}
	
	@Test
	public void jmbagTest() {
		assertThrows(NullPointerException.class, () -> JMBAG.get(null));
		assertEquals(records[1].getJmbag(),JMBAG.get(records[1]));
	}
	
	@Test
	public void interpretStringAsFieldGetterTest() {
		assertEquals(LAST_NAME,interpretStringAsFieldGetter("lastName"));
		assertEquals(FIRST_NAME,interpretStringAsFieldGetter("firstName"));
		assertEquals(JMBAG,interpretStringAsFieldGetter("jmbag"));
		assertThrows(NullPointerException.class,()->interpretStringAsFieldGetter(null));
		assertThrows(IllegalArgumentException.class,()->interpretStringAsFieldGetter("firstLast"));
	}
}
