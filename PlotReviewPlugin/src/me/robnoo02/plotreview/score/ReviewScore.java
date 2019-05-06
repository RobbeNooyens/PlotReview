package me.robnoo02.plotreview.score;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataFileFields.NewScoresField;
import me.robnoo02.plotreview.files.UserDataFileFields.PlayerInfoField;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.files.UserDataManager;

public class ReviewScore {

	// Final fields
	// Some files should are unnecessary and should be removed later on
	private final int ticketId;
	private final String world/* , plot, rank, date */;
	private final OfflinePlayer staff, reviewee;
	private final double s, t, o, c; // Structure, Terrain, Organics, Composition
	private static final double PASS_THRESHOLD = 4.0;

	private double stoc, avgStoc, totAvgStoc, totStoc, plotScore, totalPlotScore, avgPlotScore;
	private int rating, numberOfSubmissions;
	private boolean canRankup, passes, reviewed = true, online;

	private final double oldTotAvgStoc, oldTotStoc, oldTotPlotScore;
	private double addTotAvgStoc, addTotStoc, addTotPlotScore;
	private final int oldRating;
	private int addRating;

	private boolean called = false;

	public ReviewScore(int id, OfflinePlayer staff, String scores) {
		this.ticketId = id;
		this.staff = staff;

		this.reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(id)));
		this.online = reviewee.isOnline();

		HashMap<STOC, Double> scoreMap = STOC.fromStringDoubles(scores);
		this.s = scoreMap.get(STOC.STRUCTURE);
		this.t = scoreMap.get(STOC.TERRAIN);
		this.o = scoreMap.get(STOC.ORGANICS);
		this.c = scoreMap.get(STOC.COMPOSITION);

		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(id).getUserData(id);
		this.world = userData.get(TicketDataField.WORLD);

		HashMap<PlayerInfoField, String> playerInfo = UserDataManager.getUserDataFile(id).getPlayerInfo();
		this.totAvgStoc = Double.valueOf(playerInfo.get(PlayerInfoField.AVERAGE_STOC));
		this.totStoc = Double.valueOf(playerInfo.get(PlayerInfoField.TOTAL_STOC));
		this.rating = Integer.valueOf(playerInfo.get(PlayerInfoField.RATING));
		this.numberOfSubmissions = Integer.valueOf(playerInfo.get(PlayerInfoField.ACCEPTED_SUBMISSIONS)) + 1; // +1 for this submission
		this.totalPlotScore = Double.valueOf(playerInfo.get(PlayerInfoField.TOTAL_PLOT_SCORE));
		this.oldTotAvgStoc = totAvgStoc;
		this.oldTotStoc = totStoc;
		this.oldRating = rating;
		this.oldTotPlotScore = totalPlotScore;

		HashMap<NewScoresField, String> newScores = UserDataManager.getUserDataFile(id).getNewScores();
		this.addTotAvgStoc = online ? 0 : Double.valueOf(newScores.get(NewScoresField.AVERAGE_STOC));
		this.addTotStoc = online ? 0 : Double.valueOf(newScores.get(NewScoresField.TOTAL_STOC));
		this.addRating = online ? 0 : Integer.valueOf(newScores.get(NewScoresField.RATING));
		this.addTotPlotScore = online ? 0 : Double.valueOf(newScores.get(NewScoresField.TOTAL_PLOT_SCORE));
		//this.newScoresAvailable = Boolean.valueOf(newScores.get(NewScoresField.NEW_SCORES_AVAILABLE));

	}

	public void update() {
		if (called) return;
		fillSTOC();
		fillAvgSTOC();
		updateTotalSTOC();
		updateTotAvgSTOC();
		fillPlotScores();
		updateRating();
		addDifferences();
		called = true;
	}

	private void addDifferences() {
		this.addTotAvgStoc += passes ? totAvgStoc - oldTotAvgStoc : 0;
		this.addTotStoc += passes ? totStoc - oldTotStoc : 0;
		this.addTotPlotScore += passes ? totalPlotScore - oldTotPlotScore : 0;
		this.addRating += passes ? rating - oldRating : 0;
	}

	private void fillSTOC() {
		this.stoc = s + t + o + c;
	}

	private void fillAvgSTOC() {
		this.avgStoc = stoc / 4;
		this.passes = avgStoc >= PASS_THRESHOLD;
		/*
		 * int divider = 0; divider += (s==0 ? 0 : 1); divider += (t==0 ? 0 : 1);
		 * divider += (o==0 ? 0 : 1); divider += (c==0 ? 0 : 1); if(divider <= 0) {
		 * this.avgStoc = 0; return; } this.avgStoc = stoc / divider;
		 */
	}

	private void updateTotalSTOC() {
		totStoc += stoc;
	}

	private void updateTotAvgSTOC() {
		totAvgStoc = ((((numberOfSubmissions - 1) * totAvgStoc) + avgStoc) / numberOfSubmissions);
	}

	private void fillPlotScores() {
		plotScore = stoc * STOC.RankMult.fromString(world).weight();
		totalPlotScore += plotScore;
		avgPlotScore = totalPlotScore / numberOfSubmissions;

	}

	private void updateRating() {
		rating = (int) ((totalPlotScore * avgPlotScore) * (totAvgStoc / 1000));
	}

	/************
	 * Getters
	 ************/

	public int getId() {
		return ticketId;
	}

	public OfflinePlayer getStaff() {
		return staff;
	}

	public double getStructureScore() {
		return s;
	}

	public double getTerrainScore() {
		return t;
	}

	public double getOrganicsScore() {
		return o;
	}

	public double getCompositionScore() {
		return c;
	}

	public double getStoc() {
		return stoc;
	}

	public double getAvgStoc() {
		return avgStoc;
	}

	public double getTotAvgStoc() {
		return totAvgStoc;
	}

	public double getTotStoc() {
		return totStoc;
	}

	public int getRating() {
		return rating;
	}

	public boolean canRankup() {
		return canRankup;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public double getPlotScore() {
		return plotScore;
	}

	public double getTotalPlotScore() {
		return totalPlotScore;
	}

	public double getAvgPlotScore() {
		return avgPlotScore;
	}

	public OfflinePlayer getReviewee() {
		return reviewee;
	}

	public int getSubmissions() {
		return numberOfSubmissions;
	}

	public boolean passes() {
		return passes;
	}

	public double getAddTotAvgStoc() {
		return addTotAvgStoc;
	}

	public double getAddTotStoc() {
		return addTotStoc;
	}

	public double getAddTotPlotScore() {
		return addTotPlotScore;
	}

	public int getAddRating() {
		return addRating;
	}

}
