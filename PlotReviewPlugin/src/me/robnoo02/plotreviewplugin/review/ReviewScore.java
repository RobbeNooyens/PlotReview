package me.robnoo02.plotreviewplugin.review;

import java.text.DecimalFormat;
import java.util.HashMap;

import me.robnoo02.plotreviewplugin.files.ConfigManager;

/**
 * This class will not be used any longer due to unnecessary object creation.
 * Let's give the gc some rest.
 * 
 * @author Robnoo02
 *
 */
public final class ReviewScore {
	
	private static final int MIN_VAL = 1;
	private final double structure, terrain, organics, composition, overall, divider;

	private ReviewScore(HashMap<ScoreAspect, Double> scores) {
		this.structure = (scores.get(ScoreAspect.STRUCTURE) < MIN_VAL) ? 0 : scores.get(ScoreAspect.STRUCTURE);
		this.terrain = (scores.get(ScoreAspect.TERRAIN) < MIN_VAL) ? 0 : scores.get(ScoreAspect.TERRAIN);
		this.organics = (scores.get(ScoreAspect.ORGANICS) < MIN_VAL) ? 0 : scores.get(ScoreAspect.ORGANICS);
		this.composition = (scores.get(ScoreAspect.COMPOSITION) < MIN_VAL) ? 0 : scores.get(ScoreAspect.COMPOSITION);
		this.divider = getDivider();
		this.overall = calculateOverall();
	}

	public static final ReviewScore fromString(final String points) {
		return extract(points);
	}

	private static ReviewScore extract(String score) {
		HashMap<ScoreAspect, Double> scores = ConfigManager.getScore(score); // pointsStringArray
		return new ReviewScore(scores);
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
		return Double.valueOf(df.format((structure + terrain + organics + composition) / divider).replaceAll(",", "."));
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
