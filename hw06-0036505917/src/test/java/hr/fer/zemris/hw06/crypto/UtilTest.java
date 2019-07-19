package hr.fer.zemris.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.hw06.crypto.Util.*;

import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test
	public void hexToByteNull() {
		assertThrows(NullPointerException.class, () -> hextobyte(null));
	}

	@Test
	public void hexToByteWithIllegalCharacters() {
		assertThrows(IllegalArgumentException.class, () -> hextobyte("ab12u2"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("ab12 2"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("ab12.2"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("ab12+2"));
		assertThrows(IllegalArgumentException.class, () -> hextobyte("ab12\"2"));
	}

	@Test
	public void hexToByteEmptyString() {
		byte[] arr = hextobyte("");
		assertEquals(0, arr.length);
	}

	@Test
	public void hexToByteNotEvenStringSize() {
		byte[] arr = hextobyte("a2b3f");
		assertEquals((byte) 0x0a, arr[0]);
		assertEquals((byte) 0x2b, arr[1]);
		assertEquals((byte) 0x3f, arr[2]);
	}

	@Test
	public void hexToByteTest1() {
		byte[] arr = hextobyte("01aE22");
		assertEquals((byte) 0x01, arr[0]);
		assertEquals((byte) 0xae, arr[1]);
		assertEquals((byte) 0x22, arr[2]);
	}

	@Test
	public void hexToByteTest2() {
		byte[] arr2 = hextobyte("abcdef0123456789");
		assertEquals((byte) 0xab, arr2[0]);
		assertEquals((byte) 0xcd, arr2[1]);
		assertEquals((byte) 0xef, arr2[2]);
		assertEquals((byte) 0x01, arr2[3]);
		assertEquals((byte) 0x23, arr2[4]);
		assertEquals((byte) 0x45, arr2[5]);
		assertEquals((byte) 0x67, arr2[6]);
		assertEquals((byte) 0x89, arr2[7]);
	}

	@Test
	public void hexToByteTest3() {
		byte[] arr3 = hextobyte("0");
		assertEquals(1, arr3.length);
		assertEquals(0, arr3[0]);
	}

	@Test
	public void bytetohexNullArray() {
		assertThrows(NullPointerException.class, () -> bytetohex(null));
	}

	@Test
	public void bytetohexEmptyArray() {
		assertEquals("", bytetohex(new byte[0]));
	}

	@Test
	public void bytetohexTest1() {
		byte[] arr = new byte[] { (byte) 0x0a, (byte) 0x2b, (byte) 0xae };
		assertEquals("0a2bae", bytetohex(arr));
	}

	@Test
	public void bytetohexTest2() {
		byte[] arr2 = new byte[] { (byte) 0xab, (byte) 0xcd, (byte) 0xef, (byte) 0x01, (byte) 0x23, (byte) 0x45,
				(byte) 0x67, (byte) 0x89 };
		assertEquals("abcdef0123456789",bytetohex(arr2));
	}
	
	@Test
	public void bytetohexTest3() {
		byte[] arr3 = new byte[] {(byte)0x00};
		assertEquals("00", bytetohex(arr3));
	}
}
