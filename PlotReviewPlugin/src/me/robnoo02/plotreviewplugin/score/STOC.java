package me.robnoo02.plotreviewplugin.score;

import java.util.HashMap;

import me.robnoo02.plotreviewplugin.files.ConfigManager;
import me.robnoo02.plotreviewplugin.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public enum STOC {

	STRUCTURE, TERRAIN, ORGANICS, COMPOSITION;

	private int index;

	/**
	 * @param index
	 *            is the index of the position in the reviewscore string
	 */
	private void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public static void setup() {
		for (STOC score : STOC.values()) {
			String index = ConfigManager.getString("score-syntax." + score.toString().toLowerCase());
			score.setIndex(Integer.valueOf(index));
		}
	}
	
	public String getScore(String scores) {
		String[] info = scores.split("-");
		return info[getIndex()];
	}

	public static HashMap<STOC, Double> fromStringDoubles(String scores) {
		String[] info = scores.split("-");
		if (info.length < values().length) return null;
		HashMap<STOC, Double> map = new HashMap<>();
		try {
			map.put(STOC.STRUCTURE, Double.valueOf(info[STOC.STRUCTURE.getIndex() - 1]));
			map.put(STOC.ORGANICS, Double.valueOf(info[STOC.ORGANICS.getIndex() - 1]));
			map.put(STOC.TERRAIN, Double.valueOf(info[STOC.TERRAIN.getIndex() - 1]));
			map.put(STOC.COMPOSITION, Double.valueOf(info[STOC.COMPOSITION.getIndex() - 1]));
		} catch (ClassCastException exception) {
			// Staff entered wrong score format
			return null;
		}
		return map;
	}
	
	public static HashMap<STOC, String> fromStringStrings(String scores) {
		String[] info = scores.split("-");
		if (info.length < values().length) return null;
		HashMap<STOC, String> map = new HashMap<>();
		try {
			map.put(STOC.STRUCTURE, info[STOC.STRUCTURE.getIndex()]);
			map.put(STOC.ORGANICS, info[STOC.ORGANICS.getIndex()]);
			map.put(STOC.TERRAIN, info[STOC.TERRAIN.getIndex()]);
			map.put(STOC.COMPOSITION, info[STOC.COMPOSITION.getIndex()]);
		} catch (ClassCastException exception) {
			// Staff entered wrong score format
			return null;
		}
		return map;
	}

	public static enum RankMult {
		NOVICE(1), APPRENTICE(2.4), DESIGNER(4), ARCHITECT(7.2), ARTISAN(9.6), MASTER(16.1);

		private final double weight; // the rank multiplier

		private RankMult(double weight) {
			this.weight = weight;
		}

		public double weight() {
			return weight;
		}

		public static RankMult fromString(String world) {
			for (RankMult val : RankMult.values()) {
				if (val.toString().equalsIgnoreCase(world)) return val;
			}
			return null;
		}

		/**
		 * @param ticketId
		 *            is the ticket ID
		 * @return the multiplier of the plot
		 */
		public static double getWeight(int ticketId) {
			String rank = UserDataManager.getUserDataFile(ticketId).getString(ticketId, TicketDataField.WORLD);
			RankMult mult = RankMult.fromString(rank);
			return (mult == null) ? 0 : mult.weight();
		}
	}
}
