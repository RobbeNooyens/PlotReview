package me.robnoo02.plotreviewplugin.review;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import me.robnoo02.plotreviewplugin.files.ConfigManager;

public enum ScoreAspect {

	STRUCTURE, TERRAIN, ORGANICS, COMPOSITION;
	
	private int index;
	
	private void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static void setup() {
		for(ScoreAspect score: ScoreAspect.values()) {
			String index = ConfigManager.getString("score-syntax." + score.toString().toLowerCase());
			score.setIndex(Integer.valueOf(index));
		}
	}
	
	public static HashMap<ScoreAspect, String> fromString(String scores){
		String[] info = scores.split("-");
		if(info.length < values().length)
			return null;
		HashMap<ScoreAspect, String> map = new HashMap<>();
		map.put(ScoreAspect.STRUCTURE, info[ScoreAspect.STRUCTURE.getIndex()]);
		map.put(ScoreAspect.ORGANICS, info[ScoreAspect.ORGANICS.getIndex()]);
		map.put(ScoreAspect.TERRAIN, info[ScoreAspect.TERRAIN.getIndex()]);
		map.put(ScoreAspect.COMPOSITION, info[ScoreAspect.COMPOSITION.getIndex()]);
		return map;
	}
	
	public static double calculateOverall(HashMap<ScoreAspect, String> scores) {
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
}
