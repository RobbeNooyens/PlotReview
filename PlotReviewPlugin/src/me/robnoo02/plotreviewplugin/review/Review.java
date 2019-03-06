package me.robnoo02.plotreviewplugin.review;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import com.intellectualcrafters.plot.object.Plot;

public class Review {

	private final ReviewID id;
	private final Reviewee player;
	private final Plot plot;
	private ReviewStaff staff;
	private boolean reviewed = false;
	
	private Review(final OfflinePlayer offPlayer, final Plot plot) {
		this.id = ReviewID.generateID();
		this.player = Reviewee.wrap(offPlayer);
		this.plot = plot;
	}
	
	/**
	 * This method is invoked when a player types /review submit confirm.
	 */
	public static Review createReviewTicket(final OfflinePlayer offPlayer, final Plot plot) {
		return new Review(offPlayer, plot);
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
	
	public Location getPlotHomeLoc() {
		com.intellectualcrafters.plot.object.Location pLoc = plot.getBasePlot(false).getHome();
		return new Location(Bukkit.getWorld(pLoc.getWorld()), pLoc.getX(), pLoc.getY(), pLoc.getZ());
	}
	
	public void initializeStaff(OfflinePlayer p) {
		this.staff = ReviewStaff.wrap(p);
	}
}
