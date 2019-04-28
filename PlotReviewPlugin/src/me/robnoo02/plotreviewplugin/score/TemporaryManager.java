package me.robnoo02.plotreviewplugin.score;

import org.bukkit.OfflinePlayer;

public class TemporaryManager {

	public static void handleNewReview(int id, String scores, OfflinePlayer staff) {
		final ReviewScore score = new ReviewScore(id, staff, scores);
		score.calculate();
		
		// At this point, all values are up to date
		
		if(score.canRankup()) {
			// rankup
		}
		
		// Save values to config
		
	}
	
}
