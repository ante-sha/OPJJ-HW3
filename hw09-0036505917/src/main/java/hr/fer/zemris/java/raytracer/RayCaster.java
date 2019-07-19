package hr.fer.zemris.java.raytracer;

import java.util.concurrent.atomic.AtomicBoolean;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Program koji iscrtava 3D scenu zadanu kroz
 * {@link RayTracerViewer#createPredefinedScene()}. Pri iscrtavanju se zadaju
 * pozicija promatrača, vektor njegove y osi, pozicija koju je potrebno
 * iscrtati, horizontalna i vertiklana dužina tog prozora. Analiza piksela se
 * obavlja slijedno.
 * 
 * @author Ante Miličević
 *
 */
public class RayCaster {

	/**
	 * Ulazna točka programa
	 * 
	 * @param args ne koriste se
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Metoda tvornica za implementaciju IRayTracerProducer-a čija je funkcija
	 * određivanje rgb komponenti svih piksela te prosljeđivanja istih
	 * {@link IRayTracerResultObserver}-u na obradu.
	 * 
	 * @return objekt za obradu piksela scene
	 *         {@link RayTracerViewer#createPredefinedScene()}
	 */
	private static IRayTracerProducer getIRayTracerProducer() {
		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer, AtomicBoolean cancel) {
				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				// vektor odnosa promatrača i središta promatranog prozora
				Point3D zAxis = view.sub(eye).modifyNormalize();
				Point3D yAxis = viewUp.sub(view).normalize();
				Point3D xAxis = zAxis.vectorProduct(yAxis).normalize();

				// ugao prozora
				Point3D screenCorner = view.add(xAxis.scalarMultiply(-horizontal * 1.0 / 2))
						.add(yAxis.scalarMultiply(+vertical * 1.0 / 2));

				Scene scene = RayTracerViewer.createPredefinedScene();
				short[] rgb = new short[3];
				int offset = 0;

				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = calculateScreenPoint(x, y, width, height, horizontal, vertical,
								screenCorner, xAxis, yAxis);
						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
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
			private Point3D calculateScreenPoint(int x, int y, int width, int height, double horizontal, double vertical,
					Point3D corner, Point3D xAxis, Point3D yAxis) {
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
			protected void tracer(Scene scene, Ray ray, short[] rgb) {
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
			private void determineColorFor(Scene scene, RayIntersection closest, short[] rgb, Ray ray) {
				for (LightSource light : scene.getLights()) {
					Point3D source = light.getPoint();
					Ray lightRay = Ray.fromPoints(source, closest.getPoint());

					RayIntersection lightClosest = findClosestIntersection(scene, lightRay);

					if (lightClosest == null || Math
							.abs(source.sub(lightClosest.getPoint()).norm() - source.sub(closest.getPoint()).norm()) > 1e-3) {
						continue;
					}
					Point3D l = source.sub(lightClosest.getPoint()).normalize();
					Point3D normal = lightClosest.getNormal();
					Point3D reflected = normal.scalarMultiply(2 * normal.scalarProduct(l)).sub(l).normalize();

					//difuzna komponenta
					double scalarD = l.scalarProduct(normal);
					scalarD = scalarD < 0 ? 0 : scalarD;
					rgb[0] += light.getR() * scalarD * lightClosest.getKdr();
					rgb[1] += light.getG() * scalarD * lightClosest.getKdg();
					rgb[2] += light.getB() * scalarD * lightClosest.getKdb();

					//reflektivna komponenta
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
			private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
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
		};
	}
}
