package me.robnoo02.plotreviewplugin.review;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class ReviewStaff implements ReviewPlayer {
	
	private final UUID uuid;
	private final String name;
	private final Date reviewDate;
	
	private ReviewStaff(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		this.reviewDate = new Date();
	}
	
	public static ReviewStaff wrap(OfflinePlayer p) {
		return new ReviewStaff(p.getUniqueId(), p.getName());
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

}
