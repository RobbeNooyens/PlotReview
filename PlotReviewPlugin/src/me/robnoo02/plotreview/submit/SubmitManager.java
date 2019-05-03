package me.robnoo02.plotreview.submit;

/**
 * This singleton handles submitted submits.
 * Players can submit their plot with /submit.
 * Submit will be stored in the queue
 * untill they're confirmed by the player or when they
 * are removed from the queue due to convos.
 */
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreview.files.DataFileManager;
import me.robnoo02.plotreview.files.UserDataManager;
import me.robnoo02.plotreview.files.UserDataFileFields.TicketDataField;
import me.robnoo02.plotreview.review.ReviewID;
import me.robnoo02.plotreview.utils.DateFormatterUtil;
import me.robnoo02.plotreview.utils.DebugUtil;
import me.robnoo02.plotreview.utils.PlotUtil;
import me.robnoo02.plotreview.utils.RankUtil;
import me.robnoo02.plotreview.utils.SendMessageUtil;

public class SubmitManager implements DebugUtil {

	private static Set<UUID> submitQueue = new HashSet<>();

	public static boolean isSubmitQueued(Player p) {
		return submitQueue.contains(p.getUniqueId());
	}

	public static void addSubmitQueue(Player p) {
		submitQueue.add(p.getUniqueId());
	}

	public static void removeSubmitQueue(Player p) {
		submitQueue.remove(p.getUniqueId());
	}

	public static boolean submitPlot(Player p) {
		if (!possibleToSubmit(p))
			return false;
		int id = ReviewID.generateID(); // Generates unique ID for the Review
		Plot plot = PlotUtil.getCurrentPlot(p); // Gets the plot the Player is standing on
		HashMap<TicketDataField, String> fields = new HashMap<>(); // HashMap to transfer Review data easily without creating new custom class Objects
		fields.put(TicketDataField.DATE, DateFormatterUtil.formatDate(new Date()));
		fields.put(TicketDataField.PLOT, plot.getId().toString());
		fields.put(TicketDataField.RANK, RankUtil.getRankName(p));
		fields.put(TicketDataField.WORLD, plot.getWorldName());
		DataFileManager.addReview(id, DataFileManager.toDataFileFormat(p.getUniqueId().toString(), PlotUtil.formatPlot(plot), "false")); // Saves Review to datafile
		UserDataManager.getUserDataFile(id).setUserData(id, fields); // Adds review to userdatafile
		return SendMessageUtil.PLOT_SUBMITTED.send(p, true);
	}

	public static boolean possibleToSubmit(Player p) {
		Plot plot = PlotPlayer.wrap(p).getCurrentPlot();
		if (plot == null)
			return SendMessageUtil.NOT_ON_PLOT.send(p, false); // Player can't be null
		if (!plot.getOwners().contains(p.getUniqueId()))
			return SendMessageUtil.NOT_PLOTOWNER.send(p, false); // Player should be an owner of the Plot
		if (!(DataFileManager.idFromPlot(plot) == null))
			return SendMessageUtil.ALREADY_SUBMITTED.send(p, false); // Plot shouldn't be reviewed yet
		return true;
	}

	/**
	 * @return a comma seperated list containing names of queued Players
	 */
	public static String getSubmits() {
		StringBuilder builder = new StringBuilder();
		for (UUID uuid : SubmitManager.submitQueue)
			builder.append("§b" + Bukkit.getOfflinePlayer(uuid).getName() + "§7,");
		if (builder.length() == 0)
			return "§7None";
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

}
