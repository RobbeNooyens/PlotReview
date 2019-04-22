package me.robnoo02.plotreviewplugin.score;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.robnoo02.plotreviewplugin.files.ConfigManager;

public enum STOC {

	STRUCTURE, TERRAIN, ORGANICS, COMPOSITION;
	
	private int index;
	
	/**
	 * @param index is the index of the position in the reviewscore string
	 */
	private void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static void setup() {
		for(STOC score: STOC.values()) {
			String index = ConfigManager.getString("score-syntax." + score.toString().toLowerCase());
			score.setIndex(Integer.valueOf(index));
		}
	}
	
	public static HashMap<STOC, String> fromString(String scores){
		String[] info = scores.split("-");
		if(info.length < values().length)
			return null;
		HashMap<STOC, String> map = new HashMap<>();
		map.put(STOC.STRUCTURE, info[STOC.STRUCTURE.getIndex()]);
		map.put(STOC.ORGANICS, info[STOC.ORGANICS.getIndex()]);
		map.put(STOC.TERRAIN, info[STOC.TERRAIN.getIndex()]);
		map.put(STOC.COMPOSITION, info[STOC.COMPOSITION.getIndex()]);
		return map;
	}
	
	public static double calculateOverall(HashMap<STOC, String> scores) {
		Set<Double> usedVals = new HashSet<>();
		for(String s: scores.values())
			if(!(Double.valueOf(s) == 0)) usedVals.add(Double.valueOf(s));
		int divider = usedVals.size();
		if(divider <= 0)
			return 0;
		DecimalFormat df = new DecimalFormat("#.#");
		return Double.valueOf(df.format((calculateSum(usedVals)) / divider).replaceAll(",", "."));
	}
	
	private static double calculateSum(Set<Double> values) {
		double output = 0;
		for(double dbl: values)
			output += dbl;
		return output;
	}
	
	public static enum RankMult {
		NOVICE(1),
		APPRENTICE(2.4),
		DESIGNER(4),
		ARCHITECT(7.2),
		ARTISAN(9.6),
		MASTER(16.1);
		
		private final double weight;
		
		private RankMult(double weight) {
			this.weight = weight;
		}
		
		public double weight() {
			return weight;
		}
	}
}
