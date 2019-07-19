package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;
import hr.fer.zemris.math.Vector2D;

/**
 * Razred koji modelira objekt koji omogućava konfiguraciju i izgradnju sustava
 * L-gramatike kojeg se prikazuje pomoću naredbi {@link Command} u interakciji s
 * {@link Context}, {@link TurtleState}-om. Konfiguracija se može odraditi preko
 * metoda ili predavanjem teksta konfiguracije.
 * 
 * @author Ante Miličević
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {
	/**
	 * Nazivi svih podržanih konfiguracija u konstantama
	 */
	private static final String ORIGIN = "origin";
	private static final String ANGLE = "angle";
	private static final String UNIT_LENGTH = "unitLength";
	private static final String UNIT_LENGTH_DEGREE_SCALER = "unitLengthDegreeScaler";
	private static final String COMMAND = "command";
	private static final String AXIOM = "axiom";
	private static final String PRODUCTION = "production";

	/**
	 * Nazivi svih podržanih naredbi u konstantama
	 */
	private static final String DRAW = "draw";
	private static final String SKIP = "skip";
	private static final String SCALE = "scale";
	private static final String ROTATE = "rotate";
	private static final String PUSH = "push";
	private static final String POP = "pop";
	private static final String COLOR = "color";
	/**
	 * Mapa koja pod simbolom čuva naredbu koju ona predstavlja
	 */
	private Dictionary<String, Command> commands = new Dictionary<String, Command>();
	/**
	 * Mapa koja pod simbolom čuva produkciju tog simbola<br>
	 * Ne može postojati više produkcija za jedan simbol
	 */
	private Dictionary<String, String> productions = new Dictionary<String, String>();
	/**
	 * Duljina jediničnog koraka
	 */
	private double unitLength = 0.1;
	/**
	 * Faktor umanjivanja jediničnog koraka pri generiranju viših razina rekurzije
	 * sustava
	 */
	private double unitLengthDegreeScaler = 1;
	/**
	 * Početni položaj kornjače
	 */
	private Vector2D origin = new Vector2D(0, 0);
	/**
	 * Orijentacija kornjače
	 */
	private double angle = 0;
	/**
	 * Početni simbol gramatike
	 */
	private String axiom = "";

	/**
	 * Metoda koja gradi sustav temeljem predanih (ili defaultnih) konfiguracija
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Metoda koja preko parametra {@code configurations} isčitava i sprema
	 * konfiguracije. Prazni stringovi se zanemaruju.<br>
	 * <p>
	 * Podržane konfiguracije su:<br>
	 * origin, angle, unitLength, unitLengthDegreeScaler, command axiom, production
	 * </p>
	 * 
	 * @param configurations polje stringova koje sadrži konfiguracije
	 * 
	 * @throws NullPointerException     ako je {@code configurations null}
	 * @throws IllegalStateException    ako konfiguracija nije podržana
	 * @throws IllegalArgumentException ako argumenti konfiguracije nisu u dobrom
	 *                                  formatu
	 */
	@Override
	public LSystemBuilder configureFromText(String[] configurations) {
		for (String configuration : Objects.requireNonNull(configurations)) {
			configuration = configuration.trim();
			String confName = configuration.split("[ ]+")[0];
			int duljinaNaredbe = confName.length();

			if (duljinaNaredbe == 0) {
				continue;
			}
			String args = configuration.substring(duljinaNaredbe).trim();
			if (confName.equals(ORIGIN)) {
				configurateOrgin(args);
			} else if (confName.equals(ANGLE)) {
				configurateAngle(args);
			} else if (confName.equals(UNIT_LENGTH)) {
				configurateUnitLenght(args);
			} else if (confName.equals(UNIT_LENGTH_DEGREE_SCALER)) {
				configurateUnitLengthDegreeScaler(args);
			} else if (confName.equals(COMMAND)) {
				configurateCommand(args);
			} else if (confName.equals(AXIOM)) {
				configurateAxiom(args);
			} else if (confName.equals(PRODUCTION)) {
				configurateProduction(args);
			} else
				throw new IllegalStateException("Configuration " + confName + " is not supported!");
		}
		return this;
	}

	/**
	 * Metoda koja prevodi konfiguraciju produkcije.
	 * 
	 * @param string string predan uz naziv konfiguracije
	 * 
	 * @throws IllegalArgumentException ako se u stringu nalazi krivi broj
	 *                                  argumenata ili lijeva strana produkcije nije
	 *                                  samo jedan znak ili ako produkcija za taj
	 *                                  znak već postoji
	 */
	private void configurateProduction(String string) {
		String[] args = string.split("[ ]+");
		if (args.length > 2) {
			wrongArgs(PRODUCTION, true);
		}
		if (args[0].length() != 1) {
			wrongArgs(PRODUCTION, true);
		}
		if (productions.get(args[0]) != null) {
			throw new IllegalArgumentException("Production for " + args[0] + " was already defined!");
		}
		if (args.length == 2) {
			productions.put(args[0], args[1]);
		} else {
			productions.put(args[0], "");
		}
	}

	/**
	 * Metoda koja prevodi konfiguraciju aksioma. Aksiom se sprema u člansku
	 * varijablu
	 * 
	 * @param string string predan uz naziv konfiguracije
	 * 
	 * @throws IllegalArgumentException ako se u stringu nalazi krivi broj
	 *                                  argumenata
	 */
	private void configurateAxiom(String string) {
		setAxiom(string);
	}

	/**
	 * Metoda koja validira unos argumenata za konfiguriranje naredbe i ostatak
	 * prevođenja prosljeđuje {@link #registerCommand(char, String)}
	 * 
	 * @param string string koji sadrži argumente konfiguracije
	 * 
	 * @throws IllegalStateException    ako naredba ne postoji
	 * @throws IllegalArgumentException ako naredba ne sadrži dobre argumente
	 */
	private void configurateCommand(String string) {
		if (string.length() < 1 || string.charAt(1) != ' ') {
			wrongArgs(COMMAND, true);
		}
		registerCommand(string.charAt(0), string.substring(1));
	}

	/**
	 * Metoda koja validira unos argumenata za konfiguriranje jediničnog koraka
	 * 
	 * @param string string koji sadrži argumente konfiguracije
	 * 
	 * @throws IllegalArgumentException ako string ne sadrži jedan broj koji se može
	 *                                  protumačiti kao decimalan
	 */
	private void configurateUnitLenght(String string) {
		try {
			double unitLength = Double.parseDouble(string);
			this.unitLength = unitLength;
		} catch (NumberFormatException e) {
			wrongArgs(UNIT_LENGTH, true);
		}
	}

	/**
	 * Metoda koja prevodi argumente predane uz konfiguraciju
	 * unitLengthDegreeScaler. Dozvoljeni argumenti su oblika jednog decimalnog
	 * broja ili dva decimalna broja odvojena znakom '/' koji se tumače kao razlomak
	 * 
	 * @param string string koji sadrži argumente konfiguracije
	 * 
	 * @throws IllegalArgumentException ako argumenti nisu u istom formatu kao što
	 *                                  je navedeno u definiciji metode
	 */
	private void configurateUnitLengthDegreeScaler(String string) {
		try {
			if (!string.contains("/")) {
				this.unitLengthDegreeScaler = Double.parseDouble(string);
			} else {
				int delimiter = string.indexOf('/');
				double numerator = Double.parseDouble(string.substring(0, delimiter));
				double denominator = Double.parseDouble(string.substring(delimiter + 1));

				this.unitLengthDegreeScaler = numerator * 1.0 / denominator;
			}
		} catch (NumberFormatException | ArithmeticException e) {
			wrongArgs(UNIT_LENGTH_DEGREE_SCALER, true);
		}

	}

	/**
	 * Metoda koja konfigurira orijentaciju iz argumenta predanih u stringu.
	 * Argument mora biti jedan decimalni broj
	 * 
	 * @param string string koji sadrži argumente konfiguracije
	 * 
	 * @throws IllegalArgumentException ako string ne sadrži jedan decimalan broj
	 */
	private void configurateAngle(String string) {
		try {
			this.angle = Math.toRadians(Double.parseDouble(string));
		} catch (NumberFormatException e) {
			wrongArgs(ANGLE, true);
		}

	}

	/**
	 * Metoda koja konfigurira početnu točku iz 2 parametra predanih kroz string.
	 * Parametri moraju biti decimalni brojevi.
	 * 
	 * @param string string koji sadrži argumente konfiguracije
	 * 
	 * @throws IllegalArgumentException ako string ne sadrži točno 2 decimalna broja
	 */
	private void configurateOrgin(String string) {
		String[] numbers = string.split("[ ]+");
		if (numbers.length != 2) {
			wrongArgs(ORIGIN, true);
		}
		try {
			double x = Double.parseDouble(numbers[0]);
			double y = Double.parseDouble(numbers[1]);
			setOrigin(x, y);
		} catch (NumberFormatException e) {
			wrongArgs(ORIGIN, true);
		}
	}

	/**
	 * Metoda koja registrira naredbu {@code action} pod ključem {@code key}.
	 * <p>
	 * Popis podržanih naredbi: <br>
	 * draw, skip, scale, rotate, push, pop, color.
	 * </p>
	 * 
	 * @param key    ključ pod kojim se pamti naredba
	 * @param action string reprezentacija naredbe
	 * 
	 * @throws IllegalStateException    ako navedena naredba nije podržana
	 * @throws IllegalArgumentException ako argumenti nisu u dobrom formatu
	 */
	@Override
	public LSystemBuilder registerCommand(char key, String action) {
		Objects.requireNonNull(action);
		action = action.trim();

		String commandName = action.split("[ ]+")[0];
		int duljinaNaredbe = commandName.length();

		String args = action.substring(duljinaNaredbe).trim();
		if (commandName.equals(DRAW)) {
			interpretDraw(key, args);
		} else if (commandName.equals(SKIP)) {
			interpretSkip(key, args);
		} else if (commandName.equals(SCALE)) {
			interpretScale(key, args);
		} else if (commandName.equals(ROTATE)) {
			interpretRotate(key, args);
		} else if (commandName.equals(PUSH)) {
			interpretPush(key, args);
		} else if (commandName.equals(POP)) {
			interpretPop(key, args);
		} else if (commandName.equals(COLOR)) {
			interpretColor(key, args);
		} else
			throw new IllegalStateException("Command " + commandName + " is not supported!");

		return this;
	}

	/**
	 * Metoda koja interpretira {@code args} kao argument draw naredbe.<br>
	 * Dozvoljen je samo jedan decimalan broj koji reprezentira koeficijent pomaka
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako se unutar argumenta ne nalazi samo jedan
	 *                                  decimalan broj
	 */
	private void interpretDraw(char key, String args) {
		checkForSpacesInArgument(args, DRAW);
		double step = 0;
		try {
			step = Double.parseDouble(args);
		} catch (NumberFormatException e) {
			wrongArgs(DRAW, false);
		}
		commands.put(Character.toString(key), new DrawCommand(step));
	}

	/**
	 * Metoda koja interpretira {@code args} kao argument rotate naredbe.<br>
	 * Dozvoljen je samo jedan decimalan broj koji reprezentira kut u stupnjevima
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako se unutar argumenta ne nalazi samo jedan
	 *                                  decimalan broj
	 */
	private void interpretRotate(char key, String args) {
		checkForSpacesInArgument(args, ROTATE);
		double angle = 0;
		try {
			angle = Double.parseDouble(args);
		} catch (NumberFormatException e) {
			wrongArgs(ROTATE, false);
		}
		commands.put(Character.toString(key), new RotateCommand(Math.toRadians(angle)));
	}

	/**
	 * Metoda koja interpretira {@code args} kao argument skip naredbe.<br>
	 * Dozvoljen je samo jedan decimalan broj koji reprezentira koeficijent pomaka
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako se unutar argumenta ne nalazi samo jedan
	 *                                  decimalan broj
	 */
	private void interpretSkip(char key, String args) {
		checkForSpacesInArgument(args, SKIP);
		double step = 0;
		try {
			step = Double.parseDouble(args);
		} catch (NumberFormatException e) {
			wrongArgs(SKIP, false);
		}
		commands.put(Character.toString(key), new SkipCommand(step));
	}

	/**
	 * Metoda koja interpretira {@code args} kao argument scale naredbe.<br>
	 * Dozvoljen je samo jedan decimalan broj koji reprezentira faktor povećanja
	 * jedinične duljina koraka
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako se unutar argumenta ne nalazi samo jedan
	 *                                  decimalan broj
	 */
	private void interpretScale(char key, String args) {
		checkForSpacesInArgument(args, SCALE);
		double scaler = 0;
		try {
			scaler = Double.parseDouble(args);
		} catch (NumberFormatException e) {
			wrongArgs("scale", false);
		}
		commands.put(Character.toString(key), new ScaleCommand(scaler));
	}

	/**
	 * Metoda koja provjerava da li je {@code args} prazan string i tada sprema
	 * {@link PushCommand} pod ključem {@code key}
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako args nije prazan string
	 */
	private void interpretPush(char key, String args) {
		if (args.length() != 0) {
			wrongArgs(PUSH, false);
		}
		commands.put(Character.toString(key), new PushCommand());
	}

	/**
	 * Metoda koja provjerava da li je {@code args} prazan string i tada sprema
	 * {@link PopCommand} pod ključem {@code key}
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako args nije prazan string
	 */
	private void interpretPop(char key, String args) {
		if (args.length() != 0) {
			wrongArgs(POP, false);
		}
		commands.put(Character.toString(key), new PopCommand());
	}

	/**
	 * Metoda koja provjerava da li se {@code args} može protumačiti kao
	 * heksadekadski zapis 6 znamenaka koji reprezentiraju RGB boju
	 * {@link ColorCommand} naredbe i onda se naredba sprema u mapu pod ključem
	 * {@code key}
	 * 
	 * @param key  ključ pod kojim se pamti naredba
	 * @param args argumenti naredbe
	 * 
	 * @throws IllegalArgumentException ako ne sadrži samo 6 spojenih
	 *                                  heksadeksadskih znamenaka
	 */
	public void interpretColor(char key, String args) {
		checkForSpacesInArgument(args, COLOR);
		if (!args.matches("[0-9|a-f|A-F]{6}")) {
			wrongArgs(COLOR, false);
		}
		commands.put(Character.toString(key), new ColorCommand(Color.decode("0x" + args)));
	}

	/**
	 * Pomoćna metoda za bacanje {@link IllegalArgumentException}-a uz odgovarajuću
	 * poruku ovisno o tome da li se greška dogodila u parsiranju konfiguracije ili
	 * naredbe
	 * 
	 * @param name          ima konfiguracije/naredbe
	 * @param configuration true ako se radi o konfiguraciji, false ako se radi o
	 *                      naredbi
	 * 
	 * @throws IllegalArgumentException
	 */
	private void wrongArgs(String name, boolean configuration) {
		throw new IllegalArgumentException(
				"Wrong arguments for " + (configuration ? " configuration" : " command") + name);
	}

	/**
	 * Pomoćna metoda koja provjerava da li string {@code arg} sadrži u sebi
	 * praznine.
	 * 
	 * @param arg     string koji se ispituje
	 * @param command naziv naredbe za koju se vrši ispitivanje
	 * 
	 * @throws IllegalArgumentException ako {@code arg} sadrži prazninu
	 */
	private void checkForSpacesInArgument(String arg, String command) {
		if (arg.matches(".*[ |\\n|\\t]+.*"))
			throw new IllegalArgumentException("Commands " + command + " can not have more than one argument");
	}

	/**
	 * Metoda koja registrira produkciju koja obavlja prijelaz
	 * {@code key -> product}
	 * 
	 * @param key     početni znak produkcije
	 * @param product niz znakova koji nastaje primjenom produkcije
	 * 
	 * @return this
	 * 
	 * @throws IllegalArgumentException ako product sadrži praznine
	 * @throws NullPointerException     ako je product null
	 */
	@Override
	public LSystemBuilder registerProduction(char key, String product) {
		Objects.requireNonNull(product);
		if (product.contains(" ")) {
			throw new IllegalArgumentException("Product of production must not contain more than one argument");
		}
		productions.put(Character.toString(key), product);
		return this;
	}

	/**
	 * Metoda koja postavlja orijentaciju na kut {@code angle}. Kut je zadan u
	 * stupnjevima.
	 * 
	 * @param angle kut u stupnjevima
	 * 
	 * @return this
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = Math.toRadians(angle);
		return this;
	}

	/**
	 * Metoda koja postavlja početni aksiom za L-gramatiku. Aksiom mora biti niz
	 * spojenih znakova.
	 * 
	 * @param axiom aksiom
	 * 
	 * @return this
	 * 
	 * @throws NullPointerException     ako je axiom null
	 * @throws IllegalArgumentException ako axiom nije niz spojenih znakova
	 * 
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		Objects.requireNonNull(axiom);
		axiom = axiom.trim();
		if (axiom.contains(" "))
			throw new IllegalArgumentException("Axiom can not contain spaces between characters!");
		this.axiom = axiom;
		return this;
	}

	/**
	 * Metoda koja postavlja početni položaj kornjače.<br>
	 * x,y ∈ [0,1]
	 * 
	 * @param x x koordinata
	 * @param y y koordinate
	 * 
	 * @return this
	 * 
	 * @throws IllegalArgumentException ako su x i/ili y izvan dozvoljenih granica
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		if (x < 0 || x > 1) {
			throw new IllegalArgumentException("X out of bounds");
		}
		if (y < 0 || y > 1) {
			throw new IllegalArgumentException("Y out of bounds");
		}
		origin = new Vector2D(x, y);
		return this;
	}

	/**
	 * Metoda koja postavlja duljinu jediničnog koraka
	 * 
	 * @param unitLength duljina jediničnog koraka
	 * 
	 * @return this
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;
		return this;
	}

	/**
	 * Metoda koja postavalja faktor umanjivanja jediničnog koraka. Jedinični korak
	 * se umanjuje po formuli {@code unitLength * (unitLengthDegreeScaler^level)}
	 * 
	 * @param unitLengthDegreeScaler faktor umanjivanja jediničnog koraka
	 * 
	 * @return this
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;
		return this;
	}

	/**
	 * Privatni razred koji implementira sustav koji je konfiguriran u vanjskom
	 * razredu<br>
	 * Instanciranje primjerka se obavlja putem {@link LSystemBuilderImpl#build()}
	 * metode.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class LSystemImpl implements LSystem {
		/**
		 * Skup spremljenih stanja sustava
		 */
		private Context context;

		private final Color DEFAULT_COLOR = Color.BLACK;
		/**
		 * Metoda koja iscrtava produkt L-gramatike rekurzivne razine {@code level}.
		 * Iscrtavanje se vrši pomoću {@code painter}-a. L-gramatika i sve konfiguracije
		 * sustava su navedene u pripadajućem {@link LSystemBuilderImpl} objektu
		 * 
		 * @param level razina rekurzije L-gramatike
		 * @param painter objekt odgovoran za crtanje na ekran
		 * 
		 */
		@Override
		public void draw(int level, Painter painter) {
			context = prepareNewContext(level);
			String system = generate(level);

			for (char key : system.toCharArray()) {
				Command command = commands.get(Character.toString(key));
				if (command == null) {
					continue;
				}
				command.execute(context, painter);
			}

		}

		/**
		 * Pomoćna metoda koja priprema kontekst za novo iscrtavanje
		 * L-gramatike.
		 * 
		 * @param level rezina rekurzije L-gramatike
		 * 
		 * @return context
		 */
		private Context prepareNewContext(int level) {
			Context context = new Context();
			context.pushState(new TurtleState(origin.copy(), new Vector2D(1, 0).rotated(angle), DEFAULT_COLOR,
					unitLength * (Math.pow(unitLengthDegreeScaler, level))));

			return context;
		}

		/**
		 * Metoda koja generira niz znakova koji nastaju {@code level}-tom razinom
		 * rekurzije primjene produkcija na aksiom L-gramatike.
		 * 
		 * @param level razina rekurzije
		 * 
		 * @return niz generiranih znakova
		 */
		@Override
		public String generate(int level) {
			if (level == 0) {
				return axiom;
			}
			String resultOfSubTree = generate(level - 1);
			StringBuilder result = new StringBuilder();

			for (char key : resultOfSubTree.toCharArray()) {
				String production = productions.get(Character.toString(key));
				if (production == null) {
					result.append(key);
				} else {
					result.append(production);
				}
			}
			return result.toString();
		}

	}

}
