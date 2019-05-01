package me.robnoo02.plotreviewplugin.score;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import me.robnoo02.plotreviewplugin.files.DataFileManager;
import me.robnoo02.plotreviewplugin.files.UserDataFile.PlayerInfoField;
import me.robnoo02.plotreviewplugin.files.UserDataFile.TicketDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;

public class ReviewScore {
	
	// Final fields
	// Some files should are unnecessary and should be removed later on
	private final int ticketId;
	private final String world/*, plot, rank, date*/;
	private final OfflinePlayer staff, reviewee;
	private final double s, t, o, c; // Structure, Terrain, Organics, Composition
	private final double oTotAvgStoc, oTotStoc;
	private final int oRating;
	
	private double stoc, avgStoc, totAvgStoc, totStoc, plotScore, totalPlotScore, avgPlotScore;
	private int rating, numberOfSubmissions;
	private boolean canRankup, reviewed = true;
	
	public ReviewScore(int id, OfflinePlayer staff, String scores) {
		this.ticketId = id;
		this.staff = staff;
		
		this.reviewee = Bukkit.getOfflinePlayer(UUID.fromString(DataFileManager.getUUID(id)));
		
		HashMap<STOC, Double> scoreMap = STOC.fromStringDoubles(scores);
		this.s = scoreMap.get(STOC.STRUCTURE);
		this.t = scoreMap.get(STOC.TERRAIN);
		this.o = scoreMap.get(STOC.ORGANICS);
		this.c = scoreMap.get(STOC.COMPOSITION);
		
		HashMap<TicketDataField, String> userData = UserDataManager.getUserDataFile(id).getUserData(id);
		this.world = userData.get(TicketDataField.WORLD);
		/*this.rank = userData.get(TicketDataField.RANK);
		this.plot = userData.get(TicketDataField.PLOT);
		this.date = userData.get(TicketDataField.DATE);*/
		
		HashMap<PlayerInfoField, String> playerInfo = UserDataManager.getUserDataFile(id).getPlayerInfo();
		this.oTotAvgStoc = Double.valueOf(playerInfo.get(PlayerInfoField.AVARAGE_STOC));
		this.oTotStoc = Double.valueOf(playerInfo.get(PlayerInfoField.TOTAL_STOC));
		this.oRating = Integer.valueOf(playerInfo.get(PlayerInfoField.RATING));
		this.totAvgStoc = oTotAvgStoc;
		this.totStoc = oTotStoc;
		this.rating = oRating;
		this.numberOfSubmissions = Integer.valueOf(playerInfo.get(PlayerInfoField.NUMBER_OF_SUBMISSIONS)) + 1; // +1 for this submission
		this.totalPlotScore = Double.valueOf(playerInfo.get(PlayerInfoField.TOTAL_PLOT_SCORE));
	}
	
	/**
	 * Dangerous method!
	 * Calling this method twice can cause problems.
	 */
	public void update() {
		fillSTOC();
		fillAvgSTOC();
		updateTotalSTOC();
		updateTotAvgSTOC();
		fillPlotScores();
		updateRating();
	}
	
	private void fillSTOC(){
		this.stoc = s + t + o + c;
	}
	
	private void fillAvgSTOC() {
		int divider = 0;
		divider += (s==0 ? 0 : 1);
		divider += (t==0 ? 0 : 1);
		divider += (o==0 ? 0 : 1);
		divider += (c==0 ? 0 : 1);
		if(divider <= 0) {
			this.avgStoc = 0;
			return;
		}
		this.avgStoc = stoc / divider;
	}
	
	private void updateTotalSTOC() {
		totStoc += stoc;
	}
	
	private void updateTotAvgSTOC() {
		totAvgStoc = totStoc / numberOfSubmissions;
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
	
	public double getOldTotAvgStoc() {
		return oTotAvgStoc;
	}
	
	public double getOldTotStoc() {
		return oTotStoc;
	}
	
	public int getOldRating() {
		return oRating;
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

}
