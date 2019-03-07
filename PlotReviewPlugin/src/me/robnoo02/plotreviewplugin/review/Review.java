package me.robnoo02.plotreviewplugin.review;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;

public class Review {

	private final ReviewID id;
	private final Reviewee player;
	private final Plot plot;
	private ReviewStaff staff;
	private boolean reviewed = false;
	
	private Review(final OfflinePlayer offPlayer, final Plot plot) {
		this.id = ReviewID.generateID();
		this.player = Reviewee.wrap(offPlayer);
		this.plot = plot.getBasePlot(false);
	}
	
	private Review(final Reviewee reviewee, final String id, final String plotLocation) {
		this.id = ReviewID.fromString(id);
		this.player = reviewee;
		this.plot = getPlotFromString(plotLocation);
	}
	
	/**
	 * This method is invoked when a player types /review submit confirm.
	 */
	public static Review createReviewTicket(final OfflinePlayer p, final Plot plot) {
		return new Review(p, plot);
	}
	
	public static Review loadFromFile(String id, String revieweeUUID, String plotLocation, String date, String rank) {
		return new Review(Reviewee.load(revieweeUUID, rank, date), id, plotLocation);
	}
	
	public String getPlotString() {
		String world = plot.getWorldName();
		String id = plot.getId().toString();
		return (world + "-" + id);
	}
	
	public Plot getPlot() {
		return plot;
	}
	
	public ReviewID getId() {
		return id;
	}
	
	public Reviewee getReviewee() {
		return player;
	}
	
	public ReviewStaff getStaff() {
		return staff;
	}
	
	public boolean isReviewed() {
		return reviewed;
	}
	
	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}
	
	private Location getPlotHomeLoc() {
		com.intellectualcrafters.plot.object.Location pLoc = plot.getBasePlot(false).getHome();
		return new Location(Bukkit.getWorld(pLoc.getWorld()), pLoc.getX(), pLoc.getY(), pLoc.getZ());
	}
	
	public void initializeStaff(OfflinePlayer p, String points) {
		this.staff = ReviewStaff.wrap(p, points);
	}
	
	public void teleportPlayer(Player p) {
		p.teleport(getPlotHomeLoc());
	}
	
	private Plot getPlotFromString(String plotLoc) {
		String[] parts = plotLoc.split("-");
		if(parts.length < 2)
			return null;
		return PS.get().getPlot(PS.get().getPlotArea(parts[0], parts[1]), PlotId.fromString(parts[1]));
	}
}
