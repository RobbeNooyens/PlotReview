package me.robnoo02.plotreviewplugin.submit;

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

import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.files.UserDataFile.UserDataField;
import me.robnoo02.plotreviewplugin.files.UserDataManager;
import me.robnoo02.plotreviewplugin.review.ReviewID;
import me.robnoo02.plotreviewplugin.review.ReviewReference;
import me.robnoo02.plotreviewplugin.utils.DebugUtil;
import me.robnoo02.plotreviewplugin.utils.FormatterUtil;
import me.robnoo02.plotreviewplugin.utils.RankUtil;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class SubmitManager implements DebugUtil {

	private static final SubmitManager INSTANCE = new SubmitManager();

	private final Set<UUID> submitQueue = new HashSet<>();

	private SubmitManager() {
	}
	
	public static SubmitManager getInstance() {
		return INSTANCE;
	}

	public Set<UUID> getSubmitQueue() {
		return this.submitQueue;
	}

	public boolean isSubmitQueued(Player p) {
		return submitQueue.contains(p.getUniqueId());
	}

	public void addSubmitQueue(Player p) {
		submitQueue.add(p.getUniqueId());
	}

	public void removeSubmitQueue(Player p) {
		submitQueue.remove(p.getUniqueId());
	}

	public boolean submitPlot(Player p) {
		int id = ReviewID.generateID();
		Plot plot = PlotPlayer.wrap(p).getCurrentPlot();
		if (!possibleToSubmit(p))
			return false;
		HashMap<UserDataField, String> fields = new HashMap<>();
		fields.put(UserDataField.DATE, FormatterUtil.formatDate(new Date()));
		fields.put(UserDataField.PLOT, plot.getId().toString());
		fields.put(UserDataField.RANK, RankUtil.getRankName(p));
		fields.put(UserDataField.WORLD, plot.getWorldName());
		UserDataManager.getInstance().setUserData(p.getUniqueId().toString(), String.valueOf(id), fields);
		String reference = ReviewReference.stringFormat(p.getUniqueId().toString(), FormatterUtil.formatPlot(plot), "false");
		DataFile.getInstance().addReview(id, reference);
		return SendMessageUtil.PLOT_SUBMITTED.send(p, true);
	}

	public boolean possibleToSubmit(Player p) {
		Plot plot = PlotPlayer.wrap(p).getCurrentPlot();
		if (plot == null)
			return SendMessageUtil.NOT_ON_PLOT.send(p, false);
		if (!plot.getOwners().contains(p.getUniqueId()))
			return SendMessageUtil.CANT_SUBMIT.send(p, false);
		if (!canSubmit(plot))
			return SendMessageUtil.ALREADY_SUBMITTED.send(p, false);
		return true;
	}

	public boolean canSubmit(Plot plot) {
		return DataFile.getInstance().getReviewID(plot) == null;
	}

	public String getSubmits() {
		StringBuilder builder = new StringBuilder();
		for (UUID uuid : SubmitManager.getInstance().getSubmitQueue())
			builder.append("§b" + Bukkit.getOfflinePlayer(uuid).getName() + "§7,");
		if (builder.length() == 0)
			return "§7None";
		builder.deleteCharAt(builder.length() - 1);
		return builder.toString();
	}

}
