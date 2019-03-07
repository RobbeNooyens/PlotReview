package me.robnoo02.plotreviewplugin.submit;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;

import me.robnoo02.plotreviewplugin.files.DataFile;
import me.robnoo02.plotreviewplugin.review.Review;
import me.robnoo02.plotreviewplugin.review.ReviewManager;
import me.robnoo02.plotreviewplugin.utils.DebugUtil;
import me.robnoo02.plotreviewplugin.utils.SendMessageUtil;

public class SubmitManager implements DebugUtil {

	private static final SubmitManager INSTANCE = new SubmitManager();

	private final Set<UUID> submitQueue = new HashSet<>();

	private SubmitManager() {
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
		Plot plot = PlotPlayer.wrap(p).getCurrentPlot();
		if (!possibleToSubmit(p))
			return false;
		Review review = Review.createReviewTicket(p, plot);
		ReviewManager.getInstance().saveReview(review);
		DataFile.getInstance().saveReview(review);
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
		return !ReviewManager.getInstance().isAdded(plot);
	}

	public static SubmitManager getInstance() {
		return INSTANCE;
	}
	
	public String getSubmits() {
		StringBuilder builder = new StringBuilder();
		for(UUID uuid: SubmitManager.getInstance().getSubmitQueue())
			builder.append("§b" + Bukkit.getOfflinePlayer(uuid).getName() + "§7,");
		if(builder.length() == 0)
			return "§7None";
		builder.deleteCharAt(builder.length()-1);
		return builder.toString();
	}

}
