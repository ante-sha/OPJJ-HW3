package hr.fer.zemris.lsystems.impl.demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Demonstracijski program koji kofigurira {@link LSystemBuilderImpl} naredbama
 * 
 * @author Ante Miličević
 *
 */
public class Glavni1 {
	public static void main(String[] args) {
		LSystemViewer.showLSystem(createKochCurve(LSystemBuilderImpl::new));
//		LSystemViewer.showLSystem(createPlant(LSystemBuilderImpl::new));
	}

	private static LSystem createKochCurve(LSystemBuilderProvider provider) {
		return provider.createLSystemBuilder().registerCommand('F', "draw 1").registerCommand('+', "rotate 60")
				.registerCommand('-', "rotate -60").setOrigin(0.05, 0.4).setAngle(0).setUnitLength(0.9)
				.setUnitLengthDegreeScaler(1.0 / 3.0).registerProduction('F', "F+F--F+F").setAxiom("F").build();
	}

//	private static LSystem createPlant(LSystemBuilderProvider provider) {
//		return provider.createLSystemBuilder()
//				.registerCommand('F', "draw 1")
//				.registerCommand('+',"rotate 25.7")
//				.registerCommand('-',"rotate -25.7")
//				.registerCommand('[',"push")
//				.registerCommand(']',"pop")
//				.registerCommand('G',"color 00FF00")
//				.setOrigin(0.5,0.0)
//				.setAngle(90)
//				.setUnitLength(0.1)
//				.setUnitLengthDegreeScaler(1.0/2.05)
//				.setAxiom("GF")
//				.registerProduction('F',"F[+F]F[-F]F")
//				.build();
//	}
}
