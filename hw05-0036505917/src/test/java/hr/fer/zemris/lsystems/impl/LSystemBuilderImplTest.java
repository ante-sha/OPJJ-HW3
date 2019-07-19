package hr.fer.zemris.lsystems.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;

public class LSystemBuilderImplTest {

	@Test
	public void generateKochCurveLSystem() {
		LSystemBuilder builder = new LSystemBuilderImpl().setAxiom("F").registerProduction('F', "F+F--F+F");
		LSystem sistem = builder.build();
		
		assertEquals("F",sistem.generate(0));
		assertEquals("F+F--F+F",sistem.generate(1));
		assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F",sistem.generate(2));
	}
}
