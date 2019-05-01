package me.robnoo02.plotreviewplugin.handlers;

import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.files.UserDataFile;
import me.robnoo02.plotreviewplugin.files.UserDataFile.OldScoresField;
import me.robnoo02.plotreviewplugin.files.UserDataFile.PlayerInfoField;
import me.robnoo02.plotreviewplugin.files.UserDataFile.TicketDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.score.ReviewScore;

public class ScoreHandler {

	public static void handleNewReview(final int id, final String scores, final OfflinePlayer staff) {
		final ReviewScore score = new ReviewScore(id, staff, scores);
		score.update();

		// At this point, all values are up to date

		UserDataFile file = UserDataManager.getUserDataFile(id);
		file.setString(id, TicketDataField.STRUCTURE_SCORE, String.valueOf(score.getStructureScore()));
		file.setString(id, TicketDataField.TERRAIN_SCORE, String.valueOf(score.getTerrainScore()));
		file.setString(id, TicketDataField.ORGANICS_SCORE, String.valueOf(score.getOrganicsScore()));
		file.setString(id, TicketDataField.COMPOSITION_SCORE, String.valueOf(score.getCompositionScore()));
		file.setString(id, TicketDataField.STOC, String.valueOf(score.getStoc()));
		file.setString(id, TicketDataField.AVERAGE_STOC, String.valueOf(score.getAvgStoc()));
		file.setString(PlayerInfoField.TOTAL_STOC, String.valueOf(score.getTotStoc()));
		file.setString(PlayerInfoField.AVARAGE_STOC, String.valueOf(score.getTotAvgStoc()));
		file.setString(PlayerInfoField.RATING, String.valueOf(score.getRating()));
		file.setString(PlayerInfoField.NUMBER_OF_SUBMISSIONS, String.valueOf(score.getSubmissions()));
		file.setString(PlayerInfoField.TOTAL_PLOT_SCORE, String.valueOf(score.getTotalPlotScore()));
		file.setString(PlayerInfoField.LATEST_NAME, String.valueOf(score.getReviewee().getName()));

		if (score.getReviewee().isOnline()) {

		} else {
			file.setString(OldScoresField.AVARAGE_STOC, String.valueOf(score.getTotAvgStoc()));
			file.setString(OldScoresField.TOTAL_STOC, String.valueOf(score.getAvgStoc()));
			file.setString(OldScoresField.RATING, String.valueOf(score.getRating()));
			file.setString(OldScoresField.TOTAL_PLOT_SCORE, String.valueOf(score.getTotalPlotScore()));
		}

		if (score.canRankup()) {
			// rankup
		}

		// Save values to config

	}

}
