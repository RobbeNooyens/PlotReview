package me.robnoo02.plotreviewplugin.review;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class ReviewStaff implements ReviewPlayer {
	
	private final UUID uuid;
	private final String name;
	private final Date reviewDate;
	private final ReviewPoints points;
	
	private ReviewStaff(UUID uuid, String name, String points) {
		this.uuid = uuid;
		this.name = name;
		this.reviewDate = new Date();
		this.points = ReviewPoints.fromString(points);
	}
	
	public static ReviewStaff wrap(OfflinePlayer p, String points) {
		return new ReviewStaff(p.getUniqueId(), p.getName(), points);
	}

	@Override
	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getOfflinePlayer(uuid);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Date getDate() {
		return reviewDate;
	}
	
	public ReviewPoints getPoints() {
		return points;
	}

}
