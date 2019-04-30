package me.robnoo02.plotreviewplugin.handlers;

import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.score.ReviewScore;

public class ScoreHandler {

	public static void handleNewReview(final int id, final String scores, final OfflinePlayer staff) {
		final ReviewScore score = new ReviewScore(id, staff, scores);
		score.update();
		
		// At this point, all values are up to date
				
		if(!score.hasPendingTicket()) {
			// Reviewee is online
			
		}
		
		if(score.canRankup()) {
			// rankup
		}
		
		// Save values to config
		
	}
	
}
