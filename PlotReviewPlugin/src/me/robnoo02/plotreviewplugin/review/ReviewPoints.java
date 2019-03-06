package me.robnoo02.plotreviewplugin.review;

import java.text.DecimalFormat;

public final class ReviewPoints {
	
	private final double structure, terrain, organics, composition, overall;

	private ReviewPoints(final double s, final double t, final double o, final double c) {
		this.structure = s;
		this.terrain = t;
		this.organics = o;
		this.composition = c;
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
		DecimalFormat df = new DecimalFormat("#.#");      
		return Double.valueOf(df.format((structure + terrain + organics + composition) / 4));
	}
}
