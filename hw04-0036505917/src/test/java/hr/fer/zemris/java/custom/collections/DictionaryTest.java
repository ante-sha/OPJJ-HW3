package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DictionaryTest {
	Dictionary<String, Integer> mapa = new Dictionary<>();

	@BeforeEach
	public void inicijalizacija() {
		mapa.put("prvi", 1);
		mapa.put("drugi", 2);
		mapa.put("treci", 3);
		mapa.put("cetvrti", 4);
	}

	@AfterEach
	public void ciscenje() {
		mapa.clear();
	}

	@Test
	public void isEmptyKodPrazneMape() {
		Dictionary<String, Integer> test = new Dictionary<>();

		assertTrue(test.isEmpty());
	}

	@Test
	public void isEmptyKodPopunjeneMape() {
		assertFalse(mapa.isEmpty());
	}

	@Test
	public void sizeKodPrazneMape() {
		Dictionary<String, Integer> test = new Dictionary<>();

		assertEquals(0, test.size());
	}

	@Test
	public void sizeKodDodavanja() {
		Dictionary<String, Integer> test = new Dictionary<>();

		test.put("elem", 2);

		assertEquals(1, test.size());
	}

	@Test
	public void sizePoslijeCleara() {
		Dictionary<String, Integer> test = new Dictionary<>();

		test.put("elem", 2);
		test.clear();

		assertEquals(0, test.size());
	}

	@Test
	public void putSNullKljucem() {
		Dictionary<String, Integer> test = new Dictionary<>();

		assertThrows(NullPointerException.class, () -> test.put(null, 5));
	}

	@Test
	public void putSDobrimVrijednostima() {
		assertEquals(4, mapa.size());
	}

	@Test
	public void putSPostojecimKljucem() {
		mapa.put("prvi", 5);

		assertEquals(5, mapa.get("prvi"));
	}

	@Test
	public void getNepostojecegKljuca() {
		assertEquals(null, mapa.get("peti"));
	}

	@Test
	public void getNullKljuca() {
		assertEquals(null, mapa.get(null));
	}

	@Test
	public void getNullVrijednosti() {
		mapa.put("Knull", null);
		assertEquals(null, mapa.get("Knull"));
	}
}
