package hr.fer.zemris.math;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class Vector2DTest {
	
	@Test
	public void konstuktorTest() {
		Vector2D test = new Vector2D(1.5,2.7);
		assertEquals(1.5,test.getX());
		assertEquals(2.7,test.getY());
	}
	
	@Test
	public void translateTrenutniNull() {
		Vector2D test = new Vector2D(1.5,2.7);
		assertThrows(NullPointerException.class,()->test.translate(null));
	}
	
	@Test
	public void translateGenerirajuciNull() {
		Vector2D test = new Vector2D(1.5,2.7);
		assertThrows(NullPointerException.class,()->test.translated(null));
	}
	
	@Test
	public void translateTrenutniNegativniArgumenti() {
		Vector2D test = new Vector2D(1.5,2.7);
		test.translate(new Vector2D(-1.212,-2.341));
		assertEquals(new Vector2D(0.288,0.359),test);
	}
	
	@Test
	public void translateTrenutniPozitivniArgumenti() {
		Vector2D test = new Vector2D(3.1,0.7);
		test.translate(new Vector2D(2.321, 4.321));
		assertEquals(new Vector2D(5.421,5.021),test);
	}
	
	@Test
	public void translateGenerirajuciNegativniArgumenti() {
		Vector2D test = new Vector2D(1.5,2.7).translated(new Vector2D(-1.212,-2.341));
		assertEquals(new Vector2D(0.288,0.359),test);
	}
	
	@Test
	public void translateGenerirajuciPozitivniArgumenti() {
		Vector2D test = new Vector2D(3.1,0.7).translated(new Vector2D(2.321,4.321));
		assertEquals(new Vector2D(5.421,5.021),test);
	}
	
	@Test
	public void rotateTrenutniSPozitivnimKutom() {
		Vector2D test = new Vector2D(1.32,2.4);
		test.rotate(Math.PI/3);
		assertEquals(new Vector2D(-1.418461,2.343154),test);
	}
	
	@Test
	public void rotateTrenutniSPozitivnimKutomVecimOd2PI() {
		Vector2D test = new Vector2D(1.32,2.4);
		test.rotate(Math.PI/3+2*Math.PI);
		assertEquals(new Vector2D(-1.418461,2.343154),test);
	}
	
	@Test
	public void rotateTrenutniSNegativnimKutom() {
		Vector2D test = new Vector2D(1.32,2.4);
		test.rotate(-Math.PI/3);
		assertEquals(new Vector2D(2.73846,0.056846),test);
	}
	
	@Test
	public void rotateGenerirajuciSPozitivnimKutom() {
		Vector2D test = new Vector2D(-1.234,2.131).rotated(Math.PI*6/5);
		assertEquals(new Vector2D(2.250897,-0.998688),test);
	}
	
	@Test
	public void rotateGenerirajuciSKutomVecimOd2PI() {
		Vector2D test = new Vector2D(-1.234,2.131).rotated(Math.PI*6/5 + Math.PI*2);
		assertEquals(new Vector2D(2.250897,-0.998688),test);
	}
	
	@Test
	public void rotateGenerirajuciSNegativnimKutom() {
		Vector2D test = new Vector2D(-1.234,2.131).rotated(-Math.PI*6/5);
		assertEquals(new Vector2D(-0.254243,-2.449342),test);
	}
	
	@Test
	public void scaleTrenutniSNulom() {
		Vector2D test = new Vector2D(4, 2);
		test.scale(0);
		assertEquals(new Vector2D(0,0),test);
	}
	
	@Test
	public void scaleTrenutniSPozitivnim() {
		Vector2D test = new Vector2D(4,2);
		test.scale(0.7);
		assertEquals(new Vector2D(2.8,1.4),test);
		test.scale(1.5);
		assertEquals(new Vector2D(4.2,2.1),test);
	}
	
	@Test
	public void scaleTrenutniSNegativnim() {
		Vector2D test = new Vector2D(4,2);
		test.scale(-0.7);
		assertEquals(new Vector2D(-2.8,-1.4),test);
		test.scale(-1.5);
		assertEquals(new Vector2D(4.2,2.1),test);
	}
	
	@Test
	public void scaleGenerirajuciSNulom() {
		Vector2D test = new Vector2D(4, 2).scaled(0);
		assertEquals(new Vector2D(0,0),test);
	}
	
	@Test
	public void scaleGenerirajuciSPozitivnim() {
		Vector2D test = new Vector2D(4,2).scaled(0.7);
		assertEquals(new Vector2D(2.8,1.4),test);
		assertEquals(new Vector2D(4.2,2.1),test.scaled(1.5));
	}
	
	@Test
	public void scaleGenerirajuciSNegativnim() {
		Vector2D test = new Vector2D(4,2).scaled(-0.7);
		assertEquals(new Vector2D(-2.8,-1.4),test);
		assertEquals(new Vector2D(4.2,2.1),test.scaled(-1.5));
	}
	
	@Test
	public void copyTest() {
		Vector2D test = new Vector2D(3,7);
		assertEquals(test,test.copy());
	}
}
