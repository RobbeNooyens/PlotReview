package me.robnoo02.plotreviewplugin.review;

import java.util.Date;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

public final class Reviewee implements ReviewPlayer {

	private final UUID uuid;
	private final String name, currentRank;
	private final Date date;
	private final Plot plot;
	
	private Reviewee(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
		this.currentRank = "";
		this.date = new Date();
		this.plot = PlotPlayer.wrap(Bukkit.getOfflinePlayer(uuid)).getCurrentPlot();
	}
	
	public static Reviewee wrap(OfflinePlayer p) {
		return new Reviewee(p.getUniqueId(), p.getName());
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
		return date;
	}
	
	public String getCurrentRank() {
		return currentRank;
	}
	
	public String getPlotID() {
		return plot.getId().toString();
	}
}
