package me.robnoo02.plotreviewplugin.review;

import java.text.DecimalFormat;

/**
 * This class will not be used any longer due to unnecessary object creation.
 * @author Robnoo02
 *
 */
public final class ReviewPoints {
	
	private static final int MIN_VAL = 1;
	private final double structure, terrain, organics, composition, overall, divider;

	private ReviewPoints(final double s, final double t, final double o, final double c) {
		this.structure = (s < MIN_VAL) ? 0 : s;
		this.terrain = (t < MIN_VAL) ? 0 : t;
		this.organics = (o < MIN_VAL) ? 0 : o;
		this.composition = (c < MIN_VAL) ? 0 : c;
		this.divider = getDivider();
		this.overall = calculateOverall();
	}

	public static final ReviewPoints fromString(final String points) {
		return extract(points);
	}

	private static ReviewPoints extract(String points) {
		String[] pSA = points.split("-"); // pointsStringArray
		return new ReviewPoints(toDouble(pSA, 0), toDouble(pSA, 1), toDouble(pSA, 2), toDouble(pSA, 3));
	}
	
	private static double toDouble(String[] pSA, int index) {
		return Double.valueOf(pSA[index]);
	}

	public double getStructurePoints() {
		return this.structure;
	}

	public double getTerrainPoints() {
		return this.terrain;
	}

	public double getOrganicsPoints() {
		return organics;
	}
	
	public double getCompositionPoints() {
		return composition;
	}
	
	public double getOverall() {
		return overall;
	}
	
	public double calculateOverall() {
		if(divider <= 0)
			return 0;
		DecimalFormat df = new DecimalFormat("#.#");
		return Double.valueOf(df.format((structure + terrain + organics + composition) / divider));
	}
	
	public double getDivider() {
		int divider = 0;
		divider += (structure < MIN_VAL) ? 0 : 1;
		divider += (terrain < MIN_VAL) ? 0 : 1;
		divider += (organics < MIN_VAL) ? 0 : 1;
		divider += (composition < MIN_VAL) ? 0 : 1;
		return (double) divider;
	}
}
