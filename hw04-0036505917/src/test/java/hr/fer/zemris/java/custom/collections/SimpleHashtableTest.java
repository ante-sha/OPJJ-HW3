package hr.fer.zemris.java.custom.collections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import hr.fer.zemris.java.custom.collections.SimpleHashtable.TableEntry;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleHashtableTest {

	SimpleHashtable<String, Integer> mapa;

	@BeforeEach
	public void inicijalizacija() {
		mapa = new SimpleHashtable<String, Integer>(10);
		mapa.put("prvi", 1);
		mapa.put("drugi", 2);
		mapa.put("treci", 3);
		mapa.put("cetvrti", 4);
		mapa.put("peti", 5);
	}

	@AfterEach
	public void ciscenje() {
		mapa.clear();
	}

	@Test
	public void defaultKonstruktor() {
		new SimpleHashtable<String, Integer>();
	}

	@Test
	public void konstruktorSKapacitetom() {
		new SimpleHashtable<String, Integer>(5);
	}

	@Test
	public void konstruktorSPremalimKapacitetom() {
		assertThrows(IllegalArgumentException.class, () -> new SimpleHashtable<String, Integer>(0));
	}

	@Test
	public void putUPraznuMapu() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<String, Integer>(5);
		map.put("elem", 1);
		assertEquals(1, map.size());
	}

	@Test
	public void putVecPostojecegElementa() {
		assertEquals(Integer.valueOf(1), mapa.get("prvi"));
		mapa.put("prvi", 3);

		assertEquals(Integer.valueOf(3), mapa.get("prvi"));
		assertEquals(5, mapa.size());
	}

	@Test
	public void putPriPrelaskuKoeficijentaKapaciteta() {
		mapa.put("sesti", 6);
		mapa.put("sedmi", 7);
		mapa.put("osmi", 8);

		assertEquals(8, mapa.size());

		assertTrue(mapa.containsKey("prvi"));
		assertTrue(mapa.containsKey("drugi"));
		assertTrue(mapa.containsKey("treci"));
		assertTrue(mapa.containsKey("cetvrti"));
		assertTrue(mapa.containsKey("peti"));
		assertTrue(mapa.containsKey("sesti"));
		assertTrue(mapa.containsKey("sedmi"));
		assertTrue(mapa.containsKey("osmi"));
	}

	@Test
	public void putSNullKljucem() {
		assertThrows(NullPointerException.class, () -> mapa.put(null, 5));
	}

	@Test
	public void putSNullVrijednosti() {
		mapa.put("prvi", null);
		assertEquals(null, mapa.get("prvi"));
	}

	@Test
	public void getSNullKljucem() {
		assertEquals(null, mapa.get(null));
	}

	@Test
	public void getNullVrijednosti() {
		mapa.put("elem", null);
		assertEquals(null, mapa.get("elem"));
	}

	@Test
	public void getSPostojecimKljucem() {
		assertEquals(Integer.valueOf(1), mapa.get("prvi"));
	}

	@Test
	public void removeNullKljuca() {
		mapa.remove(null);
		assertEquals(5, mapa.size());
	}

	@Test
	public void removePostojecihKljuceva() {
		mapa.put("elem", 1);
		mapa.put("elem2", 2);
		mapa.remove("prvi");
		assertEquals(6, mapa.size());
		assertFalse(mapa.containsKey("prvi"));
		mapa.remove("drugi");
		assertEquals(5, mapa.size());
		assertFalse(mapa.containsKey("drugi"));
		mapa.remove("treci");
		assertEquals(4, mapa.size());
		assertFalse(mapa.containsKey("treci"));
	}

	@Test
	public void containsKeySNullKljucem() {
		assertFalse(mapa.containsKey(null));
	}

	@Test
	public void containsKeyPostojecegKljuca() {
		assertTrue(mapa.containsKey("prvi"));
		assertTrue(mapa.containsKey("drugi"));
		assertTrue(mapa.containsKey("treci"));
		assertTrue(mapa.containsKey("cetvrti"));
		assertTrue(mapa.containsKey("peti"));
	}

	@Test
	public void containsKeyNepostojecegKljuca() {
		assertFalse(mapa.containsKey("prv"));
		assertFalse(mapa.containsKey("trec"));
	}

	@Test
	public void containsValueSNullValue() {
		mapa.put("kljuc", null);
		assertTrue(mapa.containsValue(null));
	}

	@Test
	public void containsValueSPostojecimValue() {
		assertTrue(mapa.containsValue(1));
		assertTrue(mapa.containsValue(2));
		assertTrue(mapa.containsValue(3));
	}

	@Test
	public void containsKeyNepostojeceVrijednosti() {
		assertFalse(mapa.containsValue(Integer.valueOf(6)));
		assertFalse(mapa.containsValue(null));
	}

	@Test
	public void containsValueSDuplimVrijednostima() {
		mapa.put("elem1", 1);
		assertTrue(mapa.containsValue(1));
		mapa.remove("prvi");
		assertTrue(mapa.containsValue(1));
	}

	@Test
	public void toStringTest() {
		SimpleHashtable<String, Integer> tablica = new SimpleHashtable<String, Integer>();
		tablica.put("elem", 1);
		String rezultat = "[elem=1]";
		assertEquals(rezultat, tablica.toString());

		String mapaRezultat = mapa.toString();
		assertEquals('[', mapaRezultat.charAt(0));
		assertEquals(']', mapaRezultat.charAt(mapaRezultat.length() - 1));
		assertTrue(mapaRezultat.contains("prvi=1"));
		assertTrue(mapaRezultat.contains("drugi=2"));
		assertTrue(mapaRezultat.contains("treci=3"));
		assertTrue(mapaRezultat.contains("cetvrti=4"));
		assertTrue(mapaRezultat.contains("peti=5"));
	}

	@Test
	public void iteratorPrazneMape() {
		SimpleHashtable<String, Integer> map = new SimpleHashtable<String, Integer>();

		Iterator<TableEntry<String, Integer>> it = map.iterator();

		assertFalse(it.hasNext());

		assertThrows(NoSuchElementException.class, () -> it.next());
	}

	@Test
	public void iteratorPopunjeneMape() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();
		SimpleHashtable<String, Integer> rezultat = new SimpleHashtable<String, Integer>();
		while (it.hasNext()) {
			TableEntry<String, Integer> tmp = it.next();
			rezultat.put(tmp.getKey(), tmp.getValue());
		}

		assertEquals(5, rezultat.size());
		assertTrue(rezultat.containsKey("prvi"));
		assertTrue(rezultat.containsKey("drugi"));
		assertTrue(rezultat.containsKey("treci"));
		assertTrue(rezultat.containsKey("cetvrti"));
		assertTrue(rezultat.containsKey("peti"));
	}

	@Test
	public void iteratorNextNakonSvihElemenata() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();

		while (it.hasNext())
			it.next();
		assertThrows(NoSuchElementException.class, () -> it.next());
	}

	@Test
	public void iteratorKodIzmjene() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();

		it.next();
		mapa.remove("prvi");
		assertThrows(ConcurrentModificationException.class, () -> it.next());
		assertThrows(ConcurrentModificationException.class, () -> it.hasNext());
	}

	@Test
	public void removeKodIteratora() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();

		// prvi element
		TableEntry<String, Integer> entry = it.next();
		it.remove();
		assertFalse(mapa.containsKey(entry.getKey()));
		assertTrue(it.hasNext());
		// drugi
		it.next();
		// treci
		entry = it.next();
		it.remove();
		assertFalse(mapa.containsKey(entry.getKey()));
		assertTrue(it.hasNext());
		// cetvrti
		entry = it.next();
		it.remove();
		assertFalse(mapa.containsKey(entry.getKey()));
		assertTrue(it.hasNext());
		// peti
		it.next();
		assertFalse(it.hasNext());
	}

	@Test
	public void pozivRemovePoslijeIzmjene() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();

		it.next();
		mapa.remove("prvi");

		assertThrows(ConcurrentModificationException.class, () -> it.remove());
	}

	@Test
	public void pozivRemove2Puta() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();

		it.next();
		it.remove();

		assertThrows(IllegalStateException.class, () -> it.remove());
	}

	@Test
	public void paralelna2Iteratora() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();
		Iterator<TableEntry<String, Integer>> it2 = mapa.iterator();

		while (it.hasNext()) {
			assertEquals(it.next(), it2.next());
		}
	}

	@Test
	public void paralelna2IteratoraRemove() {
		Iterator<TableEntry<String, Integer>> it = mapa.iterator();
		Iterator<TableEntry<String, Integer>> it2 = mapa.iterator();

		it.next();
		it2.next();
		it.remove();
		assertThrows(ConcurrentModificationException.class, () -> it2.next());
		assertThrows(ConcurrentModificationException.class, () -> it2.remove());
		assertThrows(ConcurrentModificationException.class, () -> it2.hasNext());
		it.hasNext();
		it.next();
	}
}
