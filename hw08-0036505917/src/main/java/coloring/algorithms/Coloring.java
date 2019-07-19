package coloring.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import marcupic.opjj.statespace.coloring.Picture;

/**
 * Razred koji modelira uređenu četvorku koja definira ponašanje obilaksa
 * ograničenog podprostora u prostoru stanja pri bojanju zatvorenog prostora.
 * 
 * @author Ante Miličević
 *
 */
public class Coloring implements Predicate<Pixel>, Consumer<Pixel>, Supplier<Pixel>, Function<Pixel, List<Pixel>> {

	/**
	 * Referentni piksel
	 */
	private Pixel reference;
	/**
	 * Objekt koji čuva informacije o trenutnom stanju u GUI-u
	 */
	private Picture picture;
	/**
	 * Željena boja
	 */
	private int fillColor;
	/**
	 * Početna boja
	 */
	private int refColor;

	/**
	 * Konstruktor koji inicijalizira sve varijable iz predanih parametara.
	 * 
	 * @param reference referentni piksel
	 * @param picture   informacije o trenutnom stanju u GUI-u
	 * @param fillColor željena boja
	 * 
	 * @throws NullPointerException ako je reference ili picture null
	 */
	public Coloring(Pixel reference, Picture picture, int fillColor) {
		super();
		this.reference = Objects.requireNonNull(reference);
		this.picture = Objects.requireNonNull(picture);
		this.fillColor = fillColor;
		refColor = picture.getPixelColor(reference.getX(), reference.getY());
	}

	@Override
	public List<Pixel> apply(Pixel t) {
		List<Pixel> result = new ArrayList<>();
		if (t.getX() > 0) {
			result.add(new Pixel(t.getX() - 1, t.getY()));
		}
		if (t.getY() > 0) {
			result.add(new Pixel(t.getX(), t.getY() - 1));
		}
		if (t.getX() + 1 < picture.getHeight()) {
			result.add(new Pixel(t.getX() + 1, t.getY()));
		}
		if (t.getY() + 1 < picture.getWidth()) {
			result.add(new Pixel(t.getX(), t.getY() + 1));
		}
		return result;
	}

	@Override
	public Pixel get() {
		return reference;
	}

	@Override
	public void accept(Pixel t) {
		picture.setPixelColor(t.getX(), t.getY(), fillColor);
	}

	@Override
	public boolean test(Pixel pixel) {
		return picture.getPixelColor(pixel.getX(), pixel.getY()) == refColor;
	}

}
