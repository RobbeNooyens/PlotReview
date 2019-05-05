package me.robnoo02.plotreview.handlers;

import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreview.files.UserDataFile;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.files.UserDataFileFields.OldScoresField;
import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.score.ReviewScore;

public class ScoreHandler {

	private static final double PASS_THRESHOLD = 4.0;

	public static void handleNewReview(final int id, final String scores, final OfflinePlayer staff) {
		final ReviewScore score = new ReviewScore(id, staff, scores);
		score.update();

		// At this point, all values are up to date

		boolean passes = score.getAvgStoc() >= PASS_THRESHOLD;

		UserDataFile file = UserDataManager.getUserDataFile(id);
		file.setString(id, TicketDataField.STRUCTURE_SCORE, String.valueOf(score.getStructureScore()));
		file.setString(id, TicketDataField.TERRAIN_SCORE, String.valueOf(score.getTerrainScore()));
		file.setString(id, TicketDataField.ORGANICS_SCORE, String.valueOf(score.getOrganicsScore()));
		file.setString(id, TicketDataField.COMPOSITION_SCORE, String.valueOf(score.getCompositionScore()));
		file.setString(id, TicketDataField.STOC, String.valueOf(score.getStoc()));
		file.setString(id, TicketDataField.AVERAGE_STOC, String.valueOf(score.getAvgStoc()));
		file.setString(id, TicketDataField.STAFF, score.getStaff().getUniqueId().toString());
		
		file.setString(id, PlayerInfoField.LATEST_NAME, String.valueOf(score.getReviewee().getName()));

		if (passes) {
			file.setString(id, PlayerInfoField.TOTAL_STOC, String.valueOf(score.getTotStoc()));
			file.setString(id, PlayerInfoField.AVERAGE_STOC, String.valueOf(score.getTotAvgStoc()));
			file.setString(id, PlayerInfoField.RATING, String.valueOf(score.getRating()));
			file.setString(id, PlayerInfoField.ACCEPTED_SUBMISSIONS, String.valueOf(score.getSubmissions()));
			file.setString(id, PlayerInfoField.TOTAL_PLOT_SCORE, String.valueOf(score.getTotalPlotScore()));
		}

		if (score.getReviewee().isOnline()) {

		} else {
			file.setString(id, OldScoresField.AVERAGE_STOC, String.valueOf(score.getTotAvgStoc()));
			file.setString(id, OldScoresField.TOTAL_STOC, String.valueOf(score.getAvgStoc()));
			file.setString(id, OldScoresField.RATING, String.valueOf(score.getRating()));
			file.setString(id, OldScoresField.TOTAL_PLOT_SCORE, String.valueOf(score.getTotalPlotScore()));
		}

		if (score.canRankup()) {
			// rankup
		}

		// Save values to config

	}

}
