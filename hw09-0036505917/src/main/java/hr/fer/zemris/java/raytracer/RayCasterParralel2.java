package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerAnimator;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program prikazuje simulaciju scene
 * {@link RayTracerViewer#createPredefinedScene2()} u pokretu. Za animaciju
 * pokreta se pri iscrtavanju koristi {@link IRayTracerAnimator}, a za samo
 * iscrtavanje {@link IRayTracerProducer}. Iscrtavanje se odvija paralelno.
 * 
 * @author Ante Miličević
 *
 */
public class RayCasterParralel2 {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), getIRayTracerAnimator(), 30, 30);
	}

	private static IRayTracerAnimator getIRayTracerAnimator() {
		return new IRayTracerAnimator() {
			long time;

			@Override
			public void update(long deltaTime) {
				time += deltaTime;
			}

			@Override
			public Point3D getViewUp() { // fixed in time
				return new Point3D(0, 0, 10);
			}

			@Override
			public Point3D getView() { // fixed in time
				return new Point3D(-2, 0, -0.5);
			}

			@Override
			public long getTargetTimeFrameDuration() {
				return 150; // redraw scene each 150 milliseconds
			}

			@Override
			public Point3D getEye() { // changes in time
				double t = (double) time / 10000 * 2 * Math.PI;
				double t2 = (double) time / 5000 * 2 * Math.PI;
				double x = 50 * Math.cos(t);
				double y = 50 * Math.sin(t);
				double z = 30 * Math.sin(t2);
				return new Point3D(x, y, z);
			}
		};
	}

	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {

			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D vuv = viewUp.sub(view).normalize();
				
				// vektor odnosa promatrača i središta promatranog prozora
				Point3D zAxis = view.sub(eye).modifyNormalize();
				Point3D yAxis = vuv.sub(zAxis.scalarMultiply(zAxis.scalarProduct(vuv))).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.add(xAxis.scalarMultiply(-horizontal * 1.0 / 2))
						.add(yAxis.scalarMultiply(+vertical * 1.0 / 2));

				Scene scene = RayTracerViewer.createPredefinedScene2();

				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(new RayTracerAction(eye, xAxis, yAxis, screenCorner, scene, width, height, horizontal,
						vertical, cancel, red, green, blue, 0, height - 1));
				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Razred koji obavlja rekurzivno dijeljenje redaka mreže piksela tako da svakom
	 * novom poslu prosljeđuje pola redaka koji su predani njemu sve dok broj redaka
	 * preostao u poslu je 1 ili 2. Nakon toga obavlja analizu piksela koji se
	 * iscrtavaju.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private static class RayTracerAction extends RecursiveAction {

		private static final long serialVersionUID = 1L;
		/**
		 * Pozicija promatrača
		 */
		Point3D eye;
		/**
		 * X os promatrača
		 */
		Point3D xAxis;
		/**
		 * Y os promatrača
		 */
		Point3D yAxis;
		/**
		 * Ugao zamišljenog okvira koji definira prozor kojeg se iscrtava
		 */
		Point3D screenCorner;
		/**
		 * Scena koja sadrži sve zamišljene objekte u prostoru
		 */
		Scene scene;
		/**
		 * Širina prozora
		 */
		int width;
		/**
		 * Visina prozora
		 */
		int height;
		/**
		 * Širina zamišljenog okvira
		 */
		double horizontal;
		/**
		 * Visina zamišljenog okvira
		 */
		double vertical;
		/**
		 * Zastavica za prekid trenutnog računanja zbog dolaska novih zahtjeva u sustav
		 */
		AtomicBoolean cancel;
		/**
		 * Crvena komponenta piksela
		 */
		short[] red;
		/**
		 * Zelena komponenta piksela
		 */
		short[] green;
		/**
		 * Plava komponenta piksela
		 */
		short[] blue;
		/**
		 * Početak y osi od koje se računaju pikseli
		 */
		int yMin;
		/**
		 * Uključivi kraj y osi do koje se računaju pikseli
		 */
		int yMax;
		/**
		 * Maksimalna dopuštena razlika između yMax i yMin
		 */
		public static final int treshold = 16;

		/**
		 * Konstruktor koji inicijalizira sve parametre razreda
		 * 
		 * @param eye
		 * @param xAxis
		 * @param yAxis
		 * @param screenCorner
		 * @param scene
		 * @param width
		 * @param height
		 * @param horizontal
		 * @param vertical
		 * @param cancel
		 * @param red
		 * @param green
		 * @param blue
		 * @param yMin
		 * @param yMax
		 */
		public RayTracerAction(Point3D eye, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene, int width,
				int height, double horizontal, double vertical, AtomicBoolean cancel, short[] red, short[] green,
				short[] blue, int yMin, int yMax) {
			super();
			this.eye = eye;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.scene = scene;
			this.width = width;
			this.height = height;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.cancel = cancel;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.yMin = yMin;
			this.yMax = yMax;
		}

		@Override
		protected void compute() {
			if (yMax < yMin + 1 + treshold) {
				computeDirect();
				return;
			}
			invokeAll(
					new RayTracerAction(eye, xAxis, yAxis, screenCorner, scene, width, height, horizontal, vertical,
							cancel, red, green, blue, yMin, (yMax + yMin) / 2),
					new RayTracerAction(eye, xAxis, yAxis, screenCorner, scene, width, height, horizontal, vertical,
							cancel, red, green, blue, (yMax + yMin) / 2 + 1, yMax));

		}

		/**
		 * Metoda za izračun svih piksela [0,width-1]x[yMin,yMax].
		 */
		protected void computeDirect() {
			short[] rgb = new short[3];
			int offset = yMin * width;

			for (int y = yMin; y <= yMax; y++) {
				if (cancel.get()) {
					return;
				}
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = calculateScreenPoint(x, y, width, height, horizontal, vertical, screenCorner,
							xAxis, yAxis);
					Ray ray = Ray.fromPoints(eye, screenPoint);
					tracer(scene, ray, rgb);
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}

		/**
		 * Metoda računa točku u prostoru koju predstavlja zadani piksel na ekranu.
		 * 
		 * @param x          x-komponenta piksela
		 * @param y          y-komponenta piksela
		 * @param width      širina prikazanog prozora
		 * @param height     visina prikazanog prozora
		 * @param horizontal širina promatranog okvira u prostoru
		 * @param vertical   visina promatranog okvira u prostoru
		 * @param corner     gornji lijevi ugao promatranog okvira
		 * @param xAxis      x os u prostoru u odnosu na orijentaciju promatrača
		 * @param yAxis      y os u prostoru u odnosu na orijentaciju promatrača
		 * @return točka u ravnini promatranog okvira koja je korespondentna x,y pikselu
		 *         na ekranu
		 */
		private static Point3D calculateScreenPoint(int x, int y, int width, int height, double horizontal,
				double vertical, Point3D corner, Point3D xAxis, Point3D yAxis) {
			return corner.add(xAxis.scalarMultiply(x * 1.0 / (width - 1) * horizontal))
					.add(yAxis.scalarMultiply(-y * 1.0 / (height - 1) * vertical));
		}

		/**
		 * Metoda koja u zadanoj sceni traži presjek objekata i linije ray te ako takav
		 * postoji daljnjim analiziranjem uspostavlja rgb komponentu za presjek.
		 * 
		 * @param scene zadana scena
		 * @param ray   pravac
		 * @param rgb   polje za crvenu zelenu i plavu komponentu boje
		 */
		protected static void tracer(Scene scene, Ray ray, short[] rgb) {
			rgb[0] = 0;
			rgb[1] = 0;
			rgb[2] = 0;
			RayIntersection closest = findClosestIntersection(scene, ray);
			if (closest == null) {
				return;
			}
			rgb[0] = 15;
			rgb[1] = 15;
			rgb[2] = 15;
			determineColorFor(scene, closest, rgb, ray);

		}

		/**
		 * Metoda za određivanje boje kod presjeka. Promatrane su samo difuzna i
		 * refleksivna komponenta koju generiraju izvori svjetlosti na objektu.
		 * 
		 * @param scene   zadana scena
		 * @param closest presjek linije promatrača i objekta iz scene
		 * @param rgb     rgb komponente boje
		 * @param ray     pravac koji se presjekao s objektom iz scene
		 */
		private static void determineColorFor(Scene scene, RayIntersection closest, short[] rgb, Ray ray) {
			for (LightSource light : scene.getLights()) {
				Point3D source = light.getPoint();
				Ray lightRay = Ray.fromPoints(source, closest.getPoint());

				RayIntersection lightClosest = findClosestIntersection(scene, lightRay);

				if (lightClosest == null || Math.abs(lightClosest.getPoint().sub(closest.getPoint()).norm()) > 1e-3) {
					continue;
				}
				Point3D l = source.sub(lightClosest.getPoint()).normalize();
				Point3D normal = lightClosest.getNormal();
				Point3D reflected = normal.scalarMultiply(2 * normal.scalarProduct(l)).sub(l).normalize();

				// difuzna komponenta
				double scalarD = l.scalarProduct(normal);
				scalarD = scalarD < 0 ? 0 : scalarD;
				rgb[0] += light.getR() * scalarD * lightClosest.getKdr();
				rgb[1] += light.getG() * scalarD * lightClosest.getKdg();
				rgb[2] += light.getB() * scalarD * lightClosest.getKdb();

				// reflektivna komponenta
				double cosA = reflected.scalarProduct(ray.direction.negate());
				double scalarS = cosA < 0 ? 0 : Math.pow(cosA, closest.getKrn());
				rgb[0] += light.getR() * scalarS * lightClosest.getKrr();
				rgb[1] += light.getG() * scalarS * lightClosest.getKrg();
				rgb[2] += light.getB() * scalarS * lightClosest.getKrb();
			}
		}

		/**
		 * Metoda traži najbliži presjek pravca i objekata iz scene
		 * 
		 * @param scene scena
		 * @param ray pravac
		 * @return najbliži presjek pravca i objekata iz scene
		 */
		private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			RayIntersection closest = null;

			for (GraphicalObject object : scene.getObjects()) {
				RayIntersection intersection = object.findClosestRayIntersection(ray);
				if (intersection == null)
					continue;
				if (closest == null) {
					closest = intersection;
				} else if (closest.getDistance() > intersection.getDistance()) {
					closest = intersection;
				}

			}
			return closest;
		}

	}

}
