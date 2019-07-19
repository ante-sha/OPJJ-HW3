package hr.fer.zemris.java.hw17.jvdraw.color;

import java.awt.Color;

/**
 * Sučelje koje definira promatrača na {@link IColorProvider}.
 * 
 * @author Ante Miličević
 *
 */
public interface ColorChangeListener {
	/**
	 * Metoda koja se poziva prilikom promjene boje
	 * 
	 * @param source   objekt koji mijenja boju
	 * @param oldColor stara boja
	 * @param newColor nova boja
	 */
	public void newColorSelected(IColorProvider source, Color oldColor, Color newColor);
}
