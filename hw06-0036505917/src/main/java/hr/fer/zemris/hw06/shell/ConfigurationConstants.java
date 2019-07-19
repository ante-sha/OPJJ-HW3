package hr.fer.zemris.hw06.shell;

/**
 * Konfiguracijski razred za {@link MyShell}
 * 
 * @author Ante Miličević
 *
 */
public class ConfigurationConstants {
	/**
	 * Oznaka prompt simbola
	 */
	public static final String prompt = "PROMPT";
	/**
	 * Oznaka multiline simbola
	 */
	public static final String multiline = "MULTILINE";
	/**
	 * Oznaka morelines simbole
	 */
	public static final String morelines = "MORELINES";
	
	/**
	 * Defaultni prompt simbol
	 */
	public static final Character defaultPrompt = '>';
	/**
	 * Defaultni multiline simbol
	 */
	public static final Character defaultMultiline = '|';
	/**
	 * Defaultni morelines simbol
	 */
	public static final Character defaultMorelines = '\\';
	
	/**
	 * Naziv ljuske s njegovom verzijom
	 */
	public static final String shellName = "MyShell v 1.0";
}
