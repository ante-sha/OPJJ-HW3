package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Demonstracijski program koji kofigurira {@link LSystemBuilderImpl}
 * konfiguracijskim datotekama
 * 
 * @author Ante Miličević
 *
 */
public class Glavni3 {
	public static void main(String[] args) {
		LSystemViewer.showLSystem(LSystemBuilderImpl::new);
	}
}
