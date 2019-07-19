package hr.fer.zemris.java.raytracer.model;

/**
 * Razred predstavlja grafički model sfere koja uz svoju prostornu definiciju
 * sadrži i podatke o konstantama difuzijskih i refleksivnih komponenti
 * osvjetljenja.
 * 
 * @author Ante Miličević
 *
 */
public class Sphere extends GraphicalObject {

	/**
	 * Točka središta sfere
	 */
	private Point3D center;
	/**
	 * Polumjer sfere
	 */
	private double radius;
	/**
	 * Difuzijska konstanta crvene boje
	 */
	private double kdr;
	/**
	 * Difuzijska konstanta zelene boje
	 */
	private double kdg;
	/**
	 * Difuzijska konstanta plave boje
	 */
	private double kdb;
	/**
	 * Reflektivna konstanta crvene boje
	 */
	private double krr;
	/**
	 * Reflektivna konstanta zelene boje
	 */
	private double krg;
	/**
	 * Reflektivna konstanta plave boje
	 */
	private double krb;
	/**
	 * Koeficijent refleksije
	 */
	private double krn;

	/**
	 * Konstruktor koji inicijalizira sve podatke sfere
	 * 
	 * @param center
	 * @param radius
	 * @param kdr
	 * @param kdg
	 * @param kdb
	 * @param krr
	 * @param krg
	 * @param krb
	 * @param krn
	 */
	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		super();
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D direction = ray.direction;
		Point3D origin = ray.start;
		Point3D centerToOrigin = origin.sub(center);

		double determinant = calculateDeterminant(direction, centerToOrigin);

		if (determinant < 0) {
			return null;
		} else {
			double fixedDistance = -(direction.scalarProduct(centerToOrigin));
			if (determinant < 1e-5) {
				if (fixedDistance < 0) {
					return null;
				}
				return new SphereRayIntersection(origin.add(direction.scalarMultiply(fixedDistance)), fixedDistance, true);
			} else {
				double distance1 = fixedDistance - Math.sqrt(determinant);
				double distance2 = fixedDistance + Math.sqrt(determinant);

				if (distance2 < 0) {
					return null;
				}

				if (distance1 > 0) {
					return new SphereRayIntersection(origin.add(direction.scalarMultiply(distance1)), distance1, true);
				} else {
					return new SphereRayIntersection(origin.add(direction.scalarMultiply(distance2)), distance2, true);
				}
			}
		}
	}

	/**
	 * Metoda koja računa determinantu kvadratne kvadratne funkcije za pronalazak
	 * sjecišta pravca i sfere po formuli
	 * <p>
	 * direction*(origin-center)^2-(|origin-center|^2-radius^2)
	 * </p>
	 * 
	 * @param direction      smjer pravca
	 * @param centerToOrigin vektor origin - center (točka pravca - središte sfere)
	 * 
	 * @return determinanta kvadratne funkcije
	 */
	private double calculateDeterminant(Point3D direction, Point3D centerToOrigin) {
		double firstParam = direction.scalarProduct(centerToOrigin);
		double originToCenterNorm = centerToOrigin.norm();
		double secondParam = originToCenterNorm * originToCenterNorm - radius * radius;

		return firstParam * firstParam - secondParam;
	}

	/**
	 * Implementacija {@link RayIntersection} koji modelira presjek pravca i sfere.
	 * 
	 * @author Ante Miličević
	 *
	 */
	private class SphereRayIntersection extends RayIntersection {

		protected SphereRayIntersection(Point3D point, double distance, boolean outer) {
			super(point, distance, outer);
		}

		@Override
		public double getKdb() {
			return kdb;
		}

		@Override
		public double getKdg() {
			return kdg;
		}

		@Override
		public double getKdr() {
			return kdr;
		}

		@Override
		public double getKrb() {
			return krb;
		}

		@Override
		public double getKrg() {
			return krg;
		}

		@Override
		public double getKrn() {
			return krn;
		}

		@Override
		public double getKrr() {
			return krr;
		}

		@Override
		public Point3D getNormal() {
			return getPoint().sub(center).normalize();
		}

	}

}
