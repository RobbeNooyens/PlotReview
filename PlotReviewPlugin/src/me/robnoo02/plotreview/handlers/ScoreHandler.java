package me.robnoo02.plotreview.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import me.robnoo02.plotreview.Query.QueryElement;
import me.robnoo02.plotreview.Query.QueryGroup;
import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataFile;
import me.robnoo02.plotreview.files.UserDataFileFields.NewScoresField;
import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.score.ReviewScore;
import me.robnoo02.plotreview.utils.SendMessageUtil;

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
		file.setString(id, TicketDataField.PLOT_SCORE, String.valueOf(score.getPlotScore()));
		file.setString(id, TicketDataField.AVERAGE_STOC, String.valueOf(score.getAvgStoc()));
		file.setString(id, TicketDataField.STAFF, score.getStaff().getUniqueId().toString());
		
		file.setString(id, PlayerInfoField.LATEST_NAME, String.valueOf(score.getReviewee().getName()));
		
		DataFileManager.setReviewed(id, true);

		if (score.passes()) {
			file.setString(id, PlayerInfoField.TOTAL_STOC, String.valueOf(score.getTotStoc()));
			file.setString(id, PlayerInfoField.AVERAGE_STOC, String.valueOf(score.getTotAvgStoc()));
			file.setString(id, PlayerInfoField.RATING, String.valueOf(score.getRating()));
			file.setString(id, PlayerInfoField.ACCEPTED_SUBMISSIONS, String.valueOf(score.getSubmissions()));
			file.setString(id, PlayerInfoField.TOTAL_PLOT_SCORE, String.valueOf(score.getTotalPlotScore()));
			DataFileManager.setPassed(id, "passed");
		} else {
			DataFileManager.setPassed(id, "failed");
		}
		file.setString(id, NewScoresField.AVERAGE_STOC, String.valueOf(score.getAddTotAvgStoc()));
		file.setString(id, NewScoresField.TOTAL_STOC, String.valueOf(score.getTotStoc()));
		file.setString(id, NewScoresField.RATING, String.valueOf(score.getAddRating()));
		file.setString(id, NewScoresField.TOTAL_PLOT_SCORE, String.valueOf(score.getAddTotPlotScore()));
		file.setString(id, NewScoresField.NEW_SCORES_AVAILABLE, "true");

		if (score.getReviewee().isOnline()) {
			file.setString(id, PlayerInfoField.REVIEWED_WHILE_OFFLINE, "false");
		} else {
			file.setString(id, PlayerInfoField.REVIEWED_WHILE_OFFLINE, "true");
		}

		if (score.canRankup()) {
			// rankup
		}
		
		if(score.getReviewee().isOnline()) {
			SendMessageUtil.ONLINE_REVIEWED.send((CommandSender) score.getReviewee(), QueryGroup.PLOT_REVIEWED.get(id));
		}
		
		if(!score.passes()) {
			SendMessageUtil.WRITE_COMMENT.send((CommandSender) staff, QueryElement.TICKET_ID, String.valueOf(id));
		}

		// Save values to config

	}

}
